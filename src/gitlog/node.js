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
    constructor(additions, deletions, path) {
        this._additions = additions;
        this._deletions = deletions;
        this._path = path;
    }
}
