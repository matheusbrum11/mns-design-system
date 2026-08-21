package com.mns.designsystem.component.media

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

/**
 * Ícones próprios do design system.
 *
 * A biblioteca **não** depende de `material-icons-extended` de propósito: são
 * ~2 mil vetores que inflam o APK de todo mundo que consumir o artefato. Aqui
 * ficam só os poucos ícones que os componentes internos exigem e que não estão
 * no conjunto `material-icons-core`.
 *
 * Apps podem, à vontade, adicionar `material-icons-extended` no próprio módulo
 * e passar qualquer `ImageVector` para os componentes.
 */
public object MnsIcons {

    /** Olho aberto — alternar visibilidade de senha. */
    public val Visibility: ImageVector by lazy {
        icon(
            name = "MnsVisibility",
            pathData = "M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 " +
                "11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 " +
                "2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z",
        )
    }

    /** Olho cortado — senha oculta. */
    public val VisibilityOff: ImageVector by lazy {
        icon(
            name = "MnsVisibilityOff",
            pathData = "M12 7c2.76 0 5 2.24 5 5 0 .65-.13 1.26-.36 1.83l2.92 2.92c1.51-1.26 " +
                "2.7-2.89 3.43-4.75-1.73-4.39-6-7.5-11-7.5-1.4 0-2.74.25-3.98.7l2.16 " +
                "2.16C10.74 7.13 11.35 7 12 7zM2 4.27l2.28 2.28.46.46C3.08 8.3 1.78 10.02 1 " +
                "12c1.73 4.39 6 7.5 11 7.5 1.55 0 3.03-.3 4.38-.84l.42.42L19.73 22 21 " +
                "20.73 3.27 3 2 4.27zM7.53 9.8l1.55 1.55c-.05.21-.08.43-.08.65 0 1.66 1.34 " +
                "3 3 3 .22 0 .44-.03.65-.08l1.55 1.55c-.67.33-1.41.53-2.2.53-2.76 0-5-2.24-5-5 " +
                "0-.79.2-1.53.53-2.2zm4.31-.78l3.15 3.15.02-.16c0-1.66-1.34-3-3-3l-.17.01z",
        )
    }

    /** Traço horizontal — decremento e estado indeterminado do checkbox. */
    public val Minus: ImageVector by lazy {
        icon(name = "MnsMinus", pathData = "M19 13H5v-2h14v2z")
    }

    /** Marcador de favorito/salvo. */
    public val Bookmark: ImageVector by lazy {
        icon(
            name = "MnsBookmark",
            pathData = "M17 3H7c-1.1 0-1.99.9-1.99 2L5 21l7-3 7 3V5c0-1.1-.9-2-2-2z",
        )
    }

    /** Silhueta de QR Code — usada em estados vazios de ingresso. */
    public val QrCode: ImageVector by lazy {
        icon(
            name = "MnsQrCode",
            pathData = "M3 11h8V3H3v8zm2-6h4v4H5V5zM3 21h8v-8H3v8zm2-6h4v4H5v-4zM13 " +
                "3v8h8V3h-8zm6 6h-4V5h4v4zm0 10h2v2h-2v-2zm-6-6h2v2h-2v-2zm2 2h2v2h-2v-2zm-2 " +
                "2h2v2h-2v-2zm2 2h2v2h-2v-2zm2-2h2v2h-2v-2zm0-4h2v2h-2v-2zm2 2h2v2h-2v-2z",
        )
    }

    private fun icon(name: String, pathData: String): ImageVector =
        ImageVector.Builder(
            name = name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            addPath(
                pathData = PathParser().parsePathString(pathData).toNodes(),
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero,
            )
        }.build()
}
