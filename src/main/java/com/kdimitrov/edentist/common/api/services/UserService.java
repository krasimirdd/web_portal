package com.kdimitrov.edentist.common.api.services;

import com.kdimitrov.edentist.common.model.RoleEntity;
import com.kdimitrov.edentist.common.model.UserEntity;
import com.kdimitrov.edentist.common.repository.RoleRepository;
import com.kdimitrov.edentist.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public boolean exists(String email) {
        Optional<UserEntity> userEntityOpt = userRepository.findOneByEmail(email);

        return userEntityOpt.isPresent();
    }

    public UserEntity getOrCreateUser(String email) {

        Optional<UserEntity> userEntityOpt =
                userRepository.findOneByEmail(email);

        return userEntityOpt.
                orElseGet(() -> createUser(email));
    }

    public UserEntity getUser(String email) {

        Optional<UserEntity> userEntityOpt =
                userRepository.findOneByEmail(email);

        return userEntityOpt.orElse(null);
    }

    public void registerAndLoginUser(String userEmail, String userPassword) {
        UserEntity userEntity = createUser(userEmail, userPassword);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEntity.getEmail());

        Authentication authentication = new
                UsernamePasswordAuthenticationToken(
                userDetails,
                userEntity.getPassword(),
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public UserEntity createUser(String userEmail, String userPassword) {
        return createUser(userEmail, userPassword, null);
    }

    public UserEntity createUser(String email) {
        return this.createUser(email, null);
    }

    public UserEntity createUser(String userEmail, String userPassword, String roles) {
        LOGGER.info("Creating a new user with email [PROTECTED].");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userEmail);
        if (userPassword != null) {
            userEntity.setPassword(passwordEncoder.encode(userPassword));
        }

        if (roles != null) {
            List<RoleEntity> rolesEntityList = Arrays.stream(roles
                    .split(","))
                    .map((r) -> new RoleEntity().setRole(r))
                    .collect(Collectors.toList());

            userEntity.setRoles(rolesEntityList);
        } else {
            userEntity.setRoles(Collections.singletonList(
                    new RoleEntity().setRole("ROLE_USER")));
        }
        return userRepository.save(userEntity);
    }

    public List<UserEntity> getAllDentists() {
        RoleEntity remedialRole = roleRepository
                .findByRoleContains("REMEDIAL")
                .orElseThrow(() -> new NotFoundException("No such role"));
        return userRepository.findAllByRolesContaining(remedialRole);
    }
}
