import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Buefy from 'buefy'
import Vuex from 'vuex'

// Import the Auth0 configuration
import { domain, clientId } from '../auth.config.js';

console.log(domain)
console.log(clientId)

// Import the plugin here
import { Auth0Plugin } from './auth';

import 'buefy/dist/buefy.css'
import store from "./store";

Vue.use(Vuex)
Vue.use(Buefy)

// Install the authentication plugin here
Vue.use(Auth0Plugin, {
  domain,
  clientId,
  onRedirectCallback: appState => {
    router.push(
        appState && appState.targetUrl
            ? appState.targetUrl
            : window.location.pathname
    );
  }
});

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
