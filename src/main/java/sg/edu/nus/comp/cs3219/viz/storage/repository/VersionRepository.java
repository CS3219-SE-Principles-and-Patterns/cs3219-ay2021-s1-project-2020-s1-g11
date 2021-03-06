package sg.edu.nus.comp.cs3219.viz.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Version;

import java.util.List;

@Repository
public interface VersionRepository extends JpaRepository<Version, Version.VersionPk> {

    List<Version> findById_DataSetEquals(String dataSet);

    List<Version> findById_DataSetAndId_RecordType(String dataSet, String recordType);

    void deleteAllById_DataSetEquals(String dataSet);
}
