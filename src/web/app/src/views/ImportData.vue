<template>
  <el-main>
    <el-alert
      title="You need to login-in to view the page"
      type="error"
      v-if="!isLogin && !isAppLoading">
      &nbsp;<el-button type="warning" plain size="mini" @click="navigateToHomePage">Return to the Home Page</el-button>
    </el-alert>
    <div v-if="isLogin">
      <el-dialog
        :title="dbSchemas.length === 0 ? '' : dbSchemas[currentRecordIndex].name"
        :visible.sync="showMappingTool"
        width="80%"
        :show-close="false">
        <mapping-tool ref="mapTool" v-on:close-dialog="showMappingTool = false"></mapping-tool>
      </el-dialog>
      <el-dialog
        title="Success"
        :visible.sync="uploadSuccess"
        width="30%" center>
        <span>You have successfully imported data using the column mapping!</span>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" v-on:click="closeSuccess">OK</el-button>
        </span>
      </el-dialog>
      <el-dialog
        title="Error"
        :visible.sync="showVersionConflict"
        width="30%" center>
        <span>Records with same conference name and year exist! Please choose a different conference name or year.</span>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" v-on:click="() => { showVersionConflict = false; }">OK</el-button>
        </span>
      </el-dialog>
      <el-card>
        <div slot="header" class="clearfix">
          <span>Upload Data</span>
        </div>
      <div class="section">
        <h2> Record Information </h2>
        <el-divider></el-divider>
        <el-row class="form-card">
          <el-col :sm="12" :md="4" class="conf-name">
            <label class="label"> Conference Name </label>
            <br/>
            <el-input v-model="conferenceName" class="form-item" placeholder="Conference Name"></el-input>
          </el-col>
          <el-col :sm="12" :md="4">
            <label class="label"> Year </label>
            <br/>
            <el-input v-model="conferenceYear" class="form-item" placeholder="Year"></el-input>
          </el-col>
        </el-row>
        <div class="form-card">
          <label class="label"> Data Format </label>
          <br/>
          <el-radio-group v-model="formatType" class="form-item">
            <el-radio :label="1">EasyChair</el-radio>
            <el-radio :label="2">SoftConf</el-radio>
            <el-radio :label="3">Custom</el-radio>
          </el-radio-group>
        </div>
      </div>
      <div class="section">
        <h2>
          Data Files
        </h2>
        <el-divider></el-divider>
        <el-row>
          <el-col :md="24 / records.length" v-for="(record, idx) in records" :key="idx">
            <label class="label">{{ dbSchemas[idx].name }}</label>
            <br/>
            <el-upload class="form-item" drag action=""
                  :auto-upload="false"
                  :show-file-list="false"
                  :multiple="false"
                  :on-change="file => fileUploadHandler(file, record.tableType)"
                  :disabled="record.mappingFinished"
                  >
              <div v-if="!record.mappingFinished">
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">Drop .csv file here or <em>click to upload</em></div>
              </div>
              <div v-else>
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">File attached successfully</div>
              </div>
            </el-upload>
            <div v-if="record.mappingFinished">
              <div class="form-card">
                <el-button icon="el-icon-delete" type="danger" v-on:click="() => fileDeleteHandler(record.tableType)">Delete File</el-button>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="section">
        <el-button
          icon="el-icon-upload2"
          type="primary"
          v-on:click="submitMapping()"
          :disabled="isReadyForUpload"
        >Upload Data</el-button>
      </div>
      </el-card>
    </div>
  </el-main>
</template>

