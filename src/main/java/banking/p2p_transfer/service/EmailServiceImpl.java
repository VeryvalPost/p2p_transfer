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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
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
    @CacheEvict(value = {"userEmails", "emailById", "emailExists", "userIdByEmail"}, allEntries = true)
    public Long createEmailForUser(Long userId, EmailDTO emailDTO) {
        log.info("Начало создания email для пользователя с ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", userId);
                    return new UserNotFoundException("Пользователь не найден");
                });
        try {
            if (validateNewEmail(emailDTO)) {
                Email newEmail = emailMapper.toEntity(emailDTO);
                newEmail.setUser(user);
                emailRepository.save(newEmail);
                log.info("Email {} успешно создан для пользователя с ID: {}", newEmail.getEmail(), userId);
                return newEmail.getId();
            }
        } catch (ValidationException e) {
            log.error("Ошибка валидации email {}: {}", emailDTO.getEmail(), e.getMessage());
            throw new ValidationException("Некорректный email: " + e.getMessage());
        } catch (DatabaseException e) {
            log.error("Ошибка базы данных при создании email {}: {}", emailDTO.getEmail(), e.getMessage());
            throw new DatabaseException("Ошибка базы данных: ", e);
        } catch (Exception e) {
            log.error("Ошибка при создании email {}: {}", emailDTO.getEmail(), e.getMessage());
            throw new RuntimeException("Ошибка при создании email: " + e.getMessage());
        }
        log.info("Завершение метода createEmailForUser без изменений, возвращаем исходный ID");
        return emailDTO.getId();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"userEmails", "emailById", "emailExists", "userIdByEmail"}, allEntries = true)
    public String updateEmailForUser(Long userId, Long emailId, EmailDTO newEmailDTO) {
        log.info("Начало обновления email с ID: {} для пользователя с ID: {}", emailId, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", userId);
                    return new UserNotFoundException("Пользователь не найден");
                });
        Email existingEmail = emailRepository.findById(emailId)
                .orElseThrow(() -> {
                    log.error("Email с ID {} не найден", emailId);
                    return new ValidationException("Email не найден");
                });
        Email updatedEmail = emailMapper.toEntity(newEmailDTO);
        try {
            if (validateExistingEmail(user, existingEmail, updatedEmail)) {
                existingEmail.setEmail(updatedEmail.getEmail());
                emailRepository.save(existingEmail);
                log.info("Email с ID {} успешно обновлён для пользователя с ID: {}", emailId, userId);
                return existingEmail.getEmail();
            }
        } catch (ValidationException e) {
            log.error("Ошибка валидации обновления email с ID {}: {}", emailId, e.getMessage());
            throw new ValidationException("Некорректный email: " + e.getMessage());
        } catch (DatabaseException e) {
            log.error("Ошибка базы данных при обновлении email с ID {}: {}", emailId, e.getMessage());
            throw new DatabaseException("Ошибка базы данных: ", e);
        } catch (Exception e) {
            log.error("Ошибка при обновлении email с ID {}: {}", emailId, e.getMessage());
            throw new RuntimeException("Ошибка при обновлении email: " + e.getMessage());
        }
        log.info("Завершение метода updateEmailForUser без изменений, возвращаем исходный email");
        return newEmailDTO.getEmail();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"userEmails", "emailById", "emailExists", "userIdByEmail"}, allEntries = true)
    public void deleteEmailForUser(Long userId, Long emailId) {
        log.info("Начало удаления email с ID: {} для пользователя с ID: {}", emailId, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", userId);
                    return new UserNotFoundException("Пользователь не найден");
                });
        Email deletingEmail = emailRepository.findById(emailId)
                .orElseThrow(() -> {
                    log.error("Email с ID {} не найден", emailId);
                    return new ValidationException("Email не найден");
                });
        if (!deletingEmail.getUser().getId().equals(userId)) {
            log.error("Email с ID {} не принадлежит пользователю с ID {}", emailId, userId);
            throw new ValidationException("Email не принадлежит текущему пользователю");
        }
        try {
            if (deletingEmail.getUser().getEmails().size() > 1) {
                emailRepository.delete(deletingEmail);
                log.info("Email с ID {} успешно удалён для пользователя с ID: {}", emailId, userId);
            } else {
                log.error("Нельзя удалить последний email для пользователя с ID {}", userId);
                throw new ValidationException("Нельзя удалить последний email");
            }
        } catch (Exception e) {
            log.error("Ошибка при удалении email с ID {}: {}", emailId, e.getMessage());
            throw new DatabaseException("Ошибка при удалении email: ", e);
        }
    }

    private boolean validateNewEmail(EmailDTO emailDTO) {
        if (emailDTO.getEmail() == null || emailDTO.getEmail().isEmpty()) {
            log.error("Валидация провалилась: email не может быть пустым");
            throw new ValidationException("Email не может быть пустым");
        }
        if (emailRepository.existsByEmail(emailDTO.getEmail())) {
            log.error("Валидация провалилась: email {} уже используется", emailDTO.getEmail());
            throw new ValidationException("Email уже используется");
        }
        return true;
    }

    private boolean validateExistingEmail(User user, Email existingEmail, Email updatedEmail) {
        if (!existingEmail.getUser().getId().equals(user.getId())) {
            log.error("Валидация провалилась: email не принадлежит пользователю с ID {}", user.getId());
            throw new ValidationException("Email не принадлежит текущему пользователю");
        }
        if (!existingEmail.getEmail().equals(updatedEmail.getEmail()) &&
                emailRepository.existsByEmail(updatedEmail.getEmail())) {
            log.error("Валидация провалилась: email {} уже используется", updatedEmail.getEmail());
            throw new ValidationException("Email уже используется");
        }
        return true;
    }
}