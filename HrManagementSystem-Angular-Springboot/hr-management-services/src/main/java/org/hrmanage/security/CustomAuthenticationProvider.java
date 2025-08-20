package org.hrmanage.security;

import lombok.RequiredArgsConstructor;
import org.hrmanage.entity.EmployeeEntity;
import org.hrmanage.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Component;

import java.util.Collections;

// Removed from active configuration to avoid circular dependencies and class loading issues.
// Keeping class for reference if needed later.
//@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String usernameInput = String.valueOf(authentication.getPrincipal());
        String passwordInput = String.valueOf(authentication.getCredentials());

        if (adminUsername != null && adminUsername.equals(usernameInput)) {
            if (adminPassword != null && adminPassword.equals(passwordInput)) {
                User principal = new User(
                        adminUsername,
                        "",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );
                return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            }
            // Return null to allow fallback provider (DAO) to verify admin via UserDetailsService
            return null;
        }

        // Employees must match username and password in DB
        EmployeeEntity employee = employeeRepository.findByUsername(usernameInput);
        if (employee != null && employee.getPassword() != null && passwordEncoder.matches(passwordInput, employee.getPassword())) {
            User principal = new User(
                    employee.getUsername(),
                    "",
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
            );
            return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        }

        // Return null so other providers (if any) can try. Final failure will be handled by ProviderManager.
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}


