<template>
  <footer class="footer">
    <span id="copyright">&copy; {{ year }} | Leo Pfeiffer</span>
  </footer>
</template>

<script>
export default {
  name: "Footer",
  computed: {
    year: function() {
      return new Date().getFullYear()
    }
  }
}
</script>

<style scoped>
.footer {
  text-align: left;
  background-color: white;
}

#copyright {
  color: gray;
  font-size: small;
}
</style>