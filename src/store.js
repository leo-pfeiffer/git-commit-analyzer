import Vue from "vue";
import createPersistedState from "vuex-persistedstate";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        gitlog: {},
        authId: "",
        logType: ""
    },
    mutations: {
        setGitlog (state, newGitlog) {
            Object.assign(state.gitlog, newGitlog)
        },
        setAuthId (state, newAuthId) {
            state.authId = newAuthId
        },
        setLogTypeText (state) {
            state.logType = "TEXT"
        },
        setLogTypeGithub(state) {
            state.logType = "GITHUB"
        },
        setLogTypeNull (state) {
            state.logType = ""
        }
    },
    plugins: [createPersistedState()],
});