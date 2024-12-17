package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.TreatmentCardReq;
import com.example.jingangfarmmanagement.repository.*;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.repository.entity.Enum.ELogType;
import com.example.jingangfarmmanagement.service.TreatmentCardService;
import com.example.jingangfarmmanagement.uitl.Constant;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TreatmentCardServiceImpl extends BaseServiceImpl<TreatmentCard> implements TreatmentCardService {
    @Autowired
    TreatmentCardRepository treatmentCardRepository;
    @Autowired
    UilnessRepository uilnessRepository;
    @Autowired
    PetRepository petRepository;

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;
    @Autowired
    private LogServiceImpl logService;

    @Override
    protected BaseRepository<TreatmentCard> getRepository() {
        return treatmentCardRepository;
    }
    @Override
    public BaseResponse createTreatment(TreatmentCardReq req) {
        List<String> petInfos =new ArrayList<>();
        try {
            TreatmentCard treatmentCard = new TreatmentCard();
            treatmentCard.setCode(req.getCode());
            treatmentCard.setNote(req.getNote());
            treatmentCard.setResultTypeCard(req.getResultTypeCard());
            treatmentCard.setResultTypeCardDate(req.getResultTypeCard()==1 || req.getResultTypeCard()==2 ? DateUtil.getCurrenDateTime(): 0);
            treatmentCard.setStatus(1);
            treatmentCard.setCreateDate(req.getCreateDate());
            if(req.getResultTypeCard()==2){
                treatmentCard.setUilnesses(new ArrayList<>());
            }else{
                List<Uilness> uilnesses = new ArrayList<>();
                for(var uilnessReq : req.getUlinessName()){
                    if (uilnessRepository.findByName(uilnessReq)!=null) {
                        Uilness existUilness = uilnessRepository.findByName(uilnessReq);
                        uilnesses.add(existUilness);           }
                    else{
                        Uilness uilness =new Uilness();
                        uilness.setName(uilnessReq);
                        uilness.setScore(1);
                        uilness.setStatus(1);
                        uilnessRepository.save(uilness);
                        uilnesses.add(uilness);
                    }}
                treatmentCard.setUilnesses(uilnesses);
            }
            List<Pet> pets = new ArrayList<>();
            for (var petId : req.getPetIds()) {
                Pet pet = petRepository.findById(petId).orElseThrow();
                pets.add(pet);
            }
            treatmentCard.setPets(pets);
            treatmentCardRepository.save(treatmentCard);
             petInfos = pets.stream().map(pet-> {
                return pet.getName()+ " "+ pet.getCage().getName()+ " "+ pet.getCage().getFarm().getName();
            }).collect(Collectors.toList());

            logService.logAction(ELogType.CREATE_TREATMENT_CARD,
                    "Tạo phiếu điều trị con vật " + petInfos + " thành công \n",
                    "success");

            return new BaseResponse(200, "OK", treatmentCard);
        } catch (Exception e) {
            logService.logAction(ELogType.CREATE_TREATMENT_CARD,
                    "Tạo phiếu điều trị con vật " + petInfos + " thất bai \n" + e.getMessage(),
                    "fail");
            return new BaseResponse(500, "Internal Server Error", null);
        }
    }
    @Override
    @Transactional
    public BaseResponse updateTreatment(Long treatmentCardId, TreatmentCardReq req) {
        List<String> petInfos = new ArrayList<>();
        try {
            TreatmentCard treatmentCard = treatmentCardRepository.findById(treatmentCardId)
                    .orElseThrow(() -> new EntityNotFoundException("Treatment card not found"));

            // Update the properties
            treatmentCard.setCode(req.getCode());
            treatmentCard.setNote(req.getNote());
            treatmentCard.setStatus(1);
            treatmentCard.setResultTypeCard(req.getResultTypeCard());
            treatmentCard.setResultTypeCardDate(req.getResultTypeCard() == 1 || req.getResultTypeCard() == 2 ? DateUtil.getCurrenDateTime() : 0);
            treatmentCard.setCreateDate(req.getCreateDate());
            List<Uilness> existingUilnesses = treatmentCard.getUilnesses();
            existingUilnesses.clear();
            if (req.getResultTypeCard() == 2) {
                treatmentCard.setUilnesses(new ArrayList<>());
            } else {
                List<Uilness> uilnesses = new ArrayList<>();
                for (var uilnessReq : req.getUlinessName()) {
                    if (uilnessRepository.findByName(uilnessReq) != null) {
                        Uilness existUilness = uilnessRepository.findByName(uilnessReq);
                        uilnesses.add(existUilness);
                    } else {
                        Uilness uilness = new Uilness();
                        uilness.setName(uilnessReq);
                        uilness.setScore(1);
                        uilness.setStatus(1);
                        uilnessRepository.save(uilness);
                        uilnesses.add(uilness);
                    }
                }
                treatmentCard.setUilnesses(uilnesses);
            }

            List<Pet> existingPets = treatmentCard.getPets();
            existingPets.clear();
            for (var petId : req.getPetIds()) {
                Pet pet = petRepository.findById(petId)
                        .orElseThrow(() -> new EntityNotFoundException(Constant.PET_NOT_FOUND));
                existingPets.add(pet);
            }
            treatmentCard.setPets(existingPets);
            treatmentCardRepository.save(treatmentCard);
            petInfos = existingPets.stream().map(pet-> {
                return pet.getName()+ " "+ pet.getCage().getName()+ " "+ pet.getCage().getFarm().getName();
            }).collect(Collectors.toList());

            logService.logAction(ELogType.UPDATE_TREATMENT_CARD,
                    "Cập nhật phiếu điều trị con vật " + petInfos + " thành công \n",
                    "success");
            return new BaseResponse(200, "OK", treatmentCard);
        }catch (Exception e) {
            logService.logAction(ELogType.UPDATE_TREATMENT_CARD,
                    "Cập nhật phiếu điều trị con vật " + petInfos + " thất bai \n" + e.getMessage(),
                    "fail");
            return new BaseResponse(500, "Internal Server Error", null);
        }
    }
    @Override
    public Page<TreatmentCard> findTreatmentHistoriesByPet(List<Long> petIds, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return treatmentCardRepository.findTreatmentCardsByPet(petIds, pageable);
    }

    @Override
    public TreatmentCard getById(Long id) throws Exception {
        TreatmentCard result = this.getRepository().findById(id).orElseThrow(
                () -> new Exception(String.format("Dữ liệu có id %s không tồn tại!", id))
        );
        getAllFileWithName(result.getImages());
        return result;
    }

    public List<ImageTreatmentCart> getAllFileWithName(List<ImageTreatmentCart> req) {
        req.stream().forEach(e -> {
            try {
                String presignedObjectUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(e.getUrl()).build()
                );
                e.setUrl(presignedObjectUrl);
            } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException ex) {
                System.err.println("Error occurred: " + ex);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        return req;
    }
    public BaseResponse searchTreatmentCards(
            String code,
            String createBy,
            String uilnessName,
            String petName,
            Long startDate,
            Long endDate,
            Pageable pageable) {
        Page<TreatmentCard> treatmentCardPage
                = treatmentCardRepository.searchTreatmentCards(code, 1, createBy, uilnessName, petName, startDate, endDate, pageable);
        return new BaseResponse(200,"OK",treatmentCardPage);
    }
}
