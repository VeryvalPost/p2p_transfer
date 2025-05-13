package banking.p2p_transfer.util;


import banking.p2p_transfer.dto.UserRequestDTO;
import banking.p2p_transfer.dto.UserResponseDTO;
import banking.p2p_transfer.model.User;
import banking.p2p_transfer.model.Account;
import banking.p2p_transfer.model.Phone;
import banking.p2p_transfer.model.Email;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "account", source = "dto.balance", qualifiedByName = "mapBalanceToAccount")
    @Mapping(target = "phones", ignore = true)
    @Mapping(target = "emails", ignore = true)
    User toEntity(UserRequestDTO dto);

    @Mapping(target = "balance", source = "account.balance")
    @Mapping(target = "phones", source = "phones", qualifiedByName = "mapPhonesToStrings")
    @Mapping(target = "emails", source = "emails", qualifiedByName = "mapEmailsToStrings")
    UserResponseDTO toDto(User user);

    List<UserResponseDTO> toDtoList(List<User> users);

    @Named("mapBalanceToAccount")
    default Account mapBalanceToAccount(BigDecimal balance) {
        Account account = new Account();
        account.setBalance(balance != null ? balance : BigDecimal.ZERO);
        return account;
    }

    @Named("mapPhones")
    default List<Phone> mapPhones(List<String> phoneNumbers, User user) {
        if (phoneNumbers == null) return null;
        return phoneNumbers.stream()
                .map(phone -> {
                    Phone p = new Phone();
                    p.setPhone(phone);
                    p.setUser(user);
                    return p;
                })
                .collect(Collectors.toList());
    }

    @Named("mapEmails")
    default List<Email> mapEmails(List<String> emailAddresses, User user) {
        if (emailAddresses == null) return null;
        return emailAddresses.stream()
                .map(email -> {
                    Email e = new Email();
                    e.setEmail(email);
                    e.setUser(user);
                    return e;
                })
                .collect(Collectors.toList());
    }

    @Named("mapPhonesToStrings")
    default List<String> mapPhonesToStrings(List<Phone> phones) {
        if (phones == null) return null;
        return phones.stream()
                .map(Phone::getPhone)
                .collect(Collectors.toList());
    }

    @Named("mapEmailsToStrings")
    default List<String> mapEmailsToStrings(List<Email> emails) {
        if (emails == null) return null;
        return emails.stream()
                .map(Email::getEmail)
                .collect(Collectors.toList());
    }

    default User toEntityWithRelations(UserRequestDTO dto) {
        User user = toEntity(dto);

        Account account = mapBalanceToAccount(dto.getBalance());
        account.setUser(user);
        user.setAccount(account);

        List<Phone> phones = mapPhones(dto.getPhones(), user);
        user.setPhones(phones);

        List<Email> emails = mapEmails(dto.getEmails(), user);
        user.setEmails(emails);

        return user;
    }
}