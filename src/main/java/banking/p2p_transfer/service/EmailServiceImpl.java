package banking.p2p_transfer.service;

import banking.p2p_transfer.dto.EmailDTO;
import banking.p2p_transfer.exception.DatabaseException;
import banking.p2p_transfer.exception.UserNotFoundException;
import banking.p2p_transfer.exception.ValidationException;
import banking.p2p_transfer.model.Email;
import banking.p2p_transfer.model.User;
import banking.p2p_transfer.repository.EmailRepository;
import banking.p2p_transfer.repository.UserRepository;
import banking.p2p_transfer.util.EmailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {
    private final EmailMapper emailMapper;
    private final EmailRepository emailRepository;
    private final UserRepository userRepository;

    public EmailServiceImpl(EmailRepository emailRepository, UserRepository userRepository, EmailMapper emailMapper) {
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
        this.emailMapper = emailMapper;
    }

    @Override
    @Transactional
    public Long createEmailForUser(Long userId, EmailDTO emailDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        try {
            if (validateNewEmail(emailDTO)) {
                Email newEmail = emailMapper.toEntity(emailDTO);
                newEmail.setUser(user);
                emailRepository.save(newEmail);

                return newEmail.getId();
            }
        } catch (ValidationException e) {
            throw new ValidationException("Некорректный email: " + e.getMessage());
        } catch (DatabaseException e) {
            throw new DatabaseException("Ошибка базы данных: ", e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании email: " + e.getMessage());
        }

        return emailDTO.getId();
    }

    @Override
    @Transactional
    public String updateEmailForUser(Long userId, Long emailId, EmailDTO newEmailDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Email existingEmail = emailRepository.findById(emailId).orElseThrow(() -> new ValidationException("Email не найден"));
        Email updatedEmail = emailMapper.toEntity(newEmailDTO);

        try {
           if (validateExistingEmail(user, existingEmail, updatedEmail)) {
               existingEmail.setEmail(updatedEmail.getEmail());
               emailRepository.save(existingEmail);
               return existingEmail.getEmail();
           }

        } catch (ValidationException e) {
            throw new ValidationException("Некорректный email: " + e.getMessage());
        } catch (DatabaseException e) {
            throw new DatabaseException("Ошибка базы данных: ", e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении email: " + e.getMessage());
        }
        return newEmailDTO.getEmail();
    }

    @Override
    @Transactional
    public void deleteEmailForUser(Long userId, Long emailId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        Email deletingEmail = emailRepository.findById(emailId)
                .orElseThrow(() -> new ValidationException("Email не найден"));

        if (!deletingEmail.getUser().getId().equals(userId)) {
            throw new ValidationException("Email не принадлежит текущему пользователю");
        }

        try {
            if (user.getEmails().size()>1){
                emailRepository.delete(deletingEmail);

            } else throw new ValidationException("Нельзя удалить последний email");
        } catch (Exception e) {
            throw new DatabaseException("Ошибка при удалении email: ", e);
        }
    }


    private boolean validateNewEmail(EmailDTO emailDTO) {
        if (emailDTO.getEmail() == null || emailDTO.getEmail().isEmpty()) {
            throw new ValidationException("Email не может быть пустым");
        }
        if (emailRepository.existsByEmail(emailDTO.getEmail())) {
            throw new ValidationException("Email уже используется");
        }
        return true;
    }


    private boolean validateExistingEmail(User user, Email existingEmail, Email updatedEmail) {

        if (!existingEmail.getUser().getId().equals(user.getId())) {
            throw new ValidationException("Email не принадлежит текущему пользователю");
        }
        if (!existingEmail.getEmail().equals(updatedEmail.getEmail()) &&
                emailRepository.existsByEmail(updatedEmail.getEmail())) {
            throw new ValidationException("Email уже используется");
        }
        return true;
    }

}