<script>
  import MappingTool from "@/components/MappingTool.vue";
  import Papa from "papaparse";
  import PredefinedMappings from "@/store/data/predefinedMapping"
  import fileParser from "@/store/modules/fileParser";

  export default {
    name: "ImportData",
    data() {
      return {
        predefinedMappings: PredefinedMappings,
        showMappingTool: false,
        showVersionConflict: false,
      };
    },
    beforeCreate() {
      this.$store.dispatch('fetchDBMetaDataEntities');
      this.$store.dispatch('getVersionList');
    },
    computed: {
      isLogin() {
        return this.$store.state.userInfo.isLogin;
      },
      isAppLoading: function () {
        return this.$store.state.isPageLoading || this.$store.state.dbMetaData.entitiesStatus.isLoading;
      },
      dbSchemas: function () {
        return this.$store.state.dbMetaData.entities;
      },
      formatType: {
        get: function () {
          return this.$store.state.dataMapping.data.formatType;
        },
        set: function (newValue) {
          this.$store.commit("setFormatType", newValue);
        }
      },
      records: {
        get: function() {
          return this.$store.state.dataMapping.data.records;
        },
        set: function (newValue) {
          this.$store.commit("setRecords", newValue);
        }
      },
      conferenceName: {
        get() {
          let idx = this.$store.state.dataMapping.data.versionId.lastIndexOf('_');
          return this.$store.state.dataMapping.data.versionId.substring(0, idx);
        },
        set(newValue) {
          let idx = this.$store.state.dataMapping.data.versionId.lastIndexOf('_');
          let year = this.$store.state.dataMapping.data.versionId.substring(idx + 1);
          this.$store.commit("setVersionId", newValue + '_' + year);
        }
      },
      conferenceYear: {
        get() {
          let idx = this.$store.state.dataMapping.data.versionId.lastIndexOf('_');
          return this.$store.state.dataMapping.data.versionId.substring(idx + 1);
        },
        set(newValue) {
          let idx = this.$store.state.dataMapping.data.versionId.lastIndexOf('_');
          let name = this.$store.state.dataMapping.data.versionId.substring(0, idx);
          this.$store.commit("setVersionId", name + '_' + newValue);
        }
      },
      versionId: {
        get: function () {
          return this.$store.state.dataMapping.data.versionId;
        },
        set: function (newValue) {
          this.$store.commit("setVersionId", newValue);
        }
      },
      currentRecordIndex() {
        return this.$store.state.dataMapping.data.currentRecordIndex;
      },
      uploadSuccess: function () {
        return this.$store.state.dataMapping.isUploadSuccess;
      },
      isReadyForUpload: function () {
        return !(this.$store.state.dataMapping.data.records.some(record => record.fileUploaded) &&
          this.conferenceName !== '' && this.conferenceYear !== '');
      },
      isNewVersion: function() {
        if (!this.$store.state.dataManage.versionList) {
          return false;
        } else {
          let verList = this.$store.state.dataManage.versionList.map(v => v.versionId);
          return !verList.includes(this.$store.state.dataMapping.data.versionId);
        }
      }
    },
    watch: {
      dbSchemas(newValue) {
        if (newValue.length > 0 && !this.$store.state.dataMapping.data.initialized) {
          this.$store.commit('initDataRecords', this.$store.state.dbMetaData.entities);
        }
      }
    },
    methods: {
      setHasHeader(val, index) {
        this.$store.commit('setHasHeader', {value: val, index: index});
      },
      querySearch(queryString, cb) {
        // convert to array of string
        var links = this.$store.state.dataManage.versionList.map(v => v.versionId);
        // function to remove duplicate from array of string
        let reduceFunction = (links) => links.filter((v,i) => links.indexOf(v) === i );
        links = reduceFunction(links);
        links = links.map(v => { return { "value" : v} });
        var results = queryString ? links.filter(this.createFilter(queryString)) : links;
        cb(results);
      },
      createFilter(queryString) {
        return (link) => {
          return (link.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
      navigateToHomePage() {
        this.$router.replace("/home");
      },
      fileDeleteHandler(idx) {
        this.$store.commit("initDataRecord", {idx: idx, schema: this.$store.state.dbMetaData.entities[idx]})
      },
      submitMapping() {
        if (!this.isNewVersion) {
          this.showVersionConflict = true;
          return;
        }
        this.hasSubmitted = false;
        this.$store.dispatch("persistMappingNewVersion");
      },
      closeSuccess: function () {
        this.$router.push({ name: 'data' })
        this.$store.commit("setUploadSuccess", false);
        this.$store.commit('initDataRecords', this.$store.state.dbMetaData.entities);
        this.$store.commit("clearVersionId");
        this.$store.dispatch('getVersionList');
      },
      fileUploadHandler: function (file, idx) {
        // show loading and go parsing
        this.$store.commit("setPageLoadingStatus", true);
        this.$store.commit("setCurrentRecordIndex", idx);

        Papa.parse(file.raw, {
          // ignoring empty lines in csv file
          skipEmptyLines: true,
          complete: (result) => {
            fileParser.parser.bind(this)(result)
            if (this.formatType === 3) // show map function dialog for custom format
              this.showMappingTool = true
          }
        });

        }
    },
    components: {
      MappingTool
    }
  };
</script>

<style scoped>
  .upload-box .el-select {
    margin-top: 20px;
  }

  .button-row {
    margin-top: 30px;
  }

  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }

  .box-card {
    width: 480px;
    position: relative;
    left: 50%;
    margin-left: -240px;
  }

  .autocomplete-verid {
    position: relative;
  }

  .form-card {
    margin: 16px 0px;
  }

  .section {
    padding: 0px 16px 16px 16px;
  }

  .form-item {
    margin-top: 10px;
  }

  .conf-name {
    margin: 0 15px 0 0;
  }
</style>
