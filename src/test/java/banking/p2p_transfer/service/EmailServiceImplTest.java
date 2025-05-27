package banking.p2p_transfer.service;

import banking.p2p_transfer.dto.EmailDTO;
import banking.p2p_transfer.model.Email;
import banking.p2p_transfer.model.User;
import banking.p2p_transfer.repository.EmailRepository;
import banking.p2p_transfer.repository.UserRepository;
import banking.p2p_transfer.service.implementation.EmailServiceImpl;
import banking.p2p_transfer.util.EmailMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private EmailRepository emailRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailMapper emailMapper;

    @InjectMocks
    private EmailServiceImpl emailService;

    private User user;
    private EmailDTO emailDTO;
    private Email emailEntity;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);

        user.setEmails(new ArrayList<>());

        emailDTO = new EmailDTO();
        emailDTO.setEmail("test@test.com");
        emailDTO.setId(10L);

        emailEntity = new Email();
        emailEntity.setEmail("test@test.com");
    }

    @Test
    void createEmailForUser_success() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(emailRepository.existsByEmail(emailDTO.getEmail())).thenReturn(false);
        when(emailMapper.toEntity(emailDTO)).thenReturn(emailEntity);

        doAnswer(invocation -> {
            Email saved = invocation.getArgument(0);
            saved.setId(100L);
            return saved;
        }).when(emailRepository).save(any(Email.class));

        Long resultId = emailService.createEmailForUser(1L, emailDTO);
        assertNotNull(resultId);
        assertEquals(100L, resultId);
    }

    @Test
    void updateEmailForUser_success() {

        Email existingEmail = new Email();
        existingEmail.setId(20L);
        existingEmail.setEmail("old@example.com");
        existingEmail.setUser(user);

        user.setEmails(Collections.singletonList(existingEmail));

        EmailDTO newEmailDTO = new EmailDTO();
        newEmailDTO.setEmail("new@example.com");
        newEmailDTO.setId(20L);

        Email updatedEmail = new Email();
        updatedEmail.setEmail("new@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(emailRepository.findById(20L)).thenReturn(Optional.of(existingEmail));
        when(emailMapper.toEntity(newEmailDTO)).thenReturn(updatedEmail);
        when(emailRepository.existsByEmail(newEmailDTO.getEmail())).thenReturn(false);
        when(emailRepository.save(existingEmail)).thenReturn(existingEmail);

        String returnedEmail = emailService.updateEmailForUser(1L, 20L, newEmailDTO);
        assertEquals("new@example.com", returnedEmail);
        verify(emailRepository, times(1)).save(existingEmail);
    }

    @Test
    void deleteEmailForUser_success() {

        Email emailToDelete = new Email();
        emailToDelete.setId(30L);
        emailToDelete.setEmail("delete@example.com");
        emailToDelete.setUser(user);


        user.setEmails(new ArrayList<>());
        user.getEmails().add(emailToDelete);
        user.getEmails().add(new Email());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(emailRepository.findById(30L)).thenReturn(Optional.of(emailToDelete));

        assertDoesNotThrow(() -> emailService.deleteEmailForUser(1L, 30L));
        verify(emailRepository, times(1)).delete(emailToDelete);
    }
}
