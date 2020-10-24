import moment from "moment";
import {anonymize, deepCopy} from "@/common/utility";

const columns = {
    author: ["submission #", "first name", "last name", "email", "country", "organization", "Web page", "person #", "corresponding?"],
    review: ["Review Id", "Submission Id", "Num Review Assignment", "Reviewer Name", "Expertise Level", "Review Comment", "Confidence Level", "Overall Evaluation Score", "Column 9", "Column 10", "Column 11", "Column 12", "Day of the Review Date", "Time of the Review Date", "Has Recommended for the Best Paper"],
    submission: []
}

const parsers = {
    easychair: [
        data => data.map(row => {
            return {
                submissionId: row[0],
                firstName: anonymize(row[1], 18),
                lastName: anonymize(row[2], 18),
                email: row[3],
                country: row[4],
                organization: row[5],
                webpage: row[6],
                personId: row[7],
                corresponding: row[8]
            }
        }),
        data => data.map(row => {
            return {
                reviewId: row[0],
                submissionId: row[1],
                numReviewAssignment: row[2],
                reviewerName: row[3],
                expertiseLevel: row[4],
                confidenceLevel: "",
                reviewComment: row[5],
                overallEvaluationScore: row[7],
                reviewSubmissionTime: row[12] + row[13], // todo: use moment to parse
                hasRecommendedForBestPaper: row[14],
            }
        }),
        data => data.map(row => {
            return {
                submissionId : row[0],
                trackId : row[1],
                trackName : row[2],
                title : row[3],
                authors : row[4],
                submissionTime : row[5],
                lastUpdatedTime : row[6],
                keywords : row[8],
                isAccepted : row[9],
                isNotified : row[10],
                isReviewsSent : row[11],
                submissionAbstract : row[12]
            }
        })
    ],
    softconf: [
        data => data.map(row => {
            return {
                submissionId: row[0],
                firstName: anonymize(row[7], 18),
                lastName: anonymize(row[8], 18),
                email: row[9],
                country: row[17],
                organization: row[10],
                webpage: "",
                personId: "",
                corresponding: "no"
            }
        }),
        data => data.map(row => {
            return {
                reviewId: 0,
                submissionId: row[9],
                numReviewAssignment: 0,
                reviewerName: row[6] + " " + row[7],
                expertiseLevel: 0,
                confidenceLevel: row[38],
                reviewComment: row[39],
                overallEvaluationScore: 0,
                reviewSubmissionTime: row[12],
                hasRecommendedForBestPaper: "n/a",
            }
        }),
        data => data.map(row => {
            return {
                submissionId : row[0],
                trackId : 0,
                trackName : row[4],
                title : row[2],
                authors : row[3], //todo: include all authors
                submissionTime : row[10],
                lastUpdatedTime : "",
                keywords : "",
                isAccepted : row[6],
                isNotified : "",
                isReviewsSent : "",
                submissionAbstract : row[9]
            }
        })
    ],
}

