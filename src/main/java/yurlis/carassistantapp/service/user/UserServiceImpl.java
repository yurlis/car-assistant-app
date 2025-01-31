package yurlis.carassistantapp.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yurlis.carassistantapp.dto.authentication.UserRegistrationRequestDto;
import yurlis.carassistantapp.dto.authentication.UserRegistrationResponseDto;
import yurlis.carassistantapp.exception.RegistrationException;
import yurlis.carassistantapp.mapper.UserMapper;
import yurlis.carassistantapp.model.Role;
import yurlis.carassistantapp.model.User;
import yurlis.carassistantapp.repository.role.RoleRepository;
import yurlis.carassistantapp.repository.user.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("User with email: "
                    + requestDto.getEmail()
                    + " already exist");
        }
        User savedUser = userRepository.save(userMapper.toUserModel(requestDto));

        Optional<Role> roleFromDb = roleRepository.findRoleByRole(Role.RoleType.ROLE_USER);

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(roleFromDb.get());
        savedUser.setRoles(userRoles);

        return userMapper.toRegistrationResponseDto(savedUser);
    }
}
