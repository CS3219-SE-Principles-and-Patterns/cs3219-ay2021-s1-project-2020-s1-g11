package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.AuthorRecord;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.ReviewRecord;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.SubmissionRecord;
import sg.edu.nus.comp.cs3219.viz.logic.GateKeeper;
import sg.edu.nus.comp.cs3219.viz.logic.RecordLogic;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class RecordController extends BaseRestController {

    private GateKeeper gateKeeper;

    private RecordLogic recordLogic;

    public RecordController(GateKeeper gateKeeper, RecordLogic recordLogic) {
        this.gateKeeper = gateKeeper;
        this.recordLogic = recordLogic;
    }

    @PostMapping("/record/author")
    public ResponseEntity<?> importAuthorRecord(@RequestBody List<AuthorRecord> authorRecordList) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        this.recordLogic.removeAndPersistAuthorRecordForDataSet(userInfo.getUserEmail(), authorRecordList);

        return ResponseEntity.created(new URI("/record/author")).build();
    }

    @PostMapping("/record/review")
    public ResponseEntity<?> importReviewRecord(@RequestBody List<ReviewRecord> reviewRecordList) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        this.recordLogic.removeAndPersistReviewRecordForDataSet(userInfo.getUserEmail(), reviewRecordList);

        return ResponseEntity.created(new URI("/record/review")).build();
    }

    @PostMapping("/record/submission")
    public ResponseEntity<?> importSubmissionRecord(@RequestBody List<SubmissionRecord> submissionRecords) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        this.recordLogic.removeAndPersistSubmissionRecordForDataSet(userInfo.getUserEmail(), submissionRecords);

        return ResponseEntity.created(new URI("/record/review")).build();
    }

    /**
     * Deletes AuthorRecord for a given versionId for the current user.
     * @param versionId
     * @throws URISyntaxException
     */
    @DeleteMapping("/record/author/{versionId}")
    public ResponseEntity<?> deleteAuthorRecord(@PathVariable String versionId) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        this.recordLogic.deleteAuthorRecordForDataSet(userInfo.getUserEmail(), versionId);

        return ResponseEntity.ok().build();
    }

    /**
     * Deletes ReviewRecord of a given versionId for the current user.
     * @param versionId
     * @throws URISyntaxException
     */
    @DeleteMapping("/record/review/{versionId}")
    public ResponseEntity<?> deleteReviewRecord(@PathVariable String versionId) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        this.recordLogic.deleteReviewRecordForDataSet(userInfo.getUserEmail(), versionId);

        return ResponseEntity.ok().build();
    }

    /**
     * Deletes SubmissionRecord of a given versionId for the current user.
     * @param versionId
     * @throws URISyntaxException
     */
    @DeleteMapping("/record/submission/{versionId}")
    public ResponseEntity<?> deleteSubmissionRecord(@PathVariable String versionId) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        this.recordLogic.deleteSubmissionRecordForDataSet(userInfo.getUserEmail(), versionId);

        return ResponseEntity.ok().build();
    }

    /**
     * Deletes all records of a given versionId for the current user.
     * @param versionId
     * @throws URISyntaxException
     */
    @DeleteMapping("/record/{versionId}")
    public ResponseEntity<?> deleteAllRecords(@PathVariable String versionId) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        this.recordLogic.deleteAuthorRecordForDataSet(userInfo.getUserEmail(), versionId);
        this.recordLogic.deleteReviewRecordForDataSet(userInfo.getUserEmail(), versionId);
        this.recordLogic.deleteSubmissionRecordForDataSet(userInfo.getUserEmail(), versionId);

        return ResponseEntity.ok().build();
    }    

    /**
     * Changes all records with a given versionId to another given versionId for the current user.
     * @param old_versionId
     * @param new_versionId
     * @return ResponseEntity<?>
     * @throws URISyntaxException
     */
    @PutMapping("/record/{old_versionId}")
    public ResponseEntity<?> editVersionId(@PathVariable String old_versionId, @RequestBody String new_versionId) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();
        
        this.recordLogic.editVersionId(userInfo.getUserEmail(), old_versionId, new_versionId);

        return ResponseEntity.ok().build();
    }

    /**
     * Gets the AuthorRecord of a given version for the current user.
     * @return ResponseEntity<?>
     * @throws URISyntaxException
     */
    @GetMapping("/record/author/{versionId}")
    public ResponseEntity<?> getAuthorRecords(@PathVariable String versionId) {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        List<AuthorRecord> authorRecord = this.recordLogic.getAuthorRecordForDataSet(userInfo.getUserEmail(), versionId);

        return ResponseEntity.ok().body(authorRecord);
    }

    /**
     * Gets the ReviewRecord of a given version for the current user.
     * @return ResponseEntity<?>
     * @throws URISyntaxException
     */
    @GetMapping("/record/review/{versionId}")
    public ResponseEntity<?> getReviewRecords(@PathVariable String versionId) {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        List<ReviewRecord> reviewRecord = this.recordLogic.getReviewRecordForDataSet(userInfo.getUserEmail(), versionId);

        return ResponseEntity.ok().body(reviewRecord);
    }

    /**
     * Gets the SubmissionRecord of a given version for the current user.
     * @return ResponseEntity<?>
     * @throws URISyntaxException
     */
    @GetMapping("/record/submission/{versionId}")
    public ResponseEntity<?> getSubmissionRecord(@PathVariable String versionId) {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        List<SubmissionRecord> submissionRecord = this.recordLogic.getSubmissionRecordForDataSet(userInfo.getUserEmail(), versionId);

        return ResponseEntity.ok().body(submissionRecord);
    }
}
