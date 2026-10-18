// port-lint: source glyph/src/font.rs
package io.github.kotlinmania.abglyph

/**
 * Functionality required from font data.
 */
interface Font {
    /**
     * Get the size of the font unit ("font units per em").
     */
    fun unitsPerEm(): Float?

    /**
     * Converts pt units into PxScale.
     */
    fun ptToPxScale(ptSize: Float): PxScale? {
        val pxPerEm = ptSize * (96.0f / 72.0f)
        val units = unitsPerEm() ?: return null
        val height = heightUnscaled()
        return PxScale.from(pxPerEm * height / units)
    }

    /**
     * Unscaled glyph ascent.
     */
    fun ascentUnscaled(): Float

    /**
     * Unscaled glyph descent.
     */
    fun descentUnscaled(): Float

    /**
     * Unscaled height (ascent - descent).
     */
    fun heightUnscaled(): Float = ascentUnscaled() - descentUnscaled()

    /**
     * Unscaled line gap.
     */
    fun lineGapUnscaled(): Float

    /**
     * The slant angle of the font.
     */
    fun italicAngle(): Float = 0.0f

    /**
     * Lookup a GlyphId matching a given char.
     */
    fun glyphId(c: Char): GlyphId

    /**
     * Unscaled horizontal advance for a given glyph id.
     */
    fun hAdvanceUnscaled(id: GlyphId): Float

    /**
     * Unscaled horizontal side bearing for a given glyph id.
     */
    fun hSideBearingUnscaled(id: GlyphId): Float

    /**
     * Unscaled vertical advance for a given glyph id.
     */
    fun vAdvanceUnscaled(id: GlyphId): Float

    /**
     * Unscaled vertical side bearing for a given glyph id.
     */
    fun vSideBearingUnscaled(id: GlyphId): Float

    /**
     * Returns additional unscaled kerning to apply for a particular pair of glyph ids.
     */
    fun kernUnscaled(first: GlyphId, second: GlyphId): Float = 0.0f

    /**
     * Compute unscaled glyph outline curves and bounding box.
     */
    fun outline(id: GlyphId): Outline?

    /**
     * The number of glyphs present in this font.
     */
    fun glyphCount(): Int

    /**
     * Returns an iterator of all distinct (GlyphId, Char) pairs.
     */
    fun codepointIds(): CodepointIdIter

    /**
     * Returns a pre-rendered image of the glyph.
     */
    fun glyphRasterImage2(id: GlyphId, pixelSize: Int): V2.GlyphImage? = null

    /**
     * Returns raw SVG data of a range of glyphs which includes this one.
     */
    fun glyphSvgImage(id: GlyphId): GlyphSvg? = null

    /**
     * Returns the layout bounds of this glyph.
     */
    fun glyphBounds(glyph: Glyph): Rect {
        val sf = asScaled(glyph.scale)
        val pos = glyph.position
        return Rect(
            min = point(pos.x - sf.hSideBearing(glyph.id), pos.y - sf.ascent()),
            max = point(pos.x + sf.hAdvance(glyph.id), pos.y - sf.descent()),
        )
    }

    /**
     * Compute glyph outline ready for drawing.
     */
    fun outlineGlyph(glyph: Glyph): OutlinedGlyph? {
        val outline = outline(glyph.id) ?: return null
        val scaleFactor = asScaled(glyph.scale).scaleFactor()
        return OutlinedGlyph.new(glyph, outline, scaleFactor)
    }

    /**
     * Construct a PxScaleFont by associating with the given pixel scale.
     */
    fun asScaled(scale: PxScale): PxScaleFont<Font> = PxScaleFont(this, scale)

    fun asScaled(scale: Float): PxScaleFont<Font> = PxScaleFont(this, PxScale.from(scale))

    /**
     * Move into a PxScaleFont associated with the given pixel scale.
     */
    fun intoScaled(scale: PxScale): PxScaleFont<Font> = asScaled(scale)

    fun intoScaled(scale: Float): PxScaleFont<Font> = asScaled(scale)

    /**
     * Extracts a slice containing the raw font data.
     */
    fun fontData(): ByteArray
}
