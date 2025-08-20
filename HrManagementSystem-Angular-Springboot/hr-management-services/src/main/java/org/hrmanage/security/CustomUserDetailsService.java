package org.hrmanage.security;

import lombok.RequiredArgsConstructor;
import org.hrmanage.entity.EmployeeEntity;
import org.hrmanage.entity.UserEntity;
import org.hrmanage.repository.EmployeeRepository;
import org.hrmanage.repository.UserRepository;
// import org.hrmanage.util.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Prefer database 'user' table for admin (and any future users there)
        UserEntity user = userRepository.findByUsername(username);
        if (user != null) {
            String role = user.getRole().name();
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
            );
        }

        // Else, check employees by username
        EmployeeEntity employee = employeeRepository.findByUsername(username);
        if (employee != null && employee.getPassword() != null) {
            return new org.springframework.security.core.userdetails.User(
                    employee.getUsername(),
                    employee.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
            );
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}

