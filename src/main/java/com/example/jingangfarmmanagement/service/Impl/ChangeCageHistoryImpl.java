package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.ChangeCageHistoryRepository;
import com.example.jingangfarmmanagement.repository.PetRepository;
import com.example.jingangfarmmanagement.repository.entity.ChangeCageHistory;
import com.example.jingangfarmmanagement.service.ChangeCageHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChangeCageHistoryImpl extends BaseServiceImpl<ChangeCageHistory> implements ChangeCageHistoryService {
    private static final String DELETED_FILTER =";status>-1" ;

    @Autowired
    PetRepository petRepository;
    @Autowired
    ChangeCageHistoryRepository changeCageHistoryRepository;
    @Autowired
    private EntityManager entityManager;
    @Override
    protected BaseRepository<ChangeCageHistory> getRepository() {
        return changeCageHistoryRepository;
    }

    @Override
    public BaseResponse searchChangeCageHistoryCustom(String cageNameFrom, String cageNameTo,
                                                                               String farmNameFrom, String farmNameTo,
                                                                               Long minDate, Long maxDate,
                                                                               String petName, Pageable pageable) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<ChangeCageHistory> query = cb.createQuery(ChangeCageHistory.class);
            Root<ChangeCageHistory> root = query.from(ChangeCageHistory.class);
            List<Predicate> predicates = new ArrayList<>();

            if (cageNameFrom != null && !cageNameFrom.isEmpty()) {
                predicates.add(cb.like(root.get("cageNameFrom"), "%" + cageNameFrom + "%"));
            }
            if (cageNameTo != null && !cageNameTo.isEmpty()) {
                predicates.add(cb.like(root.get("cageNameTo"), "%" + cageNameTo + "%"));
            }
            if (farmNameFrom != null && !farmNameFrom.isEmpty()) {
                predicates.add(cb.like(root.get("farmNameFrom"), "%" + farmNameFrom + "%"));
            }
            if (farmNameTo != null && !farmNameTo.isEmpty()) {
                predicates.add(cb.like(root.get("farmNameTo"), "%" + farmNameTo + "%"));
            }
            if (minDate != null && maxDate != null) {
                predicates.add(cb.between(root.get("createDate"), minDate, maxDate));
            }
            if (petName != null && !petName.isEmpty()) {
                predicates.add(cb.like(root.get("pet").get("name"), "%" + petName + "%"));
            }

            query.where(predicates.toArray(new Predicate[0]));
            query.groupBy(root.get("createDate"));

            TypedQuery<ChangeCageHistory> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult((int) pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());

            List<ChangeCageHistory> resultList = typedQuery.getResultList();

            // Count query
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<ChangeCageHistory> countRoot = countQuery.from(ChangeCageHistory.class);
            countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
            Long count = entityManager.createQuery(countQuery).getSingleResult();

            Page<ChangeCageHistory> pageResult = new PageImpl<>(resultList, pageable, count);

            return new BaseResponse(200, "Success", pageResult);
        } catch (Exception e) {
            List<String> errorMessages = new ArrayList<>();
            errorMessages.add(e.getMessage());
            return new BaseResponse(500, "Error", String.join("; ", errorMessages));
        }
    }
}
