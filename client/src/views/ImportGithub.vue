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
              Verify
            </div>
            <div class="card-footer-item" v-bind:class="{'is-disabled': !selectionValid}" @click="routeToDashboard">
              Go to Dashboard
            </div>
          </div>

        </div>
      </div>
    </section>
  </div>
</template>

<script>

export default {
  name: 'ImportGithub',
  components: {},
  data() {
    return {
      repositories: ["repo1", "error", "repo3", "repo4", "repo5", "repo6", "repo7"],
      selectionValid: false,
      selectedValue: "",
      warningMsg: "",
      showWarning: false,
    }
  },
  methods: {
    verifySelection: function() {
      // todo do verification
      if (this.selectedValue === "error") {
        this.warningMsg = "" +
            "We couldn't load your repository. Please reload the page and try again."
        this.showWarning = true;
      } else {
        this.setSelectionValidTrue()
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
        this.$router.push({name: 'Dashboard'})
      }
    }
  }
}

</script>

