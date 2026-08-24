#!/usr/bin/env python3
"""
Gera as páginas de `docs/components/**` a partir do KDoc do design system.

Motivo de existir: uma tabela de parâmetros escrita à mão desatualiza no
primeiro pull request que adiciona um parâmetro. Aqui a fonte da verdade é o
código — o KDoc `@param` vira a coluna "Descrição", e a assinatura vira as
colunas "Tipo" e "Padrão".

Uso:
    python3 tools/generate_component_docs.py            # gera as páginas
    python3 tools/generate_component_docs.py --check    # falha se houver drift

O modo `--check` roda na CI: se alguém mexer em um parâmetro sem regerar a
documentação, o build quebra com a lista exata dos arquivos defasados.
"""
from __future__ import annotations

import argparse
import re
import sys
from dataclasses import dataclass, field
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "design_system/src/main/java/com/mns/designsystem"
DOCS = ROOT / "docs/components"
TOKEN_DOCS = ROOT / "docs/tokens"

# ── Mapa doc → (título, subtítulo, arquivos-fonte, símbolos exibidos) ─────────
# A ordem dos símbolos é a ordem em que aparecem na página.
PAGES: dict[str, dict] = {
    "action/mns-button.md": {
        "title": "MnsButton",
        "source": ["component/action/MnsButton.kt"],
        "symbols": ["MnsButton"],
        "enums": ["MnsButtonVariant", "MnsButtonSize"],
    },
    "action/mns-icon-button.md": {
        "title": "MnsIconButton",
        "source": ["component/action/MnsButton.kt"],
        "symbols": ["MnsIconButton"],
        "enums": [],
    },
    "action/mns-fab.md": {
        "title": "MnsFab",
        "source": ["component/action/MnsFab.kt"],
        "symbols": ["MnsFab"],
        "enums": [],
    },
    "action/mns-segmented-control.md": {
        "title": "MnsSegmentedControl",
        "source": ["component/action/MnsSegmentedControl.kt"],
        "symbols": ["MnsSegmentedControl"],
        "enums": [],
        "data": ["MnsSegment"],
    },
    "input/mns-text-field.md": {
        "title": "MnsTextField",
        "source": ["component/input/MnsTextField.kt"],
        "symbols": ["MnsTextField"],
        "enums": [],
    },
    "input/mns-search-field.md": {
        "title": "MnsSearchField",
        "source": ["component/input/MnsSpecializedFields.kt"],
        "symbols": ["MnsSearchField"],
        "enums": [],
    },
    "input/mns-currency-field.md": {
        "title": "MnsCurrencyField",
        "source": ["component/input/MnsSpecializedFields.kt"],
        "symbols": ["MnsCurrencyField"],
        "enums": [],
    },
    "input/mns-password-field.md": {
        "title": "MnsPasswordField",
        "source": ["component/input/MnsSpecializedFields.kt"],
        "symbols": ["MnsPasswordField"],
        "enums": [],
    },
    "input/mns-otp-field.md": {
        "title": "MnsOtpField",
        "source": ["component/input/MnsSpecializedFields.kt"],
        "symbols": ["MnsOtpField"],
        "enums": [],
    },
    "input/mns-selection-controls.md": {
        "title": "MnsCheckbox · MnsRadioButton · MnsSwitch",
        "source": ["component/input/MnsSelectionControls.kt"],
        "symbols": ["MnsCheckbox", "MnsRadioButton", "MnsSwitch", "MnsSelectionGroup"],
        "enums": ["MnsToggleState"],
    },
    "input/mns-chip.md": {
        "title": "MnsChip",
        "source": ["component/input/MnsChip.kt"],
        "symbols": ["MnsChip", "MnsChipRow"],
        "enums": [],
    },
    "input/mns-stepper.md": {
        "title": "MnsStepper",
        "source": ["component/input/MnsStepper.kt"],
        "symbols": ["MnsStepper"],
        "enums": [],
    },
    "input/mns-slider.md": {
        "title": "MnsSlider",
        "source": ["component/input/MnsSlider.kt"],
        "symbols": ["MnsSlider"],
        "enums": [],
    },
    "text/mns-text.md": {
        "title": "MnsText",
        "source": ["component/text/MnsText.kt"],
        "symbols": ["MnsText"],
        "enums": [],
    },
    "text/mns-heading.md": {
        "title": "MnsHeading",
        "source": ["component/text/MnsHeading.kt"],
        "symbols": ["MnsHeading"],
        "enums": ["MnsHeadingLevel"],
    },
    "text/mns-section-header.md": {
        "title": "MnsSectionHeader",
        "source": ["component/text/MnsHeading.kt"],
        "symbols": ["MnsSectionHeader"],
        "enums": [],
    },
    "text/mns-currency-text.md": {
        "title": "MnsCurrencyText",
        "source": ["component/text/MnsFormattedText.kt"],
        "symbols": ["MnsCurrencyText"],
        "enums": [],
    },
    "text/mns-percent-text.md": {
        "title": "MnsPercentText",
        "source": ["component/text/MnsFormattedText.kt"],
        "symbols": ["MnsPercentText"],
        "enums": [],
    },
    "text/mns-compact-number-text.md": {
        "title": "MnsCompactNumberText",
        "source": ["component/text/MnsFormattedText.kt"],
        "symbols": ["MnsCompactNumberText"],
        "enums": [],
    },
    "status/mns-badge.md": {
        "title": "MnsBadge",
        "source": ["component/status/MnsBadge.kt"],
        "symbols": ["MnsBadge", "MnsBadgedBox"],
        "enums": [],
    },
    "status/mns-tag.md": {
        "title": "MnsTag",
        "source": ["component/status/MnsBadge.kt"],
        "symbols": ["MnsTag"],
        "enums": [],
    },
    "status/mns-alert.md": {
        "title": "MnsAlert",
        "source": ["component/status/MnsAlert.kt"],
        "symbols": ["MnsAlert"],
        "enums": [],
    },
    "status/mns-progress.md": {
        "title": "MnsCircularProgress · MnsLinearProgress",
        "source": ["component/status/MnsProgress.kt"],
        "symbols": ["MnsCircularProgress", "MnsLinearProgress"],
        "enums": [],
    },
    "status/mns-rating.md": {
        "title": "MnsRating",
        "source": ["component/status/MnsEmptyState.kt"],
        "symbols": ["MnsRating"],
        "enums": [],
    },
    "status/mns-empty-state.md": {
        "title": "MnsEmptyState",
        "source": ["component/status/MnsEmptyState.kt"],
        "symbols": ["MnsEmptyState"],
        "enums": [],
    },
    "layout/mns-surface.md": {
        "title": "MnsSurface",
        "source": ["component/layout/MnsSurface.kt"],
        "symbols": ["MnsSurface"],
        "enums": [],
    },
    "layout/mns-card.md": {
        "title": "MnsCard",
        "source": ["component/layout/MnsCard.kt"],
        "symbols": ["MnsCard"],
        "enums": ["MnsCardVariant"],
    },
    "layout/mns-top-bar.md": {
        "title": "MnsTopBar",
        "source": ["component/layout/MnsTopBar.kt"],
        "symbols": ["MnsTopBar"],
        "enums": ["MnsTopBarAlignment"],
    },
    "layout/mns-bottom-nav-bar.md": {
        "title": "MnsBottomNavBar",
        "source": ["component/layout/MnsBottomNavBar.kt"],
        "symbols": ["MnsBottomNavBar"],
        "enums": [],
        "data": ["MnsNavItem"],
    },
    "layout/mns-tab-bar.md": {
        "title": "MnsTabBar · MnsFixedTabBar",
        "source": ["component/layout/MnsTabBar.kt"],
        "symbols": ["MnsTabBar", "MnsFixedTabBar"],
        "enums": [],
        "data": ["MnsTab"],
    },
    "layout/mns-bottom-sheet.md": {
        "title": "MnsBottomSheet",
        "source": ["component/layout/MnsBottomSheet.kt"],
        "symbols": ["MnsBottomSheet", "MnsSheetHeader", "MnsSheetHandle"],
        "enums": [],
    },
    "layout/mns-dialog.md": {
        "title": "MnsDialog · MnsConfirmDialog",
        "source": ["component/layout/MnsDialog.kt"],
        "symbols": ["MnsDialog", "MnsConfirmDialog"],
        "enums": [],
    },
    "layout/mns-divider.md": {
        "title": "MnsDivider",
        "source": ["component/layout/MnsDivider.kt"],
        "symbols": ["MnsDivider", "MnsVerticalDivider", "MnsLabeledDivider"],
        "enums": [],
    },
    "layout/mns-scaffold.md": {
        "title": "MnsScaffold",
        "source": ["component/layout/MnsScaffold.kt"],
        "symbols": ["MnsScaffold", "MnsScreenColumn"],
        "enums": ["MnsFabPosition"],
    },
    "list/mns-list-action.md": {
        "title": "MnsListAction",
        "source": ["component/list/MnsListAction.kt"],
        "symbols": ["MnsListAction"],
        "enums": [],
        "sealed": ["MnsListLeading"],
    },
    "list/mns-avatar.md": {
        "title": "MnsAvatar · MnsAvatarGroup",
        "source": ["component/list/MnsAvatar.kt"],
        "symbols": ["MnsAvatar", "MnsAvatarGroup"],
        "enums": [],
    },
    "shortcut/mns-shortcut-card.md": {
        "title": "MnsShortcutCard",
        "source": ["component/shortcut/MnsShortcutCard.kt"],
        "symbols": ["MnsShortcutCard"],
        "enums": [],
        "data": ["MnsShortcut"],
    },
    "shortcut/mns-shortcut-grid.md": {
        "title": "MnsShortcutGrid",
        "source": ["component/shortcut/MnsShortcutCard.kt"],
        "symbols": ["MnsShortcutGrid"],
        "enums": [],
    },
    "loading/mns-shimmer.md": {
        "title": "mnsShimmer · MnsShimmerBox",
        "source": ["component/loading/MnsShimmer.kt"],
        "symbols": ["mnsShimmer", "MnsShimmerBox"],
        "enums": [],
    },
    "loading/mns-skeletons.md": {
        "title": "MnsShimmerParagraph · MnsShimmerListItem · MnsShimmerCard",
        "source": ["component/loading/MnsShimmer.kt"],
        "symbols": ["MnsShimmerParagraph", "MnsShimmerListItem", "MnsShimmerCard"],
        "enums": [],
    },
    "code/mns-qr-code.md": {
        "title": "MnsQrCode",
        "source": ["component/code/MnsQrCode.kt", "component/code/MnsQrEncoder.kt"],
        "symbols": ["MnsQrCode"],
        "enums": ["MnsQrDotStyle", "MnsQrErrorCorrection"],
    },
    "code/mns-ticket-card.md": {
        "title": "MnsTicketCard",
        "source": ["component/code/MnsTicketCard.kt"],
        "symbols": ["MnsTicketCard", "MnsTicketPerforation"],
        "enums": [],
    },
    "media/mns-icon.md": {
        "title": "MnsIcon · MnsIcons",
        "source": ["component/media/MnsIcon.kt"],
        "symbols": ["MnsIcon"],
        "enums": [],
    },
    "media/mns-async-image.md": {
        "title": "MnsAsyncImage",
        "source": ["component/media/MnsAsyncImage.kt"],
        "symbols": ["MnsAsyncImage"],
        "enums": [],
    },
    "media/mns-cover.md": {
        "title": "MnsCover",
        "source": ["component/media/MnsCover.kt"],
        "symbols": ["MnsCover"],
        "enums": [],
    },
}


