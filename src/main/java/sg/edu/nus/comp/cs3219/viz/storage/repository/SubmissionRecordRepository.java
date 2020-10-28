package sg.edu.nus.comp.cs3219.viz.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.SubmissionRecord;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Version;

import java.util.List;

@Repository
public interface SubmissionRecordRepository extends JpaRepository<SubmissionRecord, Long> {

    //List<SubmissionRecord> findByDataSetEquals(String dataSet);
    List<SubmissionRecord> findByVersionEquals(Version version);

    //void deleteAllByDataSetEquals(String dataSet);
    void deleteAllByVersionEquals(Version version);
}
