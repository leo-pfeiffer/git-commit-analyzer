import {Commit} from "@/gitlog/commit";
import {Node} from "@/gitlog/node";

export default class GitLog {
    get log() {
        return this._log;
    }
    constructor(log) {
        this._log = log;
    }

    static fromArray(array) {
        return new GitLog(array)
    }

    static fromPlainObject(object) {
        const log = []
        for (let commit of object._log) {
            log.push(new Commit(
                commit._hash,
                commit._authorName,
                commit._authorMail,
                new Date(commit._timestamp),
                commit._message,
                commit._nodes.map(e => new Node(
                    e._additions,
                    e._deletions,
                    e._path
                )),
            ))
        }
        return new GitLog(log)

    }
}