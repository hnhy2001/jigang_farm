package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.model.req.UpdateFunctionRoleReq;
import com.example.jingangfarmmanagement.query.CustomRsqlVisitor;
import com.example.jingangfarmmanagement.repository.FunctionRoleRepository;
import com.example.jingangfarmmanagement.repository.entity.FunctionRole;
import com.example.jingangfarmmanagement.repository.entity.Role;
import com.example.jingangfarmmanagement.service.FunctionRoleService;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FunctionRoleServiceImpl implements FunctionRoleService {
    private static final String DELETED_FILTER = ";status>0";

    @Autowired
    private FunctionRoleRepository functionRoleRepository;

    @Override
    public List<FunctionRole> getByRole(Role role) {
        return functionRoleRepository.findAllByRole(role);
    }

    @Override
    public BaseResponse update(UpdateFunctionRoleReq req) {
        List<FunctionRole> functionRoleList = functionRoleRepository.findAllByRole(req.getRole());
//        Set<Long> functionSet = new HashSet<>();
//        req.getFunctionList().stream().forEach(e -> {
//            functionSet.add(e.getId());
//        });
        functionRoleList.stream().forEach(e->{
            if (req.getFunctionList().contains(e.getFunction().getId())){
                e.setStatus(1);
            }else {
                e.setStatus(0);
            }
        });
        functionRoleRepository.saveAll(functionRoleList);
        return new BaseResponse().success("Phân quyền thành công");

    }

    @Override
    public void createAll(List<FunctionRole> req) {
        functionRoleRepository.saveAll(req);
    }

    @Override
    public Page<FunctionRole> search(SearchReq req) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<FunctionRole> spec = rootNode.accept(new CustomRsqlVisitor<FunctionRole>());
        Pageable pageable = getPage(req);
        return functionRoleRepository.findAll(spec, pageable);
    }

    Pageable getPage(SearchReq req) {
        String[] sortList = req.getSort().split(",");
        Sort.Direction direction = sortList[1].equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return req.getSize() != null
                ?
                PageRequest.of(req.getPage(), req.getSize(), direction, sortList[0])
                :
                Pageable.unpaged();
    }
}
