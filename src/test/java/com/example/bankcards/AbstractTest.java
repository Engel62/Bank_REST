package com.example.bankcards;
import com.example.bankcards.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public abstract class AbstractTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupSecurityContext();
    }

    private void setupSecurityContext() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L, "testuser", "Test User", "password", List.of()
        );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}