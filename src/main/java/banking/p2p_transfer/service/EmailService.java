package banking.p2p_transfer.service;

import banking.p2p_transfer.dto.EmailDTO;

public interface EmailService {

    public Long createEmailForUser(Long userId, EmailDTO emailDTO);
    String updateEmailForUser(Long userId, Long emailId, EmailDTO newEmailDTO);
    void deleteEmailForUser(Long userId, Long emailId);
}
