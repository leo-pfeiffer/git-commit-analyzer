<template>
  <div class="home">
    <section>
      <div class="container center-screen">
        <div class="card">
          <div class="card-content">
            <div class="columns">
              <div class="column">
                <button class="button is-primary" @click="continueWithText">Continue with Text</button>
              </div>
              <div class="column">

                <button class="button is-dark" @click="authenticateWithGithub" v-if="!authenticated">
                  <Loader v-if="oAuthWindowOpen" height="20px" width="20px" thickness="3px" color="#7957d5"/>
                  <span v-if="!oAuthWindowOpen">Login with GitHub</span>
                </button>

                <button class="button is-dark" @click="continueWithGithub" v-if="authenticated">
                  <span v-if="authenticated">Continue</span>
                </button>
              </div>

              <!-- Check that the SDK client is not currently loading before accessing is methods -->
              <div v-if="!$auth.loading" class="column">
                <!-- show login when not authenticated -->
                <button class="button is-success" v-if="!$auth.isAuthenticated" @click="login">Log in</button>
                <!-- show logout when authenticated -->
                <button class="button is-success" v-if="$auth.isAuthenticated" @click="logout">Log out</button>
              </div>

            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>

import { githubAuth} from "@/assets/api";
import Loader from "@/components/Loader";

export default {
  name: 'Home',
  components: {Loader},
  data() {
    return {
      oAuthWindowOpen: false,
      authenticated: false
    }
  },
  methods: {
    authenticateWithGithub: function() {
      const oAuthWindow = githubAuth()

      this.oAuthWindowOpen = true

      let intervalId = setInterval(() => {
        if (oAuthWindow.closed) {

          // todo should check if actually authenticated!
          //  create separate route on server to check auth
          this.authenticated = true
          clearInterval(intervalId)
        }
      }, 500)
    },
    continueWithGithub: function() {
      this.$router.push({name: 'ImportGithub'})
    },
    continueWithText: function() {
      this.$router.push({name: 'ImportText'})
    },
    // Log the user in
    login() {
      this.$auth.loginWithRedirect();
    },
    // Log the user out
    logout() {
      this.$auth.logout({
        returnTo: window.location.origin
      });
    }
  }
}

</script>

