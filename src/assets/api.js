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

module.exports = {
    postTextLog: postTextLog,
}