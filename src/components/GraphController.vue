<template>
  <aside class="menu" id="graph-controller">

    <div class="box">
      <draggable v-model="keyOptions" group="key-options" @start="drag=true" @end="drag=false">
        <span class="tag is-draggable" v-for="element in keyOptions" :key="element.id">{{element.name}}</span>
      </draggable>
      <hr>
      <p class="menu-label">Key</p>
      <draggable v-model="key" group="key-options" @start="drag=true" @end="drag=false" @change="(e) => groupChange(e, this.key, this.keyOptions)">
        <span class="tag is-draggable" v-for="element in key" :key="element.id">{{element.name}}</span>
      </draggable>
      <hr>
      <p class="menu-label">[Color]</p>
      <draggable v-model="color" group="key-options" @start="drag=true" @end="drag=false" @change="(e) => groupChange(e, this.color, this.keyOptions)">
        <span class="tag is-draggable" v-for="element in color" :key="element.id">{{element.name}}</span>
      </draggable>
    </div>

    <div class="box">
      <draggable v-model="valueOptions" group="value-opts" @start="drag=true" @end="drag=false">
        <span class="tag is-draggable" v-for="element in valueOptions" :key="element.id">{{element.name}}</span>
      </draggable>
      <hr>
      <p class="menu-label">Value</p>
      <draggable v-model="value" group="value-opts" @start="drag=true" @end="drag=false" @change="(e) => groupChange(e, this.value, this.valueOptions)">
        <span class="tag is-draggable" v-for="element in value" :key="element.id">{{element.name}}</span>
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
    keyOpts: Array,
    valueOpts: Array
  },
  data() {
    return  {
      keyOptions: [],
      valueOptions: [],
      key: [],
      color: [],
      value: []
    }
  },
  mounted() {
    this.keyOptions = this.keyOpts.map((v, i) => {return {id: i, name: v}})
    this.valueOptions = this.valueOpts.map((v, i) => {return {id: i, name: v}})
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
      if (this.key.length === 1 && this.value.length === 1) {
        const selection = {
          key: this.key[0],
          value: this.value[0],
          color: (this.color.length !== 1) ? null : this.color[0]
        }
        this.$emit("optionChange", selection)
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