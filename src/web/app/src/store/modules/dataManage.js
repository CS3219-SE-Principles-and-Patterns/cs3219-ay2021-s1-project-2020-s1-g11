import axios from "axios";

export default {
    state: {
        versionList: [],
    },
    mutations: {
        setVersionList(state, payload) {
            state.versionList = payload;
        },
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
        }
    }
}
