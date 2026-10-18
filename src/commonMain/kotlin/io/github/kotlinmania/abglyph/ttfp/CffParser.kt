package io.github.kotlinmania.abglyph.ttfp

import io.github.kotlinmania.abglyph.Rect
import io.github.kotlinmania.abglyph.point
import kotlin.math.max
import kotlin.math.min

internal class CffParser(
    private val data: ByteArray,
    private val cffOffset: Int,
    private val cffLength: Int,
) {
    private var charStringsOffset = -1
    private var globalSubrsOffset = -1
    private var localSubrsOffset = -1

    init {
        parseCffHeaderAndDicts()
    }

    private fun readUint8(pos: Int): Int = data[pos].toInt() and 0xFF

    private fun readUint16(pos: Int): Int =
        ((data[pos].toInt() and 0xFF) shl 8) or (data[pos + 1].toInt() and 0xFF)

    private fun readUint24(pos: Int): Int =
        ((data[pos].toInt() and 0xFF) shl 16) or
            ((data[pos + 1].toInt() and 0xFF) shl 8) or
            (data[pos + 2].toInt() and 0xFF)

    private fun readUint32(pos: Int): Int =
        ((data[pos].toInt() and 0xFF) shl 24) or
            ((data[pos + 1].toInt() and 0xFF) shl 16) or
            ((data[pos + 2].toInt() and 0xFF) shl 8) or
            (data[pos + 3].toInt() and 0xFF)

    private fun readInt16(pos: Int): Int =
        ((data[pos].toInt() shl 8) or (data[pos + 1].toInt() and 0xFF)).toShort().toInt()

    private fun readOffset(pos: Int, offSize: Int): Int =
        when (offSize) {
            1 -> readUint8(pos)
            2 -> readUint16(pos)
            3 -> readUint24(pos)
            4 -> readUint32(pos)
            else -> 0
        }

    private class IndexInfo(
        val count: Int,
        val offSize: Int,
        val offsetArrayPos: Int,
        val dataPos: Int,
        val totalLength: Int,
    )

    private fun parseIndex(pos: Int): IndexInfo? {
        if (pos + 2 > cffOffset + cffLength) return null
        val count = readUint16(pos)
        if (count == 0) {
            return IndexInfo(0, 0, pos + 2, pos + 2, 2)
        }
        val offSize = readUint8(pos + 2)
        val offsetArrayPos = pos + 3
        val dataPos = offsetArrayPos + (count + 1) * offSize - 1
        val lastOffset = readOffset(offsetArrayPos + count * offSize, offSize)
        val totalLength = 3 + (count + 1) * offSize + (lastOffset - 1)
        return IndexInfo(count, offSize, offsetArrayPos, dataPos, totalLength)
    }

    private fun getIndexItem(index: IndexInfo, idx: Int): Pair<Int, Int>? {
        if (idx < 0 || idx >= index.count) return null
        val off1 = readOffset(index.offsetArrayPos + idx * index.offSize, index.offSize)
        val off2 = readOffset(index.offsetArrayPos + (idx + 1) * index.offSize, index.offSize)
        val start = index.dataPos + off1
        val len = off2 - off1
        return Pair(start, len)
    }

    private fun parseCffHeaderAndDicts() {
        if (cffLength < 4) return
        val hdrSize = readUint8(cffOffset + 2)
        var pos = cffOffset + hdrSize

        // Name INDEX
        val nameIndex = parseIndex(pos) ?: return
        pos += nameIndex.totalLength

        // Top DICT INDEX
        val topDictIndex = parseIndex(pos) ?: return
        pos += topDictIndex.totalLength

        // String INDEX
        val stringIndex = parseIndex(pos) ?: return
        pos += stringIndex.totalLength

        // Global Subrs INDEX
        globalSubrsOffset = pos

        // Parse Top DICT for CharStrings and Private
        val topDictItem = getIndexItem(topDictIndex, 0) ?: return
        val dictStart = topDictItem.first
        val dictLen = topDictItem.second

        var dPos = dictStart
        val dictEnd = dictStart + dictLen
        val operands = mutableListOf<Float>()
        var privateSize = 0
        var privateOffset = -1

        while (dPos < dictEnd) {
            val b0 = readUint8(dPos++)
            when (b0) {
                in 32..246 -> {
                    operands.add((b0 - 139).toFloat())
                }

                in 247..250 -> {
                    val b1 = readUint8(dPos++)
                    operands.add(((b0 - 247) * 256 + b1 + 108).toFloat())
                }

                in 251..254 -> {
                    val b1 = readUint8(dPos++)
                    operands.add((-(b0 - 251) * 256 - b1 - 108).toFloat())
                }

                28 -> {
                    operands.add(readInt16(dPos).toFloat())
                    dPos += 2
                }

                29 -> {
                    operands.add(readUint32(dPos).toFloat())
                    dPos += 4
                }

                30 -> {
                    // Real number
                    var nibble: Int
                    val sb = StringBuilder()
                    while (true) {
                        val byte = readUint8(dPos++)
                        nibble = (byte shr 4) and 0x0F
                        if (nibble == 0x0F) break
                        appendNibble(sb, nibble)
                        nibble = byte and 0x0F
                        if (nibble == 0x0F) break
                        appendNibble(sb, nibble)
                    }
                    operands.add(sb.toString().toFloatOrNull() ?: 0.0f)
                }

                17 -> {
                    // CharStrings
                    if (operands.isNotEmpty()) {
                        charStringsOffset = cffOffset + operands.last().toInt()
                    }
                    operands.clear()
                }

                18 -> {
                    // Private
                    if (operands.size >= 2) {
                        privateSize = operands[operands.size - 2].toInt()
                        privateOffset = cffOffset + operands[operands.size - 1].toInt()
                    }
                    operands.clear()
                }

                else -> {
                    operands.clear()
                }
            }
        }

        // Parse Private DICT for Subrs
        if (privateOffset >= 0 && privateSize > 0) {
            var pPos = privateOffset
            val pEnd = privateOffset + privateSize
            val pOperands = mutableListOf<Float>()
            while (pPos < pEnd) {
                val b0 = readUint8(pPos++)
                when (b0) {
                    in 32..246 -> {
                        pOperands.add((b0 - 139).toFloat())
                    }

                    in 247..250 -> {
                        val b1 = readUint8(pPos++)
                        pOperands.add(((b0 - 247) * 256 + b1 + 108).toFloat())
                    }

                    in 251..254 -> {
                        val b1 = readUint8(pPos++)
                        pOperands.add((-(b0 - 251) * 256 - b1 - 108).toFloat())
                    }

                    28 -> {
                        pOperands.add(readInt16(pPos).toFloat())
                        pPos += 2
                    }

                    29 -> {
                        pOperands.add(readUint32(pPos).toFloat())
                        pPos += 4
                    }

                    19 -> {
                        // Subrs
                        if (pOperands.isNotEmpty()) {
                            localSubrsOffset = privateOffset + pOperands.last().toInt()
                        }
                        pOperands.clear()
                    }

                    else -> {
                        pOperands.clear()
                    }
                }
            }
        }
    }

    private fun appendNibble(sb: StringBuilder, nibble: Int) {
        when (nibble) {
            in 0..9 -> sb.append(nibble)
            10 -> sb.append('.')
            11 -> sb.append('E')
            12 -> sb.append("E-")
            14 -> sb.append('-')
        }
    }

    fun outline(gid: Int, builder: OutlineCurveBuilder): Rect? {
        if (charStringsOffset < 0) return null
        val csIndex = parseIndex(charStringsOffset) ?: return null
        val item = getIndexItem(csIndex, gid) ?: return null

        val localSubrs = if (localSubrsOffset >= 0) parseIndex(localSubrsOffset) else null
        val globalSubrs = if (globalSubrsOffset >= 0) parseIndex(globalSubrsOffset) else null

        val interp = CharstringInterpreter(data, builder, localSubrs, globalSubrs)
        return interp.execute(item.first, item.second)
    }

    private inner class CharstringInterpreter(
        private val data: ByteArray,
        private val builder: OutlineCurveBuilder,
        private val localSubrs: IndexInfo?,
        private val globalSubrs: IndexInfo?,
    ) {
        private var x = 0.0f
        private var y = 0.0f
        private var minX = Float.MAX_VALUE
        private var maxX = -Float.MAX_VALUE
        private var minY = Float.MAX_VALUE
        private var maxY = -Float.MAX_VALUE
        private var hasPoints = false
        private var stemCount = 0

        private fun updateBounds(px: Float, py: Float) {
            hasPoints = true
            minX = min(minX, px)
            maxX = max(maxX, px)
            minY = min(minY, py)
            maxY = max(maxY, py)
        }

        fun execute(start: Int, length: Int): Rect? {
            val stack = mutableListOf<Float>()
            runSubroutine(start, length, stack, 0)
            builder.close()
            if (!hasPoints) return null
            return Rect(min = point(minX, minY), max = point(maxX, maxY))
        }

        private fun getSubrBias(count: Int): Int =
            when {
                count < 1240 -> 107
                count < 33900 -> 1131
                else -> 32768
            }

        private fun runSubroutine(
            start: Int,
            length: Int,
            stack: MutableList<Float>,
            depth: Int,
        ) {
            if (depth > 10) return
            var pos = start
            val end = start + length

            while (pos < end) {
                val b0 = readUint8(pos++)
                when (b0) {
                    in 32..246 -> {
                        stack.add((b0 - 139).toFloat())
                    }

                    in 247..250 -> {
                        val b1 = readUint8(pos++)
                        stack.add(((b0 - 247) * 256 + b1 + 108).toFloat())
                    }

                    in 251..254 -> {
                        val b1 = readUint8(pos++)
                        stack.add((-(b0 - 251) * 256 - b1 - 108).toFloat())
                    }

                    28 -> {
                        stack.add(readInt16(pos).toFloat())
                        pos += 2
                    }

                    255 -> {
                        val fixed = readUint32(pos)
                        stack.add(fixed.toFloat() / 65536.0f)
                        pos += 4
                    }

                    1, 3, 18, 23 -> {
                        // hstem, vstem, hstemhm, vstemhm
                        stemCount += stack.size / 2
                        stack.clear()
                    }

                    19, 20 -> {
                        // hintmask, cntrmask
                        stemCount += stack.size / 2
                        stack.clear()
                        val maskBytes = (stemCount + 7) / 8
                        pos += maskBytes
                    }

                    4 -> {
                        // vmoveto
                        if (stack.isNotEmpty()) {
                            builder.close()
                            y += stack.last()
                            builder.moveTo(x, y)
                            updateBounds(x, y)
                            stack.clear()
                        }
                    }

                    22 -> {
                        // hmoveto
                        if (stack.isNotEmpty()) {
                            builder.close()
                            x += stack.last()
                            builder.moveTo(x, y)
                            updateBounds(x, y)
                            stack.clear()
                        }
                    }

                    21 -> {
                        // rmoveto
                        if (stack.size >= 2) {
                            builder.close()
                            x += stack[stack.size - 2]
                            y += stack[stack.size - 1]
                            builder.moveTo(x, y)
                            updateBounds(x, y)
                            stack.clear()
                        }
                    }

                    5 -> {
                        // rlineto
                        var i = 0
                        while (i + 1 < stack.size) {
                            x += stack[i]
                            y += stack[i + 1]
                            builder.lineTo(x, y)
                            updateBounds(x, y)
                            i += 2
                        }
                        stack.clear()
                    }

                    6 -> {
                        // hlineto
                        var isHoriz = true
                        for (coord in stack) {
                            if (isHoriz) x += coord else y += coord
                            builder.lineTo(x, y)
                            updateBounds(x, y)
                            isHoriz = !isHoriz
                        }
                        stack.clear()
                    }

                    7 -> {
                        // vlineto
                        var isVert = true
                        for (coord in stack) {
                            if (isVert) y += coord else x += coord
                            builder.lineTo(x, y)
                            updateBounds(x, y)
                            isVert = !isVert
                        }
                        stack.clear()
                    }

                    8 -> {
                        // rrcurveto
                        var i = 0
                        while (i + 5 < stack.size) {
                            val x1 = x + stack[i]
                            val y1 = y + stack[i + 1]
                            val x2 = x1 + stack[i + 2]
                            val y2 = y1 + stack[i + 3]
                            x = x2 + stack[i + 4]
                            y = y2 + stack[i + 5]
                            builder.curveTo(x1, y1, x2, y2, x, y)
                            updateBounds(x1, y1)
                            updateBounds(x2, y2)
                            updateBounds(x, y)
                            i += 6
                        }
                        stack.clear()
                    }

                    10 -> {
                        // callsubr
                        if (stack.isNotEmpty() && localSubrs != null) {
                            val subrIdx = stack.removeAt(stack.size - 1).toInt() + getSubrBias(localSubrs.count)
                            getIndexItem(localSubrs, subrIdx)?.let { item ->
                                runSubroutine(item.first, item.second, stack, depth + 1)
                            }
                        }
                    }

                    11 -> {
                        // return
                        return
                    }

                    14 -> {
                        // endchar
                        builder.close()
                        return
                    }

                    29 -> {
                        // callgsubr
                        if (stack.isNotEmpty() && globalSubrs != null) {
                            val gsubrIdx = stack.removeAt(stack.size - 1).toInt() + getSubrBias(globalSubrs.count)
                            getIndexItem(globalSubrs, gsubrIdx)?.let { item ->
                                runSubroutine(item.first, item.second, stack, depth + 1)
                            }
                        }
                    }

                    else -> {
                        // Skip unhandled operator
                    }
                }
            }
        }
    }
}
