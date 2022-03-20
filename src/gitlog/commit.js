export class Commit {
    get authorName() {
        return this._authorName;
    }

    get authorMail() {
        return this._authorMail;
    }

    get timestamp() {
        return this._timestamp;
    }

    get message() {
        return this._message;
    }

    get nodes() {
        return this._nodes;
    }
    get hash() {
        return this._hash;
    }
    constructor(hash, authorName, authorMail, timestamp, message, nodes) {
        this._hash = hash;
        this._authorName = authorName;
        this._authorMail = authorMail;
        this._timestamp = timestamp;
        this._message = message;
        this._nodes = nodes;
    }
}