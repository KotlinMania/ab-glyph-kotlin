# ab-glyph-kotlin in Kotlin

[![GitHub link](https://img.shields.io/badge/GitHub-KotlinMania%2Fab-glyph-kotlin-blue.svg)](https://github.com/KotlinMania/ab-glyph-kotlin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlinmania/ab-glyph-kotlin)](https://central.sonatype.com/artifact/io.github.kotlinmania/ab-glyph-kotlin)
[![Build status](https://img.shields.io/github/actions/workflow/status/KotlinMania/ab-glyph-kotlin/ci.yml?branch=main)](https://github.com/KotlinMania/ab-glyph-kotlin/actions)

This is a Kotlin Multiplatform line-by-line transliteration port of [`alexheretic/ab-glyph`](https://github.com/alexheretic/ab-glyph) (crate `ab_glyph`).

**Original Project:** This port is based on [`alexheretic/ab-glyph`](https://github.com/alexheretic/ab-glyph). All design credit and project intent belong to the upstream authors; this repository is a faithful port to Kotlin Multiplatform with no behavioural changes intended.

### Porting status

This is an **in-progress port**. The goal is feature parity with the upstream Rust crate while providing a native Kotlin Multiplatform API. Every Kotlin file carries a `// port-lint: source <path>` header naming its upstream Rust counterpart so the AST-distance tool can track provenance.

---

## Upstream README — `alexheretic/ab-glyph`

> The text below is reproduced from [`https://github.com/alexheretic/ab-glyph`](https://github.com/alexheretic/ab-glyph). It is the upstream project's own description and remains under the upstream authors' authorship; links have been rewritten to absolute upstream URLs where appropriate so they continue to resolve from this repository.

## [ab_glyph](glyph) [![crates.io](https://img.shields.io/crates/v/ab_glyph.svg)](https://crates.io/crates/ab_glyph) [![Documentation](https://docs.rs/ab_glyph/badge.svg)](https://docs.rs/ab_glyph)
Fast API for loading, scaling, positioning and rasterizing OpenType font glyphs. See [readme](glyph), [changelog](glyph/CHANGELOG.md).

## [ab_glyph_rasterizer](rasterizer) [![](https://img.shields.io/crates/v/ab_glyph_rasterizer.svg)](https://crates.io/crates/ab_glyph_rasterizer) [![](https://docs.rs/ab_glyph_rasterizer/badge.svg)](https://docs.rs/ab_glyph_rasterizer)
Zero dependency coverage rasterization for lines, quadratic & cubic beziers.  See [readme](rasterizer), [changelog](rasterizer/CHANGELOG.md).

## Minimum supported rust compiler
All crates maintained with [latest stable rust](https://gist.github.com/alexheretic/d1e98d8433b602e57f5d0a9637927e0c).

---

## About this Kotlin port

### Installation

```kotlin
dependencies {
    implementation("io.github.kotlinmania:ab-glyph-kotlin:0.1.2")
}
```

### Building

```bash
./gradlew build
./gradlew test
```

### Targets

- macOS arm64
- Linux x64
- Windows mingw-x64
- iOS arm64 / simulator-arm64 (Swift export + XCFramework)
- JS (browser + Node.js)
- Wasm-JS (browser + Node.js)
- Android (API 24+)

### Porting guidelines

See [AGENTS.md](AGENTS.md) and [CLAUDE.md](CLAUDE.md) for translator discipline, port-lint header convention, and Rust → Kotlin idiom mapping.

### License

This Kotlin port is distributed under the same license terms as the upstream [`alexheretic/ab-glyph`](https://github.com/alexheretic/ab-glyph). See [LICENSE](LICENSE) (and any sibling `LICENSE-*` / `NOTICE` files mirrored from upstream) for the full text.

Original work copyrighted by the ab_glyph authors.  
Kotlin port: Copyright (c) 2026 Sydney Renee and The Solace Project.

### Acknowledgments

Thanks to the [`alexheretic/ab-glyph`](https://github.com/alexheretic/ab-glyph) maintainers and contributors for the original Rust implementation. This port reproduces their work in Kotlin Multiplatform; bug reports about upstream design or behavior should go to the upstream repository.
