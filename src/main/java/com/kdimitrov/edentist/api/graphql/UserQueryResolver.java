package com.kdimitrov.edentist.api.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.kdimitrov.edentist.model.UserEntity;
import com.kdimitrov.edentist.repository.RoleRepository;
import com.kdimitrov.edentist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@PreAuthorize("hasRole('ADMIN')")
public class UserQueryResolver implements GraphQLQueryResolver {

    private UserRepository userRepository;

    @Autowired
    public UserQueryResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }
}
