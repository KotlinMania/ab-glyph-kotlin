// port-lint: source glyph/src/codepoint_ids.rs
package io.github.kotlinmania.abglyph

/**
 * An iterator of distinct (GlyphId, Char) pairs.
 */
class CodepointIdIter(
    private val inner: Iterator<Pair<GlyphId, Char>>,
) : Iterator<Pair<GlyphId, Char>> {

    override fun hasNext(): Boolean = inner.hasNext()

    override fun next(): Pair<GlyphId, Char> = inner.next()

    override fun toString(): String = "CodepointIdIter"
}
