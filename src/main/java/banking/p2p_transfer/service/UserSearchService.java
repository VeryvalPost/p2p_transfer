package banking.p2p_transfer.service;

import banking.p2p_transfer.dto.SearchRequestDTO;
import banking.p2p_transfer.model.User;

import java.util.List;
import java.util.Optional;

public interface UserSearchService {
    List<User> searchUser (SearchRequestDTO searchRequest);
}
