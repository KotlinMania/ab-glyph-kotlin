// port-lint: source glyph/src/outlined.rs
package io.github.kotlinmania.abglyph

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.truncate

/**
 * A rectangle, with top-left corner at `min`, and bottom-right corner at `max`.
 */
data class Rect(
    val min: Point = Point(),
    val max: Point = Point(),
) {
    fun width(): Float = max.x - min.x

    fun height(): Float = max.y - min.y
}

/**
 * Glyph outline primitives.
 */
sealed class OutlineCurve {
    /**
     * Straight line from p0 to p1.
     */
    data class Line(
        val p0: Point,
        val p1: Point,
    ) : OutlineCurve()

    /**
     * Quadratic Bezier curve from p0 to p2 using p1 as the control.
     */
    data class Quad(
        val p0: Point,
        val p1: Point,
        val p2: Point,
    ) : OutlineCurve()

    /**
     * Cubic Bezier curve from p0 to p3 using p1 as control at curve start and p2 at curve end.
     */
    data class Cubic(
        val p0: Point,
        val p1: Point,
        val p2: Point,
        val p3: Point,
    ) : OutlineCurve()
}

/**
 * A "raw" collection of outline curves for a glyph, unscaled and unpositioned.
 */
data class Outline(
    val bounds: Rect,
    val curves: List<OutlineCurve>,
) {
    /**
     * Convert unscaled bounds into pixel bounds at a given scale and position.
     */
    fun pxBounds(scaleFactor: PxScaleFactor, position: Point): Rect {
        val min = bounds.min
        val max = bounds.max

        val xTrunc = truncate(position.x)
        val xFract = position.x - xTrunc
        val yTrunc = truncate(position.y)
        val yFract = position.y - yTrunc

        return Rect(
            min =
                point(
                    floor(min.x * scaleFactor.horizontal + xFract) + xTrunc,
                    floor(min.y * -scaleFactor.vertical + yFract) + yTrunc,
                ),
            max =
                point(
                    ceil(max.x * scaleFactor.horizontal + xFract) + xTrunc,
                    ceil(max.y * -scaleFactor.vertical + yFract) + yTrunc,
                ),
        )
    }
}

/**
 * A glyph that has been outlined at a scale and position.
 */
class OutlinedGlyph(
    private val glyph: Glyph,
    private val outline: Outline,
    private val scaleFactor: PxScaleFactor,
) {
    private val pxBounds: Rect = outline.pxBounds(scaleFactor, glyph.position)

    companion object {
        fun new(glyph: Glyph, outline: Outline, scaleFactor: PxScaleFactor): OutlinedGlyph =
            OutlinedGlyph(glyph, outline, scaleFactor)
    }

    fun glyph(): Glyph = glyph

    /**
     * Conservative whole number pixel bounding box for this glyph outline.
     */
    fun bounds(): Rect = pxBounds()

    fun pxBounds(): Rect = pxBounds

    /**
     * Draw this glyph outline using a pixel and coverage handling function.
     */
    fun draw(o: (Int, Int, Float) -> Unit) {
        val hFactor = scaleFactor.horizontal
        val vFactor = -scaleFactor.vertical
        val offset = glyph.position - pxBounds.min
        val w = pxBounds.width().toInt()
        val h = pxBounds.height().toInt()

        fun scaleUp(p: Point): Point = point(p.x * hFactor, p.y * vFactor)

        val rasterizer = Rasterizer.new(w, h)
        for (curve in outline.curves) {
            when (curve) {
                is OutlineCurve.Line -> {
                    rasterizer.drawLine(scaleUp(curve.p0) + offset, scaleUp(curve.p1) + offset)
                }

                is OutlineCurve.Quad -> {
                    rasterizer.drawQuad(
                        scaleUp(curve.p0) + offset,
                        scaleUp(curve.p1) + offset,
                        scaleUp(curve.p2) + offset,
                    )
                }

                is OutlineCurve.Cubic -> {
                    rasterizer.drawCubic(
                        scaleUp(curve.p0) + offset,
                        scaleUp(curve.p1) + offset,
                        scaleUp(curve.p2) + offset,
                        scaleUp(curve.p3) + offset,
                    )
                }
            }
        }
        rasterizer.forEachPixel2d(o)
    }
}
