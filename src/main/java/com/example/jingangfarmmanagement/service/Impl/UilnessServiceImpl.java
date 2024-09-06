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
        try{
        if (t.getScore() > 6 || t.getScore() < 0) {
            return new BaseResponse().fail("Score không được lớn hơn 5 hoc nhỏ hơn 0");
        }
        Uilness uilness =super.update(t);
            logService.logAction(ELogType.UPDATE_UILNESS,
                    "Cập nhật thông tin bệnh " + uilness.getName() + " thành công" ,
                    "success");
        return new BaseResponse().success(uilness);
    }catch (Exception e){
        logger.error("Error occurred while update uliness: {}", e.getMessage(), e);
        logService.logAction(ELogType.UPDATE_UILNESS,
                "Cập nhật thông tin bệnh" + t.getName() + " thất bại" + e.getMessage(),
                "fail");
        return new BaseResponse(500, "Có lỗi xảy ra khi cập nhật bệnh", null);
    }
    }
}
