package diploma.cloudapi.service;

import diploma.cloudapi.entity.FileEntity;
import diploma.cloudapi.repository.FileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class FileSerivce {

    private final FileRepository fileRepository;

    @SneakyThrows
    @Transactional
    public void saveFile(String filename, String hash,MultipartFile file)  {
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
}
