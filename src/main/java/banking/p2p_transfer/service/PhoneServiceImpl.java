package banking.p2p_transfer.service;

import banking.p2p_transfer.dto.PhoneDTO;
import banking.p2p_transfer.exception.DatabaseException;
import banking.p2p_transfer.exception.UserNotFoundException;
import banking.p2p_transfer.exception.ValidationException;
import banking.p2p_transfer.model.Phone;
import banking.p2p_transfer.model.User;
import banking.p2p_transfer.repository.PhoneRepository;
import banking.p2p_transfer.repository.UserRepository;
import banking.p2p_transfer.util.PhoneMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository phoneRepository;
    private final UserRepository userRepository;
    private final PhoneMapper phoneMapper;

    public PhoneServiceImpl(PhoneRepository phoneRepository, UserRepository userRepository, PhoneMapper phoneMapper) {
        this.phoneRepository = phoneRepository;
        this.userRepository = userRepository;
        this.phoneMapper = phoneMapper;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"userPhones", "phoneById", "phoneExists"}, allEntries = true)
    public Long createPhoneForUser(Long userId, PhoneDTO phoneDTO) {
        log.info("Начало создания телефона для пользователя с ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", userId);
                    return new UserNotFoundException("Пользователь не найден");
                });
        try {
            if (validateNewPhone(phoneDTO)) {
                Phone newPhone = phoneMapper.toEntity(phoneDTO);
                newPhone.setUser(user);
                phoneRepository.save(newPhone);
                log.info("Телефон {} успешно создан для пользователя с ID: {}", newPhone.getPhone(), userId);
                return newPhone.getId();
            }
        } catch (ValidationException e) {
            log.error("Ошибка валидации телефона {}: {}", phoneDTO.getPhone(), e.getMessage());
            throw new ValidationException("Некорректный номер телефона: " + e.getMessage());
        } catch (DatabaseException e) {
            log.error("Ошибка базы данных при создании телефона {}: {}", phoneDTO.getPhone(), e.getMessage());
            throw new DatabaseException("Ошибка базы данных: ", e);
        } catch (Exception e) {
            log.error("Ошибка при создании телефона {}: {}", phoneDTO.getPhone(), e.getMessage());
            throw new RuntimeException("Ошибка при создании телефона: " + e.getMessage());
        }
        log.info("Завершение метода createPhoneForUser без изменений, возвращаем исходный ID");
        return phoneDTO.getId();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"userPhones", "phoneById", "phoneExists"}, allEntries = true)
    public String updatePhoneForUser(Long userId, Long phoneId, PhoneDTO phoneDTO) {
        log.info("Начало обновления телефона с ID: {} для пользователя с ID: {}", phoneId, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", userId);
                    return new UserNotFoundException("Пользователь не найден");
                });
        Phone existingPhone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> {
                    log.error("Телефон с ID {} не найден", phoneId);
                    return new ValidationException("Телефон не найден");
                });
        Phone updatedPhone = phoneMapper.toEntity(phoneDTO);
        try {
            if (validateExistingPhone(user, existingPhone, updatedPhone)) {
                existingPhone.setPhone(updatedPhone.getPhone());
                phoneRepository.save(existingPhone);
                log.info("Телефон с ID {} успешно обновлён для пользователя с ID: {}", phoneId, userId);
                return existingPhone.getPhone();
            }
        } catch (ValidationException e) {
            log.error("Ошибка валидации обновления телефона с ID {}: {}", phoneId, e.getMessage());
            throw new ValidationException("Некорректный номер телефона: " + e.getMessage());
        } catch (DatabaseException e) {
            log.error("Ошибка базы данных при обновлении телефона с ID {}: {}", phoneId, e.getMessage());
            throw new DatabaseException("Ошибка базы данных: ", e);
        } catch (Exception e) {
            log.error("Ошибка при обновлении телефона с ID {}: {}", phoneId, e.getMessage());
            throw new RuntimeException("Ошибка при обновлении телефона: " + e.getMessage());
        }
        log.info("Завершение метода updatePhoneForUser без изменений, возвращаем исходный телефон");
        return phoneDTO.getPhone();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"userPhones", "phoneById", "phoneExists"}, allEntries = true)
    public void deletePhoneForUser(Long userId, Long phoneId) {
        log.info("Начало удаления телефона с ID: {} для пользователя с ID: {}", phoneId, userId);

        Phone deletingPhone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> {
                    log.error("Телефон с ID {} не найден", phoneId);
                    return new ValidationException("Телефон не найден");
                });
        if (!deletingPhone.getUser().getId().equals(userId)) {
            log.error("Телефон с ID {} не принадлежит пользователю с ID {}", phoneId, userId);
            throw new ValidationException("Телефон не принадлежит текущему пользователю");
        }
        try {
            if (deletingPhone.getUser().getPhones().size() > 1) {
                phoneRepository.deleteById(phoneId);
                log.info("Телефон с ID {} успешно удалён для пользователя с ID: {}", phoneId, userId);
            } else {
                log.error("Нельзя удалить последний телефон для пользователя с ID {}", userId);
                throw new ValidationException("Нельзя удалить последний телефон");
            }
        } catch (Exception e) {
            log.error("Ошибка при удалении телефона с ID {}: {}", phoneId, e.getMessage());
            throw new DatabaseException("Ошибка при удалении телефона: ", e);
        }
    }

    private boolean validateNewPhone(PhoneDTO phoneDTO) {
        if (phoneDTO.getPhone() == null || phoneDTO.getPhone().isEmpty()) {
            log.error("Ошибка валидации: номер телефона не может быть пустым");
            throw new ValidationException("Номер телефона не может быть пустым");
        }
        if (phoneRepository.existsByPhone(phoneDTO.getPhone())) {
            log.error("Ошибка валидации: номер телефона {} уже используется", phoneDTO.getPhone());
            throw new ValidationException("Номер телефона уже используется");
        }
        return true;
    }

    private boolean validateExistingPhone(User user, Phone existingPhone, Phone updatedPhone) {
        if (!existingPhone.getUser().getId().equals(user.getId())) {
            log.error("Ошибка валидации: телефон не принадлежит пользователю с ID {}", user.getId());
            throw new ValidationException("Телефон не принадлежит текущему пользователю");
        }
        if (!existingPhone.getPhone().equals(updatedPhone.getPhone()) &&
                phoneRepository.existsByPhone(updatedPhone.getPhone())) {
            log.error("Ошибка валидации: новый номер телефона {} уже используется", updatedPhone.getPhone());
            throw new ValidationException("Новый номер телефона уже используется");
        }
        return true;
    }
}