// port-lint: source glyph/src/ttfp/outliner.rs
package io.github.kotlinmania.abglyph.ttfp

import io.github.kotlinmania.abglyph.OutlineCurve
import io.github.kotlinmania.abglyph.Point
import io.github.kotlinmania.abglyph.point

internal class OutlineCurveBuilder {
    private var last: Point = Point()
    private var lastMove: Point? = null
    private val outline: MutableList<OutlineCurve> = mutableListOf()

    fun moveTo(x: Float, y: Float) {
        last = point(x, y)
        lastMove = last
    }

    fun lineTo(x1: Float, y1: Float) {
        val p1 = point(x1, y1)
        outline.add(OutlineCurve.Line(last, p1))
        last = p1
    }

    fun quadTo(x1: Float, y1: Float, x2: Float, y2: Float) {
        val p1 = point(x1, y1)
        val p2 = point(x2, y2)
        outline.add(OutlineCurve.Quad(last, p1, p2))
        last = p2
    }

    fun curveTo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float) {
        val p1 = point(x1, y1)
        val p2 = point(x2, y2)
        val p3 = point(x3, y3)
        outline.add(OutlineCurve.Cubic(last, p1, p2, p3))
        last = p3
    }

    fun close() {
        val m = lastMove
        if (m != null) {
            outline.add(OutlineCurve.Line(last, m))
            lastMove = null
        }
    }

    fun takeOutline(): List<OutlineCurve> {
        close()
        return outline
    }
}
