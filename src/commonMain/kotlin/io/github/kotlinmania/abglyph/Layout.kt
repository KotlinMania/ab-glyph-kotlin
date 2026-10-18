// port-lint: source dev/src/layout.rs
package io.github.kotlinmania.abglyph

/**
 * Simple paragraph layout for glyphs into target.
 * Starts at position (0, ascent).
 *
 * This is for testing and examples.
 */
fun <F : Font, SF : ScaleFont<F>> layoutParagraph(
    font: SF,
    position: Point,
    maxWidth: Float,
    text: String,
    target: MutableList<Glyph>,
) {
    val vAdvance = font.height() + font.lineGap()
    var caret = position + point(0.0f, font.ascent())
    var lastGlyph: Glyph? = null
    for (c in text) {
        if (c.isISOControl()) {
            if (c == '\n') {
                caret = point(position.x, caret.y + vAdvance)
                lastGlyph = null
            }
            continue
        }
        var glyph = font.scaledGlyph(c)
        val prev = lastGlyph
        if (prev != null) {
            caret = point(caret.x + font.kern(prev.id, glyph.id), caret.y)
            lastGlyph = null
        }
        glyph = Glyph(glyph.id, glyph.scale, caret)

        lastGlyph = glyph
        caret = point(caret.x + font.hAdvance(glyph.id), caret.y)

        if (!c.isWhitespace() && caret.x > position.x + maxWidth) {
            caret = point(position.x, caret.y + vAdvance)
            glyph = Glyph(glyph.id, glyph.scale, caret)
            lastGlyph = null
        }

        target.add(glyph)
    }
}
