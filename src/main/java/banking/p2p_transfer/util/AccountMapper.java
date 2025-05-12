package banking.p2p_transfer.util;

import banking.p2p_transfer.dto.AccountDTO;
import banking.p2p_transfer.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toEntity(AccountDTO dto);
    AccountDTO toDto(Account account);
}