package com.example.themadproject.view.entity.marker

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class PinMarkerShape(private val xSqueeze: Int = 30, private val yStretch: Int = -10): Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val center = size.center
        val radius = size.width / 2
        val path = Path().apply {
            val startAngle = 180f
            val sweepAngle = 180f
            val arcRect = Rect(
                left = center.x - radius + xSqueeze,
                top = center.y - radius,
                right = center.x + radius - xSqueeze,
                bottom = center.y + radius + yStretch
            )

            arcTo(arcRect, startAngle, sweepAngle, false)
            moveTo(center.x + xSqueeze, center.y + yStretch/2)
            lineTo(center.x - xSqueeze + radius, center.y + yStretch/2)
            lineTo(center.x, size.height)
            lineTo(center.x + xSqueeze - radius, center.y + yStretch/2)
            close()
        }
        return Outline.Generic(path)
    }
}