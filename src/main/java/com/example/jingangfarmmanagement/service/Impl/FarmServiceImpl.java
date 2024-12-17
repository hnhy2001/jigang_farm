package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.projection.FarmProjection;
import com.example.jingangfarmmanagement.query.CustomRsqlVisitor;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.FarmRepository;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.service.FarmService;
import com.example.jingangfarmmanagement.service.PermissionCageService;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FarmServiceImpl extends BaseServiceImpl<Farm> implements FarmService {
    private static final String DELETED_FILTER = ";status>-1";

    @Autowired
    private FarmRepository farmRepository;
    @Autowired
    PermissionCageService permissionCageService;

    @Override
    protected BaseRepository<Farm> getRepository() {
        return farmRepository;
    }

    @Override
    public Page<Farm> customSearch(SearchReq req,Long userId) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<Farm> spec = rootNode.accept(new CustomRsqlVisitor<>());
        Pageable pageable = getPage(req);
        List<Farm> farmList = farmRepository.findAll(spec, pageable).getContent();
        List<Farm> farms;
        if(userId == null) {
            farms = farmList;
        } else {
            List<Cage> cagePermissions= permissionCageService.getPermissionCageByUserId(userId);
            farms = cagePermissions.stream()
                    .map(Cage::getFarm)
                    .filter(farmList::contains)
                    .distinct()
                    .collect(Collectors.toList());
        }
        return new PageImpl<>(farms, pageable, farms.size());
    }

    @Override
    public List<Farm> findByFilter() {
        return farmRepository.findAll();
    }
}