export default {
    parser: function (result) {
        const recordIndex = this.$store.state.dataMapping.data.currentRecordIndex
        const type = this.$store.state.dataMapping.data.formatType
        const parser = type === 1 ? parsers.easychair[recordIndex] : parsers.softconf[recordIndex]

        const data = result.data;
        data.shift(); // remove headers
        const table = parser(data);

        //
        // //author file preprocessing
        // if (this.$store.state.dataMapping.data.currentRecordIndex === 0) {
        //     var authorres = [];
        //     //ACL file preprocessing //Softconf
        //     if (this.$store.state.dataMapping.data.formatType === 2) {
        //
        //     }
        // }
        //
        // //review file preprocessing
        // else if (this.$store.state.dataMapping.data.currentRecordIndex === 1) {
        //     //Softconf
        //     if (this.$store.state.dataMapping.data.formatType === 2) {
        //         var reviewres = [];
        //         reviewres.push();
        //
        //         for (var q = 1; q < res2.length; q++) {
        //             var z = res2[q];
        //             z[32] = "confidence: " + z[32];
        //             //console.log(typeof(z[7]));
        //             //var str=z[7].toString();
        //             var date_time = z[7].split(" ");
        //             //console.log(date_time);
        //             var date = date_time[0];
        //             var time = date_time[1].split(":")[0] + ":" + date_time[1].split(":")[1];
        //             //console.log(date,time);
        //             var element = ["", z[0], "", "", "", z[38], z[32], z[31], "", "", "", "", date, time, "", verId];
        //             reviewres.push(element);
        //         }
        //         res2 = reviewres;
        //         //console.log(reviewres);
        //     }
        //
        //         //author anonymization - JCDL
        //     // Easy Chair
        //     else if (this.$store.state.dataMapping.data.formatType === 1) {
        //         var convert_string = require("convert-string");
        //         for (var index = 1; index < res2.length; index++) {
        //             var convert = convert_string.stringToBytes(res2[index][3]);
        //             var name = "";
        //             for (var idx = 0; idx < convert.length; idx++) {
        //                 name = name.concat(String(convert[idx] + 18));
        //             }
        //             res2[index][3] = name;
        //         }
        //     }
        // }
        //
        // //ACL submission file processing
        // else if (this.$store.state.dataMapping.data.currentRecordIndex === 2) {
        //     if (this.$store.state.dataMapping.data.formatType == "2") {
        //         var submissionres = [];
        //         submissionres.push(["#", "track #", "track name", "title", "authors", "submitted", "last updated", "form fields", "keywords", "decision", "notified", "reviews sent", "abstract"]);
        //
        //         for (var l = 1; l < res2.length; l++) {
        //             var y = res2[l];
        //             var dt = moment(y[10], "D MMM YYYY HH:mm:ss").format("YYYY-M-D H:m");
        //             if (y[6].includes("Reject")) {
        //                 y[6] = "reject";
        //             } else {
        //                 y[6] = "accept";
        //             }
        //             //console.log(x);
        //             element = [y[0], "", y[4], y[2], y[3], dt, dt, "", y[13], y[6], "", "", y[9], verId];
        //             submissionres.push(element);
        //         }
            //         res2 = submissionres;
        //     }
        // }
        //
        // if (this.$store.state.dataMapping.data.formatType === 1) {
        //     var tempCSV = [];
        //     //author
        //     if (this.$store.state.dataMapping.data.currentRecordIndex === 0) {
        //         tempCSV.push(["submission #", "first name", "last name", "email", "country", "organization", "Web page", "person #", "corresponding?"]);
        //     }
        //     //review
        //     else if (this.$store.state.dataMapping.data.currentRecordIndex === 1) {
        //         tempCSV.push(["Review Id", "Submission Id", "Num Review Assignment", "Reviewer Name", "Expertise Level", "Review Comment", "Confidence Level", "Overall Evaluation Score", "Column 9", "Column 10", "Column 11", "Column 12", "Day of the Review Date", "Time of the Review Date", "Has Recommended for the Best Paper"]);
        //     }
        //     //submission
        //     else if (this.$store.state.dataMapping.data.currentRecordIndex === 2) {
        //         tempCSV.push(["#", "track #", "track name", "title", "authors", "submitted", "last updated", "form fields", "keywords", "decision", "notified", "reviews sent", "abstract"]);
        //     }
        //     // for each row of data, manipulate temporary array element[]
        //     // then push to true array res2[] for parsing
        //     var csvRow = [];
        //     for (var rowNum = 1; rowNum < res2.length; rowNum++) {
        //         csvRow = res2[rowNum];
        //         //csvRow.push(verId);
        //         tempCSV.push(csvRow);
        //     }
        //     res2 = tempCSV;
        // }


        this.$store.commit("setUploadedFile", table);
        this.$store.commit("setPageLoadingStatus", false);
        // this.showMappingTool = true;
    }
}