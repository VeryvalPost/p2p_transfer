package banking.p2p_transfer.util;

import banking.p2p_transfer.dto.PhoneDTO;
import banking.p2p_transfer.model.Phone;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhoneMapper {
    Phone toEntity(PhoneDTO dto);
    PhoneDTO toDto(Phone phone);
}
