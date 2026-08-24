package com.mns.demo.playground

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf

/**
 * Um controle do painel interativo da tela de componente.
 *
 * Cada knob corresponde a **um parâmetro real** do componente do design system.
 * A regra do catálogo é: se o componente aceita o parâmetro, existe um knob
 * para ele — é o que transforma a tela de demonstração em documentação
 * executável em vez de um screenshot glorificado.
 */
public sealed interface DemoKnob {
    /** Chave usada para ler o valor em [DemoKnobState]. */
    public val key: String

    /** Rótulo exibido no painel. */
    public val label: String

    /** Explicação curta do efeito do parâmetro. */
    public val description: String

    /** Valor inicial. */
    public val defaultValue: Any

    /** Interruptor para um parâmetro `Boolean`. */
    public data class BoolKnob(
        override val key: String,
        override val label: String,
        override val description: String = "",
        val default: Boolean = false,
    ) : DemoKnob {
        override val defaultValue: Any get() = default
    }

    /** Campo de texto para um parâmetro `String`. */
    public data class TextKnob(
        override val key: String,
        override val label: String,
        override val description: String = "",
        val default: String = "",
        val allowEmpty: Boolean = true,
    ) : DemoKnob {
        override val defaultValue: Any get() = default
    }

    /** Escolha entre valores discretos — mapeia enums do design system. */
    public data class OptionKnob(
        override val key: String,
        override val label: String,
        val options: List<String>,
        override val description: String = "",
        val default: String = options.first(),
    ) : DemoKnob {
        override val defaultValue: Any get() = default
    }

    /** Valor numérico contínuo ou discreto. */
    public data class NumberKnob(
        override val key: String,
        override val label: String,
        val range: ClosedFloatingPointRange<Float>,
        override val description: String = "",
        val default: Float = range.start,
        val steps: Int = 0,
        val format: (Float) -> String = { "%.0f".format(it) },
    ) : DemoKnob {
        override val defaultValue: Any get() = default
    }

    /**
     * Escolha de uma cor **pelo nome do papel semântico**, nunca por valor cru.
     *
     * É intencional: o catálogo não deve ensinar ninguém a passar `Color(0xFF...)`
     * para um componente. Se a cor que você quer não existe como papel, o que
     * falta é um token, não um parâmetro.
     */
    public data class ColorRoleKnob(
        override val key: String,
        override val label: String,
        val roles: List<String>,
        override val description: String = "",
        val default: String = roles.first(),
    ) : DemoKnob {
        override val defaultValue: Any get() = default
    }
}

/**
 * Estado mutável dos knobs de um componente.
 *
 * Vive enquanto a tela do componente estiver na composição. Trocar de
 * componente cria um estado novo — voltar para um componente já visitado
 * restaura os defaults, o que é o comportamento esperado num catálogo.
 */
@Stable
public class DemoKnobState(knobs: List<DemoKnob>) {

    private val values = mutableStateMapOf<String, Any>().apply {
        knobs.forEach { put(it.key, it.defaultValue) }
    }

    private val defaults: Map<String, Any> = knobs.associate { it.key to it.defaultValue }

    /** Lê um knob booleano. */
    public fun bool(key: String, fallback: Boolean = false): Boolean =
        values[key] as? Boolean ?: fallback

    /** Lê um knob de texto. */
    public fun text(key: String, fallback: String = ""): String =
        values[key] as? String ?: fallback

    /** Lê um knob de opção/cor (ambos guardam `String`). */
    public fun option(key: String, fallback: String = ""): String =
        values[key] as? String ?: fallback

    /** Lê um knob numérico. */
    public fun number(key: String, fallback: Float = 0f): Float =
        values[key] as? Float ?: fallback

    /** Lê um knob numérico já convertido para `Int`. */
    public fun int(key: String, fallback: Int = 0): Int =
        (values[key] as? Float)?.toInt() ?: fallback

    /** Atualiza o valor de um knob. */
    public fun set(key: String, value: Any) {
        values[key] = value
    }

    /** Restaura todos os knobs ao valor inicial. */
    public fun reset() {
        values.clear()
        values.putAll(defaults)
    }

    /**
     * Converte uma opção de knob no enum correspondente.
     *
     * @param key chave do knob.
     * @param entries valores do enum, na mesma ordem declarada em `options`.
     */
    public fun <T : Enum<T>> enum(key: String, entries: Array<T>, fallback: T = entries.first()): T =
        entries.firstOrNull { it.name == option(key, fallback.name) } ?: fallback
}
