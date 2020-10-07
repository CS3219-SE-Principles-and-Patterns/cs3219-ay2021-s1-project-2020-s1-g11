package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sg.edu.nus.comp.cs3219.viz.common.util.Deserializer.VersionDeserializer;
import sg.edu.nus.comp.cs3219.viz.common.util.Serializer.VersionSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@JsonSerialize(using = VersionSerializer.class)
@JsonDeserialize(using = VersionDeserializer.class)
@Exportable(name = "Version", nameInDB = "version")
@Entity
public class Version {

    @EmbeddedId
    private VersionPk pk;

    public Version(){}

    public Version(VersionPk pk){
        this.pk = pk;
    }

    public VersionPk getPk(){return pk;}

    public void setPk(VersionPk pk){this.pk = pk;}

    @Embeddable
    public static class VersionPk implements Serializable{

        @Column(name = "data_set")
        private String dataSet;
        @Column(name = "record_type")
        private String recordType;
        @Column(name = "version")
        private String versionId;

        public VersionPk(){}

        public VersionPk(String dataSet, String recordType, String versionId){
            this.dataSet = dataSet;
            this.recordType = recordType;
            this.versionId = versionId;
        }

        public String getRecordType(){return recordType;}

        public void setRecordType(String recordType){this.recordType=recordType;}

        public String getVersionId() {
            return versionId;
        }

        public void setVersionId(String version) {
            this.versionId = version;
        }

        public String getDataSet() {
            return dataSet;
        }

        public void setDataSet(String dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public boolean equals(Object o){
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            VersionPk that = (VersionPk) o;
            return this.dataSet.equals(that.dataSet) && this.recordType.equals((that.recordType)) && (this.versionId == that.versionId);
        }

        @Override
        public int hashCode(){
            return Objects.hash(dataSet,recordType, versionId);
        }
    }
}