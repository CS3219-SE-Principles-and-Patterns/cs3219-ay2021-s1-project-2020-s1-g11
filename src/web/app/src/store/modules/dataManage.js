import axios from "axios";

export default {
    state: {
        versionList: [],
    },
    getters: {
        versionIdList: state => {
            return Array.from(new Set(state.versionList.map(v => v.versionId)));
        },
        hasVersionData: state => {
            return state.versionList.length > 0;
        },
        getFileTypes: state => versionId => {
            return state.versionList.filter(v => v.versionId === versionId).map(v => v.recordType);
        },
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
            await axios.get('/api/version')
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
            await axios.delete('/api/record/author/' + versionId).then(() => {
                axios.delete('/api/version/author/' + versionId).then(() => {
                    commit('removeRecord', [versionId, 'AuthorRecord'])
                })
            })
        },
        async deleteReviewRecord({commit}, versionId) {
            await axios.delete('/api/record/review/' + versionId).then(() => {
                axios.delete('/api/version/review/' + versionId).then(() => {
                    commit('removeRecord', [versionId, 'ReviewRecord'])
                })
            })
        },
        async deleteSubmissionRecord({commit}, versionId) {
            await axios.delete('/api/record/submission/' + versionId).then(() => {
                axios.delete('/api/version/submission/' + versionId).then(() => {
                    commit('removeRecord', [versionId, 'SubmissionRecord'])
                })
            })
        },
        async deleteVersion({commit}, versionId) {
            await axios.all([
                axios.delete('/api/record/submission/' + versionId),
                axios.delete('/api/record/review/' + versionId),
                axios.delete('/api/record/author/' + versionId)
            ]).then(() => {
                axios.delete('/api/version/' + versionId).then(() => {
                    commit('removeVersion', versionId)
                })
            })
        },
        async editVersion({commit}, payload) {
            await axios.all([
                axios.post('/api/version', {versionId: payload[1], recordType: "AuthorRecord"}),
                axios.post('/api/version', {versionId: payload[1], recordType: "ReviewRecord"}),
                axios.post('/api/version', {versionId: payload[1], recordType: "SubmissionRecord"}),
            ]).then(() => {
                axios.put('/api/record/' + payload[0], payload[1]).then(() => {
                    axios.delete('/api/version/' + payload[0]).then(() => {
                        axios.get('/api/version').then(response => {
                            commit('setVersionList', response.data)
                        })
                    })
                })
            })
        },
    }
}
