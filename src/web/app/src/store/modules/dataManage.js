import axios from "axios";

export default {
    state: {
        versionList: [],
    },
    mutations: {
        setVersionList(state, payload) {
            state.versionList = payload;
        },
        removeVersion(state, payload) {
            state.versionList = state.versionList.filter(v => v.versionId !== payload);
        },
        removeRecord(state, payload) {
            state.versionList = state.versionList
                .filter(v => !(v.versionId === payload[0] && v.recordType === payload[1]));
        }
    },
    actions: {
        async getVersionList({commit}) {
            commit('setPresentationListLoading', true);
            axios.get('/api/version')
                .then(response => {
                    commit('setVersionList', response.data)
                })
                .catch(e => {
                    commit('setPresentationListApiError', e.toString());
                })
                .finally(() => {
                    commit('setPresentationListLoading', false);
                })
        },
        async deleteAuthorRecord({commit}, versionId) {
            axios.delete('/api/record/author/' + versionId)
                .then(() => {
                    commit('removeRecord', [versionId, 'AuthorRecord'])
                })
        },
        async deleteReviewRecord({commit}, versionId) {
            axios.delete('/api/record/review/' + versionId)
                .then(() => {
                    commit('removeRecord', [versionId, 'ReviewRecord'])
                })
        },
        async deleteSubmissionRecord({commit}, versionId) {
            axios.delete('/api/record/submission/' + versionId)
                .then(() => {
                    commit('removeRecord', [versionId, 'SubmissionRecord'])
                })
        },
        async deleteVersion({commit}, versionId) {
            await axios.all([
                axios.delete('/api/version/' + versionId),
                axios.delete('/api/record/submission/' + versionId),
                axios.delete('/api/record/review/' + versionId),
                axios.delete('/api/record/author/' + versionId)
            ]).then(() => {
                commit('removeVersion', versionId)
            })
        },
        async updateVersion({commit}, payload) {
            await axios.all([
                axios.put('/api/version/' + payload[0], payload[1]),
                axios.put('/api/record/' + payload[0], payload[1])
            ]).then(() => {
                axios.get('/api/version').then(response => {
                    commit('setVersionList', response.data)
                })
            })
        },
    }
}
