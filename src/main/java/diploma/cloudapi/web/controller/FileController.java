package diploma.cloudapi.web.controller;

import diploma.cloudapi.security.AppUserDetails;
import diploma.cloudapi.service.FileSerivce;
import diploma.cloudapi.web.dto.FileListResponse;
import diploma.cloudapi.web.dto.GetFileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.util.List;

@RestController
@RequestMapping("/cloud")
@RequiredArgsConstructor
public class FileController {

    private final FileSerivce fileSerivce;

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam(value = "filename") String filename,
            @RequestParam(value = "hash") String hash,
            @RequestParam(value = "file") MultipartFile file
    ){
        fileSerivce.uploadFile(filename,hash,file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(@RequestParam(value = "filename") String filename){
        fileSerivce.deleteFile(filename);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MultiValueMap<String, Object>> downloadFile(@RequestParam(value = "filename") String filename){
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA)
                .body(fileSerivce.downloadFile(filename));
    }

    @PutMapping("/file")
    public void editFile(){

    }

    @GetMapping("/list")
    public ResponseEntity<List<FileListResponse>> getAllFiles(
            @RequestParam(name = "limit", defaultValue = "5") Integer limit,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok().body(fileSerivce.getAllUserFiles(userDetails, limit));
    }
}
