package com.turnkey.turnquest.gis.quotation.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @NotNull
    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name;
        if (authentication != null) {
        var kp = (Jwt) authentication.getPrincipal();

            name = kp.getClaims().get("name")!= null ? kp.getClaims().get("name").toString(): "agencify_admin";
            return Optional.of(name);
        }

        return Optional.empty();
    }

}
