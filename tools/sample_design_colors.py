#!/usr/bin/env python3
"""
Amostra cores de um print de design.

Ferramenta de apoio ao Design Contract: em vez de estimar hex "de olho", você
lê os pixels de verdade. Decodifica PNG sem dependências externas (apenas
`zlib` da biblioteca padrão), então funciona em qualquer máquina com Python 3.9+.

Uso:
    # cores dominantes (bom para background e surface)
    python3 tools/sample_design_colors.py print.png --top 20

    # cores saturadas (filtra os cinzas; bom para primary e accent)
    python3 tools/sample_design_colors.py print.png --saturated

    # cor exata em coordenadas
    python3 tools/sample_design_colors.py print.png --at 356,635 --at 452,204

    # média de uma região (mais robusto que um pixel solto)
    python3 tools/sample_design_colors.py print.png --region 340,620,380,650

    # razão de contraste WCAG entre duas cores
    python3 tools/sample_design_colors.py --contrast "#FFFFFF" "#6255F4"
"""
from __future__ import annotations

import argparse
import collections
import struct
import sys
import zlib
from pathlib import Path

# ─────────────────────────────────────────────────────────────────────────────
#  Decodificação de PNG
# ─────────────────────────────────────────────────────────────────────────────


class Png:
    """PNG de 8 bits por canal, não entrelaçado."""

    def __init__(self, path: Path) -> None:
        data = path.read_bytes()
        if data[:8] != b"\x89PNG\r\n\x1a\n":
            raise SystemExit(f"{path} não é um PNG.")

        pos = 8
        idat = bytearray()
        self.plte: bytes | None = None
        while pos < len(data):
            (length,) = struct.unpack(">I", data[pos:pos + 4])
            kind = data[pos + 4:pos + 8]
            chunk = data[pos + 8:pos + 8 + length]
            if kind == b"IHDR":
                (self.width, self.height, depth, self.color_type,
                 _comp, _filt, interlace) = struct.unpack(">IIBBBBB", chunk)
                if depth != 8:
                    raise SystemExit("Só PNG de 8 bits por canal é suportado.")
                if interlace:
                    raise SystemExit("PNG entrelaçado não é suportado.")
            elif kind == b"IDAT":
                idat += chunk
            elif kind == b"PLTE":
                self.plte = chunk
            elif kind == b"IEND":
                break
            pos += 12 + length

        self.channels = {0: 1, 2: 3, 3: 1, 4: 2, 6: 4}[self.color_type]
        self.buf = self._unfilter(zlib.decompress(bytes(idat)))

    def _unfilter(self, raw: bytes) -> bytes:
        bpp = self.channels
        stride = self.width * bpp
        out = bytearray(self.height * stride)
        prev = bytearray(stride)
        p = 0
        for y in range(self.height):
            mode = raw[p]
            p += 1
            line = bytearray(raw[p:p + stride])
            p += stride
            if mode == 1:
                for i in range(bpp, stride):
                    line[i] = (line[i] + line[i - bpp]) & 0xFF
            elif mode == 2:
                for i in range(stride):
                    line[i] = (line[i] + prev[i]) & 0xFF
            elif mode == 3:
                for i in range(stride):
                    left = line[i - bpp] if i >= bpp else 0
                    line[i] = (line[i] + ((left + prev[i]) >> 1)) & 0xFF
            elif mode == 4:
                for i in range(stride):
                    a = line[i - bpp] if i >= bpp else 0
                    b = prev[i]
                    c = prev[i - bpp] if i >= bpp else 0
                    pa, pb, pc = abs(b - c), abs(a - c), abs(a + b - 2 * c)
                    pred = a if (pa <= pb and pa <= pc) else (b if pb <= pc else c)
                    line[i] = (line[i] + pred) & 0xFF
            out[y * stride:(y + 1) * stride] = line
            prev = line
        return bytes(out)

    def pixel(self, x: int, y: int) -> tuple[int, int, int]:
        x = max(0, min(x, self.width - 1))
        y = max(0, min(y, self.height - 1))
        i = (y * self.width + x) * self.channels
        b = self.buf
        if self.color_type == 3 and self.plte is not None:
            idx = b[i]
            return self.plte[idx * 3], self.plte[idx * 3 + 1], self.plte[idx * 3 + 2]
        if self.color_type in (2, 6):
            return b[i], b[i + 1], b[i + 2]
        v = b[i]
        return v, v, v


# ─────────────────────────────────────────────────────────────────────────────
#  Cor
# ─────────────────────────────────────────────────────────────────────────────


def to_hex(rgb: tuple[int, int, int]) -> str:
    return "#%02X%02X%02X" % rgb


def parse_hex(text: str) -> tuple[int, int, int]:
    h = text.strip().lstrip("#")
    if len(h) == 8:
        h = h[2:]
    if len(h) != 6:
        raise SystemExit(f"Cor inválida: {text}. Use #RRGGBB.")
    return int(h[0:2], 16), int(h[2:4], 16), int(h[4:6], 16)


def relative_luminance(rgb: tuple[int, int, int]) -> float:
    def channel(v: int) -> float:
        c = v / 255.0
        return c / 12.92 if c <= 0.03928 else ((c + 0.055) / 1.055) ** 2.4
    r, g, b = rgb
    return 0.2126 * channel(r) + 0.7152 * channel(g) + 0.0722 * channel(b)


