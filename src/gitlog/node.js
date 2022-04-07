export class Node {
    get additions() {
        return this._additions;
    }

    get deletions() {
        return this._deletions;
    }

    get path() {
        return this._path;
    }

    get extension() {
        const ext = this._path.slice((this._path.lastIndexOf(".") - 1 >>> 0) + 2);
        return ext.length === 0 ? "" : "." + ext;
    }

    constructor(additions, deletions, path) {
        this._additions = additions;
        this._deletions = deletions;
        this._path = path;
    }
}
