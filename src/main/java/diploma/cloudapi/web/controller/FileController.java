package diploma.cloudapi.web.controller;

import diploma.cloudapi.security.JwtTokenService;
import diploma.cloudapi.service.FileService;
import diploma.cloudapi.web.dto.file.ChangeFilenameRequest;
import diploma.cloudapi.web.dto.file.FileListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final JwtTokenService jwtTokenService;

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam(value = "filename") String filename,
            @RequestBody MultipartFile file
    ){
        String userName = jwtTokenService.getLoginFromToken(authToken);
        fileService.uploadFile(userName, filename, file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(
            @RequestParam(value = "filename") String filename,
            @RequestHeader("auth-token") String authToken
    ){
        String userName = jwtTokenService.getLoginFromToken(authToken);
        fileService.deleteFile(filename, userName);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MultiValueMap<String, Object>> downloadFile(@RequestParam(value = "filename") String filename){
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA)
                .body(fileService.downloadFile(filename));
    }

    @PutMapping("/file")
    public ResponseEntity<Void> editFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String oldFileName,
            @RequestBody ChangeFilenameRequest newFileName
            ){
        String token = jwtTokenService.getLoginFromToken(authToken);
        fileService.changeFileName(token, oldFileName, newFileName.getFilename());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileListResponse>> getAllFiles(
            @RequestParam(name = "limit", defaultValue = "5") Integer limit,
            @RequestHeader("auth-token") String authToken
    )
    {
        String userName = jwtTokenService.getLoginFromToken(authToken);
        return ResponseEntity.ok().body(fileService.getAllUserFiles(userName, limit));
    }
}
