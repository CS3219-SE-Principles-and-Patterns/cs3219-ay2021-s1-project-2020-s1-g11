<template>
    <el-main>
        <el-container>
            <el-main>
                <presentation-brief 
                    :id="id" 
                    :version="presentationFormVersion" 
                    :versions="versions"
                    :fileTypes="getFileTypes(presentationFormVersion)"
                    v-on:version-change="e => handleVersionChange(e)"
                />
                <section-list-panel 
                    :presentationId="id" 
                    :presentationFormVersion="presentationFormVersion" 
                    :versions="versions"
                    :fileTypes="getFileTypes(presentationFormVersion)"
                />
            </el-main>
        </el-container>
    </el-main>
</template>

<script>
    import ChartJsPluginDataLabels from 'chartjs-plugin-datalabels';
    import AbstractSectionDetail from "@/components/AbstractSectionDetail.vue"
    import {ID_NEW_PRESENTATION} from "@/common/const";
    import SectionListPanel from "@/components/SectionListPanel.vue";
    import PredefinedQueries from "@/store/data/predefinedQueries"
    import PresentationBrief from "@/components/PresentationBrief.vue";

    export default {
        props: {
            id: String,
        },
        data() {
            return {
                selectedNewSection: '',
                presentationId: '',
            }
        },
        beforeCreate() {
            this.$store.dispatch('getVersionList');
        },
        computed: {
            isLogin() {
                return this.$store.state.userInfo.isLogin
            },

            isPresentationEditable() {
                return this.$store.state.presentation.isPresentationEditable;
            },

            predefinedSections() {
                let sectionOptionsGroup = {};
                // grouping the predefined queries
                for (let key in PredefinedQueries) {
                    if (!PredefinedQueries.hasOwnProperty(key)) {
                        continue;
                    }
                    let groupName = PredefinedQueries[key].group;
                    if (sectionOptionsGroup[groupName] === undefined) {
                        sectionOptionsGroup[groupName] = [];
                    }
                    sectionOptionsGroup[groupName].push({
                        value: key,
                        label: PredefinedQueries[key].name,
                    })
                }

                // generate to format that element ui requires
                let sectionOptions = [];
                for (let groupName in sectionOptionsGroup) {
                    if (!sectionOptionsGroup.hasOwnProperty(groupName)) {
                        continue;
                    }
                    sectionOptions.push({
                        label: groupName,
                        options: sectionOptionsGroup[groupName]
                    })
                }
                return sectionOptions;
            },
            isNewPresentation() {
                return this.presentationId === ID_NEW_PRESENTATION
            },
            versions() {
                let list = Array.from(new Set(this.$store.state.presentation.versionList.map(v => v.versionId)));
                return list;
            },
            presentationFormVersion: {
                get() {
                    return this.$store.state.presentation.presentationForm.version;
                },
                set(value) {
                    this.$store.commit('setPresentationFormField', {
                        field: 'version',
                        value
                    });
                }
            },
            sectionList() {
                return this.$store.state.section.sectionList
            },
            isLoadingSectionList() {
                return this.$store.state.section.sectionListStatus.isLoading
            },
            isSectionListApiError() {
                return this.$store.state.section.sectionListStatus.isApiError
            },
            sectionListApiErrorMsg() {
                return this.$store.state.section.sectionListStatus.apiErrorMsg
            },
            isLoadingDBMetaData() {
                return this.$store.state.dbMetaData.entitiesStatus.isLoading
            },
        },
        components: {
            AbstractSectionDetail,
            SectionListPanel,
            PresentationBrief,
            ChartJsPluginDataLabels
        },
        mounted() {
            this.presentationId = this.$route.params.id;
            this.fetchSectionList();
            this.$store.dispatch('fetchDBMetaDataEntities');
        },
        methods: {
            fetchSectionList() {
                if (this.isNewPresentation) {
                    this.$store.commit('clearSectionList');
                } else {
                    this.$store.dispatch('fetchSectionList', this.presentationId)
                }
            },
            handleVersionChange(e) {
                this.presentationFormVersion = e;
            },
            getFileTypes(versionId) {
                return this.$store.state.presentation.versionList.filter(v => v.versionId === versionId).map(v => v.recordType);
            },
        }
    }
</script>

<style scoped>
</style>