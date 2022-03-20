import Vue from "vue";

import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        gitlog: {},
        authId: "",
    },
    mutations: {
        setGitlog (state, newGitlog) {
            Object.assign(state.gitlog, newGitlog)
        },
        setAuthId (state, newAuthId) {
            state.authId = newAuthId
        }
    }
});