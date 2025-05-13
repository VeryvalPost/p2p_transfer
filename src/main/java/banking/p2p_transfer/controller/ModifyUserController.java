package banking.p2p_transfer.controller;

import banking.p2p_transfer.dto.AccountDTO;
import banking.p2p_transfer.dto.EmailDTO;
import banking.p2p_transfer.dto.PhoneDTO;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class ModifyUserController {

    @PostMapping("{id}/createEmail")
    public ResponseEntity<?> createEmail(@RequestBody EmailDTO emailDTO, @RequestParam("id") Long id) {
        return ResponseEntity.ok().body("Email created successfully");
    }

    @PostMapping("{id}/createPhone")
    public ResponseEntity<?> createPhone(@RequestBody PhoneDTO phoneDTO, @RequestParam("id") Long id) {
        return ResponseEntity.ok().body("Email created successfully");
    }

}
