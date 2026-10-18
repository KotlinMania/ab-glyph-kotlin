// port-lint: source glyph/src/err.rs
package io.github.kotlinmania.abglyph

/**
 * Invalid font data error.
 */
class InvalidFont(message: String = "InvalidFont") : Exception(message) {
    override fun toString(): String = message ?: "InvalidFont"
}
