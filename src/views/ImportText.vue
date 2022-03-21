<template>
  <div class="home">
    <section>
      <div class="container center-screen import-container">
        <div class="card import-card">
          <div class="card-header">
            <div class="card-header-title">Import with Text</div>
          </div>
          <div class="card-content">
            <div class="is-center">
              <span>Run </span><br>
              <CopySpan :text="'git log --numstat --date=iso-strict > gitlog.txt'" :class="['is-family-code']"/><br>
              <span> and paste the content of the file.</span>
            </div>
            <hr>
            <textarea class="textarea is-family-code" placeholder="commit 2aae6c35 ..."
                      v-model="inputText" @input="setInputValidFalse"
                      v-bind:class="{'is-success': inputValid, 'is-danger': showWarning}"></textarea>

            <div id="warning-box" v-if="showWarning">
              <hr>
              <div class="warning-msg">
                {{ warningMsg }}
              </div>
            </div>
          </div>
          <div class="card-footer">
            <div class="card-footer-item" @click="verifyInput">
              <Loader height="20px" width="20px" thickness="3px" color="#7957d5" v-if="loadingVerify"/>
              <span v-if="!loadingVerify">Verify</span>
            </div>
            <div class="card-footer-item is-loading" v-bind:class="{'is-disabled': !inputValid}" @click="routeToDashboard">
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

import CopySpan from "@/components/CopySpan";
import Loader from "@/components/Loader";
import { mapState } from "vuex";
import TxtParser from "@/gitlog/txt-parser";
export default {
  name: 'ImportText',
  components: {CopySpan, Loader},
  data() {
    return {
      inputValid: false,
      inputText: "",
      warningMsg: "",
      showWarning: false,
      loadingDashboard: false,
      loadingVerify: false,
    }
  },
  computed: {
    ...mapState({
      gitlog: (state) => state.gitlog
    })
  },
  methods: {
    verifyInput: function() {
      if (this.inputValid === "") {
        this.warningMsg = "Please paste your git log first."
        this.showWarning = true;
      } else {
        this.loadingVerify = true;
        try {
          const gitlog = TxtParser.parse(this.inputText);
          console.log(gitlog)
          this.setInputValidTrue()
          this.$store.commit("setGitlog", gitlog);
        } catch (e) {
          console.error(e)
          this.warningMsg = "" +
              "We couldn't parse your input. Please check if you pasted the git log correctly and " +
              "without modification."
          this.showWarning = true;
        } finally {
          this.loadingVerify = false;
        }
      }
    },
    setInputValidFalse: function()
    {
      this.inputValid = false
      this.showWarning = false
    },
    setInputValidTrue: function() {
      this.inputValid = true
      this.showWarning = false
    },
    routeToDashboard: function() {
      if (this.inputValid) {
        this.loadingDashboard = true
        this.$router.push({name: 'Dashboard'})
      }
    }
  }
}

</script>