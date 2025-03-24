package diploma.cloudapi.utils;

import diploma.cloudapi.repository.ActiveTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupTask {

    private final ActiveTokenRepository tokenRepository;

    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredTokens();
        log.info("Expired tokens cleaned.");
    }
}
