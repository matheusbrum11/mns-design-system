package com.mns.demo.catalog.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mns.demo.catalog.DemoCategory
import com.mns.demo.catalog.DemoComponent
import com.mns.demo.playground.DemoKnob
import com.mns.designsystem.component.input.MnsCheckbox
import com.mns.designsystem.component.input.MnsChip
import com.mns.designsystem.component.input.MnsCurrencyField
import com.mns.designsystem.component.input.MnsOtpField
import com.mns.designsystem.component.input.MnsPasswordField
import com.mns.designsystem.component.input.MnsRadioButton
import com.mns.designsystem.component.input.MnsSearchField
import com.mns.designsystem.component.input.MnsSlider
import com.mns.designsystem.component.input.MnsStepper
import com.mns.designsystem.component.input.MnsSwitch
import com.mns.designsystem.component.input.MnsTextField
import com.mns.designsystem.component.input.MnsToggleState
import com.mns.designsystem.format.MnsCurrencyFormat
import com.mns.designsystem.format.MnsMaskVisualTransformation
import com.mns.designsystem.theme.MnsTheme
import androidx.compose.ui.text.input.VisualTransformation

/** Entradas do catálogo para a categoria [DemoCategory.INPUT]. */
internal fun inputEntries(): List<DemoComponent> = listOf(
    DemoComponent(
        id = "mns-text-field",
        name = "MnsTextField",
        category = DemoCategory.INPUT,
        summary = "Campo de texto com rótulo fixo, dica, erro, contador e máscara.",
        docPath = "docs/components/input/mns-text-field.md",
        icon = Icons.Filled.TextFields,
        knobs = listOf(
            DemoKnob.TextKnob("label", "label", "Rótulo acima do campo.", "E-mail"),
            DemoKnob.TextKnob("placeholder", "placeholder", "Texto exibido com o campo vazio.", "voce@exemplo.com"),
            DemoKnob.TextKnob("helper", "helperText", "Dica abaixo do campo.", "Usamos para enviar o ingresso."),
            DemoKnob.TextKnob("error", "errorMessage", "Quando preenchido, o campo entra em estado de erro.", ""),
            DemoKnob.BoolKnob("enabled", "enabled", "Bloqueia a edição.", true),
            DemoKnob.BoolKnob("readOnly", "readOnly", "Permite selecionar, impede editar.", false),
            DemoKnob.BoolKnob("singleLine", "singleLine", "Impede quebra de linha.", true),
            DemoKnob.BoolKnob("leadingIcon", "leadingIcon", "Ícone no início do campo.", true),
            DemoKnob.BoolKnob("counter", "maxLength", "Ativa limite de 40 caracteres e contador.", false),
            DemoKnob.OptionKnob(
                key = "mask",
                label = "visualTransformation",
                options = listOf("Nenhuma", "CPF", "Telefone", "Validade"),
                description = "Máscara posicional aplicada só na exibição.",
                default = "Nenhuma",
            ),
        ),
    ) { knobs ->
        var value by remember { mutableStateOf("") }
        val mask = when (knobs.option("mask", "Nenhuma")) {
            "CPF" -> MnsMaskVisualTransformation("###.###.###-##")
            "Telefone" -> MnsMaskVisualTransformation("(##) #####-####")
            "Validade" -> MnsMaskVisualTransformation("##/##")
            else -> VisualTransformation.None
        }
        MnsTextField(
            value = value,
            onValueChange = { value = it },
            label = knobs.text("label", "E-mail").ifBlank { null },
            placeholder = knobs.text("placeholder").ifBlank { null },
            helperText = knobs.text("helper").ifBlank { null },
            errorMessage = knobs.text("error").ifBlank { null },
            enabled = knobs.bool("enabled", true),
            readOnly = knobs.bool("readOnly"),
            singleLine = knobs.bool("singleLine", true),
            leadingIcon = Icons.Filled.Email.takeIf { knobs.bool("leadingIcon", true) },
            maxLength = 40.takeIf { knobs.bool("counter") },
            visualTransformation = mask,
        )
    },

    DemoComponent(
        id = "mns-search-field",
        name = "MnsSearchField",
        category = DemoCategory.INPUT,
        summary = "Campo de busca em pílula com botão de limpar contextual.",
        docPath = "docs/components/input/mns-search-field.md",
        icon = Icons.Filled.Search,
        knobs = listOf(
            DemoKnob.TextKnob("placeholder", "placeholder", "Dica exibida no campo vazio.", "Buscar eventos"),
            DemoKnob.BoolKnob("clear", "showClearButton", "Exibe o ✕ quando há texto.", true),
            DemoKnob.BoolKnob("enabled", "enabled", "Bloqueia a digitação.", true),
        ),
    ) { knobs ->
        var query by remember { mutableStateOf("jazz") }
        MnsSearchField(
            value = query,
            onValueChange = { query = it },
            placeholder = knobs.text("placeholder", "Buscar eventos"),
            enabled = knobs.bool("enabled", true),
            showClearButton = knobs.bool("clear", true),
        )
    },

    DemoComponent(
        id = "mns-currency-field",
        name = "MnsCurrencyField",
        category = DemoCategory.INPUT,
        summary = "Campo monetário em centavos, com máscara aplicada a cada tecla.",
        docPath = "docs/components/input/mns-currency-field.md",
        icon = Icons.Filled.Payments,
        knobs = listOf(
            DemoKnob.OptionKnob(
                key = "currency",
                label = "format",
                options = listOf("BRL", "USD", "EUR"),
                description = "Moeda e locale usados na formatação.",
                default = "BRL",
            ),
            DemoKnob.TextKnob("label", "label", "Rótulo do campo.", "Valor do ingresso"),
            DemoKnob.BoolKnob("enabled", "enabled", "Bloqueia a edição.", true),
        ),
    ) { knobs ->
        var cents by remember { mutableLongStateOf(12550L) }
        MnsCurrencyField(
            cents = cents,
            onCentsChange = { cents = it },
            label = knobs.text("label", "Valor").ifBlank { null },
            enabled = knobs.bool("enabled", true),
            format = when (knobs.option("currency", "BRL")) {
                "USD" -> MnsCurrencyFormat.USD
                "EUR" -> MnsCurrencyFormat.EUR
                else -> MnsCurrencyFormat.BRL
            },
            helperText = "Digite apenas números — a máscara é aplicada sozinha.",
        )
    },

    DemoComponent(
        id = "mns-password-field",
        name = "MnsPasswordField",
        category = DemoCategory.INPUT,
        summary = "Campo de senha com alternância de visibilidade acessível.",
        docPath = "docs/components/input/mns-password-field.md",
        icon = Icons.Filled.Lock,
        knobs = listOf(
            DemoKnob.BoolKnob("visible", "initiallyVisible", "Começa com a senha revelada.", false),
            DemoKnob.TextKnob("error", "errorMessage", "Mensagem de erro.", ""),
        ),
    ) { knobs ->
        var senha by remember { mutableStateOf("s3nh4-forte") }
        MnsPasswordField(
            value = senha,
            onValueChange = { senha = it },
            initiallyVisible = knobs.bool("visible"),
            errorMessage = knobs.text("error").ifBlank { null },
            helperText = "Mínimo de 8 caracteres.",
        )
    },

    DemoComponent(
        id = "mns-otp-field",
        name = "MnsOtpField",
        category = DemoCategory.INPUT,
        summary = "Código de verificação com uma caixa por dígito e colagem preservada.",
        docPath = "docs/components/input/mns-otp-field.md",
        icon = Icons.Filled.Password,
        knobs = listOf(
            DemoKnob.NumberKnob("length", "length", 4f..8f, "Quantidade de dígitos.", 6f, 3),
            DemoKnob.BoolKnob("error", "isError", "Pinta as caixas com a cor de erro.", false),
        ),
    ) { knobs ->
        var code by remember { mutableStateOf("204") }
        MnsOtpField(
            value = code,
            onValueChange = { code = it },
            length = knobs.int("length", 6),
            isError = knobs.bool("error"),
        )
    },

    DemoComponent(
        id = "mns-selection-controls",
        name = "MnsCheckbox · MnsRadioButton · MnsSwitch",
        category = DemoCategory.INPUT,
        summary = "Controles de seleção com alvo de toque na linha inteira.",
        docPath = "docs/components/input/mns-selection-controls.md",
        icon = Icons.Filled.CheckBox,
        knobs = listOf(
            DemoKnob.BoolKnob("enabled", "enabled", "Bloqueia a interação.", true),
            DemoKnob.BoolKnob("description", "description", "Exibe a linha de apoio.", true),
            DemoKnob.BoolKnob("indeterminate", "estado INDETERMINATE", "Checkbox parcialmente marcado.", false),
        ),
    ) { knobs ->
        var check by remember { mutableStateOf(MnsToggleState.CHECKED) }
        var radio by remember { mutableIntStateOf(0) }
        var switch by remember { mutableStateOf(true) }
        val desc = "Aplica-se a todos os ingressos".takeIf { knobs.bool("description", true) }
        Column(verticalArrangement = Arrangement.spacedBy(MnsTheme.spacing.xs)) {
            MnsCheckbox(
                state = if (knobs.bool("indeterminate")) MnsToggleState.INDETERMINATE else check,
                onStateChange = { check = it },
                label = "Aceito os termos do evento",
                description = desc,
                enabled = knobs.bool("enabled", true),
            )
            MnsRadioButton(
                selected = radio == 0,
                onSelect = { radio = 0 },
                label = "E-ticket",
                description = desc,
                enabled = knobs.bool("enabled", true),
            )
            MnsRadioButton(
                selected = radio == 1,
                onSelect = { radio = 1 },
                label = "Retirar na bilheteria",
                enabled = knobs.bool("enabled", true),
            )
            MnsSwitch(
                checked = switch,
                onCheckedChange = { switch = it },
                label = "Receber lembretes",
                description = desc,
                enabled = knobs.bool("enabled", true),
            )
        }
    },

    DemoComponent(
        id = "mns-chip",
        name = "MnsChip",
        category = DemoCategory.INPUT,
        summary = "Filtro selecionável, com ícone e remoção opcional.",
        docPath = "docs/components/input/mns-chip.md",
        icon = Icons.Filled.LocalOffer,
        knobs = listOf(
            DemoKnob.BoolKnob("icon", "leadingIcon", "Ícone antes do rótulo.", false),
            DemoKnob.BoolKnob("dismiss", "onDismiss", "Exibe o ✕ de remoção.", true),
            DemoKnob.BoolKnob("enabled", "enabled", "Bloqueia a seleção.", true),
        ),
    ) { knobs ->
        var selected by remember { mutableStateOf(setOf("travel")) }
        val opcoes = listOf("travel" to "travel", "music" to "music", "sport" to "sport", "tech" to "tech")
        Row(
            horizontalArrangement = Arrangement.spacedBy(MnsTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier,
        ) {
            opcoes.forEach { (id, label) ->
                MnsChip(
                    label = label,
                    selected = id in selected,
                    onClick = {
                        selected = if (id in selected) selected - id else selected + id
                    },
                    leadingIcon = Icons.Filled.LocalOffer.takeIf { knobs.bool("icon") },
                    onDismiss = if (knobs.bool("dismiss", true)) {
                        { selected = selected - id }
                    } else {
                        null
                    },
                    enabled = knobs.bool("enabled", true),
                )
            }
        }
    },

    DemoComponent(
        id = "mns-stepper",
        name = "MnsStepper",
        category = DemoCategory.INPUT,
        summary = "Incremento/decremento para intervalos pequenos, sem estado inválido.",
        docPath = "docs/components/input/mns-stepper.md",
        icon = Icons.Filled.PeopleAlt,
        knobs = listOf(
            DemoKnob.TextKnob("label", "label", "Rótulo à esquerda.", "Passageiros"),
            DemoKnob.NumberKnob("max", "range.last", 2f..10f, "Limite superior.", 6f, 7),
            DemoKnob.BoolKnob("enabled", "enabled", "Bloqueia os botões.", true),
        ),
    ) { knobs ->
        var qtd by remember { mutableIntStateOf(2) }
        MnsStepper(
            value = qtd,
            onValueChange = { qtd = it },
            range = 1..knobs.int("max", 6),
            label = knobs.text("label", "Passageiros").ifBlank { null },
            enabled = knobs.bool("enabled", true),
            formatValue = { "$it" },
        )
    },

    DemoComponent(
        id = "mns-slider",
        name = "MnsSlider",
        category = DemoCategory.INPUT,
        summary = "Faixa de valor contínua ou discreta, desenhada com tokens.",
        docPath = "docs/components/input/mns-slider.md",
        icon = Icons.Filled.Tune,
        knobs = listOf(
            DemoKnob.NumberKnob("steps", "steps", 0f..10f, "0 = contínuo.", 0f, 10),
            DemoKnob.BoolKnob("enabled", "enabled", "Bloqueia o arraste.", true),
        ),
    ) { knobs ->
        var valor by remember { mutableFloatStateOf(0.4f) }
        MnsSlider(
            value = valor,
            onValueChange = { valor = it },
            steps = knobs.int("steps", 0),
            enabled = knobs.bool("enabled", true),
            label = "Preço máximo",
            formatValue = { "R$ ${(it * 500).toInt()}" },
        )
    },
)
