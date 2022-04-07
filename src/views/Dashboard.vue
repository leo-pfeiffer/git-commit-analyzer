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
            <li><a @click="() => switchTabs('sunburstChart')" v-bind:class="{'is-active': activeTabs['sunburstChart']}">
              Sunburst
            </a></li>
            <li><a @click="() => switchTabs('barChart')" v-bind:class="{'is-active': activeTabs['barChart']}">
              Bar
            </a></li>
            <li><a @click="() => switchTabs('pieChart')" v-bind:class="{'is-active': activeTabs['pieChart']}">
              Pie
            </a></li>
            <li><a @click="() => switchTabs('scatterChart')" v-bind:class="{'is-active': activeTabs['scatterChart']}">
              Line
            </a></li>
          </ul>
        </div>
        <div>
          <section class="tab-content" v-bind:class="{'is-active': activeTabs['sunburstChart']}">
            <Plotly v-if="selected && activeTabs['sunburstChart']"
                    :data="sunburstData" :layout="layout" :display-mode-bar="false"
                    :show-link="showLink" :plotly-server-url="plotlyServerURL" :link-text="linkText"
                    :responsive="true"/>
            <p v-else>{{ helpTextSunburst }}</p>
          </section>
          <section class="tab-content" v-bind:class="{'is-active': activeTabs['barChart']}">
              <Plotly v-if="selected && activeTabs['barChart']"
                      :data="barData" :layout="layout" :display-mode-bar="false"
                      :show-link="showLink" :plotly-server-url="plotlyServerURL" :link-text="linkText"
                      :responsive="true"/>
              <p v-else>{{ helpText }}</p>
          </section>
          <section class="tab-content" v-bind:class="{'is-active': activeTabs['pieChart']}">
            <Plotly v-if="selected && activeTabs['pieChart']"
                    :data="pieData" :layout="layout" :display-mode-bar="false"
                    :show-link="showLink" :plotly-server-url="plotlyServerURL" :link-text="linkText"
                    :responsive="true"/>
            <p v-else>{{ helpText }}</p>
          </section>
          <section class="tab-content" v-bind:class="{'is-active': activeTabs['scatterChart']}">
            <Plotly v-if="selected && activeTabs['scatterChart']"
                    :data="lineData" :layout="layout" :display-mode-bar="false"
                    :show-link="showLink" :plotly-server-url="plotlyServerURL" :link-text="linkText"
                    :responsive="true"/>
            <p v-else>{{ helpText }}</p>
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
import { nullOrUndefined } from "@/utils/utils";

