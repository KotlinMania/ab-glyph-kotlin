// port-lint: source glyph/src/ttfp.rs
package io.github.kotlinmania.abglyph

import io.github.kotlinmania.abglyph.ttfp.Face
import io.github.kotlinmania.abglyph.ttfp.OutlineCurveBuilder

/**
 * Font data handle stored as byte data + parsed data.
 */
class FontRef private constructor(
    private val face: Face,
) : Font, VariableFont {

    companion object {
        /**
         * Creates a FontRef from byte data.
         */
        fun tryFromSlice(data: ByteArray): FontRef =
            tryFromSliceAndIndex(data, 0)

        /**
         * Creates a FontRef from byte data at a specific index.
         */
        fun tryFromSliceAndIndex(data: ByteArray, index: Int): FontRef {
            val face = Face.parse(data, index) ?: throw InvalidFont()
            return FontRef(face)
        }
    }

    override fun unitsPerEm(): Float? = face.unitsPerEm

    override fun ascentUnscaled(): Float = face.ascender

    override fun descentUnscaled(): Float = face.descender

    override fun lineGapUnscaled(): Float = face.lineGap

    override fun italicAngle(): Float = face.italicAngle

    override fun glyphId(c: Char): GlyphId = GlyphId(face.glyphIndex(c))

    override fun hAdvanceUnscaled(id: GlyphId): Float = face.glyphHorAdvance(id)

    override fun hSideBearingUnscaled(id: GlyphId): Float = face.glyphHorSideBearing(id)

    override fun vAdvanceUnscaled(id: GlyphId): Float = face.glyphVerAdvance(id)

    override fun vSideBearingUnscaled(id: GlyphId): Float = face.glyphVerSideBearing(id)

    override fun kernUnscaled(first: GlyphId, second: GlyphId): Float =
        face.glyphsHorKerning(first, second)

    override fun outline(id: GlyphId): Outline? {
        val outliner = OutlineCurveBuilder()
        val rect = face.outlineGlyph(id, outliner) ?: return null
        if (rect.min.x >= rect.max.x || rect.min.y >= rect.max.y) {
            return null
        }
        val curves = outliner.takeOutline()
        val bounds = Rect(
            min = point(rect.min.x, rect.max.y),
            max = point(rect.max.x, rect.min.y),
        )
        return Outline(bounds, curves)
    }

    override fun glyphCount(): Int = face.numberOfGlyphs

    override fun codepointIds(): CodepointIdIter = face.codepointIds()

    override fun fontData(): ByteArray = face.data

    override fun setVariation(tag: ByteArray, value: Float): Boolean =
        face.setVariation(tag, value)

    override fun variations(): List<VariationAxis> = face.variations()

    override fun toString(): String = "FontRef"
}

/**
 * Owned font data handle stored in a ByteArray + parsed data.
 */
class FontVec private constructor(
    private val face: Face,
) : Font, VariableFont {

    companion object {
        /**
         * Creates a FontVec from owned data.
         */
        fun tryFromVec(data: ByteArray): FontVec =
            tryFromVecAndIndex(data, 0)

        /**
         * Creates a FontVec from owned data at a specific index.
         */
        fun tryFromVecAndIndex(data: ByteArray, index: Int): FontVec {
            val face = Face.parse(data, index) ?: throw InvalidFont()
            return FontVec(face)
        }
    }

    fun asSlice(): ByteArray = face.data

    fun intoVec(): ByteArray = face.data

    override fun unitsPerEm(): Float? = face.unitsPerEm

    override fun ascentUnscaled(): Float = face.ascender

    override fun descentUnscaled(): Float = face.descender

    override fun lineGapUnscaled(): Float = face.lineGap

    override fun italicAngle(): Float = face.italicAngle

    override fun glyphId(c: Char): GlyphId = GlyphId(face.glyphIndex(c))

    override fun hAdvanceUnscaled(id: GlyphId): Float = face.glyphHorAdvance(id)

    override fun hSideBearingUnscaled(id: GlyphId): Float = face.glyphHorSideBearing(id)

    override fun vAdvanceUnscaled(id: GlyphId): Float = face.glyphVerAdvance(id)

    override fun vSideBearingUnscaled(id: GlyphId): Float = face.glyphVerSideBearing(id)

    override fun kernUnscaled(first: GlyphId, second: GlyphId): Float =
        face.glyphsHorKerning(first, second)

    override fun outline(id: GlyphId): Outline? {
        val outliner = OutlineCurveBuilder()
        val rect = face.outlineGlyph(id, outliner) ?: return null
        if (rect.min.x >= rect.max.x || rect.min.y >= rect.max.y) {
            return null
        }
        val curves = outliner.takeOutline()
        val bounds = Rect(
            min = point(rect.min.x, rect.max.y),
            max = point(rect.max.x, rect.min.y),
        )
        return Outline(bounds, curves)
    }

    override fun glyphCount(): Int = face.numberOfGlyphs

    override fun codepointIds(): CodepointIdIter = face.codepointIds()

    override fun fontData(): ByteArray = face.data

    override fun setVariation(tag: ByteArray, value: Float): Boolean =
        face.setVariation(tag, value)

    override fun variations(): List<VariationAxis> = face.variations()

    override fun toString(): String = "FontVec"
}
