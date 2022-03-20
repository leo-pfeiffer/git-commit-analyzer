// import {data} from "@/dashboard/test-log"
// import {parseLog} from "@/api/github";
// const {data} = require("./test-log.js")

export default class LogHandler {
    constructor(gitlog) {
        this.data = [...gitlog.log]
    }

    /**
     * Aggregator for top level keys of the Gitlog object.
     * @param keyFunc: function to get the key per commit
     * @param aggFunc: function to aggregate by
     * */
    aggregateBy (keyFunc, aggFunc) {
        const grouped = this.data.reduce((agg, next) => {
            const curKeyValue = keyFunc(next)
            curKeyValue in agg ? agg[curKeyValue].push(next) : agg[curKeyValue] = [next]
            return agg
        }, {})
        Object.keys(grouped).forEach((k) => {
            grouped[k] = aggFunc(grouped[k])
        })
        return grouped
    }

    // KEY FUNCTIONS ==============
    static kfHash(obj) {
        return obj.hash
    }

    static kfAuthorName(obj) {
        return obj.authorName
    }

    static kfAuthorMail(obj) {
        return obj.authorMail
    }

    static kfDate(obj) {
        const year = LogHandler.kfYear(obj)
        const month = LogHandler.kfMonthOfYear(obj)
        const date = LogHandler.kfDayOfMonth(obj)
        return new Date(year, month, date)
    }

    static kfDayOfWeek(obj) {
        return obj.timestamp.getDay()
    }

    static kfDayOfMonth(obj) {
        return obj.timestamp.getDate()
    }

    static kfYear(obj) {
        return obj.timestamp.getFullYear()
    }

    static kfMonthOfYear(obj) {
        return obj.timestamp.getMonth()
    }

    static kfHourOfDay(obj) {
        return obj.timestamp.getHours()
    }

    // AGGREGATOR FUNCTIONS ==============
    static afNumCommits(array) {
        return array.length
    }

    static afNumAdditions(array) {
        return LogHandler._numChanges(array, "additions")
    }

    static afNumDeletions(array) {
        return LogHandler._numChanges(array, "deletions")
    }

    static _numChanges(array, key) {
        return array.reduce((agg, next) => {
            return next.nodes.reduce((agg, next) => next[key] + agg, 0) + agg
        }, 0)
    }
}

// const test = function() {
//    const logHandler = new LogHandler(data);
//    console.log(logHandler.aggregateBy(LogHandler.kfDate, LogHandler.afNumCommits))
//    console.log(logHandler.aggregateBy(LogHandler.kfHourOfDay, LogHandler.afNumCommits))
//    console.log(logHandler.aggregateBy(LogHandler.kfDayOfWeek, LogHandler.afNumCommits))
// }
//
// test()
