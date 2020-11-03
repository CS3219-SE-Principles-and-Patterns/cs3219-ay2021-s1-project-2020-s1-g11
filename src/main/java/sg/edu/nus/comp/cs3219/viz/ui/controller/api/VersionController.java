package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Version;
import sg.edu.nus.comp.cs3219.viz.logic.GateKeeper;
import sg.edu.nus.comp.cs3219.viz.logic.VersionLogic;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class VersionController extends BaseRestController{

    private GateKeeper gateKeeper;
    private VersionLogic versionLogic;

    public VersionController(GateKeeper gateKeeper, VersionLogic versionLogic){
        this.gateKeeper = gateKeeper;
        this.versionLogic = versionLogic;
    }

    @GetMapping("/version")
    public List<Version> all(){
        UserInfo currentUser = gateKeeper.verifyLoginAccess();
        return versionLogic.findAllForUser(currentUser);
    }

    @GetMapping("/version/{recordType}")
    public List<Version> allVersionByRecordType(@PathVariable String recordType){
        UserInfo currentUser = gateKeeper.verifyLoginAccess();
        return versionLogic.findAllForUserWithRecordType(currentUser, recordType);
    }

    /**
     * Create new record
     * @param version
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/version")
    public ResponseEntity<?> newVersion(@RequestBody Version version) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        Version newVersion = this.versionLogic.saveForUser(version, userInfo.getUserEmail());

        return ResponseEntity.created(new URI("/version/" + newVersion.getId())).build();
    }

    /**
     * Update versionId with another versionId.
     * @param old_versionId, version_id
     * @return ResponseEntity<?>
     * @throws URISyntaxException
     */
    @PutMapping("/version/{old_versionId}")
    public ResponseEntity<?> editVersionId(@PathVariable String old_versionId, @RequestBody String new_versionId) throws URISyntaxException {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        Boolean editSuccess = this.versionLogic.editVersionId(userInfo.getUserEmail(), old_versionId, new_versionId);
        
        if (editSuccess) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/version/{versionId}")
    public ResponseEntity<Void> deleteVersion(@PathVariable String versionId) {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        Boolean deleteSuccess = this.versionLogic.deleteVersion(userInfo.getUserEmail(), versionId);

        if (deleteSuccess) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/version/{recordType}/{versionId}")
    public ResponseEntity<Void> deleteVersionRecord(@PathVariable String recordType, @PathVariable String versionId) {
        UserInfo userInfo = gateKeeper.verifyLoginAccess();

        Boolean deleteSuccess = this.versionLogic.deleteVersionRecord(userInfo.getUserEmail(), recordType, versionId);

        if (deleteSuccess) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
