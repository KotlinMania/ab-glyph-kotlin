// port-lint: source rasterizer/src/raster.rs
package io.github.kotlinmania.abglyph

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Coverage rasterizer for lines, quadratic and cubic beziers.
 */
class Rasterizer(
    var width: Int,
    var height: Int,
) {
    private var a: FloatArray = FloatArray(width * height + 4)

    companion object {
        /**
         * Allocates a new rasterizer that can draw onto a `width` x `height` alpha grid.
         */
        fun new(width: Int, height: Int): Rasterizer = Rasterizer(width, height)
    }

    /**
     * Resets the rasterizer to an empty `width` x `height` alpha grid.
     */
    fun reset(width: Int, height: Int) {
        this.width = width
        this.height = height
        val requiredSize = width * height + 4
        if (a.size < requiredSize) {
            a = FloatArray(requiredSize)
        } else {
            a.fill(0.0f)
        }
    }

    /**
     * Clears the rasterizer.
     */
    fun clear() {
        a.fill(0.0f)
    }

    /**
     * Returns the dimensions the rasterizer was built to draw to.
     */
    fun dimensions(): Pair<Int, Int> = Pair(width, height)

    /**
     * Adds a straight line from `p0` to `p1` to the outline.
     */
    fun drawLine(p0: Point, p1: Point) {
        drawLineScalar(p0, p1)
    }

    fun drawLineScalar(p0: Point, p1: Point) {
        if (abs(p0.y - p1.y) <= 1e-7f) {
            return
        }
        val (dir, startP, endP) =
            if (p0.y < p1.y) {
                Triple(1.0f, p0, p1)
            } else {
                Triple(-1.0f, p1, p0)
            }
        val dxdy = (endP.x - startP.x) / (endP.y - startP.y)
        var x = startP.x
        val y0 = if (startP.y < 0.0f) 0 else startP.y.toInt()
        if (startP.y < 0.0f) {
            x -= startP.y * dxdy
        }
        val yLimit = min(height, ceil(endP.y).toInt())
        for (y in y0 until yLimit) {
            val linestart = y * width
            val dy = min((y + 1).toFloat(), endP.y) - max(y.toFloat(), startP.y)
            val xnext = x + dxdy * dy
            val d = dy * dir
            val (x0, x1) = if (x < xnext) Pair(x, xnext) else Pair(xnext, x)
            val x0floor = floor(x0)
            val x0i = x0floor.toInt()
            val x1ceil = ceil(x1)
            val x1i = x1ceil.toInt()
            val linestartX0i = linestart + x0i
            if (linestartX0i < 0) {
                x = xnext
                continue
            }
            if (x1i <= x0i + 1) {
                val xmf = 0.5f * (x + xnext) - x0floor
                if (linestartX0i in a.indices) {
                    a[linestartX0i] += d - d * xmf
                }
                if (linestartX0i + 1 in a.indices) {
                    a[linestartX0i + 1] += d * xmf
                }
            } else {
                val s = 1.0f / (x1 - x0)
                val x0f = x0 - x0floor
                val a0 = 0.5f * s * (1.0f - x0f) * (1.0f - x0f)
                val x1f = x1 - x1ceil + 1.0f
                val am = 0.5f * s * x1f * x1f
                if (linestartX0i in a.indices) {
                    a[linestartX0i] += d * a0
                }
                if (x1i == x0i + 2) {
                    if (linestartX0i + 1 in a.indices) {
                        a[linestartX0i + 1] += d * (1.0f - a0 - am)
                    }
                } else {
                    val a1 = s * (1.5f - x0f)
                    if (linestartX0i + 1 in a.indices) {
                        a[linestartX0i + 1] += d * (a1 - a0)
                    }
                    for (xi in x0i + 2 until x1i - 1) {
                        val idx = linestart + xi
                        if (idx in a.indices) {
                            a[idx] += d * s
                        }
                    }
                    val a2 = a1 + (x1i - x0i - 3).toFloat() * s
                    val prevIdx = linestart + (x1i - 1)
                    if (prevIdx in a.indices) {
                        a[prevIdx] += d * (1.0f - a2 - am)
                    }
                }
                val lastIdx = linestart + x1i
                if (lastIdx in a.indices) {
                    a[lastIdx] += d * am
                }
            }
            x = xnext
        }
    }

    /**
     * Adds a quadratic Bezier curve from `p0` to `p2` to the outline using `p1` as the control.
     */
    fun drawQuad(p0: Point, p1: Point, p2: Point) {
        val devx = p0.x - 2.0f * p1.x + p2.x
        val devy = p0.y - 2.0f * p1.y + p2.y
        val devsq = devx * devx + devy * devy
        if (devsq < 0.333f) {
            drawLine(p0, p2)
            return
        }
        val tol = 3.0f
        val n = 1 + sqrt(sqrt(tol * devsq)).toInt()
        var p = p0
        val nrecip = 1.0f / n.toFloat()
        var t = 0.0f
        repeat(n - 1) {
            t += nrecip
            val pn = lerp(t, lerp(t, p0, p1), lerp(t, p1, p2))
            drawLine(p, pn)
            p = pn
        }
        drawLine(p, p2)
    }

    /**
     * Adds a cubic Bezier curve from `p0` to `p3` to the outline using `p1` as the control
     * at the beginning of the curve and `p2` at the end of the curve.
     */
    fun drawCubic(p0: Point, p1: Point, p2: Point, p3: Point) {
        tessellateCubic(p0, p1, p2, p3, 0)
    }

    fun tessellateCubic(p0: Point, p1: Point, p2: Point, p3: Point, n: Int) {
        val objspaceFlatness = 0.35f
        val objspaceFlatnessSquared = objspaceFlatness * objspaceFlatness
        val maxRecursionDepth = 16

        val longlen = p0.distanceTo(p1) + p1.distanceTo(p2) + p2.distanceTo(p3)
        val shortlen = p0.distanceTo(p3)
        val flatnessSquared = longlen * longlen - shortlen * shortlen

        if (n < maxRecursionDepth && flatnessSquared > objspaceFlatnessSquared) {
            val p01 = lerp(0.5f, p0, p1)
            val p12 = lerp(0.5f, p1, p2)
            val p23 = lerp(0.5f, p2, p3)

            val pa = lerp(0.5f, p01, p12)
            val pb = lerp(0.5f, p12, p23)

            val mp = lerp(0.5f, pa, pb)

            tessellateCubic(p0, p01, pa, mp, n + 1)
            tessellateCubic(mp, pb, p23, p3, n + 1)
        } else {
            drawLine(p0, p3)
        }
    }

    /**
     * Run a callback for each pixel `index` and `alpha`, with indices in `0 until width * height`.
     */
    fun forEachPixel(pxFn: (Int, Float) -> Unit) {
        var acc = 0.0f
        val limit = width * height
        for (idx in 0 until limit) {
            acc += a[idx]
            pxFn(idx, abs(acc))
        }
    }

    /**
     * Run a callback for each pixel x position, y position and alpha.
     */
    fun forEachPixel2d(pxFn: (Int, Int, Float) -> Unit) {
        val widthInt = width
        forEachPixel { idx, alpha ->
            pxFn(idx % widthInt, idx / widthInt, alpha)
        }
    }

    override fun toString(): String = "Rasterizer { width: $width, height: $height }"
}
