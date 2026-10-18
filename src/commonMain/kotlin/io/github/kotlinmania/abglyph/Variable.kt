// port-lint: source glyph/src/variable.rs
package io.github.kotlinmania.abglyph

/**
 * Logic for variable fonts.
 */
interface VariableFont {
    /**
     * Sets a variation axis coordinate value by its tag.
     * Returns false if there is no such axis tag.
     */
    fun setVariation(tag: ByteArray, value: Float): Boolean

    /**
     * Returns variation axes.
     */
    fun variations(): List<VariationAxis>
}

/**
 * Design variation for an axis.
 */
data class VariationAxis(
    val tag: ByteArray,
    val name: String?,
    val minValue: Float,
    val defaultValue: Float,
    val maxValue: Float,
    val hidden: Boolean,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VariationAxis) return false
        if (!tag.contentEquals(other.tag)) return false
        if (name != other.name) return false
        if (minValue != other.minValue) return false
        if (defaultValue != other.defaultValue) return false
        if (maxValue != other.maxValue) return false
        if (hidden != other.hidden) return false
        return true
    }

    override fun hashCode(): Int {
        var result = tag.contentHashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + minValue.hashCode()
        result = 31 * result + defaultValue.hashCode()
        result = 31 * result + maxValue.hashCode()
        result = 31 * result + hidden.hashCode()
        return result
    }
}
