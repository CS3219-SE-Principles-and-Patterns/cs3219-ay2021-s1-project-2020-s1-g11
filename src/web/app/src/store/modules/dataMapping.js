import axios from 'axios'
import {processMapping} from '@/store/helpers/processor.js'
import {REVIEW_DATE_DAY_FIELD, REVIEW_DATE_TIME_FIELD, REVIEW_TABLE_ID} from "@/common/const"
import {deepCopy} from "@/common/utility"

export default {
  state: {
    hasDBSchemaSet: false,
    hasHeaderSpecified: false,
    isUploadSuccess: false,
    data: {
      records: [],
      initialized: false,
      formatType: 1,
      isNewVersion: null,
      versionId: '_',
      currentRecordIndex: 0,
    },
    error: []
  },

  mutations: {
    initDataRecords(state, schemas) {
      state.data.records = [];
      schemas.forEach((schema, idx) => {
        let newRecord = {
          dbSchema: null,
          tableType: idx,
          hasHeader: false,
          uploadedData: [],
          uploadedRaw: [],
          uploadedLabel: [],
          mappingResult: [],
          processedResult: [],
          fileUploaded: false,
          mappingFinished: false,
        };
        let dbSchema = deepCopy(schema);
        if (idx === REVIEW_TABLE_ID) {
          dbSchema.fieldMetaDataList.push(REVIEW_DATE_DAY_FIELD);
          dbSchema.fieldMetaDataList.push(REVIEW_DATE_TIME_FIELD);
        }
        newRecord.dbSchema = dbSchema;
        state.data.records.push(newRecord);
      });
      state.data.initialized = true;
    },

    initDataRecord(state, payload) {
      let newRecord = {
        dbSchema: null,
        tableType: payload.idx,
        hasHeader: false,
        uploadedData: [],
        uploadedLabel: [],
        mappingResult: [],
        processedResult: [],
        fileUploaded: false,
        mappingFinished: false,
      };
      let dbSchema = deepCopy(payload.schema);
      if (payload.idx === REVIEW_TABLE_ID) {
        dbSchema.fieldMetaDataList.push(REVIEW_DATE_DAY_FIELD);
        dbSchema.fieldMetaDataList.push(REVIEW_DATE_TIME_FIELD);
      }
      newRecord.dbSchema = dbSchema;
      let oldRecords = deepCopy(state.data.records);
      state.data.records = [];
      for (let i = 0; i < oldRecords.length; i++) {
        if (i === payload.idx) {
          state.data.records.push(newRecord);
        } else {
          state.data.records.push(oldRecords[i])
        }
      }
    },

    setRecords(state, data) {
      state.data.records = data.copy();
    },

    setUploadSuccess(state, success) {
      state.isUploadSuccess = success;
    },

    setUploadedFile(state, {data, raw}) {
      state.data.records[state.data.currentRecordIndex].uploadedLabel = data[0];
      state.data.records[state.data.currentRecordIndex].uploadedData = data;
      state.data.records[state.data.currentRecordIndex].uploadedRaw = raw;
      state.data.records[state.data.currentRecordIndex].fileUploaded = true;
    },

    setDBSchema(state, dbSchema) {
      state.data.dbSchema = dbSchema;
      state.hasDBSchemaSet = true;
    },

    clearDBSchema(state) {
      state.data.dbSchema = [];
      state.hasDBSchemaSet = false;
    },

    setFormatType(state, formatType) {
      state.data.formatType = formatType;
      state.hasFormatTypeSpecified = true;
    },

    clearFormatType(state) {
      state.data.formatType = null;
      state.hasFormatTypeSpecified = false;
    },

    setTableType(state, selected) {
      state.data.tableType = selected;  
      state.hasTableTypeSelected = true;
    },

    clearTableType(state) {
      state.data.tableType = null;
      state.hasTableTypeSelected = false;
    },

    setVersionId(state, selected) {
      state.data.versionId = selected;
    },

    clearVersionId(state) {
      state.data.versionId = '_';
    },

    setIsNewVersion(state, selected) {
      state.data.isNewVersion = selected;
    },

    clearIsNewVersion(state) {
      state.data.isNewVersion = null;
    },

    setHasHeader(state, payload) {
      state.data.records[payload.index].hasHeader = payload.value;
    },

    setMapping(state, payload) {
      try {
        state.error = [];
        state.data.records[state.data.currentRecordIndex].mappingResult = payload.map;
        state.data.records[state.data.currentRecordIndex].mappingFinished = true;
        state.data.records[state.data.currentRecordIndex].processedResult =
          processMapping(payload.map,
            state.data.records[state.data.currentRecordIndex].uploadedData,
            state.data.records[state.data.currentRecordIndex].dbSchema,
            state.data.records[state.data.currentRecordIndex].hasHeader);
      } catch (err) {
        state.error.push(err);
        state.data.records[state.data.currentRecordIndex].mappingFinished = false;
        state.data.records[state.data.currentRecordIndex].mappingResult = [];
        state.data.records[state.data.currentRecordIndex].processedResult = [];
      }
    },

    clearMapping(state) {
      state.data.mappingResult = [];
      state.data.processedResult = [];
      state.mappingFinished = false;
    },

    setCurrentRecordIndex(state, index) {
      state.data.currentRecordIndex = index;
    },

    setDataMappingError(state, err) {
      state.error.push(err);
    },

    clearError(state) {
      state.error = [];
    }
  },

  actions: {
    async persistMappingNewVersion({commit, state}) {
      commit("setPageLoadingStatus", true);
      let fnKeyEntry = [];
      let endpoints = [];
      let records = state.data.records.filter(record => record.fileUploaded).map(record => {
        let endpoint;
        let fnKeyTable;
        switch (record.tableType) {
          case 0:
            endpoint = "author";
            fnKeyTable = "AuthorRecord";
            break;
          case 1:
            endpoint = "review";
            fnKeyTable = "ReviewRecord";
            break;
          case 2:
            endpoint = "submission";
            fnKeyTable = "SubmissionRecord";
            break;
        }
        endpoints.push(endpoint);
        fnKeyEntry.push({versionId: state.data.versionId, recordType: fnKeyTable});

        let newRecord = deepCopy(record);
        // add version to end
        newRecord.uploadedData.forEach(row => row.versionId = state.data.versionId)

        return newRecord;
      });

      axios.all(fnKeyEntry.map(entry => postVersion(entry)))
        .then(axios.spread(() => {
          axios.all(records.map((record, idx) => postTable(endpoints[idx], record.uploadedData)))
            .then(axios.spread(() => {
              commit("setPageLoadingStatus", false);
              commit("setUploadSuccess", true);
            }))
            .catch(axios.spread(function (dataErr) {
              commit("setPageLoadingStatus", false);
              commit("setUploadSuccess", false);
              commit("setDataMappingError", dataErr);
            }));
        }))
        .catch(axios.spread(function (verErr) {
          commit("setPageLoadingStatus", false);
          commit("setUploadSuccess", false);
          commit("setDataMappingError", verErr);
        }));
    },

    async persistMappingOldVersion({commit, state}) {
      commit("setPageLoadingStatus", true);
      let endpoint;
      switch (state.data.tableType) {
        case 0:
          endpoint = "author";
          break;
        case 1:
          endpoint = "review";
          break;
        case 2:
          endpoint = "submission";
          break;
      }
      // add version to end
      for (var i=0; i<state.data.processedResult.length; i++){
        var row = state.data.processedResult[i];
        row.versionId = state.data.versionId;
      }
      //console.log(state.data.processedResult);
      await axios.post("/api/record/" + endpoint, state.data.processedResult)
        .then(() => {
          commit("setPageLoadingStatus", false);
          commit("setUploadSuccess", true);
        })
        .catch(e => {
          commit("setPageLoadingStatus", false);
          commit("setUploadSuccess", false);
          commit("setDataMappingError", e.toString());
        });
    }
  }
}
function postVersion(fnKeyEntry) {
  return axios.post("/api/version", fnKeyEntry);
}

function postTable(endpoint, processedResult) {
  return axios.post("/api/record/" + endpoint, processedResult);
}