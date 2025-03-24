package diploma.cloudapi.repository;

import diploma.cloudapi.entity.FileEntity;
import diploma.cloudapi.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByFilename(String filename);
    void deleteFileEntitiesByFilename(String filename);
    Optional<List<FileEntity>> getFileEntitiesByUser(UserEntity user, Pageable pageable);
}