# Parâmetros que se repetem em quase todo componente e cujo KDoc seria ruído
# se replicado 40 vezes. A descrição canônica mora aqui.
FALLBACK_DOCS: dict[str, str] = {
    "modifier": "`Modifier` aplicado ao nó raiz do componente.",
    "enabled": "Quando `false`, o componente ignora interação e reduz a opacidade.",
    "shape": "Forma do componente. Use um papel de `MnsTheme.shapes`.",
    "content": "Slot de conteúdo do componente.",
    "interactionSource": (
        "Fonte de interação. Injete a sua para observar ou compartilhar os "
        "estados de toque/foco com outro elemento."
    ),
    "contentColor": "Cor herdada por textos e ícones filhos.",
    "containerColor": "Cor de fundo do componente.",
    "textStyle": "Estilo de texto. Use um papel de `MnsTheme.typography`.",
    "style": "Papel tipográfico. Use `MnsTheme.typography.*`.",
    "color": "Cor do conteúdo. `Color.Unspecified` herda do contexto.",
    "size": "Dimensão do componente. Use um degrau de `MnsTheme.sizing`.",
    "elevation": "Altura da sombra. Use um degrau de `MnsTheme.elevation`.",
    "onClick": "Ação disparada no toque.",
    "onValueChange": "Chamado a cada alteração do valor.",
    "keyboardOptions": "Tipo de teclado e ação de IME.",
    "keyboardActions": "Callbacks das ações de IME.",
    "maxLines": "Número máximo de linhas antes de aplicar o overflow.",
    "minLines": "Número mínimo de linhas reservadas.",
    "overflow": "O que fazer quando o texto não cabe.",
    "textAlign": "Alinhamento horizontal do texto.",
    "softWrap": "Quando `false`, o texto nunca quebra linha.",
    "visible": "Liga/desliga o efeito sem remover o componente da árvore.",
    "contentDescription": "Descrição para leitores de tela.",
    "label": "Rótulo textual do componente.",
    "modifierOrNull": "",
}


