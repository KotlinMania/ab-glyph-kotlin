// port-lint: source glyph/src/font_arc.rs
package io.github.kotlinmania.abglyph

/**
 * Font implementor that wraps another concrete Font type.
 * Provides convenient type erasure and cheap sharing.
 */
class FontArc(
    private val inner: Font,
) : Font by inner {

    companion object {
        fun new(font: Font): FontArc = FontArc(font)

        fun tryFromVec(data: ByteArray): FontArc =
            FontArc(FontVec.tryFromVec(data))

        fun tryFromSlice(data: ByteArray): FontArc =
            FontArc(FontRef.tryFromSlice(data))
    }

    override fun toString(): String = "FontArc"
}
