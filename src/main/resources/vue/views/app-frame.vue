<template id="app-frame">
  <div class="app-frame">
    <header>
      <a href="/"><span>JavalinVue demo app</span></a>
    </header>
    <slot></slot>
  </div>
</template>
<script>
  Vue.component("app-frame", {template: "#app-frame"});
</script>
<style>
  .app-frame > header {
    padding: 20px;
    background: #b6e2ff;
    font-size: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
</style>