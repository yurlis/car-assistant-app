package yurlis.carassistantapp.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderHelper {
    private final PasswordEncoder passwordEncoder;

    @Named(value = "encodedPassword")
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
