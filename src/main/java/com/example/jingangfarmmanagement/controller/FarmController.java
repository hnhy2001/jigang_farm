package com.example.jingangfarmmanagement.controller;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.service.BaseService;
import com.example.jingangfarmmanagement.service.FarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("farm")
@PreAuthorize("@appAuthorizer.authorize(authentication, 'VIEW', this)")
public class FarmController extends BaseController<Farm> {
    @Autowired
    FarmService farmService;

    @Override
    protected BaseService<Farm> getService() {
        return farmService;
    }
    @GetMapping("/custom/search")
    public Page<Farm> customSearch(@RequestParam String filter,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam String sort,
                                   @RequestParam(required = false) Long userId) {
        SearchReq req = new SearchReq();
        req.setFilter(filter);
        req.setPage(page);
        req.setSize(size);
        req.setSort(sort);
        return farmService.customSearch(req, userId);
    }

}
