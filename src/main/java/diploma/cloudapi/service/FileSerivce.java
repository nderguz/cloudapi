package diploma.cloudapi.service;

import diploma.cloudapi.entity.FileEntity;
import diploma.cloudapi.entity.UserEntity;
import diploma.cloudapi.repository.FileRepository;
import diploma.cloudapi.repository.UserRepository;
import diploma.cloudapi.security.AppUserDetails;
import diploma.cloudapi.web.dto.FileListResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;


@Service
@RequiredArgsConstructor
@Slf4j
public class FileSerivce {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @SneakyThrows
    @Transactional
    public void uploadFile(String filename, String hash,MultipartFile file)  {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(filename);

        fileEntity.setFileData(file.getBytes());

        fileEntity.setContentType(file.getContentType());
        fileEntity.setHash(hash);
        fileRepository.save(fileEntity);
    }

    @Transactional(readOnly = true)
    public MultiValueMap<String, Object> downloadFile(String filename){
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        FileEntity file = fileRepository.findByFilename(filename)
                .orElseThrow(() -> new EntityNotFoundException("File %s was not found".formatted(filename)));
        body.add("file", new ByteArrayResource(file.getFileData()){
            @Override
            public String getFilename(){
                return file.getFilename();
            }
        });
        body.add("hash", file.getHash());
        return body;
    }

    @Transactional
    public void deleteFile(String filename){
        fileRepository.deleteFileEntitiesByFilename(filename);
    }

    @Transactional(readOnly = true)
    public List<FileListResponse> getAllUserFiles(UserDetails user, Integer limit){
        //todo понять, почему всегда вызывается admin
        //todo поменять вывод размера файла
        log.info("Request for user {} files", user.getUsername().toUpperCase(Locale.ROOT));

        UserEntity currentUser = userRepository.findByLogin(user.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User %s was not found".formatted(user.getUsername())));
        List<FileEntity> userFiles = fileRepository.getFileEntitiesByUser(currentUser, PageRequest.of(0, limit))
                .orElseThrow(() -> new EntityNotFoundException("User files was not found"));
        return userFiles.stream()
                .map(it -> FileListResponse.builder()
                        .filename(it.getFilename())
                        .size(5L)
                        .build())
                .limit(limit)
                .toList();
    }
}
