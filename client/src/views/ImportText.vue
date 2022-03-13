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
              Verify
            </div>
            <div class="card-footer-item" v-bind:class="{'is-disabled': !inputValid}" @click="routeToDashboard">
              Go to Dashboard
            </div>
          </div>

        </div>
      </div>
    </section>
  </div>
</template>

<script>

import CopySpan from "@/components/CopySpan";
import {postTextLog} from "@/api/api";
export default {
  name: 'ImportText',
  components: {CopySpan},
  data() {
    return {
      inputValid: false,
      inputText: "",
      warningMsg: "",
      showWarning: false,
    }
  },
  methods: {
    verifyInput: async function() {
      // todo do verification
      const gitlog = await postTextLog(this.inputText);
      console.log(gitlog)
      if (this.inputText === "error") {
        this.warningMsg = "" +
            "We couldn't parse your input. Please check if you pasted the git log correctly and " +
            "without modification."
        this.showWarning = true;
      } else {
        this.setInputValidTrue()
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
        this.$router.push({name: 'Dashboard'})
      }
    }
  }
}

</script>