package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.ChangeCageHistoryDTO;
import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.ChangeCageHistoryRepository;
import com.example.jingangfarmmanagement.repository.PetRepository;
import com.example.jingangfarmmanagement.repository.entity.ChangeCageHistory;
import com.example.jingangfarmmanagement.repository.entity.Pet;
import com.example.jingangfarmmanagement.service.ChangeCageHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
            String sql = "SELECT\n" +
                    "    DATE_FORMAT(FROM_UNIXTIME(c.created_date / 1000), '%Y%m%d') AS createDate,\n" +
                    "    GROUP_CONCAT(CONCAT(c.id, '|', c.cage_name_from, '|', c.cage_name_to, '|', c.farm_name_from, '|', c.farm_name_to, '|', c.status, '|', c.pet_id, '|', c.created_date) SEPARATOR ';') AS changeCageHistories\n" +
                    "FROM\n" +
                    "    change_cage_history c\n" +
                    "LEFT JOIN\n" +
                    "    pet p ON c.pet_id = p.id\n" +
                    "WHERE\n" +
                    "    (:cageNameFrom IS NULL OR c.cage_name_from LIKE :cageNameFrom)\n" +
                    "    AND (:cageNameTo IS NULL OR c.cage_name_to LIKE :cageNameTo)\n" +
                    "    AND (:farmNameFrom IS NULL OR c.farm_name_from LIKE :farmNameFrom)\n" +
                    "    AND (:farmNameTo IS NULL OR c.farm_name_to LIKE :farmNameTo)\n" +
                    "    AND (:minDate IS NULL OR :maxDate IS NULL OR FROM_UNIXTIME(c.created_date / 1000) BETWEEN FROM_UNIXTIME(:minDate / 1000) AND FROM_UNIXTIME(:maxDate / 1000))\n" +
                    "    AND (:petName IS NULL OR p.name LIKE :petName)\n" +
                    "    AND c.status >= 1\n" +
                    "GROUP BY\n" +
                    "    DATE_FORMAT(FROM_UNIXTIME(c.created_date / 1000), '%Y%m%d'), c.id, c.cage_name_from, c.cage_name_to, c.farm_name_from, c.farm_name_to, c.status, c.pet_id";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("cageNameFrom", cageNameFrom != null ? "%" + cageNameFrom + "%" : null);
            query.setParameter("cageNameTo", cageNameTo != null ? "%" + cageNameTo + "%" : null);
            query.setParameter("farmNameFrom", farmNameFrom != null ? "%" + farmNameFrom + "%" : null);
            query.setParameter("farmNameTo", farmNameTo != null ? "%" + farmNameTo + "%" : null);
            query.setParameter("minDate", minDate);
            query.setParameter("maxDate", maxDate);
            query.setParameter("petName", petName != null ? "%" + petName + "%" : null);

            // Calculate total count
            List<Object[]> results = query.getResultList();
            int totalCount = results.size();

            // Apply pagination
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());

            results = query.getResultList(); // Re-fetch results after pagination

            List<ChangeCageHistoryDTO> dtos = new ArrayList<>();

            for (Object[] result : results) {
                String createDateStr = (String) result[0]; // Chuỗi "yyyymmdd"
                Long createDate = Long.parseLong(createDateStr);

                String changeCageHistoriesStr = (String) result[1];
                String[] changeCageHistoriesArr = changeCageHistoriesStr.split(";");

                List<ChangeCageHistory> changeCageHistories = new ArrayList<>();
                for (String str : changeCageHistoriesArr) {
                    String[] fields = str.split("\\|");
                    ChangeCageHistory history = new ChangeCageHistory();
                    history.setId(Long.parseLong(fields[0]));
                    history.setCageNameFrom(fields[1]);
                    history.setCageNameTo(fields[2]);
                    history.setFarmNameFrom(fields[3]);
                    history.setFarmNameTo(fields[4]);
                    history.setStatus(Integer.parseInt(fields[5]));
                    Long petId = Long.parseLong(fields[6]);
                    Long createdDate = Long.parseLong(fields[7]); // 'created_date'

                    // Fetch Pet entity from database using petId
                    Pet pet = entityManager.find(Pet.class, petId);
                    history.setPet(pet);
                    history.setCreateDate(createdDate); // Set created_date

                    changeCageHistories.add(history);
                }

                dtos.add(new ChangeCageHistoryDTO(createDate, changeCageHistories));
            }
            Page<ChangeCageHistoryDTO> pageResult = new PageImpl<>(dtos, pageable, totalCount);

            return new BaseResponse(200, "Success", pageResult);
        } catch (Exception e) {
            List<String> errorMessages = new ArrayList<>();
            errorMessages.add(e.getMessage());
            return new BaseResponse(500, "Error", String.join("; ", errorMessages));
        }
    }
}
