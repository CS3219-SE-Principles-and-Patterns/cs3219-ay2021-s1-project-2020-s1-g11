<template>
  <el-row :gutter="20" class="map-container">

    <!-- left part of the page -->
    <el-col :span="20" class="map-result">
      <el-card>
        <div>
          <h3 class="mapping-header">Preview</h3>
        </div>
        <div>
          <h3>Raw</h3>
          <table>
            <tr>
              <th v-for="cell in uploadedRaw[0]">{{cell}}</th>
            </tr>
            <tr v-for="item in uploadedRaw.slice(1, 6)">
              <td v-for="cell in item">{{cell}}</td>
            </tr>
          </table>

          <h3>Mapped</h3>
          <table>
            <tr>
              <th v-for="i in Object.keys(uploadedData[0])">
                {{i}}
              </th>
            </tr>
            <tr v-for="item in uploadedData.slice(0, 5)">

              <td v-for="cell in item">{{cell}}</td>
            </tr>
          </table>
        </div>
      </el-card>
    </el-col>
    <!-- end of left part of the page -->

    <!-- right part of the page -->
    <el-col :span="8" :offset="0" class="map-area">
      <el-card>
        <!-- db fields -->
        <div class="db-tags">
          <h3>Mapping function</h3>
          <textarea v-model="mapFunctionRaw" rows="25" cols="50">
          </textarea>
          <div v-html="mapFunctionRaw"></div>
          <el-button @click="updateMapFunction"></el-button>
        </div>
        <!-- end of db fields -->

        <!-- imported tags
        <div class="import-tags">
          <h3>Imported data fields</h3>
          <transition-group name="tags-group" tag="div">
            <div class="tag" v-for="(item, idx) in importList"
                 v-bind:key="idx"
                 v-bind:class="[ idx === selectedImportTag ? 'active' : '', mappedImportTag.includes(idx) ? 'hidden' : '' ]"
                 v-on:click="importTagClicked(idx)">
              {{ item }}
            </div>
          </transition-group>
        </div>
         end of imported tags -->

        <!-- button group -->
        <el-row class="button-row">
          <el-col>
            <el-button class="back-button" type="success" v-on:click="uploadClicked">Confirm</el-button>
            <el-button class="back-button" type="info" v-on:click="backClicked">Cancel</el-button>
          </el-col>
        </el-row>
        <!-- end of button group -->
      </el-card>
    </el-col>
    <!-- end of right part of the page -->
  </el-row>
</template>

<script>
import {deepCopy, filterPredefinedMap} from "@/common/utility"
import PredefinedMappings from "@/store/data/predefinedMapping"
import {mapState} from "vuex";

