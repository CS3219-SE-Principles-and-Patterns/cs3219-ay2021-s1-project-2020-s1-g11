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
        UserInfo currentUser = gateKeeper.verifyLoginAccess();
        Version newVersion = versionLogic.saveForUser(version, currentUser);

        return ResponseEntity
                // TODO: might change what URI is returned
                .created(new URI("/version/" + newVersion.getId()))
                .body(newVersion);
    }

    /**
     * Update version
     * @param versionId
     * @param recordType
     * @param version
     * @return
     * @throws URISyntaxException
     */
    // Keep file but change ID.
    @PutMapping("/version/{versionId}/{recordType}")
    public ResponseEntity<?> updateVersion(
            @PathVariable String versionId, @RequestBody Version version)
            throws URISyntaxException {
        // TODO: Request body is new entity, params represent old entity (to update)
        return ResponseEntity
                .created(new URI("/version/" + version.getId()))
                .body(version);
    }

    @DeleteMapping("/version/{versionId}/{recordType}")
    public ResponseEntity<Void> deleteVersion(@PathVariable String versionId, @PathVariable String recordType) {
        // TODO: Implement deletion
        return ResponseEntity
                .ok()
                .body(null);
    }
}
