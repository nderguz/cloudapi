package diploma.cloudapi.integration;

import diploma.cloudapi.web.dto.authorization.AuthorizationRequest;
import diploma.cloudapi.web.dto.file.FileListResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileServiceIntegrationTests extends AbstractTest {

    private String token;

    @BeforeEach
    public void setToken(){
        var request = new AuthorizationRequest("test_user1", "test_user1");
        token = authenticationService.authenticateUser(request);
    }

    @Test
    public void uploadFileSuccessTest(){
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test file data".getBytes());
        assertDoesNotThrow(() -> fileService.uploadFile(token, "test.txt", file));
        assertTrue(fileRepository.findByFilename("test.txt").isPresent());
    }

    @Test
    @SneakyThrows
    public void downloadFileTest(){
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test file data".getBytes());
        fileService.uploadFile(token, "test.txt", file);

        MultiValueMap<String, Object> downloadedFile = fileService.downloadFile("test.txt");
        assertNotNull(downloadedFile);
        assertEquals("test.txt", ((ByteArrayResource) downloadedFile.getFirst("file")).getFilename());
    }

    @Test
    public void deleteFileTest(){
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test file data".getBytes());
        fileService.uploadFile(token, "test.txt", file);

        assertDoesNotThrow(() -> fileService.deleteFile("test.txt", token));
        assertFalse(fileRepository.findByFilename("test.txt").isPresent());
    }

    @Test
    public void getAllUserFilesTest(){
        MockMultipartFile file1 = new MockMultipartFile("file", "file1.txt", "text/plain", "Data1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "file2.txt", "text/plain", "Data2".getBytes());
        fileService.uploadFile(token, "file1.txt", file1);
        fileService.uploadFile(token, "file2.txt", file2);

        List<FileListResponse> files = fileService.getAllUserFiles(token, 10);
        assertEquals(2, files.size());
    }

    @Test
    public void changeFilenameTest(){
        MockMultipartFile file = new MockMultipartFile("file", "oldName.txt", "text/plain", "Data".getBytes());
        fileService.uploadFile(token, "oldName.txt", file);

        assertDoesNotThrow(() -> fileService.changeFileName(token, "oldName.txt", "newName.txt"));
        assertTrue(fileRepository.findByFilename("newName.txt").isPresent());
    }
}