package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.projection.FarmProjection;
import com.example.jingangfarmmanagement.query.CustomRsqlVisitor;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.FarmRepository;
import com.example.jingangfarmmanagement.repository.entity.Farm;
import com.example.jingangfarmmanagement.service.FarmService;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmServiceImpl extends BaseServiceImpl<Farm> implements FarmService {
    private static final String DELETED_FILTER = ";status>-1";

    @Autowired
    private FarmRepository farmReponsitory;

    @Override
    protected BaseRepository<Farm> getRepository() {
        return farmReponsitory;
    }

    @Override
    public Page<FarmProjection> customSearch(SearchReq req) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<Farm> spec = rootNode.accept(new CustomRsqlVisitor<>());
        Pageable pageable = getPage(req);
        return farmReponsitory.findAll(spec, FarmProjection.class, pageable);
    }

    @Override
    public List<Farm> finByFinter(SearchReq req) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<Farm> spec = rootNode.accept(new CustomRsqlVisitor<>());
        return List.of();
    }
}
