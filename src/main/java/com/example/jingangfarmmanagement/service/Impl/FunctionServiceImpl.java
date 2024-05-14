package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.FunctionReq;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.FunctionRepository;
import com.example.jingangfarmmanagement.repository.entity.Function;
import com.example.jingangfarmmanagement.repository.entity.FunctionRole;
import com.example.jingangfarmmanagement.service.FunctionRoleService;
import com.example.jingangfarmmanagement.service.FunctionService;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FunctionServiceImpl extends BaseServiceImpl<Function> implements FunctionService {
    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private FunctionRoleService functionRoleService;
    @Override
    protected BaseRepository<Function> getRepository() {
        return functionRepository;
    }



    @Override
    public BaseResponse customCreate(FunctionReq functionReq) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        Function function = modelMapper.map(functionReq, Function.class);
        function.setStatus(1);
        function.setCreateDate(DateUtil.getCurrenDateTime());
        Function result = functionRepository.save(function);
        FunctionRole functionRole = new FunctionRole();
        functionRole.setFunction(result);
        functionRole.setRole(functionReq.getRole());
        functionRoleService.create(functionRole);
        return new BaseResponse().success(result);
    }
}
