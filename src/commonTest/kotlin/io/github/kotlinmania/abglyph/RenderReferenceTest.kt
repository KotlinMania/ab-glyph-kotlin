// port-lint: tests dev/tests/render_reference.rs
package io.github.kotlinmania.abglyph

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RenderReferenceTest {
    @Test
    fun referenceDrawTtfW() {
        val r = rasterizeTtfW()
        assertEquals(Pair(9, 8), r.dimensions())
        var totalAlpha = 0.0f
        r.forEachPixel { _, alpha -> totalAlpha += alpha }
        assertTrue(totalAlpha > 0.0f)
    }

    @Test
    fun referenceDrawTtfIota() {
        val r = rasterizeTtfIota()
        assertEquals(Pair(14, 38), r.dimensions())
        var totalAlpha = 0.0f
        r.forEachPixel { _, alpha -> totalAlpha += alpha }
        assertTrue(totalAlpha > 0.0f)
    }

    @Test
    fun referenceDrawTtfBiohazard() {
        val r = rasterizeTtfBiohazard()
        assertEquals(Pair(294, 269), r.dimensions())
        var totalAlpha = 0.0f
        r.forEachPixel { _, alpha -> totalAlpha += alpha }
        assertTrue(totalAlpha > 0.0f)
    }

    @Test
    fun referenceDrawOtfTailedE() {
        val r = rasterizeOtfTailedE()
        assertEquals(Pair(106, 183), r.dimensions())
        var totalAlpha = 0.0f
        r.forEachPixel { _, alpha -> totalAlpha += alpha }
        assertTrue(totalAlpha > 0.0f)
    }

    @Test
    fun referenceDrawTtfTailedE() {
        val r = rasterizeTtfTailedE()
        assertEquals(Pair(106, 177), r.dimensions())
        var totalAlpha = 0.0f
        r.forEachPixel { _, alpha -> totalAlpha += alpha }
        assertTrue(totalAlpha > 0.0f)
    }
}