# ── Páginas de token ─────────────────────────────────────────────────────────
# Mesma ideia das páginas de componente: a tabela sai do KDoc `@property`.
# O campo `intro` é o único texto escrito à mão — o "porquê" daquele grupo.
TOKEN_PAGES: dict[str, dict] = {
    "colors.md": {
        "title": "Tokens de cor",
        "source": ["token/MnsColors.kt", "token/MnsColorsFactory.kt"],
        "data": ["MnsColors"],
        "enums": ["MnsStatus"],
        "intro": (
            "`MnsColors` é o **vocabulário completo de cor** que os componentes "
            "conhecem. Nenhum componente do MNS pergunta \"qual é o roxo?\"; ele "
            "pergunta \"qual é a cor de ação primária?\".\n\n"
            "Os campos são todos obrigatórios de propósito — um design system que "
            "aceita token faltando termina com `Color.Unspecified` invisível em "
            "produção. Para não escrever 60 cores à mão, use as fábricas "
            "`MnsColors.light(...)` / `MnsColors.dark(...)`, que derivam por "
            "interpolação tudo que você não informar:\n\n"
            "```kotlin\n"
            "val cores = MnsColors.light(\n"
            "    primary = Color(0xFF6255F4),\n"
            "    accent = Color(0xFFA197FF),\n"
            "    background = Color(0xFFF8F8F8),\n"
            ")\n"
            "```\n\n"
            "Para ler uma cor pelo nome (útil em ferramentas e no Design Contract), "
            "use `colors.byRole(\"primaryContainer\")`. A lista completa está em "
            "`MnsColors.roleNames`."
        ),
    },
    "palette.md": {
        "title": "Paleta e utilitários de cor",
        "source": ["token/MnsPalette.kt"],
        "data": ["MnsPalette", "MnsColorRamp"],
        "intro": (
            "A camada de **reference tokens**: valores crus, sem significado de "
            "interface. Nada na UI consome uma rampa diretamente — a rampa "
            "alimenta `MnsColors`, e é `MnsColors` que os componentes leem.\n\n"
            "```\nMnsPalette (cru)  →  MnsColors (semântico)  →  Componente\n"
            "#6255F4           →  colors.primary          →  MnsButton\n```\n\n"
            "O módulo também expõe utilitários de acessibilidade: "
            "`contrastRatio(a, b)` (WCAG 2.1), `Color.relativeLuminance()` e "
            "`Color.contentColorFor()`, que escolhe entre claro e escuro o tom de "
            "maior contraste sobre uma cor arbitrária."
        ),
    },
    "typography.md": {
        "title": "Tokens de tipografia",
        "source": ["token/MnsTypography.kt"],
        "data": ["MnsTypography"],
        "intro": (
            "A escala é **fechada em 18 papéis**. Se um design pede um tamanho que "
            "não existe aqui, a resposta certa quase sempre é usar o papel mais "
            "próximo — não criar o 19º. Escala aberta é a principal causa de deriva "
            "visual em design system longevo.\n\n"
            "Trocar a fonte do produto inteiro é uma linha:\n\n"
            "```kotlin\n"
            "val tipografia = MnsTypography.default(\n"
            "    fontFamily = FontFamily(Font(R.font.inter)),\n"
            ")\n"
            "```\n\n"
            "E densificar (tablet, dashboard) é outra: "
            "`MnsTypography.default().scaledBy(0.9f)`."
        ),
    },
    "shapes.md": {
        "title": "Tokens de forma",
        "source": ["token/MnsShapes.kt"],
        "data": ["MnsShapes"],
        "intro": (
            "Duas camadas em um único objeto: a **escala** (`none` → `full`), que é "
            "o vocabulário cru de raios, e os **papéis de componente** (`button`, "
            "`card`, `input`…), que é o que os componentes realmente consomem.\n\n"
            "Essa separação é o que viabiliza um redesenho do tipo *\"cards ficam "
            "mais arredondados, botões continuam iguais\"* mexendo em um campo só.\n\n"
            "Na prática você quase nunca constrói `MnsShapes` à mão — informa o raio "
            "dos cards e deixa a escala ser derivada:\n\n"
            "```kotlin\n"
            "MnsShapes.fromBaseRadius(base = 12.dp, buttonRadius = 16.dp)\n"
            "MnsShapes.fromBaseRadius(base = 20.dp, pillButtons = true)\n"
            "```"
        ),
    },
    "spacing.md": {
        "title": "Tokens de espaçamento",
        "source": ["token/MnsSpacing.kt"],
        "data": ["MnsSpacing"],
        "intro": (
            "Grade de 4dp. Todo `padding`, margem e `Arrangement.spacedBy` do design "
            "system sai daqui — nunca de um `16.dp` literal dentro do componente. "
            "É isso que permite densificar o app inteiro em uma linha:\n\n"
            "```kotlin\n"
            "MnsSpacing.Compact       // ~75% — tablet, dashboard, listas longas\n"
            "MnsSpacing.Default       // referência\n"
            "MnsSpacing.Comfortable   // ~125% — onboarding, telas de destaque\n"
            "MnsSpacing.Default.scaledBy(0.9f)\n"
            "```"
        ),
    },
    "sizing.md": {
        "title": "Tokens de dimensão",
        "source": ["token/MnsSizing.kt"],
        "data": ["MnsSizing"],
        "intro": (
            "Alturas mínimas, tamanhos de ícone e de avatar. Fica separado do "
            "espaçamento porque **alvo de toque é regra de acessibilidade, não de "
            "estética**: `touchTarget` nunca deve cair abaixo de 48dp (WCAG 2.5.5). "
            "Um teste de integração cobra esse piso."
        ),
    },
    "elevation.md": {
        "title": "Tokens de elevação",
        "source": ["token/MnsElevation.kt"],
        "data": ["MnsElevation"],
        "intro": (
            "Além do valor em dp, o token carrega `shadowAlpha` e `ambientAlpha`, "
            "porque a mesma elevação precisa de sombras muito diferentes em tema "
            "claro e escuro — no escuro a sombra praticamente some e quem separa as "
            "camadas é a cor da superfície. Componentes que respeitam isso não "
            "\"flutuam errado\" no dark mode.\n\n"
            "Presets: `MnsElevation.Light`, `MnsElevation.Dark` e "
            "`MnsElevation.Flat` (nenhuma sombra; hierarquia só por borda e cor)."
        ),
    },
    "borders.md": {
        "title": "Tokens de traço",
        "source": ["token/MnsBorders.kt"],
        "data": ["MnsBorders"],
        "intro": (
            "Espessuras de borda e de divisor. Manter isso fora do espaçamento evita "
            "o erro clássico de um redesenho de grade engrossar todas as linhas do "
            "app de uma vez."
        ),
    },
    "opacity.md": {
        "title": "Tokens de opacidade",
        "source": ["token/MnsOpacity.kt"],
        "data": ["MnsOpacity"],
        "intro": (
            "Centraliza os alphas de estado. Sem isso, cada componente inventa o seu "
            "`0.38f` e o app termina com cinco cinzas de \"desabilitado\" "
            "ligeiramente diferentes."
        ),
    },
    "motion.md": {
        "title": "Tokens de movimento",
        "source": ["token/MnsMotion.kt"],
        "data": ["MnsMotion"],
        "intro": (
            "Durações e curvas. Além de padronizar, existe por um motivo de "
            "acessibilidade: com `reduceMotion` ligado, **todas** as durações "
            "efetivas viram zero e o app para de animar sem que nenhum componente "
            "precise saber disso.\n\n"
            "```kotlin\n"
            "val spec = MnsTheme.motion.tween<Float>(MnsTheme.motion.durationNormal)\n"
            "```\n\n"
            "Componentes do MNS **nunca** chamam `tween(...)` diretamente — sempre "
            "`MnsTheme.motion.tween(...)`, que é o que aplica `reduceMotion`."
        ),
    },
}


