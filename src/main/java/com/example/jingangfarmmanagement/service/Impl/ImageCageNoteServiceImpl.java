package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.CageNoteHistoryRepository;
import com.example.jingangfarmmanagement.repository.ImageCageNoteRepository;
import com.example.jingangfarmmanagement.repository.entity.CageNoteHistory;
import com.example.jingangfarmmanagement.repository.entity.ImageCageNote;
import com.example.jingangfarmmanagement.repository.entity.ImageTreatmentCart;
import com.example.jingangfarmmanagement.repository.entity.TreatmentCard;
import com.example.jingangfarmmanagement.service.CageNoteHistoryService;
import com.example.jingangfarmmanagement.service.ImageCageNoteService;
import com.example.jingangfarmmanagement.service.TreatmentCardService;
import com.example.jingangfarmmanagement.uitl.ContextUtil;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class ImageCageNoteServiceImpl extends BaseServiceImpl<ImageCageNote> implements ImageCageNoteService {
    @Autowired
    private ImageCageNoteRepository imageCageNoteRepository;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Autowired
    CageNoteHistoryRepository cageNoteHistoryService;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    ContextUtil contextUtil;


    @Override
    public BaseResponse uploadFile(MultipartFile file, Long id) {
        try {
            // Kiểm tra nếu bucket không tồn tại thì tạo mới
            CageNoteHistory cageNoteHistory = cageNoteHistoryService.getById(id);
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            String forder ="cage_note/" + contextUtil.getUserName() + file.getOriginalFilename() + DateUtil.getCurrenDateTime();
            // Upload file lên MinIO
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(forder).stream(file.getInputStream(), file.getInputStream().available(), -1).contentType("image/jpeg").build());
            ImageCageNote image = ImageCageNote.builder().url(forder).cageNoteHistory(cageNoteHistory).build();
            super.create(image);
            return new BaseResponse().success("File uploaded successfully: " + file.getOriginalFilename());
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println("Error occurred: " + e);
            return new BaseResponse().fail(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected BaseRepository<ImageCageNote> getRepository() {
        return imageCageNoteRepository;
    }
}
