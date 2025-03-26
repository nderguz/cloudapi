package diploma.cloudapi.web.controller;

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
@RequestMapping("/cloud")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam(value = "filename") String filename,
            @RequestBody MultipartFile file
    ){
        fileService.uploadFile(authToken.substring(7), filename, file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(
            @RequestParam(value = "filename") String filename,
            @RequestHeader("auth-token") String authToken
    ){
        fileService.deleteFile(filename, authToken.substring(7));
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MultiValueMap<String, Object>> downloadFile(@RequestParam(value = "filename") String filename){
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA)
                .body(fileService.downloadFile(filename));
    }

    @PutMapping("/file")
    public ResponseEntity<Void> editFile(
            @RequestHeader("auth-token") String token,
            @RequestParam("filename") String oldFileName,
            @RequestBody ChangeFilenameRequest newFileName
            ){
        fileService.changeFileName(token.substring(7), oldFileName, newFileName.getFilename());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileListResponse>> getAllFiles(
            @RequestParam(name = "limit", defaultValue = "5") Integer limit,
            @RequestHeader("auth-token") String token
    )
    {
        return ResponseEntity.ok().body(fileService.getAllUserFiles(token.substring(7), limit));
    }
}