@dataclass
class Param:
    name: str
    type: str
    default: str | None
    doc: str = ""


@dataclass
class Symbol:
    name: str
    kind: str            # "fun" | "enum" | "data" | "sealed"
    doc: str = ""
    params: list[Param] = field(default_factory=list)
    entries: list[tuple[str, str]] = field(default_factory=list)  # enums e sealed


def strip_kdoc(block: str) -> tuple[str, dict[str, str]]:
    """Separa o corpo do KDoc das tags @param. Devolve (descrição, {param: doc})."""
    lines = []
    for raw in block.splitlines():
        line = raw.strip()
        line = re.sub(r"^/\*\*+", "", line)
        line = re.sub(r"\*/$", "", line)
        line = re.sub(r"^\*\s?", "", line)
        lines.append(line.rstrip())
    text = "\n".join(lines).strip()

    params: dict[str, str] = {}
    body: list[str] = []
    current: str | None = None
    for line in text.splitlines():
        tag = re.match(r"@(param|property)\s+(\[?)([A-Za-z0-9_]+)\]?\s*(.*)", line.strip())
        if tag:
            current = tag.group(3)
            params[current] = tag.group(4).strip()
        elif line.strip().startswith("@"):
            current = None
        elif current is not None:
            if line.strip():
                params[current] += " " + line.strip()
            else:
                current = None
        else:
            body.append(line)
    return "\n".join(body).strip(), params


