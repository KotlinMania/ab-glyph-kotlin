// port-lint: tests rasterizer/tests/issues.rs
package io.github.kotlinmania.abglyph

import kotlin.test.Test

class RasterizerIssuesTest {

    /**
     * Index oob panic rasterizing "Gauntl" using Bitter-Regular.otf
     */
    @Test
    fun rusttype156IndexPanic() {
        val r = Rasterizer.new(6, 16)
        r.drawLine(point(5.54f, 14.299999f), point(3.7399998f, 13.799999f))
        r.drawLine(point(3.7399998f, 13.799999f), point(3.7399998f, 0.0f))
        r.drawLine(point(3.7399998f, 0.0f), point(0.0f, 0.10000038f))
    }
}
