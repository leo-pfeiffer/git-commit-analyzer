<template>
  <div class="home v-scroll columns is-mobile">
    <GraphController id="control-bar" :axis-opts="['opt1', 'opt2', 'opt3']" :agg-opts="['aggr1', 'aggr2', 'aggr3']"/>
    <section id="dashboard-section">
      <div id="tabs-with-content" class="container">
        <div class="tabs is-centered">
          <ul>
            <li><a @click="() => switchTabs('graph1')" v-bind:class="{'is-active': activeTabs['graph1']}">
              Graph 1
            </a></li>
            <li><a @click="() => switchTabs('graph2')" v-bind:class="{'is-active': activeTabs['graph2']}">
              Graph 2
            </a></li>
            <li><a @click="() => switchTabs('graph3')" v-bind:class="{'is-active': activeTabs['graph3']}">
              Graph 3
            </a></li>
          </ul>
        </div>
        <div>
          <section class="tab-content" v-bind:class="{'is-active': activeTabs['graph1']}">
<!--              <Plotly :data="data1" :layout="layout1" :display-mode-bar="false"></Plotly>-->
          </section>
          <section class="tab-content" v-bind:class="{'is-active': activeTabs['graph2']}">
<!--            <Plotly :data="data2" :layout="layout2" :display-mode-bar="false"></Plotly>-->
          </section>
          <section class="tab-content" v-bind:class="{'is-active': activeTabs['graph3']}">
<!--            <Plotly :data="data3" :layout="layout3" :display-mode-bar="false"></Plotly>-->
          </section>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
// import { Plotly } from 'vue-plotly';
import { mapState } from "vuex";
import LogHandler from "@/dashboard/processing";
import GitLog from "@/gitlog/gitlog";
import GraphController from "@/components/GraphController";

export default {
  name: 'Dashboard',
  components: {
    // Plotly,
    GraphController
  },
  data() {
    return {
      activeTabs: {
        graph1: true,
        graph2: false,
        graph3: false,
      },
    }
  },
  computed: {
    ...mapState({
      gitlog: (state) => state.gitlog
    }),
    gitlogInstance: function() {
      return GitLog.fromArray(this.gitlog._log)
    },
    data1: function() {
      const logHandler = new LogHandler(this.gitlogInstance)
      const data = logHandler.aggregateBy(LogHandler.kfHash, LogHandler.afNumAdditions)
      return [{
        x: Object.keys(data),
        y: Object.keys(data).map(e => data[e]),
        type: "bar"
      }]
    },
    data2: function() {
      const logHandler = new LogHandler(this.gitlogInstance)
      const data = logHandler.aggregateBy(LogHandler.kfDate, LogHandler.afNumCommits)
      return [{
        x: Object.keys(data),
        y: Object.keys(data).map(e => data[e]),
        type: "line"
      }]
    },
    data3: function() {
      return [{
        x: [1,2,3,4],
        y: [10,15,13,17],
        type: "scatter"
      }]
    },
    layout1: function() {return this.makeLayout("Graph 1")},
    layout2: function() {return this.makeLayout("Graph 2")},
    layout3: function() {return this.makeLayout("Graph 3")},
  },
  mounted() {
    if (this.gitlog === null || this.gitlog === undefined || this.gitlog === {}) {
      // this.backToHome("Gitlog not found!")
    }
  },
  methods: {
    makeLayout: function(title) {
      return {
        title: title,
        margin: {b: 20, l: 50, r: 50, t: 50}
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
      this.$router.push({name: 'Home'})
      console.log(msg)
      // todo emit msg
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

