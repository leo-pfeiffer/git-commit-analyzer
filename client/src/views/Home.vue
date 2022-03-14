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
                <button class="button is-dark" @click="continueWithGithub">Continue with GitHub</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>

import {getRepos, githubAuth} from "@/assets/api";

export default {
  name: 'Home',
  components: {},
  methods: {
    continueWithGithub: function() {
      // call OAuth, if successful, reroute
      githubAuth()
          .then(getRepos)
          .then((repos) => console.log(repos))
          .then(() => this.$router.push({name: 'ImportGithub'}))
    },
    continueWithText: function() {
      this.$router.push({name: 'ImportText'})
    }
  }
}

</script>