def scan(text: str):
    """
    Itera sobre `text` devolvendo (índice, caractere, profundidade).

    Trata `->` como um token só: sem isso, o `>` de uma lambda `() -> Unit`
    seria contado como fechamento de generic e toda a divisão de parâmetros
    sairia errada — foi exatamente o bug que motivou este helper.
    """
    depth = 0
    angle = 0
    in_str = False
    i = 0
    while i < len(text):
        ch = text[i]
        if ch == '"':
            in_str = not in_str
            yield i, ch, depth + angle
            i += 1
            continue
        if in_str:
            yield i, ch, depth + angle
            i += 1
            continue
        if ch == "-" and i + 1 < len(text) and text[i + 1] == ">":
            # A seta de lambda não altera profundidade; devolvemos os dois
            # caracteres originais para que o texto reconstruído fique intacto.
            yield i, "-", depth + angle
            yield i + 1, ">", depth + angle
            i += 2
            continue
        if ch in "([{":
            depth += 1
            yield i, ch, depth + angle
        elif ch in ")]}":
            depth -= 1
            yield i, ch, depth + angle
        elif ch == "<":
            angle += 1
            yield i, ch, depth + angle
        elif ch == ">":
            if angle > 0:
                angle -= 1
            yield i, ch, depth + angle
        else:
            yield i, ch, depth + angle
        i += 1


def split_params(signature: str) -> list[tuple[str, str, str | None]]:
    """Divide a lista de parâmetros respeitando parênteses, generics e lambdas."""
    chunks: list[str] = []
    buf = ""
    for _, ch, depth in scan(signature):
        if ch == "," and depth == 0:
            chunks.append(buf)
            buf = ""
            continue
        buf += ch
    if buf.strip():
        chunks.append(buf)

    parsed = []
    for chunk in chunks:
        chunk = chunk.strip()
        if not chunk:
            continue
        chunk = re.sub(r"^(?:@\w+\s+)*(?:public\s+|internal\s+)?(?:vararg|crossinline|noinline|val|var)\s+", "", chunk)
        colon = find_top_level(chunk, ":")
        if colon is None:
            continue
        name = chunk[:colon].strip()
        rest = chunk[colon + 1:].strip()
        default = None
        eq = find_top_level(rest, "=")
        if eq is not None:
            default = rest[eq + 1:].strip()
            rest = rest[:eq].strip()
        parsed.append((name, rest, default))
    return parsed


