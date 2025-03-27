package diploma.cloudapi.service;

import diploma.cloudapi.entity.FileEntity;
import diploma.cloudapi.entity.UserEntity;
import diploma.cloudapi.repository.FileRepository;
import diploma.cloudapi.repository.UserRepository;
import diploma.cloudapi.web.dto.file.FileListResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @SneakyThrows
    @Transactional
    public void uploadFile(String userName, String filename, MultipartFile file)  {
        log.info("Request to upload file {}", filename);
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(filename);
        fileEntity.setFileData(file.getBytes());

        UserEntity user = userRepository.findByLogin(userName)
                .orElseThrow(() -> new EntityNotFoundException("User %s was not found".formatted(userName)));

        fileEntity.setUser(user);
        fileEntity.setContentType(file.getContentType());
        int fileHash = file.hashCode();

        fileEntity.setHash(String.valueOf(fileHash));
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
    public void deleteFile(String filename, String userName){
        log.info("Request to delete file {}", filename);
        FileEntity file = fileRepository.findByFilename(filename)
                        .orElseThrow(() -> new EntityNotFoundException("File not found"));
        if (!Objects.equals(file.getUser().getLogin(), userName)){
            throw new IllegalArgumentException("This user is not allowed to change this file");
        }
        fileRepository.deleteFileEntitiesByFilename(filename);
    }

    @Transactional(readOnly = true)
    public List<FileListResponse> getAllUserFiles(String userName, Integer limit){
        log.info("Request for user {} files", userName.toUpperCase(Locale.ROOT));

        UserEntity currentUser = userRepository.findByLogin(userName)
                .orElseThrow(() -> new EntityNotFoundException("User %s was not found".formatted(userName)));
        List<FileEntity> userFiles = fileRepository.getFileEntitiesByUser(currentUser, PageRequest.of(0, limit))
                .orElseThrow(() -> new EntityNotFoundException("User files was not found"));
        return userFiles.stream()
                .map(it -> FileListResponse.builder()
                        .filename(it.getFilename())
                        .build())
                .limit(limit)
                .toList();
    }

    @Transactional
    public void changeFileName(String userName, String oldFileName, String newFileName){
        log.info("Request to change filename from {} to {}", oldFileName, newFileName);
        FileEntity file = fileRepository.findByFilename(oldFileName)
                .orElseThrow(() -> new EntityNotFoundException("File not found"));

        if (!Objects.equals(file.getUser().getLogin(), userName)){
            throw new IllegalArgumentException("This user is not allowed to change this file");
        }

        file.setFilename(newFileName);
        fileRepository.save(file);
    }
}