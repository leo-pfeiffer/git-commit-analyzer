export default class LogHandler {
    // KEY FUNCTIONS ==============
    static keyMap = {
        Commit: {func: LogHandler.kfHash, transform: (hash) => hash.substr(0, 4)},
        AuthorName: {func: LogHandler.kfAuthorName, transform: (_) => _},
        AuthorMail: {func: LogHandler.kfAuthorMail, transform: (_) => _},
        Date: {func: LogHandler.kfDate, transform: (date) => new Date(date)},
        WeekDay: {func: LogHandler.kfWeekDay, transform: (_) => _},
        DayOfMonth: {func: LogHandler.kfDayOfMonth, transform: (_) => _},
        Year: {func: LogHandler.kfYear, transform: (_) => _},
        Month: {func: LogHandler.kfMonth, transform: (_) => _},
        Hour: {func: LogHandler.kfHourOfDay, transform: (_) => _},
    }
    // VALUE FUNCTIONS ==============
    static valMap = {
        NumCommits: LogHandler.vfNumCommits,
        Additions: LogHandler.vfNumAdditions,
        Deletions: LogHandler.vfNumDeletions,
    }

    constructor(gitlog) {
        this.data = [...gitlog.log]
    }

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
        const month = LogHandler.kfMonth(obj)
        const date = LogHandler.kfDayOfMonth(obj)
        return new Date(year, month, date)
    }

    static kfWeekDay(obj) {
        return obj.timestamp.getDay()
    }

    static kfDayOfMonth(obj) {
        return obj.timestamp.getDate()
    }

    static kfYear(obj) {
        return obj.timestamp.getFullYear()
    }

    static kfMonth(obj) {
        return obj.timestamp.getMonth()
    }

    static kfHourOfDay(obj) {
        return obj.timestamp.getHours()
    }

    static vfNumCommits(array) {
        return array.length
    }

    static vfNumAdditions(array) {
        return LogHandler._numChanges(array, "additions")
    }

    static vfNumDeletions(array) {
        return LogHandler._numChanges(array, "deletions")
    }

    static _numChanges(array, key) {
        return array.reduce((agg, next) => {
            return next.nodes.reduce((agg, next) => next[key] + agg, 0) + agg
        }, 0)
    }

    /**
     * Group by a key function.
     * @param keyFunc: function to get the key per commit
     * */
    groupBy(keyFunc) {
        return this.data.reduce((agg, next) => {
            const curKeyValue = keyFunc(next)
            curKeyValue in agg ? agg[curKeyValue].push(next) : agg[curKeyValue] = [next]
            return agg
        }, {})
    }

    /**
     * Aggregator for top level keys of the Gitlog object.
     * @param keyFunc: function to get the key per commit
     * @param valueFunc: function to aggregate by
     * */
    aggregateBy(keyFunc, valueFunc) {
        const grouped = this.groupBy(keyFunc)
        Object.keys(grouped).forEach((k) => {
            grouped[k] = {
                value: valueFunc(grouped[k]),
            }
        })
        return grouped
    }

    groupAggregateBy(groupFunc, keyFunc, valueFunc) {
        const grouped = this.data.reduce((agg, next) => {
            const curKey = [keyFunc(next), groupFunc(next)]
            curKey in agg ? agg[curKey].push(next) : agg[curKey] = [next]
            return agg
        }, {})
        Object.keys(grouped).forEach((k) => {
            grouped[k] = {
                key: keyFunc(grouped[k][0]),
                group: groupFunc(grouped[k][0]),
                value: valueFunc(grouped[k])
            }
        })
        return grouped
    }
}

