package sg.edu.nus.comp.cs3219.viz.logic;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.*;
import sg.edu.nus.comp.cs3219.viz.storage.repository.AuthorRecordRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.ReviewRecordRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.SubmissionAuthorRecordRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.SubmissionRecordRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Component
public class RecordLogic {
    private AuthorRecordRepository authorRecordRepository;

    private SubmissionRecordRepository submissionRecordRepository;

    private SubmissionAuthorRecordRepository submissionAuthorRecordRepository;

    private ReviewRecordRepository reviewRecordRepository;

    public RecordLogic(AuthorRecordRepository authorRecordRepository,
                       SubmissionRecordRepository submissionRecordRepository,
                       SubmissionAuthorRecordRepository submissionAuthorRecordRepository,
                       ReviewRecordRepository reviewRecordRepository) {
        this.authorRecordRepository = authorRecordRepository;
        this.submissionRecordRepository = submissionRecordRepository;
        this.submissionAuthorRecordRepository = submissionAuthorRecordRepository;
        this.reviewRecordRepository = reviewRecordRepository;
    }

    @Transactional
    public void removeAndPersistAuthorRecordForDataSet(String dataSet, List<AuthorRecord> authorRecordList) {
        if (authorRecordList.isEmpty()){
            return;
        }
        //authorRecordRepository.deleteAllByDataSetEquals(dataSet);
        // remove author entries of indicated version. This should effectively be 'updating' of author entries
        // It is of reasonable assumption that all records within the same upload are of the same version
        AuthorRecord temp = authorRecordList.get(0);
        Version v = new Version(new Version.VersionPk(dataSet, "AuthorRecord", temp.getVersion().getId().getVersionId()));
        authorRecordRepository.deleteAllByVersionEquals(v);

        authorRecordRepository.saveAll(authorRecordList.stream().peek(r -> {
            // should not set ID when creating records
            r.setId(null);
            // should set dataSet
            r.setVersion(v);
            //r.setDataSet(dataSet);
            // the other field can be arbitrary
        }).collect(Collectors.toList()));
    }

    @Transactional
    public void removeAndPersistReviewRecordForDataSet(String dataSet, List<ReviewRecord> reviewRecordList) {
        //reviewRecordRepository.deleteAllByDataSetEquals(dataSet);
        if (reviewRecordList.isEmpty()){
            return;
        }
        ReviewRecord temp = reviewRecordList.get(0);
        Version v = new Version(new Version.VersionPk(dataSet, "ReviewRecord", temp.getVersion().getId().getVersionId()));
        reviewRecordRepository.deleteAllByVersionEquals(v);

        reviewRecordRepository.saveAll(reviewRecordList.stream().peek(r -> {
            // should not set ID when creating records
            r.setId(null);
            // should set dataSet
            r.setVersion(v);
            //r.setDataSet(dataSet);
            // the other field can be arbitrary
        }).collect(Collectors.toList()));
    }

    @Transactional
    public void removeAndPersistSubmissionRecordForDataSet(String dataSet, List<SubmissionRecord> submissionRecordList) {
        //submissionRecordRepository.deleteAllByDataSetEquals(dataSet);
        //submissionAuthorRecordRepository.deleteAllByDataSetEquals(dataSet);
        if (submissionRecordList.isEmpty()){
            return;
        }
        SubmissionRecord temp = submissionRecordList.get(0);
        System.out.println(temp.getVersion().getId().getVersionId());
        Version v = new Version(new Version.VersionPk(dataSet, "SubmissionRecord", temp.getVersion().getId().getVersionId()));
        submissionRecordRepository.deleteAllByVersionEquals(v);

        submissionRecordRepository.saveAll(submissionRecordList.stream().peek(s -> {
            // should not set ID when creating records
            s.setId(null);
            // should set dataSet
            s.setVersion(v);
            //s.setDataSet(dataSet);
            // create many to many relationship for authors
            List<SubmissionAuthorRecord> submissionAuthorRecords = s.getAuthors().stream()
                    .map(authorName -> {
                        SubmissionAuthorRecord existing = submissionAuthorRecordRepository.findFirstByNameEqualsAndDataSetEquals(authorName, dataSet);
                        if (existing == null) {
                            existing = new SubmissionAuthorRecord();
                            existing.setDataSet(dataSet);
                            existing.setName(authorName);
                            existing = submissionAuthorRecordRepository.save(existing);
                        }
                        return existing;
                    })
                    .collect(Collectors.toList());
            s.setAuthorSet(submissionAuthorRecords);
            // the other field can be arbitrary
        }).collect(Collectors.toList()));
    }

