<template>
  <div class="home">
    <section>
      <div class="container center-screen import-container">
        <div class="card import-card">
          <div class="card-header">
            <div class="card-header-title">Import with Github</div>
          </div>
          <div class="card-content">
            <div class="is-centered">
              <span>Select the repository you want to analyze.</span><br>
            </div>
            <hr>
            <div class="columns is-centered is-mobile">
              <Loader height="40px" width="40px" thickness="5px" color="#7957d5" v-if="loadingRepos"/>
            </div>
            <div class="select is-full" v-bind:class="{'is-success': selectionValid, 'is-danger': showWarning}" v-if="!loadingRepos">
              <select v-model="selectedValue">
                <option v-for="item in repositories" :value="item" :key="item">{{item}}</option>
              </select>
            </div>
            <div id="warning-box" v-if="showWarning">
              <hr>
              <div class="warning-msg">
                {{ warningMsg }}
              </div>
            </div>
          </div>
          <div class="card-footer">
            <div class="card-footer-item" @click="verifySelection">
              <Loader height="20px" width="20px" thickness="3px" color="#7957d5" v-if="loadingVerify"/>
              <span v-if="!loadingVerify">Verify</span>
            </div>
            <div class="card-footer-item" v-bind:class="{'is-disabled': !selectionValid}" @click="routeToDashboard">
              <Loader height="20px" width="20px" thickness="3px" color="#7957d5" v-if="loadingDashboard"/>
              <span v-if="!loadingDashboard">Go to Dashboard</span>
            </div>
          </div>

        </div>
      </div>
    </section>
  </div>
</template>

<script>
import Loader from "@/components/Loader";
import {getCommits} from "@/assets/api";
import Pizzly from "pizzly-js";
import parse from 'parse-link-header';
import {mapState} from "vuex";

const pizzly = new Pizzly({ host: 'https://pizzly-ljp.herokuapp.com' })
const githubApi = pizzly.integration('github')

export default {
  name: 'ImportGithub',
  components: {Loader},
  data() {
    return {
      repositories: [],
      selectionValid: false,
      selectedValue: "",
      warningMsg: "",
      showWarning: false,
      loadingDashboard: false,
      loadingVerify: false,
      loadingRepos: true,
    }
  },
  computed: {
    ...mapState({
      authId: (state) => state.authId
    }),
  },
  async mounted() {
    if (this.authId === null || this.authId === undefined || this.authId === "") {
      this.backToHome("Please login.")
    }
    this.repositories = await this.getRepos()
    this.loadingRepos = false;
  },
  methods: {
    verifySelection: async function() {
      // todo do verification
      if (this.selectedValue === "error") {
        this.warningMsg = "" +
            "We couldn't load your repository. Please reload the page and try again."
        this.showWarning = true;
      } else {
        const commits = await getCommits(this.selectedValue)
        console.log(commits)
        this.loadingVerify = true;
        this.setSelectionValidTrue()
        this.loadingVerify = false;
      }
    },
    setSelectionValidFalse: function()
    {
      this.selectionValid = false
    },
    setSelectionValidTrue: function() {
      this.selectionValid = true
      this.showWarning = false
    },
    routeToDashboard: function() {
      if (this.selectionValid) {
        this.loadingDashboard = true
        this.$router.push({name: 'Dashboard'})
      }
    },
    getRepos: async function() {
      const perPage = 30
      const lastPage = await githubApi
          .auth(this.authId)
          .head('/user/repos', {
            headers: {"Content-Type": "application/vnd.github.v3+json"},
            query: {"page": 1, "per_page": perPage}
          })
          .then(res => parse(res.headers.get("link")).last.page)
          .catch(err => {
            console.error(err);
            this.$store.commit("setAuthId", "");
            this.$router.push({name: 'Home'})
            // todo show error message to user saying what happened
          })

      const responses = []
      for (let i = 1; i <= lastPage; i++) {
        responses.push(this._getRepoPage(i, perPage))
      }

      return Promise.all(responses).then(values => values.flat()).then(arr => [...new Set(arr)]);
    },
    _getRepoPage: function(page, perPage) {
      return githubApi
          .auth(this.authId)
          .get('/user/repos', {
            headers: {"Content-Type": "application/vnd.github.v3+json"},
            query: {"page": page, "per_page": perPage, "visibility": "all"}
          })
          .then(res => res.json())
          .then(jsn => jsn.map(e => e.name))
          .catch(err => {
            console.error(err);
            this.backToHome("Error message...")
            // todo show error message to user saying what happened
          })
    },
    backToHome(msg) {
      this.$store.commit("setAuthId", "");
      this.$router.push({name: 'Home'})
      console.log(msg)
      // todo emit msg
    }
  }
}

</script>

