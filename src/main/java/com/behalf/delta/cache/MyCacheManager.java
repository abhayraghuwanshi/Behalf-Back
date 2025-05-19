package com.behalf.delta.cache;

import com.behalf.delta.auth.AuthenticationFacade;
import com.behalf.delta.auth.UnauthorizedException;
import com.behalf.delta.entity.UserInformation;
import com.behalf.delta.exception.UserNotFoundException;
import com.behalf.delta.repo.UserInformationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MyCacheManager {

    private final UserInformationRepo userRepository;

    private final AuthenticationFacade authenticationFacade;

    private final RedisTemplate<String, UserInformation> redisTemplate;


    public UserInformation getUserInfoFromCache() throws UserNotFoundException {
        Authentication authentication = authenticationFacade.getAuthentication();
        if (authentication == null) {
            throw new UnauthorizedException("No authenticated user found");
        }

        // For OIDC, get email from the principal
        String email;
        if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
            email = oidcUser.getEmail();
        } else {
            // Fallback for other authentication methods
            email = authentication.getName();
        }

        // Try to fetch from Redis using email as the key
        String cacheKey = "user:" + email;
        UserInformation cachedUser = (UserInformation) redisTemplate.opsForValue().get(cacheKey);

        if (cachedUser != null) {
            log.info("User found in Redis cache: {}", email);
            return cachedUser;
        }

        // Fallback to database if not in Redis
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

}
