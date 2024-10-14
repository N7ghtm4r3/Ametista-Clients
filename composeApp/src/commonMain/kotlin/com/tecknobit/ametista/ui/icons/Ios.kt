package com.tecknobit.ametista.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Ios: ImageVector
	get() {
		if (_Ios != null) {
			return _Ios!!
		}
		_Ios = ImageVector.Builder(
            name = "Ios",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
			path(
    			fill = SolidColor(Color.Black),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(160f, 360f)
				verticalLineToRelative(-80f)
				horizontalLineToRelative(80f)
				verticalLineToRelative(80f)
				close()
				moveToRelative(0f, 320f)
				verticalLineToRelative(-240f)
				horizontalLineToRelative(80f)
				verticalLineToRelative(240f)
				close()
				moveToRelative(280f, 0f)
				horizontalLineToRelative(-80f)
				quadToRelative(-33f, 0f, -56.5f, -23.5f)
				reflectiveQuadTo(280f, 600f)
				verticalLineToRelative(-240f)
				quadToRelative(0f, -33f, 23.5f, -56.5f)
				reflectiveQuadTo(360f, 280f)
				horizontalLineToRelative(80f)
				quadToRelative(33f, 0f, 56.5f, 23.5f)
				reflectiveQuadTo(520f, 360f)
				verticalLineToRelative(240f)
				quadToRelative(0f, 33f, -23.5f, 56.5f)
				reflectiveQuadTo(440f, 680f)
				moveToRelative(-80f, -80f)
				horizontalLineToRelative(80f)
				verticalLineToRelative(-240f)
				horizontalLineToRelative(-80f)
				close()
				moveToRelative(200f, 80f)
				verticalLineToRelative(-80f)
				horizontalLineToRelative(160f)
				verticalLineToRelative(-80f)
				horizontalLineToRelative(-80f)
				quadToRelative(-33f, 0f, -56.5f, -23.5f)
				reflectiveQuadTo(560f, 440f)
				verticalLineToRelative(-80f)
				quadToRelative(0f, -33f, 23.5f, -56.5f)
				reflectiveQuadTo(640f, 280f)
				horizontalLineToRelative(160f)
				verticalLineToRelative(80f)
				horizontalLineTo(640f)
				verticalLineToRelative(80f)
				horizontalLineToRelative(80f)
				quadToRelative(33f, 0f, 56.5f, 23.5f)
				reflectiveQuadTo(800f, 520f)
				verticalLineToRelative(80f)
				quadToRelative(0f, 33f, -23.5f, 56.5f)
				reflectiveQuadTo(720f, 680f)
				close()
			}
		}.build()
		return _Ios!!
	}

private var _Ios: ImageVector? = null
