<template>
  <el-menu class="navbar" mode="horizontal" router>
    <el-menu-item class="chairvise-logo" index="/home">
      <img id="logo" alt="Chairvise logo" src="@/assets/logo.png"/>
      <span> Chairvise </span>
    </el-menu-item>
    <!--<el-menu-item index="/importData" :disabled="!isLogin">Import Data</el-menu-item>-->
    <!--<el-menu-item index="/analyze" :disabled="!isLogin">Analyze</el-menu-item>-->
    <el-submenu index="2" class="navbar-submenu" popper-class="navbar-submenu-menu" v-if="isLogin" v-loading.fullscreen.lock="isFullscreenLoading"
      style="{}">
      <template slot="title"> 
        <i class="el-icon-user-solid"></i> 
        <span class="hidden-sm-and-down">{{ userNickname }}</span>
      </template>
      <el-menu-item class="hidden-sm-and-down" index="/data" :disabled="!isLogin">
        <i class="el-icon-files"></i>
        My Data
      </el-menu-item>
      <el-menu-item index="/analyze" :disabled="!isLogin">
        <i class="el-icon-data-analysis"></i>
        My Presentations
      </el-menu-item>
      <el-menu-item index="/conference" :disabled="!isLogin">
        <i class="el-icon-data-line"></i>
        My Conferences
      </el-menu-item>
      <el-menu-item index="/logout" v-if="isLogin" @click="logout"
                  v-loading.fullscreen.lock="isFullscreenLoading">
        <i class="el-icon-close"></i>
        Logout
      </el-menu-item>
    </el-submenu>
    <el-menu-item class="login-button" index="/login" v-if="!isLogin" :disabled="isApiError" @click="login"
                  v-loading.fullscreen.lock="isFullscreenLoading">
      <i class="hidden-sm-and-down el-icon-user-solid"></i>
      Login
    </el-menu-item>
    <el-menu-item class="user-guide-button" index="/userGuide" :disabled="isApiError">
      User Guide
    </el-menu-item>
  </el-menu>
</template>

<script>
  export default {
    name: 'MenuBar',
    data() {
      return {
        isFullscreenLoading: false,
      }
    },
    computed: {
      isLogin() {
        return this.$store.state.userInfo.isLogin
      },
      userNickname() {
        return this.$store.state.userInfo.userNickname
      },
      isApiError() {
        return this.$store.state.userInfo.isApiError
      }
    },
    methods: {
      login() {
        // enter full screen loading and wait browser to redirect to google login page
        this.$data.isFullscreenLoading = true;
        window.location.href = this.$store.state.userInfo.loginUrl
      },
      logout() {
        // enter full screen loading and wait browser to redirect to google login page
        this.$data.isFullscreenLoading = true;
        window.location.href = this.$store.state.userInfo.logoutUrl
      }
    }
  }
</script>
<style scoped>
#logo {
  margin-right: 5px;
  width: 48px;
  height: 48px;
}

.el-menu--horizontal > .el-submenu.navbar-submenu {
  float:right; 
}

.el-menu--horizontal > .login-button {
  float: right;
}

.el-menu--horizontal > .user-guide-button {
  float: right;
}

i.el-icon-user-solid {
  margin-right: 0.5rem;
}

.login-button {
  float: right;
}

.navbar > .chairvise-logo.is-active {
    border-bottom: 0;
}
</style>