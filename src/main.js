import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Buefy from 'buefy'
import Vuex from 'vuex'

Vue.config.productionTip = false

import 'buefy/dist/buefy.css'
import store from "./store";

Vue.use(Vuex)
Vue.use(Buefy)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
