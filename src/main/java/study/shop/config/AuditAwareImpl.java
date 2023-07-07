package study.shop.config;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if(authentication!=null)
            userId = authentication.getName();

        return Optional.of(userId);
    }
}
