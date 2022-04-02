<template id="user-profile">
  <app-frame>
    <dl v-if="user">
      <dt>User ID</dt>
      <dd>{{user.id}}</dd>
      <dt>Name</dt>
      <dd>{{user.name}}</dd>
      <dt>Email</dt>
      <dd>{{user.email}}</dd>
    </dl>
  </app-frame>
</template>

<script>
Vue.component("user-profile", {
  template: "#user-profile",
  data: () => ({
    user: null,
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    fetch(`/api/users/${userId}`)
      .then(res => res.json())
      .then(res => { this.user = res })
      .catch(() => alert("Error while fetching user"))
  }
});
</script>
