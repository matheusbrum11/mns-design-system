package com.mns.designsystem.foundation

import androidx.compose.foundation.Indication
import androidx.compose.material3.ripple
import com.mns.designsystem.token.MnsColors
import com.mns.designsystem.token.MnsOpacity

/**
 * Indicação de toque do design system.
 *
 * Existe para que a cor do ripple venha do token `colors.primary` em vez do
 * `LocalContentColor` do Material. Sem isso, um tema com fundo escuro e ação
 * clara produz ripple invisível.
 */
public fun MnsRippleIndication(
    colors: MnsColors,
    @Suppress("UNUSED_PARAMETER") opacity: MnsOpacity,
): Indication = ripple(color = colors.primary)
