<template>
  <div class="home v-scroll columns is-mobile">
    <GraphController id="control-bar"
                     :key-opts="availableKeys"
                     :value-opts="availableValues"
                     @optionChange="updateOptions"
    />
    <section id="dashboard-section">
      <div id="tabs-with-content" class="container">
        <div class="tabs is-centered">
          <ul>
            <li><a @click="() => switchTabs('barChart')" v-bind:class="{'is-active': activeTabs['barChart']}">
              Bar chart
            </a></li>
            <li><a @click="() => switchTabs('pieChart')" v-bind:class="{'is-active': activeTabs['pieChart']}">
              Pie chart
            </a></li>
            <li><a @click="() => switchTabs('scatterChart')" v-bind:class="{'is-active': activeTabs['scatterChart']}">
              Scatter chart
            </a></li>
          </ul>
        </div>
        <div>
          <section class="tab-content" v-bind:class="{'is-active': activeTabs['barChart']}">
              <Plotly v-if="selected" :data="data1" :layout="layout" :display-mode-bar="false"></Plotly>
          </section>
          <section class="tab-content" v-bind:class="{'is-active': activeTabs['pieChart']}">
            <Plotly v-if="selected" :data="data2" :layout="layout" :display-mode-bar="false"></Plotly>
          </section>
          <section class="tab-content" v-bind:class="{'is-active': activeTabs['scatterChart']}">
            <Plotly v-if="selected" :data="data3" :layout="layout" :display-mode-bar="false"></Plotly>
          </section>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { Plotly } from 'vue-plotly';
import { mapState } from "vuex";
import LogHandler from "@/dashboard/processing";
import GitLog from "@/gitlog/gitlog";
import GraphController from "@/components/GraphController";

export default {
  name: 'Dashboard',
  components: {
    Plotly,
    GraphController
  },
  data() {
    return {
      activeTabs: {
        barChart: true,
        pieChart: false,
        scatterChart: false,
      },
      selectedOpts: {},
      selected: false
    }
  },
  computed: {
    ...mapState({
      gitlog: (state) => state.gitlog,
      logType: (state) => state.logType,
    }),
    availableKeys: function() {
      return Object.keys(LogHandler.keyMap)
    },
    availableValues: function() {
      if (this.logType === "TEXT") {
        return Object.keys(LogHandler.valMap)
      } else {
        // todo this is nasty
        return Object.keys(LogHandler.valMap).filter(e => (e !== 'Additions' && e !== 'Deletions'))
      }
    },
    selectedKeyFunc: function() {
      return this.selectedOpts.key.name
    },
    selectedValueFunc: function() {
      return this.selectedOpts.value.name
    },
    selectedGroupFunc: function() {
      return this.selectedOpts.color.name
    },
    gitlogInstance: function() {
      return GitLog.fromArray(this.gitlog._log)
    },
    data1: function() {
      const logHandler = new LogHandler(this.gitlogInstance)
      const keyFunc = LogHandler.keyMap[this.selectedOpts.key.name]
      const valFunc = LogHandler.valMap[this.selectedOpts.value.name]
      const data = logHandler.aggregateBy(keyFunc, valFunc)
      return [{
        x: Object.keys(data),
        y: Object.keys(data).map(e => data[e]).map(e => e.value),
        type: "bar"
      }]
    },
    data2: function() {
      const logHandler = new LogHandler(this.gitlogInstance)
      const keyFunc = LogHandler.keyMap[this.selectedOpts.key.name]
      const valFunc = LogHandler.valMap[this.selectedOpts.value.name]
      const data = logHandler.aggregateBy(keyFunc, valFunc)
      return [{
        labels: Object.keys(data),
        values: Object.keys(data).map(e => data[e]).map(e => e.value),
        type: "pie"
      }]
    },
    data3: function() {
      const logHandler = new LogHandler(this.gitlogInstance)
      const keyFunc = LogHandler.keyMap[this.selectedOpts.key.name]
      const valFunc = LogHandler.valMap[this.selectedOpts.value.name]
      const groupFunc = this.selectedOpts.color === null ? null : LogHandler.valMap[this.selectedOpts.color.name]
      const data = logHandler.aggregateBy(keyFunc, valFunc, groupFunc)
      return [{
        x: Object.keys(data),
        y: Object.keys(data).map(e => data[e]).map(e => e.value),
        type: "scatter",
        mode: 'markers',
        // transforms: [{
        //   type: 'groupby',
        //   groups: Object.keys(data).map(e => data[e]).map(e => e.group)
        // }]
      }]
    },
    layout: function() {
      return this.makeLayout()
    },
  },
  mounted() {
    if (this.gitlog === null || this.gitlog === undefined || this.gitlog === {}) {
      // this.backToHome("Gitlog not found!")
    }
  },
  methods: {
    makeLayout: function() {
      const x = this.selectedKeyFunc
      const y = this.selectedValueFunc
      return {
        title: `${y} per ${x}`,
        margin: {b: 20, l: 50, r: 50, t: 50},
        xaxis: {title: {text: x}},
        yaxis: {title: {text: y}},
      }
    },
    deactivateAllTabs: function () {
      Object.keys(this.activeTabs).forEach(tab => {
        this.activeTabs[tab] = false
      });
    },
    activateTabsContent: function (tab) {
      this.activeTabs[tab] = true;
    },
    switchTabs: function(tab) {
      this.deactivateAllTabs();
      this.activateTabsContent(tab);
    },
    backToHome(msg) {
      this.$store.commit("setAuthId", "");
      this.$store.commit("setGitlog", null);
      this.$store.commit("setLogTypeNull")
      this.$router.push({name: 'Home'})
      console.log(msg)
      // todo emit msg
    },
    updateOptions(options) {
      this.selectedOpts = options
      this.selected = true;
    }
  }
};

</script>

<style scoped>
#tabs-with-content .tabs:not(:last-child) {
  margin-bottom: 0;
}

#tabs-with-content .tab-content {
  padding: 1rem;
  display: none;
}

#tabs-with-content .tab-content.is-active {
  display: block;
}

#control-bar {
  height: 100%;
  width: 250px;
  min-width: 250px;
}

#dashboard-section {
  width: 100%;
  min-height: 400px;
  min-width: 500px;
}
</style>

