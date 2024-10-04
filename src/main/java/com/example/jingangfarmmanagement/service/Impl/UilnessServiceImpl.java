package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.UilnessRepository;
import com.example.jingangfarmmanagement.repository.entity.Enum.ELogType;
import com.example.jingangfarmmanagement.repository.entity.Uilness;
import com.example.jingangfarmmanagement.service.UilnessService;
import com.example.jingangfarmmanagement.service.UserService;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UilnessServiceImpl extends BaseServiceImpl<Uilness> implements UilnessService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    @Autowired
    UilnessRepository uilnessRepository;
    @Autowired
    private LogServiceImpl logService;
    @Override
    protected BaseRepository<Uilness> getRepository() {
        return uilnessRepository;
    }

    @Override
    public BaseResponse customeCreate(Uilness t) throws Exception {
        Uilness uilness = new Uilness();
        try{
            if (t.getScore() > 6 || t.getScore() < 0) {
                return new BaseResponse().fail("Score không được lớn hơn 5 hoc nhỏ hơn 0");
            }
             uilness =super.create(t);
            logService.logAction(ELogType.CREATE_UILNESS,
                    "Tạo thông tin bệnh " + uilness.getName() + " thành công" ,
                    "success");
            return new BaseResponse().success(uilness);
        }catch (Exception e){
            logger.error("Error occurred while create uliness: {}", e.getMessage(), e);
            logService.logAction(ELogType.CREATE_UILNESS,
                    "Tạo thông tin bệnh  " + uilness.getName() + " thất bại" + e.getMessage(),
                    "fail");
            return new BaseResponse(500, "Có lỗi xảy ra khi tạo tài khoản", null);
        }

    }

    @Override
    public BaseResponse customeUpdate(Uilness t) {
        try {
            if (t.getScore() > 6 || t.getScore() < 0) {
                return new BaseResponse().fail("Score không được lớn hơn 6 hoặc nhỏ hơn 0");
            }

            // Fetch the existing Uilness entity from the database
            Optional<Uilness> existingUilnessOpt = uilnessRepository.findById(t.getId());
            Uilness existingUilness = existingUilnessOpt.orElseThrow(() -> new RuntimeException("Uilness not found"));

            StringBuilder changeLog = new StringBuilder("Cập nhật: " + t.getName() + " ");
            boolean isUpdated = false;

            // Check and log changes for each field
            if (!existingUilness.getCode().equals(t.getCode())) {
                changeLog.append("Code: '").append(existingUilness.getCode()).append("' -> '").append(t.getCode()).append("'; ");
                existingUilness.setCode(t.getCode());
                isUpdated = true;
            }
            if (!existingUilness.getName().equals(t.getName())) {
                changeLog.append("Name: '").append(existingUilness.getName()).append("' -> '").append(t.getName()).append("'; ");
                existingUilness.setName(t.getName());
                isUpdated = true;
            }
            if (!existingUilness.getType().equals(t.getType())) {
                changeLog.append("Type: '").append(existingUilness.getType()).append("' -> '").append(t.getType()).append("'; ");
                existingUilness.setType(t.getType());
                isUpdated = true;
            }
            if (existingUilness.getScore() != t.getScore()) {
                changeLog.append("Score: '").append(existingUilness.getScore()).append("' -> '").append(t.getScore()).append("'; ");
                existingUilness.setScore(t.getScore());
                isUpdated = true;
            }
            if (!existingUilness.getRecoment().equals(t.getRecoment())) {
                changeLog.append("Recoment: '").append(existingUilness.getRecoment()).append("' -> '").append(t.getRecoment()).append("'; ");
                existingUilness.setRecoment(t.getRecoment());
                isUpdated = true;
            }

            if (!isUpdated) {
                return new BaseResponse(304, "Không có thay đổi nào cần cập nhật", null); // No changes
            }

            // Proceed with updating the Uilness
            Uilness updatedUilness = super.update(existingUilness);

            // Log the successful update and changes
            logService.logAction(ELogType.UPDATE_UILNESS,
                    "Cập nhật thông tin bệnh " + updatedUilness.getName() + " thành công: Dữ liệu thay đổi " + changeLog,
                    "success");

            return new BaseResponse().success(updatedUilness);
        } catch (Exception e) {
            logger.error("Error occurred while updating uilness: {}", e.getMessage(), e);
            logService.logAction(ELogType.UPDATE_UILNESS,
                    "Cập nhật thông tin bệnh " + t.getName() + " thất bại: " + e.getMessage(),
                    "fail");
            return new BaseResponse(500, "Có lỗi xảy ra khi cập nhật bệnh", null);
        }
    }

}
