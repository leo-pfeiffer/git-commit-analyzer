const axios = require("axios")

const FILE = "https://gist.githubusercontent.com/leo-pfeiffer/fdafd6edbe5c8887850eb87089563dc0/raw/211547723b4621a622fc56978d74aa416cbd1729/Programming_Languages_Extensions.json";

const getExtensionFile = function(url) {
    return axios.get(url).then(res => res.data)
}

const getLanguageForExtension = function (data, extension) {
    return data.find(e => {
        if ("extensions" in e) {
            return e["extensions"].indexOf(extension) !== -1
        }
        return false
    })["name"]
}

// const proc = async function() {
//     const data = await getExtensionFile(FILE)
//     const lang = await getLanguageForExtension(data, ".js")
//     console.log(lang)
// }

module.exports = {
    getExtensionFile: () => getExtensionFile(FILE),
    getLanguageForExtension: getLanguageForExtension,
}
