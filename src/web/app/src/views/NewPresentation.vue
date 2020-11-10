<template>
    <el-main>
        <el-card>
            <div slot="header" class="clearfix">
                <span> Create New Presentation </span>
            </div>
            <el-alert v-if="isNewPresentation && !isLogin" title="Please login to create new presentation" type="error" show-icon
                    class="errorMsg"/>
            <el-form v-else :rules="rules" ref="presentationForm"
                    :model="presentationForm" v-loading="isLoading">
                <el-form-item label="Name" :prop="'name'" >
                    <el-col>
                        <el-input v-model="presentationFormName" placeholder="Enter name"/>
                    </el-col>
                </el-form-item>
                <el-form-item label="Data Set" :prop="'dataset'">
                    <el-col>
                        <el-select :value="presentationFormVersion" v-on:change="setPresentationFormVersion" placeholder="Please select a data set" >
                            <el-option v-for="v in versions" :key="v" :label="v" :value="v"></el-option>
                        </el-select>
                    </el-col>
                </el-form-item>
                <el-form-item label="Description">
                    <el-col>
                        <el-input type="textarea" v-model="presentationFormDescription" placeholder="Enter description"/>
                    </el-col>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-check" @click="uploadClicked()">Save</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <!-- dialogs -->
        <el-dialog
        title="Success"
        :visible.sync="saveSuccess"
        width="30%" center>
            <span>You have successfully added a new presentation!</span>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" v-on:click="closeSuccess">OK</el-button>
            </span>
        </el-dialog>
        <!-- end of dialogs -->
    </el-main>
</template>

<script>
    import {AccessLevel, ID_NEW_PRESENTATION, SPECIAL_IDENTIFIER_PUBLIC} from "@/common/const";

    export default {
        name: 'PresentationBrief',
        props: {
            id: String
        },
        watch: {
            'id'() {
                this.updatePresentationForm()
            },
        },
        beforeCreate() {
            this.$store.dispatch('getVersionList');
        },
        mounted() {
            this.updatePresentationForm();
        },
        computed: {
            isLogin() {
                return this.$store.state.userInfo.isLogin
            },
            presentationForm() {
                return {
                    name: this.presentationFormName,
                    creatorIdentifier: this.presentationFormCreatorIdentifier,
                    description: this.presentationFormDescription,
                    dataset: this.presentationFormVersion,
                }
            },
            presentationFormCreatorIdentifier() {
                return this.$store.state.presentation.presentationForm.creatorIdentifier
            },
            versions() {
                return this.$store.getters.versionIdList;
            },
            presentationFormName: {
                get() {
                    return this.$store.state.presentation.presentationForm.name
                },
                set(value) {
                    this.$store.commit('setPresentationFormField', {
                        field: 'name',
                        value
                    })
                },
            },
            presentationFormDescription: {
                get() {
                    return this.$store.state.presentation.presentationForm.description
                },
                set(value) {
                    this.$store.commit('setPresentationFormField', {
                        field: 'description',
                        value
                    })
                },
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
            isNewPresentation() {
                return this.id === ID_NEW_PRESENTATION
            },
            isInEditMode() {
                return this.isEditing || this.isNewPresentation
            },
            saveSuccess() {
                return this.$store.state.presentation.isSaveSuccess
            },
            isLoading() {
                return this.$store.state.presentation.presentationFormStatus.isLoading
            },
            isError() {
                return this.$store.state.presentation.presentationFormStatus.isApiError
            },
            apiErrorMsg() {
                return this.$store.state.presentation.presentationFormStatus.apiErrorMsg
            }
        },
        data() {
            return {
                hasSubmitted: false,
                rules: {
                    name: [
                        {required: true, message: 'Please enter presentation name!', trigger: 'blur'},
                        {min: 3, message: 'The length should be more than 3 character!', trigger: 'blur'}
                    ],
                    dataset: [
                        {required: true, message: 'Please select a data set!', trigger: 'blur'},
                    ],
                }
            }
        },
        methods: {
            setPresentationFormVersion(version) {
                this.presentationFormVersion = version;
            },
            addPresentation() {
                this.hasSubmitted = false;
                this.$store.dispatch('savePresentation').then(() => {
                    if (this.isNewPresentation && !this.isLogin) {
                        return
                    }
                });
            },
            updatePresentationForm() {
                if (this.$refs['presentationForm']) {
                    this.$refs['presentationForm'].clearValidate();
                }
                this.$store.commit('resetPresentationForm');
                if (this.id !== ID_NEW_PRESENTATION) {
                    this.$store.dispatch('getPresentation', this.id)
                        .then(() => {
                            // check writable or not
                            this.$store.dispatch('fetchAccessControlList', this.id)
                                .then(() => {
                                    let currentUser = this.$store.state.userInfo.userEmail;
                                    let accessControlList = this.$store.state.accessControl.accessControlList;
                                    let isPresentationEditable =
                                        currentUser === this.presentationFormCreatorIdentifier
                                        || accessControlList.some(acl => acl.userIdentifier === currentUser && acl.accessLevel === AccessLevel.CAN_WRITE)
                                        || accessControlList.some(acl => acl.userIdentifier === SPECIAL_IDENTIFIER_PUBLIC && acl.accessLevel === AccessLevel.CAN_WRITE);
                                    this.$store.commit('setIsPresentationEditable', isPresentationEditable)
                                })
                        })
                } else {
                    this.$store.dispatch("getVersionList")
                }
            },
            uploadClicked() {
                this.$refs['presentationForm'].validate((valid, object) => {
                    if (!valid) {
                        if('name' in object) {
                            this.$notify.error({
                                title: 'Error',
                                message: object.name[0].message
                            });
                        } else if('dataset' in object) {
                            this.$notify.error({
                                title: 'Error',
                                message: object.dataset[0].message
                            });
                        }
                        return
                    }
                    this.$refs['presentationForm'].clearValidate();
                    this.addPresentation();
                });
            },
            closeSuccess() {
                this.$store.commit("setSaveSuccess", false);
                this.$router.push({
                    name: 'analyze'
                });
            }
        },
        components: {
        },
    }
</script>

<style scoped>
    .errorMsg {
        margin-bottom: 18px;
    }

    .el-select {
        display: block;
    }
</style>
