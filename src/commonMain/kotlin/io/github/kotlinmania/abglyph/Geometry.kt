// port-lint: source rasterizer/src/geometry.rs
package io.github.kotlinmania.abglyph

import kotlin.math.sqrt

/**
 * An (x, y) coordinate.
 */
data class Point(
    val x: Float = 0.0f,
    val y: Float = 0.0f,
) : Comparable<Point> {
    /**
     * Calculates the Euclidean distance to another point.
     */
    fun distanceTo(other: Point): Float {
        val dx = other.x - this.x
        val dy = other.y - this.y
        return sqrt(dx * dx + dy * dy)
    }

    /**
     * Subtract other.x from x, other.y from y.
     */
    operator fun minus(other: Point): Point = point(x - other.x, y - other.y)

    /**
     * Add other.x to x, other.y to y.
     */
    operator fun plus(other: Point): Point = point(x + other.x, y + other.y)

    override fun compareTo(other: Point): Int {
        val cmpX = x.compareTo(other.x)
        if (cmpX != 0) return cmpX
        return y.compareTo(other.y)
    }

    override fun toString(): String = "point($x, $y)"
}

/**
 * Point constructor.
 */
fun point(x: Float, y: Float): Point = Point(x, y)

/**
 * Linear interpolation between points.
 */
internal fun lerp(t: Float, p0: Point, p1: Point): Point =
    point(p0.x + t * (p1.x - p0.x), p0.y + t * (p1.y - p0.y))
