<template>
  <el-container v-loading.fullscreen.lock="isAppLoading">
    <el-header style="padding: 0;">
      <menu-bar style="position: fixed; width: 100vw; z-index: 1;"></menu-bar>
    </el-header>
    <transition name="fade">
      <router-view/>
    </transition>
  </el-container>
</template>

<script>
  import MenuBar from '@/components/MenuBar.vue'

  export default {
    watch: {
      '$route'() {
        this.$store.dispatch('getAuthInfo');
      },
      'isFetchUserInfoError'() {
        if (!this.isFetchUserInfoError) {
          return
        }
        this.$notify.error({
          title: 'Auth request fail',
          message: this.$store.state.userInfo.apiErrorMsg,
          duration: 0
        });
      }
    },
    components: {
      'menu-bar': MenuBar,
    },
    computed: {
      isAppLoading() {
        return this.$store.state.isPageLoading
      },
      isFetchUserInfoError() {
        return this.$store.state.userInfo.isApiError
      }
    },
  }
</script>

<style>
@import url('https://fonts.googleapis.com/css?family=Montserrat:300,400,500');

body {
  font-family: 'Montserrat', sans-serif;

  /* try 1 */ /*
  background-color: #A8D0e6;
  color: #ffffff;
  */

  /* try 2 */ /*
  background: radial-gradient(#D8F2f2, #88BDBC);
  color: #ffffff;
  */
  
  /* current */ 
  /*background: #1e9fc4;*/
  color: #303133;
  
  margin: 0;
}

.fade-enter-active, .fade-leave-active {
  transition-property: opacity;
  transition-duration: .25s;
}

.fade-enter-active {
  transition-delay: .5s;
}

.fade-enter, .fade-leave-active {
  opacity: 0;
}

.el-dialog__body {
  word-break: normal !important;
}

@media (max-width: 991px) { 
  .el-dialog {
    width: 80% !important;
  }

  .el-main {
    padding: 0px !important;
  }

  /* .el-form-item {
    margin-bottom: 0px !important;
  } */

  .el-form-item .el-form-item__label {
    padding: 0px !important;
  }

  .el-select-dropdown {
    max-width: 100% !important;
  }

  h1 {
    margin-left: 5px !important;
    margin-top: 5px !important;
  }
}
</style>
