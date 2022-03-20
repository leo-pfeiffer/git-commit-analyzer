<template>
  <div class="home">
    <section>
      <div class="container center-screen">
        <div class="card">
          <div class="card-content is-center">
            <div class="columns">
              <div class="column">
                  <button class="panel-heading button is-primary" @click="continueWithText">
                    <span>Upload</span><span class="is-family-code">&nbsp;git log</span>
                  </button>
                <p class="panel-block" v-for="item of features" :key="item.text">
                  <span class="panel-icon">
                    <font-awesome-icon icon="check" v-if="item.text"/>
                    <font-awesome-icon icon="xmark" v-else/>
                  </span>
                  {{ item.name }}
                </p>
              </div>
              <hr class="is-mobile">
              <div class="column">
                <button class="panel-heading button is-dark" @click="authenticateWithGithub" v-if="!authenticated">
                  <Loader v-if="oAuthWindowOpen" height="20px" width="20px" thickness="3px" color="#7957d5"/>
                  <span v-if="!oAuthWindowOpen">Import from GitHub</span>
                </button>
                <p class="panel-block" v-for="item of features" :key="item.text">
                  <span class="panel-icon">
                    <font-awesome-icon icon="check" v-if="item.github"/>
                    <font-awesome-icon icon="xmark" v-else/>
                  </span>
                  {{ item.name }}
                </p>
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
import { mapState } from "vuex";
import {authenticate} from "@/api/github";
import { library } from '@fortawesome/fontawesome-svg-core'
import { faCheck, faXmark } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

library.add(faCheck, faXmark)

export default {
  name: 'Home',
  components: {Loader, FontAwesomeIcon},
  data() {
    return {
      oAuthWindowOpen: false,
      authenticated: false,
      features: [
        {name: "Graphs", text: true, github: true},
        {name: "Changes per commit", text: true, github: false},
        {name: "Github integration", text: false, github: true},
      ]
    }
  },
  computed: {
    ...mapState({
      authId: (state) => state.authId
    }),
  },
  methods: {
    authenticateWithGithub: function() {
      authenticate()
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