export default {
  name: "MappingTool",
  data() {
    return {
      mapFunction: x => x,
      mapFunctionRaw: '',
      uploadedData: this.$store.state.dataMapping.data.records[this.$store.state.dataMapping.data.currentRecordIndex].uploadedData,
      uploadedRaw:  this.$store.state.dataMapping.data.records[this.$store.state.dataMapping.data.currentRecordIndex].uploadedRaw,

      // currently selected database tag and imported tag todo cleanup
      selectedDBTag: -1,
      selectedImportTag: -1,

      // ordered list of tags that have been todo cleanup
      // mapped with their data type details
      mappedDBTag: [],
      mappedImportTag: [],

      hasSubmitted: false,
      tableType: ""
    };
  },
  computed: {
    currentRecordIndex() {
      return this.$store.state.dataMapping.data.currentRecordIndex;
    },
    dbList: function () {
      return this.$store.state.dataMapping.data.records[this.$store.state.dataMapping.data.currentRecordIndex].dbSchema
    },
    // a list of size k * 2, k is the number of mapped pairs
    // the mapped pairs are indexes.
    mappedPairs: function () {
      let temp = this.mappedImportTag;
      return this.mappedDBTag.map(function (e, i) {
        return [e, temp[i]];
      });
    },

    // generates imported tags.
    // if initially no tag, just display column number
    importList: function () {
      if (this.$store.state.dataMapping.data.records[this.$store.state.dataMapping.data.currentRecordIndex].hasHeader) {
        return this.$store.state.dataMapping.data.records[this.$store.state.dataMapping.data.currentRecordIndex].uploadedLabel;
      }
      let lst = [];
      for (let i = 0; i < this.$store.state.dataMapping.data.records[this.$store.state.dataMapping.data.currentRecordIndex].uploadedLabel.length; i++) {
        lst.push("Column " + (i + 1));
      }
      return lst;
    },

    // gets errors
    errors: function () {
      return this.$store.state.dataMapping.error;
    },

    // whether upload is successful
    uploadSuccess: function () {
      return this.$store.state.dataMapping.isUploadSuccess;
    }
  },

  // display errors
  watch: {
    currentRecordIndex() {
      this.mappedDBTag = [];
      this.mappedImportTag = [];

      if (localStorage.mapFunctionRaw) {
        const obj = JSON.parse(localStorage.mapFunctionRaw);
        this.mapFunctionRaw = obj[this.currentRecordIndex]
      }
    },
    errors(newValue) {
      if (newValue.length > 0) {
        this.$notify.error({
          title: 'Error',
          message: newValue.join("\n")
        });
      }
    },
    mapFunctionRaw(mapFunctionRaw) {
      if (localStorage.mapFunctionRaw) {
        const obj = JSON.parse(localStorage.mapFunctionRaw);
        obj[this.currentRecordIndex] = mapFunctionRaw
        localStorage.mapFunctionRaw = JSON.stringify(obj)
      }
      else
        localStorage.mapFunctionRaw = JSON.stringify(["", "", ""])
    }
  },
  methods: {
    updateMapFunction() {
      try {
        this.mapFunction = eval(this.mapFunctionRaw);
        if (this.mapFunction)
          this.uploadedData = this.mapFunction(this.uploadedRaw.slice(1));
      } catch (e) {
        console.error(e)
      }

    },
    loadPredefined() {
      let predefinedMapping = PredefinedMappings[this.$store.state.dataMapping.data.currentRecordIndex + 1].mapping;
      this.mappedDBTag = filterPredefinedMap(deepCopy(predefinedMapping.dbTagIndices),
          this.$store.state.dataMapping.data.records[this.$store.state.dataMapping.data.currentRecordIndex].dbSchema.fieldMetaDataList);
      this.mappedImportTag = filterPredefinedMap(deepCopy(predefinedMapping.importedTagIndices),
          this.$store.state.dataMapping.data.records[this.$store.state.dataMapping.data.currentRecordIndex].uploadedLabel);
    },
    dbTagClicked: function (idx) {
      if (idx === this.selectedDBTag) {
        this.selectedDBTag = -1;
        return;
      }
      this.selectedDBTag = idx;
      if (this.selectedImportTag !== -1 && this.selectedDBTag !== -1) {
        this.mappedDBTag.push(this.selectedDBTag);
        this.mappedImportTag.push(this.selectedImportTag);
        this.selectedDBTag = -1;
        this.selectedImportTag = -1;
      }
    },
    importTagClicked: function (idx) {
      if (idx === this.selectedImportTag) {
        this.selectedImportTag = -1;
        return;
      }
      this.selectedImportTag = idx;
      if (this.selectedImportTag !== -1 && this.selectedDBTag !== -1) {
        this.mappedDBTag.push(this.selectedDBTag);
        this.mappedImportTag.push(this.selectedImportTag);
        this.selectedDBTag = -1;
        this.selectedImportTag = -1;
      }
    },
    removeMapClicked: function (idx) {
      this.mappedDBTag.splice(idx, 1);
      this.mappedImportTag.splice(idx, 1);
    },
    backClicked() {
      this.$emit('close-dialog');
    },
    uploadClicked: function () {
      let map = deepCopy(this.mappedPairs);
      this.$store.commit("setMapping", {"map": map});
      if (this.errors.length === 0) {
        this.hasSubmitted = true;
        this.$emit('close-dialog');
      }
    },
  },
  mounted() {
    if (localStorage.mapFunctionRaw) {
      const obj = JSON.parse(localStorage.mapFunctionRaw);
      this.mapFunctionRaw = obj[this.currentRecordIndex]
    }
    else
      localStorage.mapFunctionRaw = JSON.stringify(["", "", ""])
  },
  updated() {
  }
};
</script>

