import TxtParser from "@/gitlog/txt-parser";
const fs = require("fs")

describe("Txt Parser", () => {

    it("'matchHash' should get numbers", () => {
        const hash = "1234567890123456789012345678901234567890"
        const result = TxtParser.matchHash("notAHash " + hash)
        expect(result).toBe(hash)
    })

    it("'matchHash' should get mix of letters A-F and numbers", () => {
        const hash = "abcdefabcdabcdefabcd12345678901234567890"
        const result = TxtParser.matchHash("notAHash " + hash)
        expect(result).toBe(hash)
    })

    it("'matchHash' should not get letters other than A-F", () => {
        const hash = "asdfghjklmasdfghjklmasdfghjklmasdfghjklm"
        expect(() => TxtParser.matchHash("notAHash " + hash))
            .toThrowError("Error parsing")
    })

    it("'matchHash' should not get space", () => {
        const hash = "abcdefabcdabcdefab dabcdefabcdabcdefabcd"
        expect(() => TxtParser.matchHash("notAHash " + hash))
            .toThrowError("Error parsing")
    })

    it("'matchHash' should work with branch info", () => {
        const hash = "30eb60e4d65c29b6459fc84c32613180a828cb79"
        const result = TxtParser.matchHash("commit " + hash + " (HEAD -> master)")
        expect(result).toBe(hash)
    })

    it("'matchAuthorName' should get name", () => {
        const name = "some-name"
        const result = TxtParser.matchAuthorName("Author: " + name + " <foo")
        expect(result).toBe(name)
    })

    it("'matchAuthorName' should not get name if 'Author' missing", () => {
        const name = "some-name"
        expect(() => TxtParser.matchAuthorName(name + " <foo"))
            .toThrowError("Error parsing")
    })

    it("'matchAuthorName' should not get name if '<' missing", () => {
        const name = "some-name"
        expect(() => TxtParser.matchAuthorName("Author: " + name + " foo"))
            .toThrowError("Error parsing")
    })

    it("'matchAuthorMail' should get mail", () => {
        const mail = "me@mail.com"
        const result = TxtParser.matchAuthorMail("Author: foo <" + mail + ">")
        expect(result).toBe(mail)
    })

    it("'matchAuthorMail' should not get mail if '<' is missing", () => {
        const mail = "me@mail.com"
        expect(() => TxtParser.matchAuthorMail("Author: foo " + mail + ">"))
            .toThrowError("Error parsing")
    })

    it("'matchAuthorMail' should not get mail if '>' is missing", () => {
        const mail = "me@mail.com"
        expect(() => TxtParser.matchAuthorMail("Author: foo <" + mail + " foo"))
            .toThrowError("Error parsing")
    })

    it("'matchNodeAddition' should get 0 with tab", () => {
        const result = TxtParser.matchNodeAddition("0\t0\ttest.txt")
        expect(result).toBe(0);
    })

    it("'matchNodeAddition' should get single non-zero digit with tab", () => {
        const result = TxtParser.matchNodeAddition("1\t0\ttest.txt")
        expect(result).toBe(1)
    })

    it("'matchNodeAddition' should get double digit with tab", () => {
        const result = TxtParser.matchNodeAddition("11\t0\ttest.txt")
        expect(result).toBe(11)
    })

    it("'matchNodeAddition' should get triple digit with tab", () => {
        const result = TxtParser.matchNodeAddition("111\t0\ttest.txt")
        expect(result).toBe(111)
    })

    it("'matchNOdeAddition' should get single digit", () => {
        const node = "1       2       nothing.sh"
        const result = TxtParser.matchNodeAddition(node)
        expect(result).toBe(1)
    })

    it("'matchNOdeAddition' should get double digit", () => {
        const node = "12      2       nothing.sh"
        const result = TxtParser.matchNodeAddition(node)
        expect(result).toBe(12)
    })

    it("'matchNOdeAddition' should get hyphen as 0", () => {
        const node = "-       2       nothing.sh"
        const result = TxtParser.matchNodeAddition(node)
        expect(result).toBe(0)
    })

    it("'matchNodeDeletion' should get single digit", () => {
        const node = "1       2       nothing.sh"
        const result = TxtParser.matchNodeDeletion(node)
        expect(result).toBe(2)
    })

    it("'matchNodeDeletion' should get double digit", () => {
        const node = "1      12       nothing.sh"
        const result = TxtParser.matchNodeDeletion(node)
        expect(result).toBe(12)
    })

    it("'matchNodeDeletion' should get double digit after double digit", () => {
        const node = "13     12       nothing.sh"
        const result = TxtParser.matchNodeDeletion(node)
        expect(result).toBe(12)
    })

    it("'matchNodeDeletion' should get hyphen as 0", () => {
        const node = "1       -       nothing.sh"
        const result = TxtParser.matchNodeDeletion(node)
        expect(result).toBe(0)
    })

    it("'matchNodeDeletion' should get hyphen as 0 after hyphen", () => {
        const node = "-       -       nothing.sh"
        const result = TxtParser.matchNodeDeletion(node)
        expect(result).toBe(0)
    })
    it("'matchNodeText' should get after two single digits", () => {
        const node = "1       2       nothing.sh"
        const result = TxtParser.matchNodeText(node)
        expect(result).toEqual("nothing.sh")
    })

    it("'matchNodeText' should get after single digit, double digit", () => {
        const node = "1      12       nothing.sh"
        const result = TxtParser.matchNodeText(node)
        expect(result).toEqual("nothing.sh")
    })

    it("'matchNodeText' should get after double digit, single digit", () => {
        const node = "12     1       nothing.sh"
        const result = TxtParser.matchNodeText(node)
        expect(result).toEqual("nothing.sh")
    })

    it("'matchNodeText' should get after two double digits", () => {
        const node = "13     12       nothing.sh"
        const result = TxtParser.matchNodeText(node)
        expect(result).toEqual("nothing.sh")
    })

    it("'matchNodeText' should get after single digit and hyphen", () => {
        const node = "1       -       nothing.sh"
        const result = TxtParser.matchNodeText(node)
        expect(result).toEqual("nothing.sh")
    })

    it("'matchNodeText' should get after hyphen and single digit", () => {
        const node = "-       1       nothing.sh"
        const result = TxtParser.matchNodeText(node)
        expect(result).toEqual("nothing.sh")
    })

    it("'matchNodeText' should get after double hyphen", () => {
        const node = "-       -       nothing.sh"
        const result = TxtParser.matchNodeText(node)
        expect(result).toEqual("nothing.sh")
    })

    it("'matchTimeStamp' should get after Date:", () => {
        const date = "2022-03-08T08:11:29+00:00"
        const node = "foo bar\nAuthor: foo bar\nDate:   " + date + "\n\n"
        const result = TxtParser.matchTimestamp(node)
        expect(result).toEqual(date)
    })

    it("'matchTimeStamp' should not get without Date:", () => {
        const date = "2022-03-08T08:11:29+00:00"
        const node = "foo bar\nAuthor: foo bar\nFoo Bar:   " + date + "\n\n"
        expect(() => TxtParser.matchTimestamp(node)).toThrowError("Error parsing")
    })

    it("'matchHeader' should read short header", () => {
        const longCommit = "commit 83d6daa0c8828e6730ddb0797a4518588b9b7a99\n" +
            "Author: leo-pfeiffer <leopold.pfeiffer@icloud.com>\n" +
            "Date:   2022-03-06T21:44:05+00:00\n\n" +
            "    test: this is a test\n    \n" +
            "    And this is the body of the commit message\n\n" +
            "1\t1\thello.txt"

        const result = TxtParser.matchHeader(longCommit)
        expect(result).toEqual("test: this is a test")
    })


    it("'matchHeader' should read long header", () => {
        const longCommit = "commit 30eb60e4d65c29b6459fc84c32613180a828cb79 (HEAD -> master)\n" +
            "Author: leo-pfeiffer <leopold.pfeiffer@icloud.com>\n" +
            "Date:   2022-03-08T08:11:29+00:00\n\n    " +
            "This is a very very very long message Please scan " +
            "QR code for Test and Protect or ask a member of staff " +
            "for a form rector's cafe wholesome culture macbook pro nirvana\n\n" +
            "0       0       nkanslan.log\n"

        const result = TxtParser.matchHeader(longCommit)
        expect(result.length).toEqual(166)
    })

    it("'matchBody' should read one-part body", () => {
        const longCommit = "commit 92a435cf26bc71c8c31dca6c6fdfa63ca80dd4be (HEAD -> master)" +
            "\nAuthor: leo-pfeiffer <leopold.pfeiffer@icloud.com>\n" +
            "Date:   2022-03-08T10:45:17+00:00\n\n" +
            "    feat: an addition with a long commit body\n" +
            "    \n" +
            "    Lorem Ipsum is simply dummy text." +
            "\n\n" +
            "1       0       nkanslan.log"

        const expected = "Lorem Ipsum is simply dummy text."

        const result = TxtParser.matchBody(longCommit)
        expect(result).toBe(expected)
    })

    it("'matchBody' should read multi-part body", () => {
        const longCommit = "commit 92a435cf26bc71c8c31dca6c6fdfa63ca80dd4be (HEAD -> master)" +
            "\nAuthor: leo-pfeiffer <leopold.pfeiffer@icloud.com>\n" +
            "Date:   2022-03-08T10:45:17+00:00\n\n" +
            "    feat: a long body with two paragraphs\n    \n    " +
            "Foo bar 1.\n" +
            "    \n    " +
            "Foo bar 2." +
            "\n\n" +
            "1       0       nkanslan.log"

        const expected = "Foo bar 1.\n\nFoo bar 2."

        const result = TxtParser.matchBody(longCommit)
        expect(result).toBe(expected)
    })

    it("'matchNodes' should match single nodes", () => {
        const commit = "commit 5acf2d7aebcad417c5d73cc5dc41f66596786a98" +
            "\nAuthor: leo-pfeiffer <leopold.pfeiffer@icloud.com>" +
            "\nDate:   2022-03-07T07:24:29+00:00" +
            "\n\n    feat: new file" +
            "\n\n" +
            "2\t0\thello.txt"

        const result = TxtParser.makeNodes(commit)
        expect(result.length).toEqual(1)
    })

    it("'matchNodes' should match multiple nodes", () => {
        const commit = "commit 5acf2d7aebcad417c5d73cc5dc41f66596786a98" +
            "\nAuthor: leo-pfeiffer <leopold.pfeiffer@icloud.com>" +
            "\nDate:   2022-03-07T07:24:29+00:00" +
            "\n\n    feat: new file" +
            "\n\n" +
            "0\t0\tanotherfile.txt\n" +
            "2\t0\thello.txt"

        const result = TxtParser.makeNodes(commit)
        expect(result.length).toEqual(2)
    })

    it("'matchNodes' should match hyphen - num - node", () => {
        const commit = "commit 5acf2d7aebcad417c5d73cc5dc41f66596786a98" +
            "\nAuthor: leo-pfeiffer <leopold.pfeiffer@icloud.com>" +
            "\nDate:   2022-03-07T07:24:29+00:00" +
            "\n\n    feat: new file" +
            "\n\n" +
            "-\t0\thello.txt"

        const result = TxtParser.makeNodes(commit)
        expect(result.length).toEqual(1)
    })

    it("'matchNodes' should match num - hyphen - node", () => {
        const commit = "commit 5acf2d7aebcad417c5d73cc5dc41f66596786a98" +
            "\nAuthor: leo-pfeiffer <leopold.pfeiffer@icloud.com>" +
            "\nDate:   2022-03-07T07:24:29+00:00" +
            "\n\n    feat: new file" +
            "\n\n" +
            "1\t-\thello.txt"

        const result = TxtParser.makeNodes(commit)
        expect(result.length).toEqual(1)
    })

    it("'matchNodes' should match hyphen - hyphen - node", () => {
        const commit = "commit 5acf2d7aebcad417c5d73cc5dc41f66596786a98" +
            "\nAuthor: leo-pfeiffer <leopold.pfeiffer@icloud.com>" +
            "\nDate:   2022-03-07T07:24:29+00:00" +
            "\n\n    feat: new file" +
            "\n\n" +
            "-\t-\thello.txt"

        const result = TxtParser.makeNodes(commit)
        expect(result.length).toEqual(1)
    })

    it("'splitIntoCommits' should produce correct number of splits", () => {
        fs.readFile("tests/resources/test-gitlog.txt", "utf8", (err, data) => {
            const result = TxtParser.splitIntoCommits(data)
            expect(result.length).toEqual(13);
        })
    })
    
})