def contrast_ratio(a: tuple[int, int, int], b: tuple[int, int, int]) -> float:
    la, lb = relative_luminance(a), relative_luminance(b)
    lighter, darker = max(la, lb), min(la, lb)
    return (lighter + 0.05) / (darker + 0.05)


def saturation(rgb: tuple[int, int, int]) -> float:
    mx, mn = max(rgb), min(rgb)
    return 0.0 if mx == 0 else (mx - mn) / mx


# ─────────────────────────────────────────────────────────────────────────────
#  Comandos
# ─────────────────────────────────────────────────────────────────────────────


def cmd_top(png: Png, limit: int, saturated: bool, step: int) -> None:
    counter: collections.Counter = collections.Counter()
    for y in range(0, png.height, step):
        for x in range(0, png.width, step):
            rgb = png.pixel(x, y)
            if saturated:
                if saturation(rgb) <= 0.18 or max(rgb) <= 40:
                    continue
                # Agrupa em blocos de 8 para tolerar compressão e antialiasing.
                rgb = (rgb[0] // 8 * 8, rgb[1] // 8 * 8, rgb[2] // 8 * 8)
            counter[rgb] += 1

    total = sum(counter.values()) or 1
    titulo = "cores saturadas" if saturated else "cores dominantes"
    print(f"\n── {limit} {titulo} ──")
    for rgb, n in counter.most_common(limit):
        print(f"  {to_hex(rgb)}   {100 * n / total:6.2f}%   sat={saturation(rgb):.2f}")


def cmd_at(png: Png, points: list[str]) -> None:
    print("\n── pontos ──")
    for point in points:
        try:
            x, y = (int(v) for v in point.split(","))
        except ValueError:
            raise SystemExit(f"Ponto inválido: '{point}'. Use x,y.")
        rgb = png.pixel(x, y)
        print(f"  ({x:5d},{y:5d}) = {to_hex(rgb)}   sat={saturation(rgb):.2f}")


def cmd_region(png: Png, regions: list[str]) -> None:
    print("\n── regiões (média) ──")
    for region in regions:
        try:
            x0, y0, x1, y1 = (int(v) for v in region.split(","))
        except ValueError:
            raise SystemExit(f"Região inválida: '{region}'. Use x0,y0,x1,y1.")
        soma = [0, 0, 0]
        n = 0
        for y in range(min(y0, y1), max(y0, y1) + 1):
            for x in range(min(x0, x1), max(x0, x1) + 1):
                r, g, b = png.pixel(x, y)
                soma[0] += r
                soma[1] += g
                soma[2] += b
                n += 1
        media = tuple(v // max(n, 1) for v in soma)
        print(f"  [{x0},{y0} → {x1},{y1}]  média = {to_hex(media)}  ({n} px)")


def cmd_contrast(pair: list[str]) -> None:
    a, b = parse_hex(pair[0]), parse_hex(pair[1])
    ratio = contrast_ratio(a, b)
    normal = "PASSA" if ratio >= 4.5 else "FALHA"
    grande = "PASSA" if ratio >= 3.0 else "FALHA"
    print(f"\n── contraste WCAG ──")
    print(f"  {to_hex(a)} sobre {to_hex(b)} = {ratio:.2f}:1")
    print(f"  Texto normal (≥ 4.5:1): {normal}")
    print(f"  Texto grande (≥ 3.0:1): {grande}")


def main() -> int:
    ap = argparse.ArgumentParser(
        description="Amostra cores de um print de design para o MNS Design Contract.",
    )
    ap.add_argument("image", nargs="?", help="caminho do PNG")
    ap.add_argument("--top", type=int, default=0, metavar="N",
                    help="lista as N cores dominantes")
    ap.add_argument("--saturated", action="store_true",
                    help="considera apenas cores saturadas (filtra cinzas e fotos claras)")
    ap.add_argument("--step", type=int, default=2, metavar="N",
                    help="amostra 1 pixel a cada N (padrão 2; use 1 para precisão máxima)")
    ap.add_argument("--at", action="append", default=[], metavar="X,Y",
                    help="cor exata em um ponto (pode repetir)")
    ap.add_argument("--region", action="append", default=[], metavar="X0,Y0,X1,Y1",
                    help="cor média de uma região (pode repetir)")
    ap.add_argument("--contrast", nargs=2, metavar=("COR_A", "COR_B"),
                    help="razão de contraste WCAG entre duas cores; dispensa a imagem")
    args = ap.parse_args()

    if args.contrast:
        cmd_contrast(args.contrast)
        return 0

    if not args.image:
        ap.error("informe a imagem, ou use --contrast.")

    png = Png(Path(args.image))
    print(f"{args.image}  {png.width}×{png.height}  colorType={png.color_type}")

    if args.top or args.saturated:
        cmd_top(png, args.top or 20, args.saturated, max(1, args.step))
    if args.at:
        cmd_at(png, args.at)
    if args.region:
        cmd_region(png, args.region)
    if not (args.top or args.saturated or args.at or args.region):
        cmd_top(png, 20, False, max(1, args.step))
        cmd_top(png, 12, True, max(1, args.step))
    return 0


if __name__ == "__main__":
    sys.exit(main())
