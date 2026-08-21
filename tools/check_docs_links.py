#!/usr/bin/env python3
"""
Verifica os links internos da documentação.

Um índice com link quebrado é pior que um índice ausente: ele promete e não
entrega. Este script percorre todo `.md` do repositório e confirma que cada
link relativo aponta para um arquivo que existe.

Links dentro de blocos de código são ignorados — ali eles são exemplo, não
navegação.

Uso:
    python3 tools/check_docs_links.py
"""
from __future__ import annotations

import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
# `.claude/worktrees/` guarda cópias do próprio repositório: percorrê-lo
# duplicaria a contagem e verificaria os mesmos arquivos duas vezes.
IGNORAR = ("build/", ".gradle/", ".git/", "node_modules/", ".claude/worktrees/")
LINK = re.compile(r"\[([^\]]*)\]\(([^)]+)\)")
FENCE = re.compile(r"^\s*(```|~~~)")


def sem_blocos_de_codigo(texto: str) -> str:
    """Devolve o texto com o conteúdo das cercas de código removido."""
    saida = []
    dentro = False
    for linha in texto.splitlines():
        if FENCE.match(linha):
            dentro = not dentro
            continue
        saida.append("" if dentro else linha)
    return "\n".join(saida)


def main() -> int:
    quebrados: list[str] = []
    verificados = 0

    for md in sorted(ROOT.rglob("*.md")):
        rel = md.relative_to(ROOT).as_posix()
        if any(parte in rel for parte in IGNORAR):
            continue
        texto = sem_blocos_de_codigo(md.read_text(encoding="utf-8"))
        for m in LINK.finditer(texto):
            alvo = m.group(2).split("#")[0].strip()
            if not alvo or alvo.startswith(("http://", "https://", "mailto:")):
                continue
            verificados += 1
            if not (md.parent / alvo).resolve().exists():
                quebrados.append(f"{rel}: [{m.group(1)}]({alvo})")

    if quebrados:
        print(f"{len(quebrados)} link(s) interno(s) quebrado(s):")
        for item in quebrados:
            print(f"  - {item}")
        return 1

    print(f"OK — {verificados} links internos verificados, nenhum quebrado.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
