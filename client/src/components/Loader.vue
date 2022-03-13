<template>
  <span class="loader"
        :style="{height: `${height}`, width: `${width}`, 'border-top': `${thickness} solid ${color}`}"
  ></span>
</template>

<script>
export default {
  name: "Loader",
  props: {
    height: String,
    width: String,
    thickness: String,
    color: String
  }
}
</script>

<style scoped>
.loader {
  border: 3px solid #f3f3f3;
  border-radius: 50%;
  border-top: 3px solid #7957d5;
  width: 20px;
  height: 20px;
  -webkit-animation: spin 1.5s linear infinite; /* Safari */
  animation: spin 1.5s linear infinite;
}

/* Safari */
@-webkit-keyframes spin {
  0% { -webkit-transform: rotate(0deg); }
  100% { -webkit-transform: rotate(360deg); }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>