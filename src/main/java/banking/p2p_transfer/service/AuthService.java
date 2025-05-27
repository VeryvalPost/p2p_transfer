package banking.p2p_transfer.service;

import banking.p2p_transfer.dto.JwtRequestDTO;
import banking.p2p_transfer.dto.JwtResponseDTO;

public interface AuthService {
    JwtResponseDTO authenticate(JwtRequestDTO request);
}