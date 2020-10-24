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
            <div v-if="!record.mappingFinished">
              <div class="form-card">
                <el-checkbox :value="record.hasHeader" v-on:change="val => setHasHeader(val, record.tableType)">
                  File Has Header
                </el-checkbox>
              </div>
            </div>
            <div v-else>
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
      
      <!-- <div class="section">
        <h2> 
          Version Information

          <el-tooltip placement="top">
            <div slot="content">
              If the input version is an existing version, current record will be replaced based on record type.
              <br/>
              If the input version is a new version, current record will be created based on record type.
            </div>
            <el-button type="text" icon="el-icon-question" circle></el-button>
          </el-tooltip>
        </h2>        
        <el-divider></el-divider>
        
        <el-row class="form-card">
          <el-col>
            <label class="label">
              Version
            </label>
            <br/>
            <el-autocomplete
              class="inline-input"
              v-model="versionId"
              :fetch-suggestions="querySearch"
              placeholder="Input Version"
            ></el-autocomplete>
          </el-col>
        </el-row> -->

      </el-card>
    </div>
  </el-main>
</template>

<script>
  import MappingTool from "@/components/MappingTool.vue";
  import Papa from "papaparse";
  import PredefinedMappings from "@/store/data/predefinedMapping"
  import moment from "moment"

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
        this.$router.push({ name: 'home' })
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
          complete: function (result) {

            var res = result;
            var res2 = res.data;
            var verId = this.$store.state.dataMapping.data.versionId;

            //author file preprocessing
            if(this.$store.state.dataMapping.data.currentRecordIndex === 0) {
              var authorres=[];
              //ACL file preprocessing //Softconf
              if(this.$store.state.dataMapping.data.formatType === 2) {
                authorres.push(["submission #","first name","last name","email","country","organization","Web page","person #","corresponding?"]);
                // for each row of data, manipulate temporary array element[] 
                // then push to true array res2[] for parsing
                for (var i = 1; i < res2.length; i++) {
                  var x = res2[i];
                  //console.log(x);
                  var k=0,j=14,element=[],corr="",country="";
                  while(x[j]!=""){
                    if(x[j]==x[65] && x[j+1]==x[66]){
                      corr = "yes";
                      country=x[78];
                    }
                    else {
                      corr="no";
                      country="";
                    }
                    element[k]= [x[0],x[j],x[j+1],x[j+2],country,x[j+3],"","",corr, verId];
                    authorres.push(element[k]);
                    k+=1;
                    j=14+k*5;
                  }
                //var element1=[x[0],x[14],x[15],x[16],"",x[17],"",""];
                }
                res2=authorres;
                //console.log(authorres)
              }

              //author anonymization - Both formats
              var convertstring=require("convert-string");
              for (var m=1;m<res2.length;m++) {
                var conv1=convertstring.stringToBytes(res2[m][1]);
                var conv2=convertstring.stringToBytes(res2[m][2]);
                var firstname="";
                var lastname="";
                for (var a=0;a<conv1.length;a++) {
                  firstname=firstname.concat(String(conv1[a]+18));
                }
                for (var w=0;w<conv2.length;w++) {
                  lastname=lastname.concat(String(conv2[w]+18));
                }
                res2[m][1]=firstname;
                res2[m][2]=lastname;
              }
              //console.log(res2);
            }

            //review file preprocessing
            else if( this.$store.state.dataMapping.data.currentRecordIndex === 1) {
              //Softconf
              if(this.$store.state.dataMapping.data.formatType === 2) {
                var reviewres=[];
                reviewres.push(["Review Id","Submission Id", "Num Review Assignment", "Reviewer Name", "Expertise Level", "Review Comment","Confidence Level", "Overall Evaluation Score", "Column 9","Column 10","Column 11","Column 12", "Day of the Review Date", "Time of the Review Date", "Has Recommended for the Best Paper"]);

                for (var q = 1; q < res2.length; q++) {
                    var z = res2[q];
                    z[32]="confidence: "+z[32];
                    //console.log(typeof(z[7]));
                    //var str=z[7].toString();
                    var date_time=z[7].split(" ");
                    //console.log(date_time);
                    var date=date_time[0];
                    var time=date_time[1].split(":")[0]+":"+date_time[1].split(":")[1];
                    //console.log(date,time);
                    element=["",z[0],"","","",z[38],z[32],z[31],"","","","",date,time,"",verId];
                    reviewres.push(element);
                }
                res2=reviewres;
                //console.log(reviewres);
              }

              //author anonymization - JCDL
              // Easy Chair
              else if(this.$store.state.dataMapping.data.formatType === 1){
                var convert_string=require("convert-string");
                for(var index=1;index<res2.length;index++){
                  var convert=convert_string.stringToBytes(res2[index][3]);
                  var name="";
                  for(var idx=0;idx<convert.length;idx++) {
                      name=name.concat(String(convert[idx]+18));
                  }
                  res2[index][3]=name;
                }
              }
            }

            //ACL submission file processing
            else if( this.$store.state.dataMapping.data.currentRecordIndex === 2) {
              if(this.$store.state.dataMapping.data.formatType=="2"){
              var submissionres=[];
              submissionres.push(["#", "track #", "track name", "title", "authors", "submitted","last updated", "form fields", "keywords", "decision", "notified", "reviews sent", "abstract"]);

              for (var l = 1; l < res2.length; l++) {
                var y = res2[l];
                var dt = moment(y[10], "D MMM YYYY HH:mm:ss").format("YYYY-M-D H:m");
                if(y[6].includes("Reject")){y[6]="reject";}
                else {y[6]="accept";}
                //console.log(x);
                element=[y[0],"",y[4],y[2],y[3],dt,dt,"",y[13],y[6],"","",y[9], verId];
                submissionres.push(element);
              }
                res2=submissionres;
              }
            }

            if (this.$store.state.dataMapping.data.formatType === 1) {
              var tempCSV=[];
              //author
              if(this.$store.state.dataMapping.data.currentRecordIndex === 0){
                tempCSV.push(["submission #","first name","last name","email","country","organization","Web page","person #","corresponding?"]);
              }
              //review
              else if(this.$store.state.dataMapping.data.currentRecordIndex === 1){
                tempCSV.push(["Review Id","Submission Id", "Num Review Assignment", "Reviewer Name", "Expertise Level", "Review Comment","Confidence Level", "Overall Evaluation Score", "Column 9","Column 10","Column 11","Column 12", "Day of the Review Date", "Time of the Review Date", "Has Recommended for the Best Paper"]);
              }
              //submission
              else if(this.$store.state.dataMapping.data.currentRecordIndex === 2){
                tempCSV.push(["#", "track #", "track name", "title", "authors", "submitted","last updated", "form fields", "keywords", "decision", "notified", "reviews sent", "abstract"]);
              }
              // for each row of data, manipulate temporary array element[] 
              // then push to true array res2[] for parsing
              var csvRow=[];
              for (var rowNum = 1; rowNum < res2.length; rowNum++) {
                csvRow = res2[rowNum];
                //csvRow.push(verId);
                tempCSV.push(csvRow);
              }
              res2=tempCSV;
            }
            this.$store.commit("setUploadedFile",res2);
            this.$store.commit("setPageLoadingStatus", false);
            this.showMappingTool = true;
          }.bind(this)
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
