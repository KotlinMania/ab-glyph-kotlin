// port-lint: source glyph/src/scale.rs
package io.github.kotlinmania.abglyph

import kotlin.math.round

/**
 * Pixel scale.
 *
 * This is the pixel-height of text.
 */
data class PxScale(
    val x: Float,
    val y: Float,
) : Comparable<PxScale> {

    companion object {
        /**
         * Uniform scaling where x and y are the same.
         */
        fun from(s: Float): PxScale = PxScale(s, s)
    }

    /**
     * Returns a PxScale with both x and y scale values set to the nearest integer.
     */
    fun round(): PxScale = PxScale(round(x), round(y))

    override fun compareTo(other: PxScale): Int {
        val cmpX = x.compareTo(other.x)
        if (cmpX != 0) return cmpX
        return y.compareTo(other.y)
    }
}

/**
 * 2D scale factors for use with unscaled metrics.
 */
data class PxScaleFactor(
    val horizontal: Float,
    val vertical: Float,
) : Comparable<PxScaleFactor> {
    override fun compareTo(other: PxScaleFactor): Int {
        val cmpH = horizontal.compareTo(other.horizontal)
        if (cmpH != 0) return cmpH
        return vertical.compareTo(other.vertical)
    }
}

/**
 * A Font with an associated pixel scale.
 */
interface ScaleFont<out F : Font> {
    fun scale(): PxScale
    fun font(): F

    fun hScaleFactor(): Float = scale().x / font().heightUnscaled()
    fun vScaleFactor(): Float = scale().y / font().heightUnscaled()
    fun scaleFactor(): PxScaleFactor = PxScaleFactor(hScaleFactor(), vScaleFactor())

    fun ascent(): Float = vScaleFactor() * font().ascentUnscaled()
    fun descent(): Float = vScaleFactor() * font().descentUnscaled()
    fun height(): Float = scale().y
    fun lineGap(): Float = vScaleFactor() * font().lineGapUnscaled()

    fun glyphId(c: Char): GlyphId = font().glyphId(c)
    fun scaledGlyph(c: Char): Glyph = font().glyphId(c).withScale(scale())

    fun hAdvance(id: GlyphId): Float = hScaleFactor() * font().hAdvanceUnscaled(id)
    fun hSideBearing(id: GlyphId): Float = hScaleFactor() * font().hSideBearingUnscaled(id)
    fun vAdvance(id: GlyphId): Float = vScaleFactor() * font().vAdvanceUnscaled(id)
    fun vSideBearing(id: GlyphId): Float = vScaleFactor() * font().vSideBearingUnscaled(id)
    fun kern(first: GlyphId, second: GlyphId): Float = hScaleFactor() * font().kernUnscaled(first, second)

    fun glyphBounds(glyph: Glyph): Rect = font().glyphBounds(glyph)
    fun glyphCount(): Int = font().glyphCount()
    fun codepointIds(): CodepointIdIter = font().codepointIds()
    fun outlineGlyph(glyph: Glyph): OutlinedGlyph? = font().outlineGlyph(glyph)
}

/**
 * A Font and an associated pixel scale.
 */
data class PxScaleFont<out F : Font>(
    val font: F,
    val scale: PxScale,
) : ScaleFont<F> {

    fun withScale(newScale: PxScale): PxScaleFont<F> = PxScaleFont(font, newScale)
    fun withScale(newScale: Float): PxScaleFont<F> = PxScaleFont(font, PxScale.from(newScale))

    override fun scale(): PxScale = scale
    override fun font(): F = font
}
