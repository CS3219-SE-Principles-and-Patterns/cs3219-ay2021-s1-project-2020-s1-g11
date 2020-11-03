package sg.edu.nus.comp.cs3219.viz.logic;

import org.springframework.stereotype.Component;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Version;
import sg.edu.nus.comp.cs3219.viz.storage.repository.VersionRepository;

import java.util.List;

@Component
public class VersionLogic {

    private final VersionRepository versionRepository;

    public VersionLogic(VersionRepository versionRepository){
        this.versionRepository = versionRepository;
    }

    public List<Version> findAllForUser(UserInfo userInfo){
        return versionRepository.findById_DataSetEquals(userInfo.getUserEmail());
    }

    public List<Version> findAllForUserWithRecordType(UserInfo userInfo, String recordType){
        return versionRepository.findById_DataSetAndId_RecordType(userInfo.getUserEmail(), recordType);
    }

    public Version saveForUser(Version version, String dataSet){
        Version newVersion = new Version();
        Version.VersionPk newVersionID = version.getId();
        newVersionID.setDataSet(dataSet);
        newVersion.setId(newVersionID);

        return versionRepository.save(newVersion);
    }

    public Boolean editVersionId(String dataSet, String old_versionId, String new_versionId) {
        System.out.print("edit Logic\n\n\n\n");
        editVersionRecordIfExist(dataSet, "AuthorRecord", old_versionId, new_versionId);
        editVersionRecordIfExist(dataSet, "ReviewRecord", old_versionId, new_versionId);
        editVersionRecordIfExist(dataSet, "SubmissionRecord", old_versionId, new_versionId);
        return true;
    }

    public Boolean editVersionRecordIfExist(String dataSet, String recordType, String old_versionId, String new_versionId) {
        Version.VersionPk new_versionPk = new Version.VersionPk();
        new_versionPk.setVersionId(new_versionId);
        new_versionPk.setDataSet(dataSet);
        new_versionPk.setRecordType(recordType);
        if (versionRepository.existsById(new_versionPk)) {
            System.out.print("new_versionId already exists. \n\n\n\n");
            return false;
        } 
        
        Version.VersionPk old_versionPk = new Version.VersionPk();
        old_versionPk.setVersionId(old_versionId);
        old_versionPk.setDataSet(dataSet);
        old_versionPk.setRecordType(recordType);
        Version versionToEdit;
        
        try {
            versionToEdit = versionRepository.getOne(old_versionPk);
        } catch(Exception e) {
            System.out.print("could not get old_versionPk. \n\n\n\n");
            return false;
        }

        versionToEdit.setId(new_versionPk);
        return true;
    }

    public Boolean deleteVersion(String dataSet, String versionId) {
        deleteVersionRecordIfExist(dataSet, "AuthorRecord", versionId);
        deleteVersionRecordIfExist(dataSet, "ReviewRecord", versionId);
        deleteVersionRecordIfExist(dataSet, "SubmissionRecord", versionId);
        return true;
    }

    public void deleteVersionRecordIfExist(String dataSet, String recordType, String versionId) {
        Version.VersionPk versionPk = new Version.VersionPk();
        versionPk.setVersionId(versionId);
        versionPk.setDataSet(dataSet);
        versionPk.setRecordType(recordType);
        if (versionRepository.existsById(versionPk)) {
            versionRepository.deleteById(versionPk);
        }
    }

    public Boolean deleteVersionRecord(String dataSet, String recordType, String versionId) {
        Version.VersionPk versionPk = new Version.VersionPk();
        versionPk.setVersionId(versionId);
        versionPk.setDataSet(dataSet);
        versionPk.setRecordType(recordType);
        versionRepository.deleteById(versionPk);
        return true;
    }
}
