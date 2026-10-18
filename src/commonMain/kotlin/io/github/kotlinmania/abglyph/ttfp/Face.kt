package io.github.kotlinmania.abglyph.ttfp

import io.github.kotlinmania.abglyph.CodepointIdIter
import io.github.kotlinmania.abglyph.GlyphId
import io.github.kotlinmania.abglyph.OutlineCurve
import io.github.kotlinmania.abglyph.Point
import io.github.kotlinmania.abglyph.Rect
import io.github.kotlinmania.abglyph.VariationAxis
import io.github.kotlinmania.abglyph.point
import kotlin.math.max
import kotlin.math.min

internal class TableRecord(
    val tag: String,
    val offset: Int,
    val length: Int,
)

/**
 * Pure Kotlin Multiplatform TrueType and OpenType font face parser.
 */
internal class Face private constructor(
    val data: ByteArray,
    val fontIndex: Int,
) {
    private val tables = mutableMapOf<String, TableRecord>()

    var unitsPerEm: Float = 1000.0f
        private set
    var ascender: Float = 0.0f
        private set
    var descender: Float = 0.0f
        private set
    var lineGap: Float = 0.0f
        private set
    var italicAngle: Float = 0.0f
        private set
    var numberOfGlyphs: Int = 0
        private set

    private var numberOfHMetrics: Int = 0
    private var indexToLocFormat: Int = 0
    private var locaOffset: Int = -1
    private var locaLength: Int = 0
    private var glyfOffset: Int = -1
    private var hmtxOffset: Int = -1
    private var kernOffset: Int = -1
    private var cffOffset: Int = -1
    private var cffLength: Int = 0
    private var fvarOffset: Int = -1

    // Pre-parsed cmap
    private val cmapTable = mutableMapOf<Char, Int>()
    private val codepointsList = mutableListOf<Pair<GlyphId, Char>>()

    // Pre-parsed kerning
    private val kernTable = mutableMapOf<Long, Float>()

    // Variation axes
    private val variationAxesList = mutableListOf<VariationAxis>()

    init {
        parseTables()
    }

    companion object {
        fun parse(data: ByteArray, index: Int = 0): Face? =
            try {
                Face(data, index)
            } catch (e: Exception) {
                null
            }
    }

    private fun readUint8(pos: Int): Int = data[pos].toInt() and 0xFF

    private fun readInt8(pos: Int): Int = data[pos].toInt()

    private fun readUint16(pos: Int): Int =
        ((data[pos].toInt() and 0xFF) shl 8) or (data[pos + 1].toInt() and 0xFF)

    private fun readInt16(pos: Int): Int =
        ((data[pos].toInt() shl 8) or (data[pos + 1].toInt() and 0xFF)).toShort().toInt()

    private fun readUint32(pos: Int): Long =
        (
            ((data[pos].toLong() and 0xFF) shl 24) or
                ((data[pos + 1].toLong() and 0xFF) shl 16) or
                ((data[pos + 2].toLong() and 0xFF) shl 8) or
                (data[pos + 3].toLong() and 0xFF)
        )

    private fun readInt32(pos: Int): Int =
        (
            ((data[pos].toInt() and 0xFF) shl 24) or
                ((data[pos + 1].toInt() and 0xFF) shl 16) or
                ((data[pos + 2].toInt() and 0xFF) shl 8) or
                (data[pos + 3].toInt() and 0xFF)
        )

    private fun readTag(pos: Int): String =
        data.decodeToString(pos, pos + 4)

    private fun parseTables() {
        if (data.size < 12) throw IllegalArgumentException("Font data too small")

        var headerOffset = 0
        val magic = readTag(0)
        if (magic == "ttcf") {
            // TrueType Collection
            val numFonts = readUint32(8).toInt()
            if (fontIndex < 0 || fontIndex >= numFonts) {
                throw IllegalArgumentException("Font index out of range")
            }
            headerOffset = readUint32(12 + fontIndex * 4).toInt()
        }

        val numTables = readUint16(headerOffset + 4)
        var dirOffset = headerOffset + 12
        repeat(numTables) {
            val tag = readTag(dirOffset)
            val offset = readUint32(dirOffset + 8).toInt()
            val length = readUint32(dirOffset + 12).toInt()
            tables[tag] = TableRecord(tag, offset, length)
            dirOffset += 16
        }

        // Parse head
        tables["head"]?.let { r ->
            unitsPerEm = readUint16(r.offset + 18).toFloat()
            indexToLocFormat = readInt16(r.offset + 50)
        }

        // Parse hhea
        tables["hhea"]?.let { r ->
            ascender = readInt16(r.offset + 4).toFloat()
            descender = readInt16(r.offset + 6).toFloat()
            lineGap = readInt16(r.offset + 8).toFloat()
            numberOfHMetrics = readUint16(r.offset + 34)
        }

        // Parse maxp
        tables["maxp"]?.let { r ->
            numberOfGlyphs = readUint16(r.offset + 4)
        }

        // Parse post
        tables["post"]?.let { r ->
            val fixed = readInt32(r.offset + 4)
            italicAngle = fixed.toFloat() / 65536.0f
        }

        // Store offsets for hmtx, loca, glyf, kern, cff, fvar
        tables["hmtx"]?.let { hmtxOffset = it.offset }
        tables["loca"]?.let {
            locaOffset = it.offset
            locaLength = it.length
        }
        tables["glyf"]?.let { glyfOffset = it.offset }
        tables["kern"]?.let { kernOffset = it.offset }
        tables["CFF "]?.let {
            cffOffset = it.offset
            cffLength = it.length
        }
        tables["fvar"]?.let { fvarOffset = it.offset }

        parseCmap()
        parseKern()
        parseFvar()
    }

    private fun parseCmap() {
        val cmap = tables["cmap"] ?: return
        val off = cmap.offset
        val numTables = readUint16(off + 2)
        var chosenOffset = -1
        var chosenFormat = -1

        for (i in 0 until numTables) {
            val pId = readUint16(off + 4 + i * 8)
            val eId = readUint16(off + 4 + i * 8 + 2)
            val subOffset = off + readUint32(off + 4 + i * 8 + 4).toInt()
            val format = readUint16(subOffset)

            if (format == 12) {
                chosenOffset = subOffset
                chosenFormat = 12
                break
            } else if (format == 4 && (chosenFormat != 4 || (pId == 0 || (pId == 3 && eId == 1)))) {
                chosenOffset = subOffset
                chosenFormat = 4
            }
        }

        if (chosenOffset < 0) return

        if (chosenFormat == 4) {
            val segCountX2 = readUint16(chosenOffset + 6)
            val segCount = segCountX2 / 2
            val endCodeOffset = chosenOffset + 14
            val startCodeOffset = endCodeOffset + segCount * 2 + 2
            val idDeltaOffset = startCodeOffset + segCount * 2
            val idRangeOffset = idDeltaOffset + segCount * 2

            val seenGlyphs = mutableSetOf<Int>()
            for (s in 0 until segCount) {
                val start = readUint16(startCodeOffset + s * 2)
                val end = readUint16(endCodeOffset + s * 2)
                val idDelta = readInt16(idDeltaOffset + s * 2)
                val rangeOffset = readUint16(idRangeOffset + s * 2)

                if (start == 0xFFFF) break
                for (c in start..end) {
                    val gid =
                        if (rangeOffset == 0) {
                            (c + idDelta) and 0xFFFF
                        } else {
                            val glyphArrayOffset = idRangeOffset + s * 2 + rangeOffset + (c - start) * 2
                            val g = readUint16(glyphArrayOffset)
                            if (g != 0) (g + idDelta) and 0xFFFF else 0
                        }
                    if (gid > 0) {
                        val ch = c.toChar()
                        cmapTable[ch] = gid
                        if (seenGlyphs.add(gid)) {
                            codepointsList.add(Pair(GlyphId(gid), ch))
                        }
                    }
                }
            }
        } else if (chosenFormat == 12) {
            val numGroups = readUint32(chosenOffset + 12).toInt()
            val seenGlyphs = mutableSetOf<Int>()
            var grpOffset = chosenOffset + 16
            repeat(numGroups) {
                val startChar = readUint32(grpOffset).toInt()
                val endChar = readUint32(grpOffset + 4).toInt()
                val startGid = readUint32(grpOffset + 8).toInt()
                grpOffset += 12

                for (code in startChar..endChar) {
                    val gid = startGid + (code - startChar)
                    if (gid > 0 && code <= 0xFFFF) {
                        val ch = code.toChar()
                        cmapTable[ch] = gid
                        if (seenGlyphs.add(gid)) {
                            codepointsList.add(Pair(GlyphId(gid), ch))
                        }
                    }
                }
            }
        }
    }

    private fun parseKern() {
        if (kernOffset < 0) return
        val nTables = readUint16(kernOffset + 2)
        var subtableOffset = kernOffset + 4
        repeat(nTables) {
            val version = readUint16(subtableOffset)
            val length = readUint16(subtableOffset + 2)
            val coverage = readUint16(subtableOffset + 4)
            if (version == 0 && (coverage and 0x0001) != 0 && (coverage shr 8) == 0) {
                val nPairs = readUint16(subtableOffset + 6)
                var pairOffset = subtableOffset + 14
                repeat(nPairs) {
                    val left = readUint16(pairOffset)
                    val right = readUint16(pairOffset + 2)
                    val value = readInt16(pairOffset + 4).toFloat()
                    pairOffset += 6
                    val key = (left.toLong() shl 16) or right.toLong()
                    kernTable[key] = value
                }
                return
            }
            subtableOffset += length
        }
    }

    private fun parseFvar() {
        if (fvarOffset < 0) return
        val axesCount = readUint16(fvarOffset + 8)
        var axisOffset = fvarOffset + readUint16(fvarOffset + 4)
        val axisSize = readUint16(fvarOffset + 10)
        repeat(axesCount) {
            val tagBytes = data.copyOfRange(axisOffset, axisOffset + 4)
            val minVal = readInt32(axisOffset + 4).toFloat() / 65536.0f
            val defVal = readInt32(axisOffset + 8).toFloat() / 65536.0f
            val maxVal = readInt32(axisOffset + 12).toFloat() / 65536.0f
            val flags = readUint16(axisOffset + 16)
            variationAxesList.add(
                VariationAxis(
                    tag = tagBytes,
                    name = null,
                    minValue = minVal,
                    defaultValue = defVal,
                    maxValue = maxVal,
                    hidden = (flags and 0x0001) != 0,
                ),
            )
            axisOffset += axisSize
        }
    }

    fun glyphIndex(c: Char): Int = cmapTable[c] ?: 0

    fun glyphHorAdvance(id: GlyphId): Float {
        if (hmtxOffset < 0 || id.value < 0 || id.value >= numberOfGlyphs) return 0.0f
        return if (id.value < numberOfHMetrics) {
            readUint16(hmtxOffset + id.value * 4).toFloat()
        } else if (numberOfHMetrics > 0) {
            readUint16(hmtxOffset + (numberOfHMetrics - 1) * 4).toFloat()
        } else {
            0.0f
        }
    }

    fun glyphHorSideBearing(id: GlyphId): Float {
        if (hmtxOffset < 0 || id.value < 0 || id.value >= numberOfGlyphs) return 0.0f
        return if (id.value < numberOfHMetrics) {
            readInt16(hmtxOffset + id.value * 4 + 2).toFloat()
        } else {
            readInt16(hmtxOffset + numberOfHMetrics * 4 + (id.value - numberOfHMetrics) * 2).toFloat()
        }
    }

    fun glyphVerAdvance(id: GlyphId): Float = 0.0f

    fun glyphVerSideBearing(id: GlyphId): Float = 0.0f

    fun glyphsHorKerning(first: GlyphId, second: GlyphId): Float {
        val key = (first.value.toLong() shl 16) or (second.value.toLong() and 0xFFFF)
        return kernTable[key] ?: 0.0f
    }

    fun codepointIds(): CodepointIdIter =
        CodepointIdIter(codepointsList.iterator())

    fun variations(): List<VariationAxis> = variationAxesList

    fun setVariation(tag: ByteArray, value: Float): Boolean {
        for (axis in variationAxesList) {
            if (axis.tag.contentEquals(tag)) {
                return true
            }
        }
        return false
    }

    fun outlineGlyph(id: GlyphId, builder: OutlineCurveBuilder): Rect? {
        if (glyfOffset >= 0 && locaOffset >= 0) {
            return outlineGlyphTrueType(id.value, builder)
        }
        if (cffOffset >= 0) {
            return outlineGlyphCff(id.value, builder)
        }
        return null
    }

    private fun outlineGlyphTrueType(gid: Int, builder: OutlineCurveBuilder): Rect? {
        if (gid < 0 || gid >= numberOfGlyphs) return null
        val offset: Int
        val nextOffset: Int
        if (indexToLocFormat == 0) {
            offset = readUint16(locaOffset + gid * 2) * 2
            nextOffset = readUint16(locaOffset + (gid + 1) * 2) * 2
        } else {
            offset = readUint32(locaOffset + gid * 4).toInt()
            nextOffset = readUint32(locaOffset + (gid + 1) * 4).toInt()
        }

        if (offset == nextOffset) return null // Empty outline (e.g. space)

        val gOffset = glyfOffset + offset
        val numberOfContours = readInt16(gOffset)
        var xMin = readInt16(gOffset + 2)
        var yMin = readInt16(gOffset + 4)
        var xMax = readInt16(gOffset + 6)
        var yMax = readInt16(gOffset + 8)

        if (numberOfContours >= 0) {
            val endPts = IntArray(numberOfContours)
            for (i in 0 until numberOfContours) {
                endPts[i] = readUint16(gOffset + 10 + i * 2)
            }
            val insLen = readUint16(gOffset + 10 + numberOfContours * 2)
            var pos = gOffset + 12 + numberOfContours * 2 + insLen
            val nPts = if (numberOfContours > 0) endPts.last() + 1 else 0
            if (nPts == 0) return null

            val flags = IntArray(nPts)
            var fIdx = 0
            while (fIdx < nPts) {
                val flag = readUint8(pos++)
                flags[fIdx++] = flag
                if ((flag and 0x08) != 0) {
                    val rep = readUint8(pos++)
                    repeat(rep) {
                        if (fIdx < nPts) flags[fIdx++] = flag
                    }
                }
            }

            val xs = FloatArray(nPts)
            var currX = 0
            for (i in 0 until nPts) {
                val f = flags[i]
                if ((f and 0x02) != 0) {
                    val dx = readUint8(pos++)
                    currX += if ((f and 0x10) != 0) dx else -dx
                } else if ((f and 0x10) == 0) {
                    currX += readInt16(pos)
                    pos += 2
                }
                xs[i] = currX.toFloat()
            }

            val ys = FloatArray(nPts)
            var currY = 0
            for (i in 0 until nPts) {
                val f = flags[i]
                if ((f and 0x04) != 0) {
                    val dy = readUint8(pos++)
                    currY += if ((f and 0x20) != 0) dy else -dy
                } else if ((f and 0x20) == 0) {
                    currY += readInt16(pos)
                    pos += 2
                }
                ys[i] = currY.toFloat()
            }

            // Build contours
            var startIdx = 0
            for (c in 0 until numberOfContours) {
                val endIdx = endPts[c]
                val len = endIdx - startIdx + 1
                if (len <= 0) continue

                val cXs = FloatArray(len) { xs[startIdx + it] }
                val cYs = FloatArray(len) { ys[startIdx + it] }
                val cOn = BooleanArray(len) { (flags[startIdx + it] and 0x01) != 0 }

                if (len == 1) {
                    builder.moveTo(cXs[0], cYs[0])
                    builder.close()
                    startIdx = endIdx + 1
                    continue
                }

                val firstOn = cOn[0]
                val lastOn = cOn[len - 1]
                val startX: Float
                val startY: Float
                val offsetIdx: Int

                if (firstOn) {
                    startX = cXs[0]
                    startY = cYs[0]
                    offsetIdx = 1
                } else if (lastOn) {
                    startX = cXs[len - 1]
                    startY = cYs[len - 1]
                    offsetIdx = 0
                } else {
                    startX = (cXs[0] + cXs[len - 1]) * 0.5f
                    startY = (cYs[0] + cYs[len - 1]) * 0.5f
                    offsetIdx = 0
                }

                builder.moveTo(startX, startY)
                var i = 0
                while (i < len) {
                    val idx = (offsetIdx + i) % len
                    val px = cXs[idx]
                    val py = cYs[idx]
                    val on = cOn[idx]

                    if (on) {
                        builder.lineTo(px, py)
                        i++
                    } else {
                        val nextIdx = (idx + 1) % len
                        val nextX = cXs[nextIdx]
                        val nextY = cYs[nextIdx]
                        val nextOn = cOn[nextIdx]
                        if (nextOn) {
                            builder.quadTo(px, py, nextX, nextY)
                            i += 2
                        } else {
                            val midX = (px + nextX) * 0.5f
                            val midY = (py + nextY) * 0.5f
                            builder.quadTo(px, py, midX, midY)
                            i += 1
                        }
                    }
                }
                builder.close()
                startIdx = endIdx + 1
            }

            // Zero bounding box fallback (e.g. airstrip.ttf issue 29)
            if (xMin >= xMax || yMin >= yMax) {
                var calcMinX = Float.MAX_VALUE
                var calcMaxX = -Float.MAX_VALUE
                var calcMinY = Float.MAX_VALUE
                var calcMaxY = -Float.MAX_VALUE
                for (i in 0 until nPts) {
                    calcMinX = min(calcMinX, xs[i])
                    calcMaxX = max(calcMaxX, xs[i])
                    calcMinY = min(calcMinY, ys[i])
                    calcMaxY = max(calcMaxY, ys[i])
                }
                xMin = calcMinX.toInt()
                xMax = calcMaxX.toInt()
                yMin = calcMinY.toInt()
                yMax = calcMaxY.toInt()
            }

            return Rect(
                min = point(xMin.toFloat(), yMin.toFloat()),
                max = point(xMax.toFloat(), yMax.toFloat()),
            )
        }

        // Composite glyph
        return outlineCompositeGlyph(gOffset + 10, xMin, yMin, xMax, yMax, builder)
    }

    private fun outlineCompositeGlyph(
        startPos: Int,
        xMin: Int,
        yMin: Int,
        xMax: Int,
        yMax: Int,
        builder: OutlineCurveBuilder,
    ): Rect? {
        var pos = startPos
        var minX = xMin.toFloat()
        var minY = yMin.toFloat()
        var maxX = xMax.toFloat()
        var maxY = yMax.toFloat()

        do {
            val flags = readUint16(pos)
            val subGid = readUint16(pos + 2)
            pos += 4

            var dx = 0.0f
            var dy = 0.0f
            if ((flags and 0x0002) != 0) { // ARGS_ARE_XY_VALUES
                if ((flags and 0x0001) != 0) { // ARG_1_AND_2_ARE_WORDS
                    dx = readInt16(pos).toFloat()
                    dy = readInt16(pos + 2).toFloat()
                    pos += 4
                } else {
                    dx = readInt8(pos).toFloat()
                    dy = readInt8(pos + 1).toFloat()
                    pos += 2
                }
            } else {
                if ((flags and 0x0001) != 0) pos += 4 else pos += 2
            }

            var a = 1.0f
            var b = 0.0f
            var c = 0.0f
            var d = 1.0f
            if ((flags and 0x0008) != 0) { // WE_HAVE_A_SCALE
                val s = readInt16(pos).toFloat() / 16384.0f
                a = s
                d = s
                pos += 2
            } else if ((flags and 0x0040) != 0) { // WE_HAVE_AN_X_AND_Y_SCALE
                a = readInt16(pos).toFloat() / 16384.0f
                d = readInt16(pos + 2).toFloat() / 16384.0f
                pos += 4
            } else if ((flags and 0x0080) != 0) { // WE_HAVE_A_TWO_BY_TWO
                a = readInt16(pos).toFloat() / 16384.0f
                b = readInt16(pos + 2).toFloat() / 16384.0f
                c = readInt16(pos + 4).toFloat() / 16384.0f
                d = readInt16(pos + 6).toFloat() / 16384.0f
                pos += 8
            }

            val subBuilder = OutlineCurveBuilder()
            val subRect = outlineGlyphTrueType(subGid, subBuilder)
            if (subRect != null) {
                fun transform(pt: Point): Point =
                    point(pt.x * a + pt.y * c + dx, pt.x * b + pt.y * d + dy)

                for (curve in subBuilder.takeOutline()) {
                    when (curve) {
                        is OutlineCurve.Line -> {
                            val p0 = transform(curve.p0)
                            val p1 = transform(curve.p1)
                            builder.moveTo(p0.x, p0.y)
                            builder.lineTo(p1.x, p1.y)
                            builder.close()
                            minX = min(minX, min(p0.x, p1.x))
                            maxX = max(maxX, max(p0.x, p1.x))
                            minY = min(minY, min(p0.y, p1.y))
                            maxY = max(maxY, max(p0.y, p1.y))
                        }

                        is OutlineCurve.Quad -> {
                            val p0 = transform(curve.p0)
                            val p1 = transform(curve.p1)
                            val p2 = transform(curve.p2)
                            builder.moveTo(p0.x, p0.y)
                            builder.quadTo(p1.x, p1.y, p2.x, p2.y)
                            builder.close()
                            minX = min(minX, min(p0.x, min(p1.x, p2.x)))
                            maxX = max(maxX, max(p0.x, max(p1.x, p2.x)))
                            minY = min(minY, min(p0.y, min(p1.y, p2.y)))
                            maxY = max(maxY, max(p0.y, max(p1.y, p2.y)))
                        }

                        is OutlineCurve.Cubic -> {
                            val p0 = transform(curve.p0)
                            val p1 = transform(curve.p1)
                            val p2 = transform(curve.p2)
                            val p3 = transform(curve.p3)
                            builder.moveTo(p0.x, p0.y)
                            builder.curveTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y)
                            builder.close()
                            minX = min(minX, min(p0.x, min(p1.x, min(p2.x, p3.x))))
                            maxX = max(maxX, max(p0.x, max(p1.x, max(p2.x, p3.x))))
                            minY = min(minY, min(p0.y, min(p1.y, min(p2.y, p3.y))))
                            maxY = max(maxY, max(p0.y, max(p1.y, max(p2.y, p3.y))))
                        }
                    }
                }
            }
        } while ((flags and 0x0020) != 0) // MORE_COMPONENTS

        return Rect(min = point(minX, minY), max = point(maxX, maxY))
    }

    private fun outlineGlyphCff(gid: Int, builder: OutlineCurveBuilder): Rect? {
        val cff = CffParser(data, cffOffset, cffLength)
        return cff.outline(gid, builder)
    }
}
