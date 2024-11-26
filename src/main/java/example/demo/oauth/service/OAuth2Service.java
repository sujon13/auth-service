package example.demo.oauth.service;


import example.demo.oauth.model.OAuthUser;
import example.demo.oauth.model.OAuthUserResponse;
import example.demo.service.UserRoleService;
import example.demo.service.UserService;
import example.demo.service.auth.AuthenticationService;
import example.demo.signup.enums.AccountType;
import example.demo.signup.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2Service {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserRoleService userRoleService;

    private User registerUser(OAuthUser oAuthUser) {
        User newUser = userService.createAndSaveUser(oAuthUser);
        userRoleService.assignUserRole(newUser.getId());
        return newUser;
    }

    private User updateUser(User existingUser, OAuthUser oAuthUser) {
        log.info("User already exist with accountId: {}, email: {}", existingUser.getAccountId(), existingUser.getEmail());
        if (existingUser.getAccountId() == null)
            existingUser.setAccountId(oAuthUser.getAccountId());

        if (existingUser.isRegularUser()) {
            if (existingUser.getName() == null)
                existingUser.setName(oAuthUser.getName());
        } else {
            if (oAuthUser.getName() != null)
                existingUser.setName(oAuthUser.getName());
        }
        return existingUser;
    }

    @Transactional
    public User registerOrUpdateUser(OAuthUser oAuthUser) {
        return userService.findByAccountIdOrEmail(oAuthUser.getAccountId(), oAuthUser.getEmail())
                .map(existingUser -> updateUser(existingUser, oAuthUser))
                .orElseGet(() -> registerUser(oAuthUser));
    }

    private OAuthUserResponse createOAuthUserResponse(final User user) {
        return OAuthUserResponse.builder()
                .userId(user.getId())
                .accountId(user.getAccountId())
                .build();
    }

    private String buildErrorMessage(AccountType accountType) {
        final String vendor = accountType.name().toLowerCase();
        return "User is verified by " + StringUtils.capitalize(vendor) +
                " but also needs to create a userName to complete the registration";
    }

    public OAuthUserResponse buildErrorResponse(final User user) {
        OAuthUserResponse oAuthUserResponse = createOAuthUserResponse(user);

        final String errorMessage = buildErrorMessage(user.getAccountType());
        log.error(errorMessage);

        oAuthUserResponse.setMessage(errorMessage);
        return oAuthUserResponse;
    }

    public OAuthUserResponse buildJwtResponse(final User user) {
        OAuthUserResponse oAuthUserResponse = createOAuthUserResponse(user);
        final String jwt = authenticationService.createAuthenticationToken(user);
        oAuthUserResponse.setMessage(jwt);
        return oAuthUserResponse;
    }
}