export default {
  name: 'Dashboard',
  components: {
    Plotly,
    GraphController
  },
  data() {
    return {
      helpText: "To show the chart, select key and value by dragging the tags in the control bar.",
      helpTextSunburst: "To show the chart, select a key by dragging the tags in the control bar.",
      activeTabs: {
        sunburstChart: true,
        barChart: false,
        pieChart: false,
        scatterChart: false,
      },
      selectedOpts: {},
      selected: false,
      linkText: "Edit in Chart Studio",
      showLink: true,
      plotlyServerURL: "https://chart-studio.plotly.com",
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
    barData: function() {
      const data = this.getData()

      const r = {
        x: data.x,
        y: data.y,
        type: "bar"
      }

      if (data.z !== null) {
        r['transforms'] = [{type: 'groupby', groups: data.z}]
        return [r]
      } else {
        return [r]
      }
    },
    pieData: function() {
      const data = this.getData()
      return [{
        labels: data.x,
        values: data.y,
        type: "pie"
      }]
    },
    lineData: function() {
      const data = this.getData()

      const r = {
        x: data.x,
        y: data.y,
        type: "scatter",
      }

      if (data.z !== null) {
        r['transforms'] = [{type: 'groupby', groups: data.z}]
        return [r]
      } else {
        return [r]
      }
    },
    sunburstData: function() {

      const logHandler = new LogHandler(this.gitlogInstance)

      const keyKf = LogHandler.keyMap[this.selectedKeyFunc]
      const keyFunc = keyKf.func
      const keyTransformer = keyKf.transform

      const isCommitKf = keyKf === LogHandler.keyMap.Commit;

      const grouped = logHandler.groupBy(keyFunc)

      const hashTransformer = LogHandler.keyMap.Commit.transform

      const ids = []
      const labels = []
      const parents = []

      Object.keys(grouped).forEach((k) => {
        if (!isCommitKf) {
          ids.push(k)
          labels.push(keyTransformer(k));
          parents.push("");
        }
        grouped[k].forEach((c) => {
          ids.push(c.hash)
          labels.push(hashTransformer(c.hash));
          if (isCommitKf) {
            parents.push("");
          } else {
            parents.push(k);
          }
          c.nodes.forEach((n) => {
            ids.push(n.path);
            labels.push(n.path);
            parents.push(c.hash);
          })
        })
      })

      return [{
        type: "sunburst",
        maxdepth: 2,
        ids: ids,
        labels: labels,
        parents: parents,
        outsidetextfont: {size: 20},
        marker: {line: {width: 2}},
      }];


    },
    layout: function() {
      return this.makeLayout()
    },
  },
  mounted() {
    this.$store.commit("setGitlog", GitLog.fromPlainObject(this.gitlog));
    if (this.gitlog === null || this.gitlog === undefined || this.gitlog === {}) {
      this.backToHome("Gitlog not found!")
    }
  },
  methods: {
    makeLayout: function() {
      if (this.activeTabs.sunburstChart) {
        return this._sunburstLayout();
      } else {
        return this._regularLayout();
      }
    },
    _sunburstLayout: function() {
      return {
        height: 500,
        margin: {b: 50, l: 50, r: 50, t: 50},
      }
    },
    _regularLayout: function() {
      const x = this.selectedKeyFunc
      const y = this.selectedValueFunc
      return {
        title: `${y} per ${x}`,
        height: 500,
        margin: {b: 50, l: 50, r: 50, t: 50},
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
      this.selected = false;
      this.activateTabsContent(tab);
      this.updateSelected();
    },
    backToHome(msg) {
      this.$store.commit("setAuthId", "");
      this.$store.commit("setGitlog", null);
      this.$store.commit("setLogTypeNull")
      this.$router.push({name: 'Home'})
      console.log(msg)
      // todo emit msg
    },
    updateSelected() {
      this.selected =
          (this.activeTabs.sunburstChart &&
              !nullOrUndefined(this.selectedOpts.key)) ||
          (!this.activeTabs.sunburstChart &&
              !nullOrUndefined(this.selectedOpts.key) &&
              !nullOrUndefined(this.selectedOpts.value));
    },
    updateOptions(options) {
      this.selectedOpts = options
      this.updateSelected()
    },
    getData: function() {
      const logHandler = new LogHandler(this.gitlogInstance)

      const keyKf = LogHandler.keyMap[this.selectedKeyFunc]
      const keyFunc = keyKf.func
      const keyTransformer = keyKf.transform

      const valFunc = LogHandler.valMap[this.selectedValueFunc]

      if (this.selectedOpts.color === null) {
        const data = logHandler.aggregateBy(keyFunc, valFunc)
        return {
          x: Object.keys(data).map(e => keyTransformer(e)),
          y: Object.keys(data).map(e => data[e]).map(e => e.value),
          z: null
        }
      } else {
        const groupKf = LogHandler.keyMap[this.selectedGroupFunc]

        const groupFunc = groupKf.func
        const groupTransformer = groupKf.transform

        const data = logHandler.groupAggregateBy(groupFunc, keyFunc, valFunc)
        return  {
          x: Object.keys(data).map(e => data[e]).map(e => e.key).map(e => keyTransformer(e)),
          y: Object.keys(data).map(e => data[e]).map(e => e.value),
          z: Object.keys(data).map(e => data[e]).map(e => e.group).map(e => groupTransformer(e)),
        }
      }
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

