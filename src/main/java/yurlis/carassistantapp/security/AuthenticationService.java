package yurlis.carassistantapp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import yurlis.carassistantapp.dto.authentication.UserLoginRequestDto;
import yurlis.carassistantapp.dto.authentication.UserLoginResponseDto;
import yurlis.carassistantapp.model.User;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                requestDto.email(),
                                requestDto.password()
                        )
                );
        String token = jwtUtil.generateToken(authentication.getName());
        User loginUser = (User) authentication.getPrincipal();
        return new UserLoginResponseDto(token, loginUser.getFirstName(), loginUser.getLastName());
    }
}
