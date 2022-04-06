import {Node} from "@/gitlog/node";

describe("Node", () => {
    it("should get extension for basic case", () => {
        const node = new Node(0, 0, "index.js")
        expect(node.extension).toBe(".js")
    })

    it("should get extension for name with space", () => {
        const node = new Node(0, 0, "in dex.js")
        expect(node.extension).toBe(".js")
    })

    it("should get extension for name with hyphen", () => {
        const node = new Node(0, 0, "in-dex.js")
        expect(node.extension).toBe(".js")
    })

    it("should get extension for name with '..'", () => {
        const node = new Node(0, 0, "index..js")
        expect(node.extension).toBe(".js")
    })

    it("should get extension for name with 'x.x.'", () => {
        const node = new Node(0, 0, "in.dex.js")
        expect(node.extension).toBe(".js")
    })

    it("should get extension for file with only extension", () => {
        const node = new Node(0, 0, ".gitignore")
        expect(node.extension).toBe("")
    })

    it("should get extension for file with no period", () => {
        const node = new Node(0, 0, "index")
        expect(node.extension).toBe("")
    })
})