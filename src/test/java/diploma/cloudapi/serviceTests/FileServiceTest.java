package diploma.cloudapi.serviceTests;

import diploma.cloudapi.entity.FileEntity;
import diploma.cloudapi.entity.UserEntity;
import diploma.cloudapi.exception.ApiException;
import diploma.cloudapi.repository.FileRepository;
import diploma.cloudapi.repository.UserRepository;
import diploma.cloudapi.security.JwtTokenService;
import diploma.cloudapi.service.FileService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {
    @Mock
    private FileRepository fileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    private FileService fileService;

    private UserEntity testUser;
    private FileEntity testFile;

    @BeforeEach
    public void setUp(){
        testUser = UserEntity.builder()
                .login("testUserLogin")
                .build();
        testFile = FileEntity.builder()
                .filename("test.txt")
                .user(testUser)
                .fileData(new byte[]{1, 2, 3})
                .hash("123456")
                .build();
    }

    @Test
    @SneakyThrows
    public void downloadFileTest(){
        when(fileRepository.findByFilename("test.txt")).thenReturn(Optional.of(testFile));
        MultiValueMap<String, Object> result = fileService.downloadFile("test.txt");
        assertNotNull(result);
        assertEquals("test.txt", ((ByteArrayResource) result.getFirst("file")).getFilename());
        assertEquals("123456", result.getFirst("hash"));
    }

    @Test
    @SneakyThrows
    public void uploadFileTest(){
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(file.getContentType()).thenReturn("text/plain");
        when(userRepository.findByLogin("testUser")).thenReturn(Optional.of(testUser));

        fileService.uploadFile("testUser", "test.txt", file);

        verify(fileRepository, times(1)).save(any(FileEntity.class));
    }

    @Test
    public void deleteFileTest(){
        when(fileRepository.findByFilename("test.txt")).thenReturn(Optional.of(testFile));

        fileService.deleteFile("test.txt", "testUserLogin");

        verify(fileRepository, times(1)).deleteFileEntitiesByFilename("test.txt");
    }

    @Test
    public void deleteFileUnauthorizedTest(){
        when(fileRepository.findByFilename("test.txt")).thenReturn(Optional.of(testFile));

        assertThrows(IllegalArgumentException.class, () -> fileService.deleteFile("test.txt", "otherUser"));

        verify(fileRepository, never()).deleteFileEntitiesByFilename(anyString());
    }

    @Test
    public void changeFileNameTest(){
        when(fileRepository.findByFilename("test.txt")).thenReturn(Optional.of(testFile));

        fileService.changeFileName("testUserLogin", "test.txt", "new.txt");

        verify(fileRepository, times(1)).save(any(FileEntity.class));
        assertEquals("new.txt", testFile.getFilename());
    }

    @Test
    public void changeFileNameUnauthorizedTest(){
        when(fileRepository.findByFilename("test.txt")).thenReturn(Optional.of(testFile));

        assertThrows(ApiException.class, () -> fileService.changeFileName("otherUser", "test.txt", "new.txt"));

        verify(fileRepository, never()).save(any(FileEntity.class));
    }
}
