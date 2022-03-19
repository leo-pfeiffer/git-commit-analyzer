<template>
  <div class="home">
    <section>
      <div class="container center-screen import-container">
        <div class="card import-card">
          <div class="card-header">
            <div class="card-header-title">Import with Github</div>
          </div>
          <div class="card-content">
            <div class="is-center">
              <span>Select the repository you want to analyze.</span><br>
            </div>
            <hr>
            <div class="select is-full" v-bind:class="{'is-success': selectionValid, 'is-danger': showWarning}">
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
import {getCommits, getRepos} from "@/assets/api";
export default {
  name: 'ImportGithub',
  components: {Loader},
  data() {
    return {
      repositories: ["repo1", "error", "repo3", "repo4", "repo5", "repo6", "repo7"],
      selectionValid: false,
      selectedValue: "",
      warningMsg: "",
      showWarning: false,
      loadingDashboard: false,
      loadingVerify: false,
    }
  },
  async mounted() {
    this.repositories = await getRepos()
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
    }
  }
}

</script>

