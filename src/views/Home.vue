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
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>

import Loader from "@/components/Loader";
import Pizzly from 'pizzly-js';
import { mapState } from "vuex";

const pizzly = new Pizzly({host: 'https://pizzly-ljp.herokuapp.com', })
const githubApi = pizzly.integration('github')

export default {
  name: 'Home',
  components: {Loader},
  data() {
    return {
      oAuthWindowOpen: false,
      authenticated: false,
    }
  },
  computed: {
    ...mapState({
      authId: (state) => state.authId
    }),
  },
  methods: {
    authenticateWithGithub: function() {
      githubApi
          .connect()
          .then(({ authId }) => {
            this.$store.commit("setAuthId", authId);
            this.authenticated = true;
            console.log('Successfully connected!', authId)
            this.$router.push({name: 'ImportGithub'})
          })
          .catch(console.error)
    },
    continueWithText: function() {
      this.$router.push({name: 'ImportText'})
    }
  }
}

</script>

