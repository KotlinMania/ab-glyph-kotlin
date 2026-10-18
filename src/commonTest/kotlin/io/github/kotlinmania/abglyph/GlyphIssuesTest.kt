// port-lint: tests dev/tests/issues.rs
package io.github.kotlinmania.abglyph

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GlyphIssuesTest {
    /**
     * https://github.com/alexheretic/ab-glyph/issues/29
     */
    @Test
    fun ttfZeroBoundingBox29() {
        val airstrip = FontRef.tryFromSlice(AIRSTRIP_TTF_BYTES)

        val outline = airstrip.outline(GlyphId(89))
        assertNotNull(outline, "No outline for glyph")

        assertTrue(abs(outline.bounds.min.x - 0.0f) <= 1e-4f)
        assertTrue(abs(outline.bounds.max.x - 1102.0f) <= 1e-4f)

        assertTrue(abs(outline.bounds.min.y - 1301.0f) <= 1e-4f)
        assertTrue(abs(outline.bounds.max.y - (-3.0f)) <= 1e-4f)
    }
}
