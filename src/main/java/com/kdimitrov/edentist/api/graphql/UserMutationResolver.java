package com.kdimitrov.edentist.api.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.kdimitrov.edentist.api.services.UserService;
import com.kdimitrov.edentist.model.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserMutationResolver implements GraphQLMutationResolver {

    private final UserService userService;

    public UserMutationResolver(final UserService userService) {
        this.userService = userService;
    }

    public UserEntity createUser(final String email, final String password, final String role) {
        return userService.createUser(email, password, role);
    }
}
