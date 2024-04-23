package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.projection.CageProjection;
import com.example.jingangfarmmanagement.projection.FarmProjection;
import com.example.jingangfarmmanagement.query.CustomRsqlVisitor;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.CageRepository;
import com.example.jingangfarmmanagement.repository.entity.Cage;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.service.CageService;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CageServiceImpl extends BaseServiceImpl<Cage> implements CageService {
    private static final String DELETED_FILTER = ";status>-1";

    @Autowired
    private CageRepository cageReponsitory;

    @Override
    protected BaseRepository<Cage> getRepository() {
        return cageReponsitory;
    }

    @Override
    public Page<CageProjection> customSearch(SearchReq req) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<Cage> spec = rootNode.accept(new CustomRsqlVisitor<>());
        Pageable pageable = getPage(req);
        return cageReponsitory.findAll(spec, CageProjection.class, pageable);
    }
}
