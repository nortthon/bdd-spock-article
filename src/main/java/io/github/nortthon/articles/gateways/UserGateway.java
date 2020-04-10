package io.github.nortthon.articles.gateways;

import io.github.nortthon.articles.domains.Credential;

public interface UserGateway {
    Credential find(String user);
    Credential save(Credential credential);
}
