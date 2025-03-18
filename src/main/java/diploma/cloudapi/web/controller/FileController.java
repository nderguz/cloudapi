package diploma.cloudapi.web.controller;

import diploma.cloudapi.web.dto.FileListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cloud")
public class FileController {

    @PostMapping("/file")
    public void uploadFile(@RequestParam(value = "filename") String filename){

    }

    @DeleteMapping("/file")
    public void deleteFile(){

    }

    @GetMapping("/file")
    public void downloadFile(){

    }

    @PutMapping("/file")
    public void editFile(){

    }

    @GetMapping("/list")
//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> getAllFiles(@RequestParam(name = "limit", defaultValue = "5") Integer limit){
        return ResponseEntity.ok(List.of(new FileListResponse("Первый файл", 15),
                new FileListResponse("Второй файл", 30),
                new FileListResponse("Третий файл", 55)));
    }
}
