package ru.hogwarts.school.service;

import org.springframework.data.domain.PageRequest;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;
    private static final String AVATAR_DIR = "avatars/";

    @Autowired
    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    public Avatar saveAvatar(Long studentId, byte[] fileData, String fileName, String mediaType) throws IOException {
        Student student = studentService.getStudent(studentId);

        Path directory = Paths.get(AVATAR_DIR);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        String filePath = AVATAR_DIR + studentId + "_" + fileName;
        Files.write(Paths.get(filePath), fileData);

        Avatar avatar = new Avatar(filePath, fileData.length, mediaType, fileData, student);
        return avatarRepository.save(avatar);
    }

    public Avatar getAvatarFromDb(Long studentId) {
        return avatarRepository.findByStudentId(studentId);
    }

    public Path getAvatarFilePath(Long studentId) throws IOException {
        Avatar avatar = avatarRepository.findByStudentId(studentId);
        if (avatar == null) {
            throw new IOException("Avatar not found for student " + studentId);
        }
        return Paths.get(avatar.getFilePath());
    }

    public Page<Avatar> getAllAvatars(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return avatarRepository.findAll(pageable);
    }
}
