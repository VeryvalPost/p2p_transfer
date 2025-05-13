package banking.p2p_transfer.service;

import banking.p2p_transfer.dto.PhoneDTO;

public interface PhoneService {
    Long createPhoneForUser(Long userId, PhoneDTO phoneDTO);
    String updatePhoneForUser(Long userId, Long phoneId, PhoneDTO phoneDTO);
    void deletePhoneForUser(Long userId, Long phoneId);
}