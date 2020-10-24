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

    public Version saveForUser(Version version, UserInfo userInfo){
        Version newVersion = new Version();
        Version.VersionPk newVersionID = version.getId();
        newVersionID.setDataSet(userInfo.getUserEmail());
        newVersion.setId(newVersionID);

        return versionRepository.save(newVersion);
    }

    public Version saveForUser(String versionId, String dataSet) {
        Version newVersion = new Version();
        Version.VersionPk newVersionPk = new Version.VersionPk();
        newVersionPk.setVersionId(versionId);
        newVersionPk.setDataSet(dataSet);
        newVersion.setId(newVersionPk);

        return versionRepository.save(newVersion);
    }

    public Boolean editVersionId(String dataSet, String old_versionId, String new_versionId) {
        Version.VersionPk new_versionPk = new Version.VersionPk();
        new_versionPk.setVersionId(new_versionId);
        new_versionPk.setDataSet(dataSet);
        if (versionRepository.existsById(new_versionPk)) {
            return false;
        } 
        
        Version.VersionPk old_versionPk = new Version.VersionPk();
        old_versionPk.setVersionId(old_versionId);
        old_versionPk.setDataSet(dataSet);
        Version versionToEdit;
        
        try {
            versionToEdit = versionRepository.getOne(old_versionPk);
        } catch(Exception e) {
            return false;
        }

        versionToEdit.setId(new_versionPk);
        return true;
    }

    public Boolean deleteVersion(String dataSet, String versionId) {
        Version.VersionPk versionPk = new Version.VersionPk();
        versionPk.setVersionId(versionId);
        versionPk.setDataSet(dataSet);
        versionRepository.deleteById(versionPk);
        return true;
    }
}
