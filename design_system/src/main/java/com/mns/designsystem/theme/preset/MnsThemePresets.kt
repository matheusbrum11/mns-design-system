package com.mns.designsystem.theme.preset

import com.mns.designsystem.theme.MnsThemeProvider

/**
 * Catálogo dos presets que acompanham a biblioteca.
 *
 * Serve a dois consumidores: o seletor de tema do `app_demo` e os testes de
 * integração, que rodam a bateria inteira de componentes contra **todos** os
 * presets — é assim que garantimos que nenhum componente escondeu um valor
 * hard-coded.
 */
public object MnsThemePresets {

    /** Padrão da biblioteca: uma cor de marca, alto contraste, cantos moderados. */
    public val IndigoTicket: MnsThemeProvider = MnsIndigoTicket

    /** Monocromático com acentos de seleção — para produtos guiados por imagem. */
    public val MonoEvents: MnsThemeProvider = MnsMonoEvents

    /** Pastel arredondado — para produtos de lazer e descoberta. */
    public val PastelGlass: MnsThemeProvider = MnsPastelGlass

    /** Todos os presets, na ordem em que aparecem no `app_demo`. */
    public val all: List<MnsThemeProvider> = listOf(IndigoTicket, MonoEvents, PastelGlass)

    /** Busca um preset pelo [MnsThemeProvider.id]; `null` se não existir. */
    public fun byId(id: String): MnsThemeProvider? = all.firstOrNull { it.id == id }
}
