#if canImport(Testing)
import Testing
import AbGlyph

@Suite("AbGlyph Swift Export Tests")
struct AbGlyphExportTests {
    @Test("AbGlyph swift module imported cleanly")
    func testSwiftModuleLoads() throws {
        #expect(Bool(true), "AbGlyph swift module imported cleanly")
    }
}
#elseif canImport(XCTest)
import XCTest
import AbGlyph

final class AbGlyphExportTests: XCTestCase {
    func testSwiftModuleLoads() throws {
        XCTAssertTrue(true, "AbGlyph swift module imported cleanly")
    }
}
#endif
