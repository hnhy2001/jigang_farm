package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.SearchReq;
import com.example.jingangfarmmanagement.query.CustomRsqlVisitor;
import com.example.jingangfarmmanagement.repository.BOLRepository;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.service.BOLService;
import com.example.jingangfarmmanagement.service.MaterialsBOLService;
import com.example.jingangfarmmanagement.service.MaterialsService;
import com.example.jingangfarmmanagement.service.MaterialsWarehouseService;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class BOLServiceImpl extends BaseServiceImpl<BOL> implements BOLService {
    private static final String DELETED_FILTER = ";status>-1";

    @Autowired
    BOLRepository bolRepository;

    @Autowired
    MaterialsService materialsService;

    @Autowired
    MaterialsWarehouseService materialsWarehouseService;

    @Autowired
    MaterialsBOLService materialsBOLService;

    @Override
    protected BaseRepository<BOL> getRepository() {
        return bolRepository;
    }

    @Override
    @Transactional
    public BaseResponse customCreate(BOL bol) throws Exception {
        if(bol.getType() != 1 && bol.getType() != 2){
            return new BaseResponse().fail("Loại phiếu không đúng!");
        }

        if (bol.getConsignee() == null){
            return new BaseResponse().fail("Người nhận hàng không được để trống");
        }

        if (bol.getMaterialsList().isEmpty()){
            return new BaseResponse().fail("Nguyên vật liệu không được để trống");
        }

        if (!bol.getMaterialsList().stream().filter(e -> e.getActualQuantity().compareTo(0L) < 0 || e.getEstimateQuantity().compareTo(0L) < 0 || e.getUnitPridce().compareTo(0L) < 0 || e.getPrice().compareTo(0L) < 0).collect(Collectors.toList()).isEmpty()){
            return new BaseResponse().fail("Các giá trị số lượng nguyên liệu không được nhỏ hơn 0");
        }
        if (!bol.getMaterialsList().stream().filter(e -> e.getWarehouse() == null).collect(Collectors.toList()).isEmpty()){
            return new BaseResponse().fail("Kho chứa nguyên vật liệu không được để trống");
        }
        bol.setStatus(1);
        bol.setCreateDate(DateUtil.getCurrenDateTime());
        bol.setUpdateDate(DateUtil.getCurrenDateTime());
        BOL result = bolRepository.save(bol);
        List<MaterialsBOL> materialsBOLList = new ArrayList<>();
        if (bol.getType() == 1){
            List<Materials> materialsList = materialsService.createMaterials(bol.getMaterialsList());
            if (materialsList.isEmpty()){
                throw new Exception();
            }
            materialsList.stream().forEach(e -> {
                MaterialsBOL materialsBOL = new MaterialsBOL();
                materialsBOL.setBol(result);
                materialsBOL.setMaterials(e);
                materialsBOL.setStatus(1);
                materialsBOL.setCreateDate(DateUtil.getCurrenDateTime());
                materialsBOL.setUpdateDate(DateUtil.getCurrenDateTime());
                materialsBOL.setUnitPridce(e.getUnitPridce());
                materialsBOL.setPrice(e.getPrice());
                materialsBOL.setActualQuantity(e.getActualQuantity());
                materialsBOL.setEstimateQuantity(e.getEstimateQuantity());
                materialsBOL.setWarehouse(e.getWarehouse());
                materialsBOL.setExpirationDate(e.getExpirationDate());
                materialsBOLList.add(materialsBOL);
            });

            if (materialsBOLService.createMaterialsBOLList(materialsBOLList).isEmpty()){
                throw new Exception();
            }
            List<MaterialsWarehouse> materialsWarehouses = new ArrayList<>();
            materialsList.stream().forEach(e -> {
                MaterialsWarehouse materialsWarehouse = new MaterialsWarehouse();
                materialsWarehouse.setWarehouse(e.getWarehouse());
                materialsWarehouse.setMaterials(e);
                materialsWarehouse.setQuatity(e.getActualQuantity());
                materialsWarehouse.setStatus(1);
                materialsWarehouse.setCreateDate(DateUtil.getCurrenDateTime());
                materialsWarehouse.setUpdateDate(DateUtil.getCurrenDateTime());
                materialsWarehouses.add(materialsWarehouse);
            });

            if (materialsWarehouseService.createMaterialsWarehouses(materialsWarehouses).isEmpty()){
                throw new Exception();
            }
        }

        if (bol.getType() == 2){
            AtomicInteger checkQuantity = new AtomicInteger();
            List<MaterialsWarehouse> materialsWarehouseList = materialsWarehouseService.getAll();
            bol.getMaterialsList().stream().forEach(e -> {
                MaterialsBOL materialsBOL = new MaterialsBOL();
                materialsBOL.setBol(result);
                materialsBOL.setMaterials(e);
                materialsBOL.setStatus(1);
                materialsBOL.setUpdateDate(DateUtil.getCurrenDateTime());
                materialsBOL.setCreateDate(DateUtil.getCurrenDateTime());
                materialsBOL.setPrice(e.getPrice());
                materialsBOL.setActualQuantity(e.getActualQuantity());
                materialsBOL.setEstimateQuantity(e.getEstimateQuantity());
                materialsBOL.setWarehouse(e.getWarehouse());
                materialsBOL.setExpirationDate(e.getExpirationDate());
                materialsBOLList.add(materialsBOL);
            });
            if (materialsBOLService.createMaterialsBOLList(materialsBOLList).isEmpty()){
                throw new Exception();
            }
            List<MaterialsWarehouse> updateMaterialsWarehouseList = new ArrayList<>();
            materialsWarehouseList.stream().forEach(e -> {
                bol.getMaterialsList().stream().forEach(m -> {
                    if (e.getMaterials().getId().equals(m.getId())){
                        if (e.getQuatity() < m.getActualQuantity()){
                            checkQuantity.set(1);
                        }else{
                            e.setQuatity(e.getQuatity() - m.getActualQuantity());
                            e.setUpdateDate(DateUtil.getCurrenDateTime());
                            updateMaterialsWarehouseList.add(e);
                        }
                    }
                });
            });
            if (checkQuantity.get() == 1){
                throw new Exception();
            }
            if (materialsWarehouseService.createMaterialsWarehouses(updateMaterialsWarehouseList).isEmpty()){
                throw new Exception();
            }
        }


        return new BaseResponse().success(result);
    }

    @Override
    @Transactional
    public BaseResponse customUpdate(BOL req) throws Exception {
//        if (req.getId() == null){
//            return new BaseResponse().fail("Id phiếu không được để trống");
//        }
//        BOL bol = super.getById(req.getId());
        return null;
    }

    @Override
    @Transactional
    public BaseResponse cancel(Long id) throws Exception {
        BOL bol = super.getById(id);
        List<MaterialsWarehouse> materialsWarehouseList = materialsWarehouseService.getAll();
        List<MaterialsBOL> materialsBOLList = materialsBOLService.getAll();
        if (bol == null){
            return new BaseResponse().fail("Không tồn tại phiếu với id tương ứng");
        }
        materialsBOLList.stream().forEach(e -> {
            if (e.getBol().getId().equals(bol.getId())){
                materialsWarehouseList.stream().forEach(mW -> {
                    if (mW.getMaterials().getId().equals(e.getMaterials().getId())){
                        if (bol.getType() == 1){
                            mW.setQuatity(mW.getQuatity()-e.getActualQuantity());
                            mW.setUpdateDate(DateUtil.getCurrenDateTime());
                        }else {
                            mW.setQuatity(mW.getQuatity() + e.getActualQuantity());
                            mW.setUpdateDate(DateUtil.getCurrenDateTime());
                        }
                    }
                });
            }
        });

        bol.setStatus(0);
        bolRepository.save(bol);
        materialsWarehouseService.createMaterialsWarehouses(materialsWarehouseList);
        return new BaseResponse().success("Hủy thành công phiếu");
    }

    @Override
    public Page<BOL> search(SearchReq req) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<BOL> spec = rootNode.accept(new CustomRsqlVisitor<BOL>());
        Pageable pageable = getPage(req);
        Page<BOL> bolPage = this.getRepository().findAll(spec, pageable);
        List<MaterialsBOL> materialsBOLList = materialsBOLService.getAll();
        bolPage.getContent().stream().forEach(e -> {
            List<Materials> materialsList = new ArrayList<>();
            materialsBOLList.stream().forEach(mB -> {
                if (e.getId().equals(mB.getBol().getId())){
                    mB.getMaterials().setUnitPridce(mB.getUnitPridce());
                    mB.getMaterials().setPrice(mB.getPrice());
                    mB.getMaterials().setActualQuantity(mB.getActualQuantity());
                    mB.getMaterials().setEstimateQuantity(mB.getEstimateQuantity());
                    mB.getMaterials().setWarehouse(mB.getWarehouse());
                    materialsList.add(mB.getMaterials());
                }
            });
            e.setMaterialsList(materialsList);
        });
        System.out.println(bolPage);
        return bolPage;
    }
}

