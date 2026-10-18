#if canImport(Testing)
import Testing
import AbGlyph

@Suite("AbGlyph Swift Export Tests")
struct AbGlyphExportTests {
    @Test("AbGlyph swift module imported cleanly and point instantiated")
    func testSwiftModuleLoads() throws {
        let p = point(x: 3.0, y: 4.0)
        #expect(p.x == 3.0)
        #expect(p.y == 4.0)
    }
}
#elseif canImport(XCTest)
import XCTest
import AbGlyph

final class AbGlyphExportTests: XCTestCase {
    func testSwiftModuleLoads() throws {
        let p = point(x: 3.0, y: 4.0)
        XCTAssertEqual(p.x, 3.0)
        XCTAssertEqual(p.y, 4.0)
    }
}
#endif
