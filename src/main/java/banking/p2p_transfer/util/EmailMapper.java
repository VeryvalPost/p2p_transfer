package banking.p2p_transfer.util;

import banking.p2p_transfer.dto.EmailDTO;
import banking.p2p_transfer.model.Email;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailMapper {
    Email toEntity(EmailDTO dto);
    EmailDTO toDto(Email email);
}