def find_top_level(text: str, token: str) -> int | None:
    """Índice da primeira ocorrência de `token` fora de qualquer aninhamento."""
    for i, ch, depth in scan(text):
        if ch != token or depth != 0:
            continue
        if token == "=":
            before = text[i - 1] if i > 0 else ""
            after = text[i + 1] if i + 1 < len(text) else ""
            if before in "=!<>" or after == "=":
                continue
        return i
    return None


def parse_file(path: Path) -> dict[str, Symbol]:
    text = path.read_text(encoding="utf-8")
    symbols: dict[str, Symbol] = {}

    kdoc_re = re.compile(r"/\*\*(?:.|\n)*?\*/", re.MULTILINE)
    docs: list[tuple[int, int, str]] = [(m.start(), m.end(), m.group(0)) for m in kdoc_re.finditer(text)]

    def doc_before(pos: int) -> str:
        best = ""
        for start, end, block in docs:
            between = text[end:pos]
            if end <= pos and between.strip() in ("", "@Composable", "@Composable\n") \
                    or (end <= pos and re.fullmatch(r"[\s]*(@[A-Za-z0-9_.()\[\]:,= ]+\s*)*", between)):
                best = block
        return best

    # Funções (inclui extensões: `fun Modifier.mnsShimmer(`)
    for m in re.finditer(
        r"^public (?:inline )?fun\s+(?:<[^>]+>\s*)?(?:[A-Za-z0-9_.]+\.)?([A-Za-z0-9_]+)\s*\(",
        text, re.MULTILINE,
    ):
        name = m.group(1)
        if name in symbols:
            continue
        open_paren = m.end() - 1
        close = matching(text, open_paren)
        sig = text[open_paren + 1:close]
        body, param_docs = strip_kdoc(doc_before(m.start()))
        params = [
            Param(n, t, d, param_docs.get(n) or FALLBACK_DOCS.get(n, ""))
            for n, t, d in split_params(sig)
        ]
        symbols[name] = Symbol(name=name, kind="fun", doc=body, params=params)

    # Enums
    for m in re.finditer(r"^public enum class ([A-Za-z0-9_]+)\s*(?:\([^)]*\))?\s*\{", text, re.MULTILINE):
        name = m.group(1)
        close = matching(text, m.end() - 1)
        block = text[m.end():close]
        body, _ = strip_kdoc(doc_before(m.start()))
        entries = []
        for em in re.finditer(r"/\*\*(.*?)\*/\s*\n\s*([A-Z][A-Z0-9_]*)\s*(?:\([^)]*\))?\s*[,;]", block, re.DOTALL):
            desc, _ = strip_kdoc("/**" + em.group(1) + "*/")
            entries.append((em.group(2), desc.replace("\n", " ").strip()))
        symbols[name] = Symbol(name=name, kind="enum", doc=body, entries=entries)

    # data class
    for m in re.finditer(r"^public data class ([A-Za-z0-9_]+)\s*\(", text, re.MULTILINE):
        name = m.group(1)
        close = matching(text, m.end() - 1)
        sig = text[m.end():close]
        body, param_docs = strip_kdoc(doc_before(m.start()))
        params = [
            Param(n, t, d, param_docs.get(n) or FALLBACK_DOCS.get(n, ""))
            for n, t, d in split_params(sig)
        ]
        symbols[name] = Symbol(name=name, kind="data", doc=body, params=params)

    # sealed interface + subtipos
    for m in re.finditer(r"^public sealed interface ([A-Za-z0-9_]+)\s*\{", text, re.MULTILINE):
        name = m.group(1)
        close = matching(text, m.end() - 1)
        block = text[m.end():close]
        body, _ = strip_kdoc(doc_before(m.start()))
        entries = []
        for em in re.finditer(
            r"/\*\*(.*?)\*/\s*\n\s*public data (?:class|object) ([A-Za-z0-9_]+)", block, re.DOTALL,
        ):
            desc, _ = strip_kdoc("/**" + em.group(1) + "*/")
            entries.append((em.group(2), desc.replace("\n", " ").strip()))
        symbols[name] = Symbol(name=name, kind="sealed", doc=body, entries=entries)

    return symbols


def matching(text: str, open_index: int) -> int:
    pairs = {"(": ")", "{": "}", "[": "]"}
    opener = text[open_index]
    closer = pairs[opener]
    depth = 0
    in_str = False
    i = open_index
    while i < len(text):
        ch = text[i]
        if ch == '"':
            in_str = not in_str
        if not in_str:
            if ch == opener:
                depth += 1
            elif ch == closer:
                depth -= 1
                if depth == 0:
                    return i
        i += 1
    raise ValueError("delimitador não fechado")


KDOC_LINK = re.compile(r"\[([A-Za-z][A-Za-z0-9_.]*)\](?!\()")


def kdoc_links(text: str) -> str:
    """Converte referências KDoc `[Simbolo]` em código inline."""
    return KDOC_LINK.sub(r"`\1`", text)


def md_escape(text: str) -> str:
    return kdoc_links(text).replace("|", "\\|").replace("\n", " ").strip()


