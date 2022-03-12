<template>
  <div class="home">
    <section>
      <div class="container center-screen">
        <div class="card">
          <div class="card-content">
            Import with Text
            <hr>
            <button class="button" @click="() => this.$router.push({name: 'Dashboard'})">Go to Dashboard</button>
          </div>

        </div>
      </div>
    </section>
  </div>
</template>

<script>

export default {
  name: 'ImportText',
  components: {}
}

</script>
