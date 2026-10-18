// port-lint: tests rasterizer/src/geometry.rs
package io.github.kotlinmania.abglyph

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

class GeometryTest {

    @Test
    fun distanceTo() {
        val distance = point(0.0f, 0.0f).distanceTo(point(3.0f, 4.0f))
        assertTrue(abs(distance - 5.0f) <= 1e-5f)
    }
}
