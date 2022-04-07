<template>
  <div class="home">
    <section>
      <div class="container center-screen import-container">
        <div class="card import-card">
          <div class="card-header">
            <div class="card-header-title">Import from Github</div>
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
              <span v-if="!loadingDashboard" v-bind:class="{'': selectionValid}">Go to Dashboard</span>
            </div>
          </div>

        </div>
      </div>
    </section>
  </div>
</template>

<script>
import Loader from "@/components/Loader";
import {mapState} from "vuex";
import {getRepos, getCommits, parseLog} from "@/api/github";


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
    this.repositories = await getRepos(this.authId)
        .then((repos) => {
          this.loadingRepos = false;
          return repos
        })
        .catch(err => {
          console.error(err);
          this.backToHome("ERROR!");
    })
  },
  methods: {
    verifySelection: async function() {
      if (this.selectedValue === "error") {
        this.warningMsg = "" +
            "We couldn't load your repository. Please reload the page and try again."
        this.showWarning = true;
      } else {
        this.loadingVerify = true;
        await getCommits(this.authId, this.selectedValue)
            .then(parseLog)
            .then((gitlog) => {
              this.loadingVerify = true;
              this.setSelectionValidTrue()
              this.$store.commit("setGitlog", gitlog);
              this.$store.commit("setLogTypeGithub");
              this.loadingVerify = false;
            }).catch(err => {
              console.error(err);
              this.loadingVerify = false;
            })
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
    backToHome(msg) {
      this.$store.commit("setAuthId", "");
      this.$router.push({name: 'Home'})
      console.log(msg)
      // todo emit msg
    }
  }
}

</script>

