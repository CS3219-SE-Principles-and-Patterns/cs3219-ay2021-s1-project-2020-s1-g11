<template>
  <el-main>
    <el-alert
        title="You need to login to view the page"
        type="error"
        v-if="!isLogin && !isAppLoading">
      &nbsp;<el-button type="warning" plain size="mini" @click="navigateToHomePage">Return to the Home Page</el-button>
    </el-alert>
    <h1 class="alignLeft">My Data</h1>
    <el-button class="alignRight" type="primary" icon="el-icon-plus"
           v-if="!isDataListEmpty" @click="handleImportData">Import Data</el-button>
    <br/>
    <el-divider></el-divider>
    <el-card v-if="isDataListEmpty" >
      <EmptyData />
    </el-card>
    <ul class="infinite-list" infinite-scroll-disabled="disabled" v-loading="isAppLoading">
      <li v-for="(version, index) in versions" :key="version">
        <zoom-center-transition :duration="500" :delay="100 * (index - 1)">
          <el-card shadow="hover">
            <div slot="header" class="clearfix card-header">
              <span v-if="version !== editingVersion" class="card-title"><b>{{ version }}</b></span>
              <span>
                <el-input v-if="version === editingVersion" v-model="conferenceName" placeholder="Conference Name" class="card-title"></el-input>
                <el-input v-if="version === editingVersion" v-model="conferenceYear" placeholder="Year" class="card-title"></el-input>
              </span>
              <span class="card-title">
                <el-dropdown @command="command => uploadRecord(version, command)">
                  <el-button type="success" :disabled="allRecords.filter(x => !getFileTypes(version).includes(x)).length === 0">
                    Upload<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-for="record in allRecords.filter(x => !getFileTypes(version).includes(x))" :key="record" :command="record">{{record}}</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
                <el-button-group>
                  <el-button v-if="version !== editingVersion" type="primary" @click="() => {editingVersion = version; editedVersionName = version;}" icon="el-icon-edit">
                    Edit
                  </el-button>
                  <el-button v-if="version === editingVersion" type="primary" @click="saveNewVersionId(editingVersion, editedVersionName)" icon="el-icon-check">
                    Save
                  </el-button>
                  <el-button type="danger"
                          icon="el-icon-delete" @click="deleteAllRecords(version)">
                    Delete
                  </el-button>
                </el-button-group>
              </span>
            </div>
            <el-row>
              <el-col :span="3">
                File Types:
              </el-col>
              <el-col :span="21">
                <el-tag v-for="t in getFileTypes(version)" :key="t" closable type="warning" class="record-tag" v-on:close="deleteRecord(version, t)">{{t}}</el-tag>
              </el-col>
            </el-row>
          </el-card>
        </zoom-center-transition>
      </li>
    </ul>
  </el-main>
</template>

<script>
import EmptyData from "@/components/emptyStates/EmptyData";
import {ZoomCenterTransition} from 'vue2-transitions'

export default {
  name: "ManageData",
  components: {
    EmptyData,
    ZoomCenterTransition,
  },
  data() {
    return {
      editingVersion: "",
      editedVersionName: "_",
      allRecords: ["AuthorRecord", "SubmissionRecord", "ReviewRecord"],
    }
  },
  beforeCreate() {
    this.$store.dispatch('getVersionList');
  },
  computed: {
    isLogin() {
      return this.$store.state.userInfo.isLogin;
    },
    isAppLoading: function () {
      return this.$store.state.isPageLoading || this.$store.state.dbMetaData.entitiesStatus.isLoading;
    },
    isDataListEmpty() {
      return this.versions.length <= 0;
    },
    versions() {
      return Array.from(new Set(this.$store.state.dataManage.versionList.map(v => v.versionId)));
    },
    conferenceName: {
      get() {
        let idx = this.editedVersionName.lastIndexOf('_');
        return this.editedVersionName.substring(0, idx);
      },
      set(newValue) {
        let idx = this.editedVersionName.lastIndexOf('_');
        let year = this.editedVersionName.substring(idx + 1);
        this.editedVersionName = newValue + '_' + year;
      }
    },
    conferenceYear: {
      get() {
        let idx = this.editedVersionName.lastIndexOf('_');
        return this.editedVersionName.substring(idx + 1);
      },
      set(newValue) {
        let idx = this.editedVersionName.lastIndexOf('_');
        let name = this.editedVersionName.substring(0, idx);
        this.editedVersionName = name + '_' + newValue;
      }
    },
  },
  methods: {
    navigateToHomePage() {
      this.$router.replace("/home");
    },
    handleImportData() {
      this.$router.push("/importData");
    },
    getFileTypes(versionId) {
      return this.$store.state.dataManage.versionList.filter(v => v.versionId === versionId).map(v => v.recordType);
    },
    saveNewVersionId(oldVersionId, newVersionId) {
      this.editingVersion = ""
      return this.$store.dispatch("updateVersion", [oldVersionId, newVersionId]);
    },
    deleteRecord(versionId, recordType) {
      switch (recordType) {
        case 'AuthorRecord':
          return this.$store.dispatch("deleteAuthorRecord", versionId);
        case 'ReviewRecord':
          return this.$store.dispatch("deleteReviewRecord", versionId);
        case 'SubmissionRecord':
          return this.$store.dispatch("deleteSubmissionRecord", versionId);
      }
    },
    deleteAllRecords(versionId) {
      return this.$store.dispatch("deleteVersion", versionId);
    },
    /* eslint-disable no-unused-vars */
    uploadRecord(versionId, recordType) {
      // TODO: Wait for mapping to be done
    }
    /* eslint-enable no-unused-vars */
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
  .infinite-list {
    list-style: none;
    padding: 0;
    margin: 0;
  }
  .el-row {
    margin-bottom: 6px;
  }
  .el-card {
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
  .record-tag {
    margin: 0 4px;
  }
  .el-input {
    width: 200px;
    margin-right: 8px;
  }
  .el-dropdown {
    margin-right: 6px;
  }
</style>
