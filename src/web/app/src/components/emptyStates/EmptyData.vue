<template>
  <div class="emptySection">
    <el-row>
      <el-col>
        <div class="icon">
          <img src="@/assets/businessman.png" />
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col>
        <p>
          <i> No data found. </i>
        </p>
        <p>
          Let's get started by importing data.
        </p>
        <el-button type="primary" icon="el-icon-plus" @click="importData">Import Data</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  methods: {
    importData() {
      this.$router.push("/importData");
    },
  }
}
</script>

<style scoped>
.icon {
  margin-left: 04.5rem;
  margin-bottom: 0.25rem;
}
.emptySection {
  text-align: center;
  vertical-align: middle;
}
</style>
