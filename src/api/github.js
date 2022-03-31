import { Commit } from "@/gitlog/commit"
import Pizzly from "pizzly-js";
import parse from "parse-link-header";
import GitLog from "@/gitlog/gitlog";

const HOST = process.env.VUE_APP_PIZZLY_HOST
const SECRET = process.env.VUE_APP_PIZZLY_SECRET_KEY

const pizzly = new Pizzly({host: HOST, publishableKey: SECRET})
const githubApi = pizzly.integration('github')

const parseLog = function(array) {
    const commits = [];
    for (let e of array) {
        commits.push(
            new Commit(
                e["sha"],
                e["commit"]["author"]["name"],
                e["commit"]["author"]["email"],
                new Date(e["commit"]["author"]["date"]),
                e["commit"]["message"],
                []
            )
        )
    }
    return new GitLog(commits)
}

const authenticate = function() {
    return githubApi.connect()
}

const getRepos = async function(authId) {
    const perPage = 30
    const lastPage = await githubApi
        .auth(authId)
        .head('/user/repos', {
            headers: {"Content-Type": "application/vnd.github.v3+json"},
            query: {"page": 1, "per_page": perPage}
        })
        .then(res => {
            if (res.headers.has("link")) return parse(res.headers.get("link")).last.page
            else return 1
        })

    const responses = []
    for (let i = 1; i <= lastPage; i++) {
        responses.push(_getRepoPage(i, perPage, authId))
    }

    return Promise.all(responses).then(values => values.flat()).then(arr => [...new Set(arr)]);
}

const _getRepoPage = function(page, perPage, authId) {
    return githubApi
        .auth(authId)
        .get('/user/repos', {
            headers: {"Content-Type": "application/vnd.github.v3+json"},
            query: {"page": page, "per_page": perPage, "visibility": "all"}
        })
        .then(res => res.json())
        .then(jsn => jsn.map(e => e.name))
}

const getCommits = async function(authId, repo) {

    const username = await getUserName(authId)
    const perPage = 30
    const url = `/repos/${username}/${repo}/commits`
    const lastPage = await githubApi
        .auth(authId)
        .head(url, {
            headers: {"Content-Type": "application/vnd.github.v3+json"},
            query: {"page": 1, "per_page": perPage}
        })
        .then(res => {
            if (res.headers.has("link")) return parse(res.headers.get("link")).last.page
            else return 1
        })

    const responses = []
    for (let i = 1; i <= lastPage; i++) {
        responses.push(_getCommitPage(url, i, perPage, authId))
    }

    return Promise.all(responses).then(values => values.flat()).then(arr => [...new Set(arr)]);
}

const _getCommitPage = function (url, page, perPage, authId) {
    return githubApi
        .auth(authId)
        .get(url, {
            headers: {"Content-Type": "application/vnd.github.v3+json"},
            query: {"page": page, "per_page": perPage, "visibility": "all"}
        })
        .then(res => res.json())
}

const getUserName = function(authId) {
    return githubApi
        .auth(authId)
        .get('/user', {
            headers: {"Content-Type": "application/vnd.github.v3+json"}
        })
        .then(res => res.json())
        .then(jsn => jsn.login)
}

export {
    parseLog,
    authenticate,
    getRepos,
    getCommits
}
