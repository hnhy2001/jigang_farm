package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.repository.entity.BaseEntity;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.service.BaseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
//@PreAuthorize("@appAuthorizer.authorize(authentication, #action, this)")
public abstract class BaseController<T extends BaseEntity> {
    protected abstract BaseService<T> getService();

    @PostMapping("/create")
    public BaseResponse create(@RequestBody T t) throws Exception {
        return new BaseResponse(200, "Tạo thành công!", this.getService().create(t));
    }

    @GetMapping("/search")
//    @PreAuthorize("@appAuthorizer.authorize(authentication, 'VIEW', this)")
    public BaseResponse search(SearchReq req) {
        return new BaseResponse(200, "Lấy dữ liệu thành công!", this.getService().search(req));
    }

    @GetMapping("/detail")
    public BaseResponse getById(@RequestParam(value = "id") String id) throws Exception {
        return new BaseResponse(200, "Lấy dữ liệu thành công!", this.getService().getById(id));
    }

    @PutMapping("/update")
    public BaseResponse update(@RequestBody T t) throws Exception {
        return new BaseResponse(200, "Cập nhật thành công!", this.getService().update(t));
    }


    @DeleteMapping("/delete")
    public BaseResponse deleteById(@RequestParam(name = "id") String id) {
        this.getService().delete(id);
        return new BaseResponse(200, "Xóa thành công!");
    }
}