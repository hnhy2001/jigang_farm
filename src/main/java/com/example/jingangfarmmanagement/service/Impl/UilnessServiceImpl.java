package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.UilnessRepository;
import com.example.jingangfarmmanagement.repository.entity.Uilness;
import com.example.jingangfarmmanagement.service.UilnessService;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UilnessServiceImpl extends BaseServiceImpl<Uilness> implements UilnessService {
    @Autowired
    UilnessRepository uilnessRepository;
    @Override
    protected BaseRepository<Uilness> getRepository() {
        return uilnessRepository;
    }

    @Override
    public BaseResponse customeCreate(Uilness t) throws Exception {
        if (t.getScore() > 6 || t.getScore() < 0) {
            return new BaseResponse().fail("Score không được lớn hơn 5 hoc nhỏ hơn 0");
        }

        return new BaseResponse().success(super.create(t));
    }

    @Override
    public BaseResponse customeUpdate(Uilness t) throws Exception {
        if (t.getScore() > 6 || t.getScore() < 0) {
            return new BaseResponse().fail("Score không được lớn hơn 5 hoc nhỏ hơn 0");
        }
        return new BaseResponse().success(super.update(t));
    }
}
