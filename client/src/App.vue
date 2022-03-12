<!--suppress CssUnusedSymbol -->
<template>
  <div id="app">
    <div id="nav">
      <router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link>
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
</style>
