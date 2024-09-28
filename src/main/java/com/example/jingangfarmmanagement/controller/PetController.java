package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.*;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.Impl.EmailServiceImpl;
import com.example.jingangfarmmanagement.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.File;

@CrossOrigin
@RestController
@RequestMapping("pet")
public class PetController extends BaseController<Pet> {
    @Autowired
    PetService petService;
    @Autowired
    private EmailServiceImpl emailService;

    @Override
    protected BaseService<Pet> getService() {
        return petService;
    }

    @PostMapping("change-cage")
    public BaseResponse changeCage(@RequestBody ChangeCageReq changeCageReq) {
        return petService.changeCage(changeCageReq);
    }

    @PostMapping("/create-pet")
    public BaseResponse create(@RequestBody Pet pet) throws Exception {
        return petService.createPet(pet);
    }

    @PutMapping("/update-pet")
    public BaseResponse update(@RequestBody Pet pet) throws Exception {
        return petService.updatePet(pet);
    }

    //    @GetMapping("/search")
//    public BaseResponse search(SearchReq req) {
//        return new BaseResponse(200, "Lấy dữ liệu thành công!", petService.customSearch(req));
//    }
    @PutMapping("/update-pet-weight")
    public BaseResponse updatePetWeight(@RequestBody UpdateWeightPetReq updateWeightPet) {
        return petService.updatePetWeight(updateWeightPet);
    }

    @GetMapping("/export-pet")
    public BaseResponse findPetWithCageAndFarm(
            @RequestParam(required = false) List<Long> cageId,
            @RequestParam(required = false) List<Long> farmId,
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate) {
        return new BaseResponse(200, "Lấy dữ liệu thành công!", petService.findPetWithCageAndFarm(cageId, farmId, startDate, endDate));
    }

    @PostMapping("heath/change-status")
    public BaseResponse updatePetHeathStatus(@RequestBody List<ChangeStatusPetReq> changeStatusPetReqs) {
        return petService.updatePetStatus(changeStatusPetReqs);
    }

    @PostMapping("/change-condition")
    public BaseResponse updatePetCondition(@RequestBody List<ChangeStatusPetReq> changeStatusPetReqs) {
        return petService.updatePetHealthCondition(changeStatusPetReqs);
    }

    @PostMapping("/statistic-quantity-uniness")
    public BaseResponse statisticQuantityUniness(@RequestBody StatisticQuantityUnilessReq req) {
        return petService.statisticQuantityUnilness(req);
    }

    @PostMapping("/statistic-quantity-uniness-type")
    public BaseResponse statisticQuantityUninessType(@RequestBody StatisticQuantityUnilessReq req) {
        return petService.statisticQuantityUnilnessType(req);
    }

    @PostMapping("/get-all")
    public BaseResponse findAllPet() {
        return petService.findAllPet();
    }

    @PostMapping("/change-cage-by-name")
    public BaseResponse changeCageByName(@RequestBody ChangeCageByNameReq req) {
        return petService.changeCageByName(req);
    }

    @PostMapping("/uploadAndEmailExcel")
    public String uploadPetsAndSendEmail(@RequestParam("file") MultipartFile file) {
        try {
            // Step 1: Validate the file is not empty
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty!");
            }

            // Step 2: Get the original filename
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.endsWith(".xlsx")) {
                throw new IllegalArgumentException("Invalid file type, expected an Excel file!");
            }

            // Step 3: Save the file temporarily to a location (optional, if needed)
            // You can save the file to a temporary directory or process it directly from the MultipartFile object

            // Step 4: Send the email with the file as attachment
            emailService.sendEmailWithAttachment(
                    "tcnhpts@gmail.com",
                    "Danh sách vật nuôi từ file đính kèm",
                    "Tải xuống danh sách vật nuôi từ file đính kèm.",
                    file
            );

            return "Email with uploaded Excel attachment sent successfully!";
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            return "Error occurred while sending email!";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}