def render_page(rel: str, cfg: dict, symbols: dict[str, Symbol]) -> str:
    title = cfg["title"]
    depth = "../" * (rel.count("/") + 1)
    out: list[str] = []
    out.append(f"# {title}")
    out.append("")
    out.append(
        f"> Gerado por `tools/generate_component_docs.py` a partir do KDoc. "
        f"**Não edite à mão** — altere o KDoc do componente e rode o gerador."
    )
    out.append("")
    out.append(
        f"[← Todos os componentes]({depth}components/README.md) · "
        f"[Tokens]({depth}tokens/README.md) · "
        f"[Tematização]({depth}theming.md)"
    )
    out.append("")

    for sym_name in cfg["symbols"]:
        sym = symbols.get(sym_name)
        if sym is None:
            raise SystemExit(f"[docgen] símbolo '{sym_name}' não encontrado para {rel}")
        out.append(f"## `{sym.name}`")
        out.append("")
        if sym.doc:
            out.append(kdoc_links(sym.doc))
            out.append("")
        if sym.params:
            out.append("### Parâmetros")
            out.append("")
            out.append("| Parâmetro | Tipo | Padrão | Descrição |")
            out.append("|---|---|---|---|")
            for p in sym.params:
                default = f"`{md_escape(p.default)}`" if p.default else "—"
                doc = md_escape(p.doc) or "—"
                out.append(f"| `{p.name}` | `{md_escape(p.type)}` | {default} | {doc} |")
            out.append("")

    for group_key, heading in (("data", "Modelos"), ("enums", "Enums"), ("sealed", "Tipos selados")):
        for name in cfg.get(group_key, []):
            sym = symbols.get(name)
            if sym is None:
                raise SystemExit(f"[docgen] símbolo '{name}' não encontrado para {rel}")
            out.append(f"## `{sym.name}`")
            out.append("")
            if sym.doc:
                out.append(kdoc_links(sym.doc))
                out.append("")
            if sym.entries:
                out.append("| Valor | Significado |")
                out.append("|---|---|")
                for value, desc in sym.entries:
                    out.append(f"| `{value}` | {md_escape(desc) or '—'} |")
                out.append("")
            if sym.params:
                out.append("| Propriedade | Tipo | Padrão | Descrição |")
                out.append("|---|---|---|---|")
                for p in sym.params:
                    default = f"`{md_escape(p.default)}`" if p.default else "—"
                    out.append(
                        f"| `{p.name}` | `{md_escape(p.type)}` | {default} | {md_escape(p.doc) or '—'} |"
                    )
                out.append("")

    out.append("---")
    out.append("")
    out.append("### Ver no app de demonstração")
    out.append("")
    out.append(
        "Abra o módulo `:app_demo`, navegue até a categoria correspondente e toque no "
        "componente. A tela traz um preview interativo com **todos** os parâmetros "
        "acima expostos como controles."
    )
    out.append("")
    sources = ", ".join(f"`design_system/src/main/java/com/mns/designsystem/{s}`" for s in cfg["source"])
    out.append(f"**Fonte:** {sources}")
    out.append("")
    return "\n".join(out)


CATEGORY_LABELS: dict[str, tuple[str, str]] = {
    "action": ("Ações", "Botões e gatilhos de ação."),
    "input": ("Entrada", "Coleta de dados do usuário."),
    "text": ("Texto", "Tipografia e formatação de valores."),
    "status": ("Status", "Feedback e estado do sistema."),
    "layout": ("Layout", "Estrutura e containers de tela."),
    "list": ("Listas", "Coleções e itens acionáveis."),
    "shortcut": ("Atalhos", "Grades de acesso rápido."),
    "loading": ("Carregamento", "Placeholders e progresso."),
    "code": ("Códigos", "QR Code e ingressos."),
    "media": ("Mídia", "Ícones e imagens."),
}


CATALOG_DIR = ROOT / "app_demo/src/main/java/com/mns/demo/catalog/entries"


def catalog_summaries() -> dict[str, str]:
    """
    Lê os resumos direto do catálogo do `:app_demo`.

    Evita manter a mesma frase em dois lugares: o texto que aparece na lista do
    app é exatamente o que aparece no índice da documentação. Se divergirem, é
    porque alguém mexeu em um e esqueceu do outro — e aqui isso não acontece.
    """
    summaries: dict[str, str] = {}
    for kt in sorted(CATALOG_DIR.glob("*.kt")):
        text = kt.read_text(encoding="utf-8")
        for block in re.split(r"\n\s{4}DemoComponent\(", text)[1:]:
            summary = re.search(r'summary\s*=\s*"((?:[^"\\]|\\.)*)"', block)
            doc = re.search(r'docPath\s*=\s*"docs/components/([^"]+)"', block)
            if summary and doc:
                summaries[doc.group(1)] = summary.group(1).replace('\\"', '"')
    return summaries


