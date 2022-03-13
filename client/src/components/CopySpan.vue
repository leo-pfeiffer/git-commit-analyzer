<template>
    <span id="text" class="tooltip" v-bind:class="cssClass" @click="copy">
      {{ text }}
      <span class="tooltip-text">Click to copy</span>
    </span>
</template>

<script>
export default {
  name: "CopySpan",
  props: {
    text: String,
    cssClass: Array
  },
  methods: {
    copy: function() {
      navigator.clipboard.writeText(this.text);
    }
  }
}
</script>

<style scoped>
#text {
  cursor: pointer
}
.tooltip {
  position: relative;
  display: inline-block;
}

.tooltip .tooltip-text {
  visibility: hidden;
  width: 120px;
  font-size: small;
  background-color: rgba(82, 77, 77, 0.72);
  color: #fff;
  text-align: center;
  border-radius: 6px;
  padding: 5px;
  position: absolute;
  z-index: 1;
  bottom: 150%;
  left: 50%;
  margin-left: -75px;
  opacity: 0;
  transition: opacity 0.3s;
}

.tooltip .tooltip-text::after {
  content: "";
  position: absolute;
  top: 100%;
  left: 50%;
  margin-left: -5px;
  border-width: 5px;
  border-style: solid;
  border-color: rgba(82, 77, 77, 0.72) transparent transparent transparent;
}

.tooltip:hover .tooltip-text {
  visibility: visible;
  opacity: 1;
}
</style>