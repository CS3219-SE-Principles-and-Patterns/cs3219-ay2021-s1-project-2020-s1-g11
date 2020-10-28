import {stringToBytes} from "convert-string";

export const deepCopy = (obj) => {
  return JSON.parse(JSON.stringify(obj));
};

//    deepcopy(predefinedMapping.dbTagIndices), dbSchema.fieldMetaDataList
// or deepcopy(predefinedMapping.importedTagIndices), data.uploadedLabel
export const filterPredefinedMap = (mappedIdArray, originalArray) => {
  let result = [];
  for (let i = 0; i < mappedIdArray.length; i++) {
    if (mappedIdArray[i] < originalArray.length) {
      result.push(mappedIdArray[i]);
    }
  }
  return result;
};

export const anonymize = (string, offset) => {
  return stringToBytes(string).map(i => i + offset).join("")
}