<style scoped>
@keyframes pulse {
  from {
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }

  50% {
    -webkit-transform: scale3d(1.1, 1.1, 1.1);
    transform: scale3d(1.1, 1.1, 1.1);
  }

  to {
    -webkit-transform: scale3d(1, 1, 1);
    transform: scale3d(1, 1, 1);
  }
}

.map-container h3 {
  letter-spacing: 1px;
}

.tags-group-move {
  transition: all 300ms ease-in-out 50ms;
}

.map-group-move {
  transition: all 600ms ease-in-out 50ms;
}

/* appearing */
.map-group-enter-active {
  transition: all 300ms ease-out;
}

/* disappearing */
.map-group-leave-active {
  transition: all 200ms ease-in;
}

/* appear at / disappear to */
.map-group-enter {
  opacity: 0;
  transform: translateY(30px);
}

.map-group-leave-to {
  opacity: 0;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter, .fade-leave-to
  /* .component-fade-leave-active below version 2.1.8 */
{
  opacity: 0;
}

.map-container {
  display: flex;
  flex-direction: row;
}

.db-tags {
  min-height: 90px;
}

.import-tags {
  min-height: 90px;
}

.tag {
  display: inline-block;
  height: 20px;
  margin: 5px 5px;
  padding: 7px 14px;
  background-color: #ffffff;
  border: 1px solid #007bff;
  color: #007bff;
  font-size: 14px;
  cursor: pointer;
  opacity: 1;
  z-index: 1;
  transition: opacity 0.2s, transform 0.3s, background-color 0.2s;
  border-radius: 5px;
  max-width: 400px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  /* box-shadow: 0 2px 4px -1px rgba(0,0,0,.2), 0 4px 5px 0 rgba(0,0,0,.14), 0 1px 10px 0 rgba(0,0,0,.12); */
}

.tag.active {
  animation: pulse 1s infinite;
  background-color: #007bff;
  color: #ffffff;
  transition: 0.3s;
}

.tag.hidden {
  position: absolute;
  opacity: 0;
  z-index: -1;
  transition: 0.2s;
}

.tag:hover {
  background-color: #007bff;
  color: #ffffff;
  transition: 0.2s;
}

.map-result {
  display: flex;
  flex-direction: column;
  /* border: 1px dashed #565656; */
  border-radius: 5px;
  min-height: 300px;
  /* padding: 30px 10px; */
  transition: all 0.3s ease;
}

.pair-tag {
  margin: 5px 5px;
  padding: 15px 14px;
  letter-spacing: 1px;
  border-bottom: 1px solid #eee;
  color: #565656;
}

.pair-tag .pair-info {
  margin-left: 10px;
  transition: 1s ease;
  font-size: 14px;
  display: inline;
}

.pair-tag .el-icon-close {
  margin-top: 4px;
  cursor: pointer;
  transition: 0.3s;
  float: right;
}

.pair-tag .el-icon-caret-right {
  position: relative;
  top: 2px;
}

.pair-tag .el-icon-close:hover {
  color: crimson;
  transition: 0.3s;
}

.no-map-info {
  color: #777;
  font-weight: 300;
  top: 65px;
  margin-left: 10px;
}

.el-tag {
  margin-left: 0;
  padding: 0;
  width: 70px;
  text-align: center;
  font-size: 9px;
}

.el-input {
  margin-left: 78px;
  margin-top: 8px;
  width: 185px;
}

.button-row {
  margin-top: 40px;
}

.mapping-header {
  display: inline-block;
}

.load-default-btn {
  float: right;
  margin: 10px 24px;
}
</style>