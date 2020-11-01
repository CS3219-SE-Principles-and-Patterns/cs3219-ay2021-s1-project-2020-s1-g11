<template>
  <el-main>
    <h1 class="alignLeft">My Presentations </h1>
    <el-button class="alignRight" type="primary" icon="el-icon-plus"
           v-if="!isPresentationListEmpty" @click="createPresentation">
      <span class="hidden-sm-and-down">Add New Presentation</span>
    </el-button>
    <br/>
    <el-divider></el-divider>
    <div class="infinite-list-wrapper">
      <el-card v-if="isPresentationListEmpty" >
        <EmptyPresentation v-on:createPresentationClick="createPresentation"/>
      </el-card>
      <ul class="infinite-list" v-infinite-scroll="loadMorePresentation" infinite-scroll-disabled="disabled" v-loading="isLoading">
        <li v-for="(presentation, index) in presentations" :key="presentation.id">
          <zoom-center-transition :duration="500" :delay="100 * (index - 1)">
            <el-card shadow="hover" @click.native="viewPresentation(presentation.id)">
              <div slot="header" class="clearfix card-header">
                <span class="card-title"><b>{{ presentation.name }}</b></span>
                <el-button type="danger" icon="el-icon-delete" @click="event => handleDeleteClicked(event, presentation.id, presentation.name)">Delete</el-button>
              </div>
              <el-row>
                <el-col :md="3" :sm="24">
                  Data Set:
                </el-col>
                <el-col :span="21">
                  {{ presentation.version }}
                </el-col>
              </el-row>
              <el-row>
                <el-col :md="3" :sm="24">
                  Description:
                </el-col>
                <el-col :span="21">
                  {{ presentation.description !== "" ? presentation.description : "No description" }}
                </el-col>
              </el-row>
            </el-card>
          </zoom-center-transition>
        </li>
      </ul>
    </div>
    <el-dialog
      title="Warning"
      :visible.sync="showDeleteDialog"
      width="30%" center>
      <span>Are you sure you want to delete "{{ deleteName }}"? This action cannot be undone.</span>
      <span slot="footer" class="dialog-footer">  
        <el-button type="primary-outline" v-on:click="() => { showDeleteDialog = false; }">Cancel</el-button>
        <el-button type="danger" v-on:click="() => { 
            showDeleteDialog = false;
            deletePresentation(deleteId); 
          }">Delete</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="Error"
      :visible.sync="showNoDataAlert"
      width="30%" center>
      <span>There is no data to analyse yet! Please upload some data first.</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary-outline" v-on:click="() => { showNoDataAlert = false; }">Cancel</el-button>
        <el-button type="primary" v-on:click="() => { 
            showNoDataAlert = false;
            importData(); 
          }">Upload</el-button>
      </span>
    </el-dialog>
  </el-main>
</template>

<script>
  import {ZoomCenterTransition} from 'vue2-transitions'
  import EmptyPresentation from '@/components/emptyStates/EmptyPresentation.vue'

  export default {
    name: 'Analyze',
    props: {
      id: String,
    },
    data() {
      return {
        show: false,
        count: 0,
        showNoDataAlert: false,
        showDeleteDialog: false,
        deleteId: -1,
        deleteName: "",
      }
    },
    beforeCreate() {
      this.$store.dispatch('getVersionList');
    },
    watch: {
      'isError'() {
        if (!this.isError) {
          return
        }
        this.$notify.error({
          title: 'Presentation list API request fail',
          message: this.$store.state.presentation.presentationListStatus.apiErrorMsg,
          duration: 0
        });
      }
    },
    computed: {
      isLogin() {
        return this.$store.state.userInfo.isLogin
      },
      isAppLoading() {
        return this.$store.state.isPageLoading
      },
      isLoading() {
        return this.$store.state.presentation.presentationListStatus.isLoading
      },
      presentations() {
        return this.$store.state.presentation.presentationList;
      },
      isPresentationListEmpty() {
        return this.$store.state.presentation.presentationList.length <= 0;
      },
      isError() {
        return this.$store.state.presentation.presentationListStatus.isApiError
      },
      hasData: function() {
        return this.$store.state.dataManage.versionList.length > 0;
      }
    },
    components: {
      ZoomCenterTransition,
      EmptyPresentation
    },
    methods: {
      createPresentation() {
        if (this.hasData) {
          this.$router.push("/analyze/create");
        } else {
          this.showNoDataAlert = true;
        }
      },
      loadPresentations() {
        this.show = true;
      },
      loadMorePresentation () {
        this.count += 5
      },
      viewPresentation(id) {
        this.$router.push("/analyze/" + id);
      },
      deletePresentation(id) {
        this.$store.dispatch('deletePresentation', id)
          .then(() => {
            if (this.isError) {
              return
            }
          })
      },
      handleDeleteClicked(event, id, name) {
        event.stopPropagation();
        this.deleteId = id;
        this.deleteName = name;
        this.showDeleteDialog = true;
      },
      importData() {
        this.$router.push("/importData");
      }
    },
    mounted() {
      this.$store.dispatch('getPresentationList')
      this.loadPresentations();
    }
  }
</script>

<style scoped>
  .alignLeft {
    float: left;
    display: inline-block;
    margin: 0;
  }
  .alignRight {
    float: right;
    display: inline-block;
    margin: 0;
  }
  .background {
    background-color: transparent;
    border-style: hidden;
  }
  .presentationCard {
    width: 100%;
    height: 100%;
    margin-bottom: 16px;
    background-color: white;
    text-align:left;
    color: black;
    padding: 4px 16px;
  }
  .menuCard {
    width: 100%;
    height: 100%;
  }
  .infinite-list {
    list-style: none;
    padding: 0;
    margin: 0;
  }
  .presentationCard .button {
    color: black;
    text-align: left;
  } 
  .presentation-image {
    text-align: center;
    vertical-align: middle;
    margin-top: 1rem;
  }
  .presentation-id {
    margin-top: 1.7rem;
  }
  .el-row {
    margin-bottom: 6px;
  }
  .el-card {
    cursor: pointer;
    margin-bottom: 12px;
  }
  .card-header {
    display: flex;
    justify-content: space-between;
  }
  .card-title {
    display: inline-flex;
    align-items: center;
  }
</style>