// port-lint: source glyph/src/glyph.rs
package io.github.kotlinmania.abglyph

/**
 * Glyph id.
 */
data class GlyphId(val value: Int = 0) : Comparable<GlyphId> {
    constructor(u16Value: UShort) : this(u16Value.toInt())

    /**
     * Construct a Glyph with given scale and position.
     */
    fun withScaleAndPosition(scale: PxScale, position: Point): Glyph =
        Glyph(this, scale, position)

    fun withScaleAndPosition(scale: Float, position: Point): Glyph =
        Glyph(this, PxScale.from(scale), position)

    /**
     * Construct a Glyph with given scale and position point(0.0, 0.0).
     */
    fun withScale(scale: PxScale): Glyph =
        withScaleAndPosition(scale, Point())

    fun withScale(scale: Float): Glyph =
        withScaleAndPosition(PxScale.from(scale), Point())

    override fun compareTo(other: GlyphId): Int = value.compareTo(other.value)

    override fun toString(): String = "GlyphId($value)"
}

/**
 * A glyph with pixel scale and position.
 */
data class Glyph(
    val id: GlyphId,
    val scale: PxScale,
    val position: Point,
)

/**
 * Old version of GlyphImage. Deprecated in upstream ab_glyph in favor of V2.GlyphImage.
 */
data class GlyphImage(
    val origin: Point,
    val scale: Float,
    val data: ByteArray,
    val format: GlyphImageFormat,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GlyphImage) return false
        if (origin != other.origin) return false
        if (scale != other.scale) return false
        if (!data.contentEquals(other.data)) return false
        if (format != other.format) return false
        return true
    }

    override fun hashCode(): Int {
        var result = origin.hashCode()
        result = 31 * result + scale.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + format.hashCode()
        return result
    }
}

/**
 * Raw SVG data of a range of glyphs.
 */
data class GlyphSvg(
    val data: ByteArray,
    val startGlyphId: GlyphId,
    val endGlyphId: GlyphId,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GlyphSvg) return false
        if (!data.contentEquals(other.data)) return false
        if (startGlyphId != other.startGlyphId) return false
        if (endGlyphId != other.endGlyphId) return false
        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + startGlyphId.hashCode()
        result = 31 * result + endGlyphId.hashCode()
        return result
    }
}

object V2 {
    /**
     * A pre-rendered image of a glyph, usually used for emojis or other glyphs
     * that cannot be represented only using an outline.
     */
    data class GlyphImage(
        val origin: Point,
        val width: Int,
        val height: Int,
        val pixelsPerEm: Int,
        val data: ByteArray,
        val format: GlyphImageFormat,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is GlyphImage) return false
            if (origin != other.origin) return false
            if (width != other.width) return false
            if (height != other.height) return false
            if (pixelsPerEm != other.pixelsPerEm) return false
            if (!data.contentEquals(other.data)) return false
            if (format != other.format) return false
            return true
        }

        override fun hashCode(): Int {
            var result = origin.hashCode()
            result = 31 * result + width
            result = 31 * result + height
            result = 31 * result + pixelsPerEm
            result = 31 * result + data.contentHashCode()
            result = 31 * result + format.hashCode()
            return result
        }
    }
}

/**
 * Valid formats for a GlyphImage.
 */
enum class GlyphImageFormat {
    Png,
    BitmapMono,
    BitmapMonoPacked,
    BitmapGray2,
    BitmapGray2Packed,
    BitmapGray4,
    BitmapGray4Packed,
    BitmapGray8,
    BitmapPremulBgra32,
}
