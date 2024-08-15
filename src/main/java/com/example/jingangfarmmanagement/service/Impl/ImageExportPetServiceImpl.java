package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.ExportPetRepository;
import com.example.jingangfarmmanagement.repository.ImageExportPetRepository;
import com.example.jingangfarmmanagement.repository.ImageTreatmentCartRepository;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.service.ExportPetService;
import com.example.jingangfarmmanagement.service.ImageExportPetService;
import com.example.jingangfarmmanagement.service.ImageTreatmentCartService;
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
public class ImageExportPetServiceImpl extends BaseServiceImpl<ImageExportPet> implements ImageExportPetService {
    @Autowired
    private ImageExportPetRepository imageExportPetRepository;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Autowired
    ExportPetRepository exportPetService;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    ContextUtil contextUtil;

    @Override
    protected BaseRepository<ImageExportPet> getRepository() {
        return imageExportPetRepository;
    }

    @Override
    public BaseResponse uploadFile(MultipartFile file, Long id) {
        try {
            // Kiểm tra nếu bucket không tồn tại thì tạo mới
            ExportPet exportPet = exportPetService.getById(id);
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            String forder = "export_pet" + contextUtil.getUserName() + file.getOriginalFilename() + DateUtil.getCurrenDateTime();
            // Upload file lên MinIO
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(forder).stream(file.getInputStream(), file.getInputStream().available(), -1).contentType("image/jpeg").build());
            ImageExportPet image = ImageExportPet.builder().url(forder).exportPet(exportPet).build();
            super.create(image);
            return new BaseResponse().success("File uploaded successfully: " + file.getOriginalFilename());
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println("Error occurred: " + e);
            return new BaseResponse().fail(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
