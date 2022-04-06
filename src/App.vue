<!--suppress CssUnusedSymbol -->
<template>
  <div id="app">
    <div id="nav">
      <router-link to="/">Home</router-link> |
      <a href="https://github.com/leo-pfeiffer/git-commit-analyzer" target="_blank">About</a>
    </div>
    <router-view/>
  </div>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #000000;
}

#nav a.router-link-exact-active {
  color: #7957d5;
}

.center-screen {
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  min-height: 75vh;
}

@media (min-width: 500px) {
  .center-screen {
    padding-right: 50px;
    padding-left: 50px;
  }
}

@media (max-width: 500px) {
  .center-screen {
    padding-right: 10px;
    padding-left: 10px;
  }
}

.card-footer-item {
  cursor: pointer;
}

.card-footer-item.is-disabled {
  cursor: not-allowed;
  pointer-events: none;
  color: #c0c0c0;
  background-color: #ffffff;
}

.v-scroll {
  overflow-y: auto;
}

.h-scroll {
  overflow-x: auto;
}

@media (min-width: 850px) {
  .container.import-container {
    width: 800px;
  }
}

.card.import-card {
  width: 100%
}

.warning-msg {
  color: red;
}

.select.is-full {
  width: 100%;
}

.select.is-full > select {
  width: 100%;
}

</style>
