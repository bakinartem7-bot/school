package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/avatars")
public class AvatarController {
    private final AvatarService avatarService;

    @Autowired
    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping("/upload/{studentId}")
    public ResponseEntity<Long> uploadAvatar(
            @PathVariable Long studentId,
            @RequestParam("file") MultipartFile file) throws IOException {

        Avatar savedAvatar = avatarService.saveAvatar(
                studentId,
                file.getBytes(),
                file.getOriginalFilename(),
                file.getContentType()
        );
        return ResponseEntity.ok(savedAvatar.getId());
    }

    @GetMapping("/db/{studentId}")
    public ResponseEntity<byte[]> getAvatarFromDatabase(@PathVariable Long studentId) throws IOException {
        Avatar avatar = avatarService.getAvatarFromDb(studentId);
        if (avatar == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("Content-Type", avatar.getMediaType())
                .body(avatar.getData());
    }

    @GetMapping("/file/{studentId}")
    public ResponseEntity<byte[]> getAvatarFromFile(@PathVariable Long studentId) throws IOException {
        Path filePath = avatarService.getAvatarFilePath(studentId);
        byte[] fileData = Files.readAllBytes(filePath);
        Avatar avatar = avatarService.getAvatarFromDb(studentId);

        return ResponseEntity.ok()
                .header("Content-Type", avatar.getMediaType())
                .body(fileData);
    }

    @GetMapping
    public ResponseEntity<Page<Avatar>> getAllAvatars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Avatar> avatarsPage = avatarService.getAllAvatars(page, size);
        return ResponseEntity.ok(avatarsPage);
    }
}
