package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.*;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.Impl.EmailServiceImpl;
import com.example.jingangfarmmanagement.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/exportAndEmailExcel")
    public String exportPetsAndSendEmail() {
        try {
            // Step 1: Get the directory and ensure it exists
            String directoryPath = System.getProperty("user.home") + "/pet_exports";
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Step 2: Define the file name with the current date
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String filePath = new File(directory, "pets_" + currentDate + ".xlsx").getAbsolutePath();

            // Step 3: Create the Excel file (this method should write the file)
            petService.exportToExcel(filePath);

            // Step 4: Check if the file exists after creation
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException("File not found: " + filePath);
            }

            // Step 5: Send the email with the attachment
            emailService.sendEmailWithAttachment(
                    "tcnhpts@gmail.com",
                    "Danh sách vật nuôi ngày " + currentDate,
                    "Tải xuống danh sách vật nuôi ",
                    filePath
            );

            return "Email with Excel attachment sent successfully!";
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            return "Error occurred while sending email!";
        }
    }
}
