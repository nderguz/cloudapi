package diploma.cloudapi.repository;

import diploma.cloudapi.entity.ActiveTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActiveTokenRepository extends JpaRepository<ActiveTokenEntity, String> {
    Optional<ActiveTokenEntity> findByToken(String token);

    @Transactional
    void deleteActiveTokenEntityByUsername(String username);

    @Modifying
    @Transactional
    @Query("DELETE FROM ActiveTokenEntity t WHERE t.expiresAt < CURRENT_TIMESTAMP")
    void deleteExpiredTokens();
}
