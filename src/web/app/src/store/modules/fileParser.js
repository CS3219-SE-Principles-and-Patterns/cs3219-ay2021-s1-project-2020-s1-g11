import moment from "moment";
import {anonymize, deepCopy} from "@/common/utility";

// const columns = {
//     author: ["submission #", "first name", "last name", "email", "country", "organization", "Web page", "person #", "corresponding?"],
//     review: ["Review Id", "Submission Id", "Num Review Assignment", "Reviewer Name", "Expertise Level", "Review Comment", "Confidence Level", "Overall Evaluation Score", "Column 9", "Column 10", "Column 11", "Column 12", "Day of the Review Date", "Time of the Review Date", "Has Recommended for the Best Paper"],
//     submission: []
// }

moment(); // temp to fix unused import error todo remove

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

        const data = deepCopy(result.data);
        data.shift(); // remove headers
        const table = parser(data);

        this.$store.commit("setUploadedFile", {data:table, raw:result.data});
        this.$store.commit("setPageLoadingStatus", false);
    }
}