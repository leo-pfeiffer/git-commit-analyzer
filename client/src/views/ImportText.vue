<template>
  <div class="home">
    <section>
      <div class="container center-screen">
        <div class="card">
          <div class="card-header">
            <div class="card-header-title">Import with Text</div>
          </div>
          <div class="card-content">
            <div class="is-center">
              <span>Run </span><br>
              <span class="is-family-code">git log --numstat --date=iso-strict > gitlog.txt</span><br>
              <span> and paste the content of the file.</span>
            </div>
            <hr>
            <textarea class="textarea" placeholder="commit 2aae6c35 ..."
                      v-model="inputText" @input="setInputValidFalse"></textarea>

            <div id="warning-box" v-if="showWarning">
              <hr>
              <div id="warning-msg">
                {{ warningMsg }}
              </div>
            </div>
          </div>
          <div class="card-footer">
            <div class="card-footer-item" @click="verifyInput">
              Verify
            </div>
            <div class="card-footer-item" v-bind:class="{'is-disabled': !inputValid}" @click="() => this.$router.push({name: 'Dashboard'})">
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
  name: 'ImportText',
  components: {},
  data() {
    return {
      inputValid: false,
      inputText: "",
      warningMsg: "",
      showWarning: false,
    }
  },
  methods: {
    verifyInput: function() {
      // todo do verification
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
    }

  }
}

</script>

<style scoped>

#warning-msg {
  color: red;
}

</style>