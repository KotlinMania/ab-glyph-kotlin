// port-lint: source glyph/src/ttfp/variable.rs
package io.github.kotlinmania.abglyph.ttfp

import io.github.kotlinmania.abglyph.VariationAxis

internal fun variations(face: Face): List<VariationAxis> =
    face.variations()
