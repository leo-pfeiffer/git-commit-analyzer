<template>
  <aside class="menu" id="graph-controller">

    <div class="box">
      <p class="menu-label">Axis Options</p>
      <draggable v-model="axisOptions" group="axisoptions" @start="drag=true" @end="drag=false">
        <span class="tag is-draggable" v-for="element in axisOptions" :key="element.id">{{element.name}}</span>
      </draggable>
      <hr>
      <p class="menu-label">x-axis</p>
      <draggable v-model="xAxis" group="axisoptions" @start="drag=true" @end="drag=false" @change="(e) => groupChange(e, this.xAxis, this.axisOptions)">
        <span class="tag is-draggable" v-for="element in xAxis" :key="element.id">{{element.name}}</span>
      </draggable>
      <hr>
      <p class="menu-label">y-axis</p>
      <draggable v-model="yAxis" group="axisoptions" @start="drag=true" @end="drag=false" @change="(e) => groupChange(e, this.yAxis, this.axisOptions)">
        <span class="tag is-draggable" v-for="element in yAxis" :key="element.id">{{element.name}}</span>
      </draggable>
    </div>

    <div class="box">
      <p class="menu-label">Aggregator Options</p>
      <draggable v-model="aggregatorOptions" group="aggoptions" @start="drag=true" @end="drag=false">
        <span class="tag is-draggable" v-for="element in aggregatorOptions" :key="element.id">{{element.name}}</span>
      </draggable>
      <hr>
      <p class="menu-label">aggregator</p>
      <draggable v-model="aggregator" group="aggoptions" @start="drag=true" @end="drag=false" @change="(e) => groupChange(e, this.aggregator, this.aggregatorOptions)">
        <span class="tag is-draggable" v-for="element in aggregator" :key="element.id">{{element.name}}</span>
      </draggable>
    </div>

  </aside>
</template>

<script>
import draggable from 'vuedraggable'

export default {
  name: "GraphController",
  components: {
    draggable,
  },
  props: {
    axisOpts: Array,
    aggOpts: Array
  },
  data() {
    return  {
      axisOptions: [],
      aggregatorOptions: [],
      xAxis: [],
      yAxis: [],
      aggregator: []
    }
  },
  mounted() {
    this.axisOptions = this.axisOpts.map((v, i) => {return {id: i, name: v}})
    this.aggregatorOptions = this.aggOpts.map((v, i) => {return {id: i, name: v}})
  },
  methods: {
    groupChange: function(event, array, optionsArray) {
      if ("added" in event) {
        this.removeOld(event.added.newIndex, array, optionsArray)
        this.submit()
      }
      // todo handle removed
    },
    removeOld: function (newIndex, array, optionsArray) {
      if (array.length > 1) {
        const idxToRemove = 1 - newIndex;
        const itemToRemove = array[idxToRemove]
        array.splice(idxToRemove, 1);
        optionsArray.push(itemToRemove)
      }
    },
    submit: function() {
      if (this.xAxis.length == 1 && this.yAxis.length === 1) {
        const selection = {
          x: this.xAxis[0],
          y: this.yAxis[0],
          z: (this.aggregator.length !== 1) ? null : this.aggregator[0]
        }
        console.log(selection)
        // todo emit...
      }
    }
  }
}
</script>

<style scoped>

#graph-controller {
  background-color: deeppink;
  height: 100%
}

.menu {
  position: sticky;
  display: inline-block;
  vertical-align: top;
  max-height: 100vh;
  overflow-y: auto;
  width: 250px;
  top: 0;
  bottom: 0;
  padding: 30px;
}

tag, .is-draggable {
  cursor: grab;
}

tag, .is-draggable:active {
  cursor: grabbing;
}

.menu-label {
  margin: 1px
}

hr {
  margin-top: 10px;
  margin-bottom: 10px;
}


</style>