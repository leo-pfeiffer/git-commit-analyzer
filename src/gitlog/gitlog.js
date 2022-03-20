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
}