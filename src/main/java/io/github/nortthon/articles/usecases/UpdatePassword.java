package io.github.nortthon.articles.usecases;

import io.github.nortthon.articles.domains.Credential;
import io.github.nortthon.articles.gateways.UserGateway;
import lombok.RequiredArgsConstructor;

import static java.util.regex.Pattern.matches;

@RequiredArgsConstructor
public class UpdatePassword {

    private static final String REGEX = "([0-9].*[a-zA-Z])|([a-zA-Z].*[0-9])";

    private final UserGateway userGateway;

    public Credential execute(final String user, final String newPassword) {
        if (!matches(REGEX, newPassword) || newPassword.length() < 8) {
            throw new IllegalArgumentException("the password must consist of numbers and letters, and at least 8 characters");
        }

        final var credential = userGateway.find(user);
        if (credential.getPassword().equals(newPassword)) {
            throw new IllegalArgumentException("the password must be different from the current one");
        }

        credential.setPassword(newPassword);
        return userGateway.save(credential);
    }
}
