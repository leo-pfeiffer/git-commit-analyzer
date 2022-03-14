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
    const url = `${BASE_URL}/gh-login`

    return fetch(url, {
        method: 'GET',
        //redirect: 'follow',
    }).catch(err => console.error(err))
}

// const getToken = function() {
//     const url = `${BASE_URL}/gh-token`
//
//     const headers = new Headers();
//     headers.append("Content-Type", "application/json");
//
//     return fetch(url, {
//         method: 'POST',
//         headers: headers
//     }).then(res => res.json())
//         .then(jsn => jsn["token"])
//         .catch(err => err)
// }

const getRepos = function(token) {
    const url = `${BASE_URL}/github/repos`

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
}