def render_token_page(rel: str, cfg: dict, symbols: dict[str, Symbol]) -> str:
    """Monta uma página de token: intro escrita à mão + tabela vinda do KDoc."""
    out = [
        f"# {cfg['title']}",
        "",
        "> A tabela abaixo é gerada por `tools/generate_component_docs.py` a partir "
        "do KDoc. **Não edite a tabela à mão.**",
        "",
        "[← Todos os tokens](README.md) · [Componentes](../components/README.md) · "
        "[Tematização](../theming.md)",
        "",
        cfg["intro"],
        "",
    ]
    for group_key, plural in (("data", "Propriedades"), ("enums", "Valores")):
        for name in cfg.get(group_key, []):
            sym = symbols.get(name)
            if sym is None:
                raise SystemExit(f"[docgen] símbolo '{name}' não encontrado para tokens/{rel}")
            out.append(f"## `{sym.name}`")
            out.append("")
            if sym.doc:
                out.append(kdoc_links(sym.doc))
                out.append("")
            if sym.entries:
                out.append("| Valor | Significado |")
                out.append("|---|---|")
                for value, desc in sym.entries:
                    out.append(f"| `{value}` | {md_escape(desc) or '—'} |")
                out.append("")
            if sym.params:
                out.append(f"### {plural}")
                out.append("")
                out.append("| Token | Tipo | Padrão | O que faz |")
                out.append("|---|---|---|---|")
                for p in sym.params:
                    default = f"`{md_escape(p.default)}`" if p.default else "—"
                    out.append(
                        f"| `{p.name}` | `{md_escape(p.type)}` | {default} | "
                        f"{md_escape(p.doc) or '—'} |"
                    )
                out.append("")
    sources = ", ".join(
        f"`design_system/src/main/java/com/mns/designsystem/{s}`" for s in cfg["source"]
    )
    out.append("---")
    out.append("")
    out.append(f"**Fonte:** {sources}")
    out.append("")
    return "\n".join(out)


def render_index() -> str:
    """Monta `docs/components/README.md` a partir do mesmo mapa que gera as páginas."""
    summaries = catalog_summaries()
    out = [
        "# Componentes",
        "",
        "> Gerado por `tools/generate_component_docs.py`. **Não edite à mão.**",
        "",
        "[← Documentação](../README.md) · [Tokens](../tokens/README.md) · "
        "[Tematização](../theming.md) · [Design Contract](../design-contract.md)",
        "",
        f"São **{len(PAGES)} páginas** cobrindo todos os componentes públicos do "
        "`:design_system`. Cada página traz a descrição, quando usar, e a tabela "
        "completa de parâmetros com tipo, valor padrão e o que cada um faz.",
        "",
        "Todo componente listado aqui tem uma demonstração interativa no módulo "
        "`:app_demo` — abra o app, encontre-o pela aba da categoria e mexa nos "
        "parâmetros para ver o efeito em tempo real.",
        "",
    ]
    for folder, (label, desc) in CATEGORY_LABELS.items():
        pages = [(rel, cfg) for rel, cfg in PAGES.items() if rel.startswith(f"{folder}/")]
        if not pages:
            continue
        out.append(f"## {label}")
        out.append("")
        out.append(f"_{desc}_")
        out.append("")
        out.append("| Componente | Quando usar |")
        out.append("|---|---|")
        for rel, cfg in pages:
            resumo = summaries.get(rel) or " · ".join(f"`{s}`" for s in cfg["symbols"])
            out.append(f"| [{cfg['title']}]({rel}) | {resumo} |")
        out.append("")
    return "\n".join(out)


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--check", action="store_true", help="falha se a documentação estiver defasada")
    args = ap.parse_args()

    cache: dict[str, dict[str, Symbol]] = {}
    stale: list[str] = []

    for rel, cfg in PAGES.items():
        symbols: dict[str, Symbol] = {}
        for src in cfg["source"]:
            if src not in cache:
                cache[src] = parse_file(SRC / src)
            symbols.update(cache[src])
        content = render_page(rel, cfg, symbols)
        target = DOCS / rel
        target.parent.mkdir(parents=True, exist_ok=True)
        if args.check:
            if not target.exists() or target.read_text(encoding="utf-8") != content:
                stale.append(rel)
        else:
            target.write_text(content, encoding="utf-8")

    for rel, cfg in TOKEN_PAGES.items():
        symbols: dict[str, Symbol] = {}
        for src in cfg["source"]:
            if src not in cache:
                cache[src] = parse_file(SRC / src)
            symbols.update(cache[src])
        content = render_token_page(rel, cfg, symbols)
        target = TOKEN_DOCS / rel
        target.parent.mkdir(parents=True, exist_ok=True)
        if args.check:
            if not target.exists() or target.read_text(encoding="utf-8") != content:
                stale.append(f"tokens/{rel}")
        else:
            target.write_text(content, encoding="utf-8")

    index = render_index()
    index_path = DOCS / "README.md"
    if args.check:
        if not index_path.exists() or index_path.read_text(encoding="utf-8") != index:
            stale.append("README.md")
    else:
        index_path.write_text(index, encoding="utf-8")

    if args.check:
        if stale:
            print("Documentação defasada. Rode: python3 tools/generate_component_docs.py")
            for rel in stale:
                print(f"  - docs/components/{rel}")
            return 1
        print(f"OK — {len(PAGES) + len(TOKEN_PAGES)} páginas em dia.")
        return 0

    print(f"Geradas {len(PAGES)} páginas em docs/components/ e {len(TOKEN_PAGES)} em docs/tokens/.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
