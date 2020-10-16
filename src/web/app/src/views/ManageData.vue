<template>
  <el-main>
    <el-alert
        title="You need to login-in to view the page"
        type="error"
        v-if="!isLogin && !isAppLoading">
      &nbsp;<el-button type="warning" plain size="mini" @click="navigateToHomePage">Return to the Home Page</el-button>
    </el-alert>
    <el-card v-if="isDataListEmpty" >
      <EmptyData />
    </el-card>
  </el-main>
</template>

<script>
import EmptyData from "@/components/emptyStates/EmptyData";
export default {
  name: "ManageData",
  components: {EmptyData},
  computed: {
    isLogin() {
      return this.$store.state.userInfo.isLogin;
    },
    isAppLoading: function () {
      return this.$store.state.isPageLoading || this.$store.state.dbMetaData.entitiesStatus.isLoading;
    },
    isDataListEmpty() {
      // return this.$store.state.presentation.presentationList.length <= 0;
      return true;
    },
  },
  methods: {
    navigateToHomePage() {
      this.$router.replace("/home");
    }
  }
}
</script>

<style scoped>

</style>