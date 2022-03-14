const {getCookie} = require("@/assets/utils");
const BASE_URL = "http://localhost:9000"

const postTextLog = function(gitlog) {
    const url = `${BASE_URL}/gitlog`

    const headers = new Headers();
    headers.append("Content-Type", "text/plain");

    return fetch(url, {
        method: 'POST',
        headers: headers,
        body: gitlog
    }).then(res => res.json()).catch(err => console.error(err))
}

const githubAuth = function() {
    const url = `${BASE_URL}/github/login`

    const createOauthWindow = function(url, name) {
        if (url === null) return null
        const options = "toolbar=no, menubar=no, width=600, height=700, top=100, left=100"
        return window.open(url, name, options);
    }

    let loopCount = 1200;
    let intervalLength = 100;

    const windowHandle = createOauthWindow(url, "OAuth")
    windowHandle.focus();

    // close automatically after 120 seconds
    let intervalId = window.setInterval(() => {
        if (loopCount-- < 0) {
            window.clearInterval(intervalId);
            windowHandle.close();
        }
    }, intervalLength);

    return windowHandle

}

const getToken = function() {
    return getCookie("GtaGhAuthToken")
}

const getRepos = function() {
    const url = `${BASE_URL}/github/repos`

    const token = getToken();

    const headers = new Headers();
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", `token ${token}`);

    return fetch(url, {
        method: 'GET',
        headers: headers
    }).then(res => res.json())
        .then(jsn => jsn.map(e => e.name))
        .catch(err => err)
}

const getCommits = async function(repo) {
    // todo get GitLog instead
    const url = `${BASE_URL}/github/commits/${repo}`

    const token = getToken();

    const headers = new Headers();
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", `token ${token}`);

    return fetch(url, {
        method: 'GET',
        headers: headers
    }).then(res => res.json())
        .catch(err => err)
}

module.exports = {
    postTextLog: postTextLog,
    githubAuth: githubAuth,
    getRepos: getRepos,
    getCommits: getCommits
}