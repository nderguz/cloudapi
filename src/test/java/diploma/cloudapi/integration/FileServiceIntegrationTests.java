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

    private String userName;

    @BeforeEach
    public void setup(){
        var request = new AuthorizationRequest("test_user1", "test_user1");
        String token = authenticationService.authenticateUser(request);
        userName = jwtTokenService.getLoginFromToken(token);
    }

    @Test
    public void uploadFileSuccessTest(){
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test file data".getBytes());
        assertDoesNotThrow(() -> fileService.uploadFile(userName, "test.txt", file));
        assertTrue(fileRepository.findByFilename("test.txt").isPresent());
    }

    @Test
    @SneakyThrows
    public void downloadFileTest(){
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test file data".getBytes());
        fileService.uploadFile(userName, "test.txt", file);

        MultiValueMap<String, Object> downloadedFile = fileService.downloadFile("test.txt");
        assertNotNull(downloadedFile);
        assertEquals("test.txt", ((ByteArrayResource) downloadedFile.getFirst("file")).getFilename());
    }

    @Test
    public void deleteFileTest(){
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test file data".getBytes());
        fileService.uploadFile(userName, "test.txt", file);

        assertDoesNotThrow(() -> fileService.deleteFile("test.txt", userName));
        assertFalse(fileRepository.findByFilename("test.txt").isPresent());
    }

    @Test
    public void getAllUserFilesTest(){
        MockMultipartFile file1 = new MockMultipartFile("file", "file1.txt", "text/plain", "Data1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "file2.txt", "text/plain", "Data2".getBytes());
        fileService.uploadFile(userName, "file1.txt", file1);
        fileService.uploadFile(userName, "file2.txt", file2);

        List<FileListResponse> files = fileService.getAllUserFiles(userName, 10);
        assertEquals(2, files.size());
    }

    @Test
    public void changeFilenameTest(){
        MockMultipartFile file = new MockMultipartFile("file", "oldName.txt", "text/plain", "Data".getBytes());
        fileService.uploadFile(userName, "oldName.txt", file);

        assertDoesNotThrow(() -> fileService.changeFileName(userName, "oldName.txt", "newName.txt"));
        assertTrue(fileRepository.findByFilename("newName.txt").isPresent());
    }
}