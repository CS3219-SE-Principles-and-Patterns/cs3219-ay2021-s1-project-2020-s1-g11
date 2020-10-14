package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

        return ResponseEntity.created(new URI("/record/submission")).build();
    }

    // TODO: Add CRUD endpoints for each record
    @DeleteMapping("/record/author")
    public ResponseEntity<?> deleteAuthorRecord(@RequestBody String versionId) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        this.recordLogic.deleteAuthorRecord(userInfo.getUserEmail(), versionId);

        return ResponseEntity.created(new URI("/record/author")).build();
    }

    @DeleteMapping("/record/review")
    public ResponseEntity<?> deleteReviewRecord(@RequestBody String versionId) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        this.recordLogic.deleteReviewRecord(userInfo.getUserEmail(), versionId);

        return ResponseEntity.created(new URI("/record/review")).build();
    }

    @DeleteMapping("/record/submission")
    public ResponseEntity<?> deleteSubmissionRecord(@RequestBody String versionId) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        this.recordLogic.deleteSubmissionRecord(userInfo.getUserEmail(), versionId);

        return ResponseEntity.created(new URI("/record/submission")).build();
    }
}
