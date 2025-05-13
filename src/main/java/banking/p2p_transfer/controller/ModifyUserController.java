package banking.p2p_transfer.controller;

import banking.p2p_transfer.dto.AccountDTO;
import banking.p2p_transfer.dto.EmailDTO;
import banking.p2p_transfer.dto.PhoneDTO;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class ModifyUserController {

    @PostMapping("{userId}/createEmail")
    public ResponseEntity<?> createEmail(@Valid @RequestBody EmailDTO emailDTO, @RequestParam("userId") Long id) {
        return ResponseEntity.ok().body("Email created successfully");
    }

    @PostMapping("{userId}/createPhone")
    public ResponseEntity<?> createPhone(@Valid @RequestBody PhoneDTO phoneDTO, @RequestParam("userId") Long id) {
        return ResponseEntity.ok().body("Email created successfully");
    }

    @PutMapping("{userId}/updateEmail")
    public ResponseEntity<?> updateEmail(@Valid @RequestBody EmailDTO emailDTO, @RequestParam("emailId") Long emailId, @RequestParam("userId") Long id) {
        return ResponseEntity.ok().body("Email updated successfully");
    }
    @PutMapping("{userId}/updatePhone")
    public ResponseEntity<?> updatePhone(@Valid @RequestBody PhoneDTO phoneDTO, @RequestParam("phoneId") Long phoneId, @RequestParam("userId") Long id) {
        return ResponseEntity.ok().body("Email updated successfully");
    }
    @DeleteMapping("{userId}/deleteEmail/{emailId}")
    public ResponseEntity<?> deleteEmail(@RequestParam("userId") Long userId, @RequestParam("emailId") Long emailId) {
        return ResponseEntity.ok().body("Email deleted successfully");
    }
    @DeleteMapping("{userId}/deletePhone/{phoneId}")
    public ResponseEntity<?> deletePhone(@RequestParam("userId") Long userId, @RequestParam("phoneId") Long phoneId) {
        return ResponseEntity.ok().body("Email deleted successfully");
    }

}
