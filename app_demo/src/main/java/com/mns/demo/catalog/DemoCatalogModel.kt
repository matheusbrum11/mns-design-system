package com.mns.demo.catalog

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.mns.demo.playground.DemoKnob
import com.mns.demo.playground.DemoKnobState

/**
 * Agrupamento de componentes na tela inicial.
 *
 * As categorias seguem a taxonomia usual de design systems (Material, Carbon,
 * Polaris): agrupar por **função na interface**, não por complexidade nem por
 * ordem alfabética. Quem procura um componente sabe o que quer fazer, não como
 * ele se chama.
 */
public enum class DemoCategory(
    public val id: String,
    public val label: String,
    public val description: String,
) {
    /** Botões, FAB, controle segmentado. */
    ACTION("action", "Ações", "Botões e gatilhos de ação"),

    /** Campos, seleção, chips, sliders. */
    INPUT("input", "Entrada", "Coleta de dados do usuário"),

    /** Texto, títulos, valores formatados. */
    TEXT("text", "Texto", "Tipografia e formatação de valores"),

    /** Badges, tags, alertas, progresso, estado vazio. */
    STATUS("status", "Status", "Feedback e estado do sistema"),

    /** Scaffold, cards, sheets, dialogs, barras. */
    LAYOUT("layout", "Layout", "Estrutura e containers de tela"),

    /** Itens de lista, avatares. */
    LIST("list", "Listas", "Coleções e itens acionáveis"),

    /** Atalhos e grades de categoria. */
    SHORTCUT("shortcut", "Atalhos", "Grades de acesso rápido"),

    /** Shimmer e esqueletos. */
    LOADING("loading", "Carregamento", "Placeholders e progresso"),

    /** QR Code, ingressos. */
    CODE("code", "Códigos", "QR Code e ingressos"),

    /** Ícones, capas, imagens. */
    MEDIA("media", "Mídia", "Ícones e imagens"),
}

/**
 * Uma entrada do catálogo.
 *
 * @property id slug estável, usado na rota de navegação e nos testes.
 * @property name nome do componente, igual ao da função `@Composable`.
 * @property category agrupamento na home.
 * @property summary uma linha explicando quando usar.
 * @property docPath caminho do documento em `docs/`, para o link "Ver documentação".
 * @property knobs parâmetros expostos no painel interativo.
 * @property preview render do componente em função do estado dos knobs.
 * @property fullWidthPreview quando `true`, o preview não recebe padding lateral
 *   (usado por barras e scaffolds, que precisam encostar nas bordas).
 */
public data class DemoComponent(
    val id: String,
    val name: String,
    val category: DemoCategory,
    val summary: String,
    val docPath: String,
    val knobs: List<DemoKnob> = emptyList(),
    val fullWidthPreview: Boolean = false,
    val icon: ImageVector? = null,
    val preview: @Composable (DemoKnobState) -> Unit,
)
