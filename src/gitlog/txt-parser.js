import {Node} from "@/gitlog/node";
import {Commit} from "@/gitlog/commit";
import GitLog from "@/gitlog/gitlog";

export default class TxtParser {

    static parse(rawTxt) {
        const rawSplit = TxtParser.splitIntoCommits(rawTxt)
        const commits = []

        for (let split of rawSplit) {
            commits.push(new Commit(
                TxtParser.matchHash(split),
                TxtParser.matchAuthorName(split),
                TxtParser.matchAuthorMail(split),
                new Date(TxtParser.matchTimestamp(split)),
                TxtParser.matchHeader(split) + TxtParser.matchBody(split),
                TxtParser.makeNodes(split)
            ))
        }

        return new GitLog(commits)
    }

    static splitIntoCommits(rawTxt) {
        const commitStart = /(?=^commit\s[A-Fa-f0-9]{40}\n)/gm
        return rawTxt.split(commitStart)
    }

    static matchHash(string) {
        return TxtParser.matchPattern(string, /([A-Fa-f0-9]{40})/g)
    }

    static matchAuthorName(string) {
        return TxtParser.matchPattern(string, /(?<=Author:\s)[\S].*(?=\s<)/g)
    }

    static matchAuthorMail(string) {
        return TxtParser.matchPattern(string, /(?<=Author:\s.*<)[\S].*(?=>)/g)
    }

    static matchTimestamp(string) {
        return TxtParser.matchPattern(string, /(?<=Date:\s+)[\d{4}][\S].*/g)
    }

    static matchHeader(string) {
        return TxtParser.matchPattern(string, /(?<=\n\s{4})[\S].*/g)
    }

    static matchBody(string) {
        const match = [];
        let array;
        const pattern = /(?<=\n\s{4})([\S].*)/g;

        while ((array = pattern.exec(string)) !== null) {
            match.push(array[0])
        }

        return match.join("\n\n")
    }

    static makeNodes(string) {
        const match = [];
        let array;
        const pattern = /\n?[-\d]+[\s\t]+[-\d]+[\s\t]+([\S].+)/g;

        while ((array = pattern.exec(string)) !== null) {
            match.push(array[0])
        }

        return match.map(TxtParser.makeNodeHelper)
    }

    static makeNodeHelper(rawNode) {
        const add = TxtParser.matchNodeAddition(rawNode)
        const del = TxtParser.matchNodeDeletion(rawNode)
        const txt = TxtParser.matchNodeText(rawNode)
        return new Node(add, del, txt)
    }

    static matchNodeAddition(string) {
        const num = TxtParser.matchGroup(string, /\n?(\d+)([\s\t]+)([-\d]+)([\s\t]+)([\S].+)/g, 1)
        const hyp = TxtParser.matchGroup(string, /\n?(-)([\s\t]+)([-\d]+)([\s\t]+)([\S].+)/g, 1)
        return hyp === null ? parseInt(num) : 0
    }

    static matchNodeDeletion(string) {
        const num = TxtParser.matchGroup(string, /\n?([-\d]+)([\s\t]+)(\d+)([\s\t]+)([\S].+)/g, 3)
        const hyp = TxtParser.matchGroup(string, /\n?([-\d]+)([\s\t]+)(-)([\s\t]+)([\S].+)/g, 3)
        return hyp === null ? parseInt(num) : 0
    }

    static matchNodeText(string) {
        return TxtParser.matchGroup(string, /\n?[-\d]+[\s\t]+[-\d]+[\s\t]+([\S].+)/g, 1)
    }


    static matchPattern(string, pattern) {
        const match = pattern.exec(string)
        return match === null ? null : match[0]
    }

    static matchGroup(string, pattern, group) {
        let match = pattern.exec(string)
        if (match !== null) {
            return match[group]
        } else {
            return null
        }
    }
}