    @Transactional
    public void deleteAuthorRecordForDataSet(String dataSet, String versionId) {
        Version versionToDelete = new Version(new Version.VersionPk(dataSet, "AuthorRecord", versionId));
        authorRecordRepository.deleteAllByVersionEquals(versionToDelete);
    }

    @Transactional
    public void deleteReviewRecordForDataSet(String dataSet, String versionId) {
        Version versionToDelete = new Version(new Version.VersionPk(dataSet, "ReviewRecord", versionId));
        reviewRecordRepository.deleteAllByVersionEquals(versionToDelete);
    }

    @Transactional
    public void deleteSubmissionRecordForDataSet(String dataSet, String versionId) {
        Version versionToDelete = new Version(new Version.VersionPk(dataSet, "SubmissionRecord", versionId));
        submissionRecordRepository.deleteAllByVersionEquals(versionToDelete);
    }

    @Transactional
    public void editVersionId(String dataSet, String old_versionId, String new_versionId) {
        new_versionId = new_versionId.substring(18, new_versionId.length() - 2);

        // Author
        Version oldAuthorVersion = new Version(new Version.VersionPk(dataSet, "AuthorRecord", old_versionId));
        Version newAuthorVersion = new Version(new Version.VersionPk(dataSet, "AuthorRecord", new_versionId));
        List<AuthorRecord> authorRecordList = new ArrayList<AuthorRecord>(authorRecordRepository.findByVersionEquals(oldAuthorVersion));
        for (AuthorRecord authorRecord : authorRecordList) {
            authorRecord.setVersion(newAuthorVersion);
        }

        // Review
        Version oldReviewVersion = new Version(new Version.VersionPk(dataSet, "ReviewRecord", old_versionId));
        Version newReviewVersion = new Version(new Version.VersionPk(dataSet, "ReviewRecord", new_versionId));
        List<ReviewRecord> reviewRecordList = new ArrayList<ReviewRecord>(reviewRecordRepository.findByVersionEquals(oldReviewVersion));
        for (ReviewRecord reviewRecord : reviewRecordList) {
            reviewRecord.setVersion(newReviewVersion);
        }
        
        // Submission
        Version oldSubmissionVersion = new Version(new Version.VersionPk(dataSet, "SubmissionRecord", old_versionId));
        Version newSubmissionVersion = new Version(new Version.VersionPk(dataSet, "SubmissionRecord", new_versionId));
        List<SubmissionRecord> submissionRecordList = new ArrayList<SubmissionRecord>(submissionRecordRepository.findByVersionEquals(oldSubmissionVersion));
        for (SubmissionRecord submissionRecord : submissionRecordList) {
            submissionRecord.setVersion(newSubmissionVersion);
        }
    }

    @Transactional
    public List <AuthorRecord> getAuthorRecordForDataSet(String dataSet, String versionId) {
        Version versionToGet = new Version(new Version.VersionPk(dataSet, "AuthorRecord", versionId));
        List <AuthorRecord> authorRecordList = authorRecordRepository.findByVersionEquals(versionToGet);
        return authorRecordList;
    }

    @Transactional
    public List <ReviewRecord> getReviewRecordForDataSet(String dataSet, String versionId) {
        Version versionToGet = new Version(new Version.VersionPk(dataSet, "ReviewRecord", versionId));
        List <ReviewRecord> reviewRecordList = reviewRecordRepository.findByVersionEquals(versionToGet);
        return reviewRecordList;
    }

    @Transactional
    public List <SubmissionRecord> getSubmissionRecordForDataSet(String dataSet, String versionId) {
        Version versionToGet = new Version(new Version.VersionPk(dataSet, "SubmissionRecord", versionId));
        List <SubmissionRecord> submissionRecordList = submissionRecordRepository.findByVersionEquals(versionToGet);
        return submissionRecordList;
    }
}
