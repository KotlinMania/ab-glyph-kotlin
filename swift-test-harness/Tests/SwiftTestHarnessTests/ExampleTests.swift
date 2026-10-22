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

    @Test("Point arithmetic and distance calculation")
    func testPointArithmetic() throws {
        let p1 = Point(x: 1.0, y: 2.0)
        let p2 = Point(x: 4.0, y: 6.0)
        let sum = p1 + p2
        #expect(sum.x == 5.0)
        #expect(sum.y == 8.0)

        let diff = p2 - p1
        #expect(diff.x == 3.0)
        #expect(diff.y == 4.0)

        let dist = point(x: 0.0, y: 0.0).distanceTo(other: point(x: 3.0, y: 4.0))
        #expect(dist == 5.0)
    }

    @Test("Rasterizer dimensions and reset lifecycle")
    func testRasterizerLifecycle() throws {
        let rasterizer = Rasterizer(width: 64, height: 48)
        #expect(rasterizer.width == 64)
        #expect(rasterizer.height == 48)

        rasterizer.reset(width: 32, height: 16)
        #expect(rasterizer.width == 32)
        #expect(rasterizer.height == 16)

        rasterizer.clear()
        #expect(rasterizer.width == 32)
        #expect(rasterizer.height == 16)
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

    func testPointArithmetic() throws {
        let p1 = Point(x: 1.0, y: 2.0)
        let p2 = Point(x: 4.0, y: 6.0)
        let sum = p1 + p2
        XCTAssertEqual(sum.x, 5.0)
        XCTAssertEqual(sum.y, 8.0)

        let diff = p2 - p1
        XCTAssertEqual(diff.x, 3.0)
        XCTAssertEqual(diff.y, 4.0)

        let dist = point(x: 0.0, y: 0.0).distanceTo(other: point(x: 3.0, y: 4.0))
        XCTAssertEqual(dist, 5.0)
    }

    func testRasterizerLifecycle() throws {
        let rasterizer = Rasterizer(width: 64, height: 48)
        XCTAssertEqual(rasterizer.width, 64)
        XCTAssertEqual(rasterizer.height, 48)

        rasterizer.reset(width: 32, height: 16)
        XCTAssertEqual(rasterizer.width, 32)
        XCTAssertEqual(rasterizer.height, 16)

        rasterizer.clear()
        XCTAssertEqual(rasterizer.width, 32)
        XCTAssertEqual(rasterizer.height, 16)
    }
}
#endif
