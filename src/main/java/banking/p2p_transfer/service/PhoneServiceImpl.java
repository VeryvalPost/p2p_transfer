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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long createPhoneForUser(Long userId, PhoneDTO phoneDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        try {
            if (validateNewPhone(phoneDTO)) {
                Phone newPhone = phoneMapper.toEntity(phoneDTO);
                newPhone.setUser(user);
                phoneRepository.save(newPhone);

                return newPhone.getId();
            }
        } catch (ValidationException e) {
            throw new ValidationException("Некорректный номер телефона: " + e.getMessage());
        } catch (DatabaseException e) {
            throw new DatabaseException("Ошибка базы данных: ", e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании телефона: " + e.getMessage());
        }
        return phoneDTO.getId();
    }

    @Override
    @Transactional
    public String updatePhoneForUser(Long userId, Long phoneId, PhoneDTO phoneDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Phone existingPhone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new ValidationException("Телефон не найден"));
        Phone updatedPhone = phoneMapper.toEntity(phoneDTO);

        try {
            if (validateExistingPhone(user, existingPhone, updatedPhone)) {
                existingPhone.setPhone(updatedPhone.getPhone());
                phoneRepository.save(existingPhone);
                return existingPhone.getPhone();
            }
        } catch (ValidationException e) {
            throw new ValidationException("Некорректный номер телефона: " + e.getMessage());
        } catch (DatabaseException e) {
            throw new DatabaseException("Ошибка базы данных: ", e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении телефона: " + e.getMessage());
        }
        return phoneDTO.getPhone();
    }

    @Override
    @Transactional
    public void deletePhoneForUser(Long userId, Long phoneId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Phone deletingPhone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new ValidationException("Телефон не найден"));

        if (!deletingPhone.getUser().getId().equals(userId)) {
            throw new ValidationException("Телефон не принадлежит текущему пользователю");
        }

        try {
            if (user.getPhones().size() > 1) {
                phoneRepository.delete(deletingPhone);

            } else {
                throw new ValidationException("Нельзя удалить последний телефон");
            }
        } catch (Exception e) {
            throw new DatabaseException("Ошибка при удалении телефона: ", e);
        }
    }

    private boolean validateNewPhone(PhoneDTO phoneDTO) {
        if (phoneDTO.getPhone() == null || phoneDTO.getPhone().isEmpty()) {
            throw new ValidationException("Номер телефона не может быть пустым");
        }
        if (phoneRepository.existsByPhone(phoneDTO.getPhone())) {
            throw new ValidationException("Номер телефона уже используется");
        }
        return true;
    }

    private boolean validateExistingPhone(User user, Phone existingPhone, Phone updatedPhone) {
        if (!existingPhone.getUser().getId().equals(user.getId())) {
            throw new ValidationException("Телефон не принадлежит текущему пользователю");
        }
        if (!existingPhone.getPhone().equals(updatedPhone.getPhone()) &&
                phoneRepository.existsByPhone((updatedPhone.getPhone()))){
            throw new ValidationException("Новый номер телефона уже используется");
        }
        return true;
    }
}