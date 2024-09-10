package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.config.logger.Loggable;
import com.example.jingangfarmmanagement.constants.Status;
import com.example.jingangfarmmanagement.model.BaseResponse;
import com.example.jingangfarmmanagement.model.req.*;
import com.example.jingangfarmmanagement.model.response.StatisticQuantityUnilessItemRes;
import com.example.jingangfarmmanagement.model.response.StatisticQuantityUnilessRes;
import com.example.jingangfarmmanagement.model.response.StatisticQuantityUnilessTypeItemRes;
import com.example.jingangfarmmanagement.model.response.StatisticQuantityUnilessTypeRes;
import com.example.jingangfarmmanagement.projection.PetProjection;
import com.example.jingangfarmmanagement.query.CustomRsqlVisitor;
import com.example.jingangfarmmanagement.repository.*;
import com.example.jingangfarmmanagement.repository.entity.*;
import com.example.jingangfarmmanagement.repository.entity.Enum.ELogType;
import com.example.jingangfarmmanagement.service.CageService;
import com.example.jingangfarmmanagement.service.FarmService;
import com.example.jingangfarmmanagement.service.PetService;
import com.example.jingangfarmmanagement.service.UilnessService;
import com.example.jingangfarmmanagement.uitl.DateUtil;
import com.example.jingangfarmmanagement.uitl.ObjectMapperUtils;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl extends BaseServiceImpl<Pet> implements PetService {
    private static final Logger logger = LogManager.getLogger(PetService.class);

    private static final String DELETED_FILTER = ";status>-1";

    @Autowired
    private PetRepository petRepository;
    @Autowired
    PetStatisticImpl petStatistic;
    @Autowired
    private ChangeCageHistoryRepository changeCageHistoryRepository;

    @Autowired
    private UilnessService uilnessService;

    @Autowired
    private CageRepository cageService;
    @Autowired
    private CageRepository cageRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private FarmRepository farmRepository;

    @Override
    protected BaseRepository<Pet> getRepository() {
        return petRepository;
    }

    @Override
    public Page<PetProjection> customSearch(SearchReq req) {
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<Pet> spec = rootNode.accept(new CustomRsqlVisitor<>());
        Pageable pageable = getPage(req);
        return petRepository.findAll(spec, PetProjection.class, pageable);
    }

    @Override
    public PetProjection customDetails(Long id) {
        return null;
    }

    @Override
    public List<Pet> getByStatus(int status) {
        return petRepository.findAllByStatus(1);
    }
    @Override
    public void delete(Long id) {
        Pet pet = this.getRepository().findAllById(id);
        try {
            pet.setStatus(Status.DELETED);
            Pet savedPet = this.getRepository().save(pet);
            logService.logAction(ELogType.DELETE_PET,"Xóa thông tin con vật "+ savedPet.getName()+  " chuồng "+ savedPet.getCage().getName()+" trại "+savedPet.getCage().getFarm().getName()+" thành công ","success" );
        } catch (Exception e) {
            logger.error("Error occurred while delete pet: {}", e.getMessage(), e);
            logService.logAction(ELogType.DELETE_PET,"Xóa thông tin con vật "+ pet.getName()+ " chuồng "+ pet.getCage().getName()+" trại "+pet.getCage().getFarm().getName()+" thất bại "+e.getMessage(),"fail" );
        }
    }


    @Override
    @Transactional
    public BaseResponse changeCage(ChangeCageReq changeCageReq) {
        List<Pet> pets = petRepository.findByIdIn(changeCageReq.getPetList());
        List<ChangeCageHistory> changeHistories = pets.stream().map(pet -> {
            ChangeCageHistory changeCageHistory = new ChangeCageHistory();
            changeCageHistory.setPet(pet);
            changeCageHistory.setStatus(1);
            changeCageHistory.setFarmNameFrom(pet.getCage().getFarm().getName());
            changeCageHistory.setFarmNameTo(changeCageReq.getCage().getFarm().getName());
            changeCageHistory.setCageNameFrom(pet.getCage().getName());
            changeCageHistory.setCageNameTo(changeCageReq.getCage().getName());
            changeCageHistory.setCreateDate(DateUtil.getCurrenDateTime());
            changeCageHistory.setNote(changeCageReq.getNote());
            pet.setCage(changeCageReq.getCage());
            return changeCageHistory;
        }).collect(Collectors.toList());

        changeCageHistoryRepository.saveAll(changeHistories);
        petRepository.saveAll(pets);
        return new BaseResponse(200, "OK", "Chuyển chuồng thành công");
    }
//    @Loggable(ELogType.CREATE_PET)
    @Override
    public BaseResponse createPet(Pet pet) {
        try {
            logger.info("Entering createPet method with pet: {}", pet);
            Pet existingPet = petRepository.findByCageAndFarmAndName(pet.getName(), pet.getCage().getName(), pet.getCage().getFarm().getName());

            if (existingPet != null && !existingPet.getId().equals(pet.getId())) {
                return new BaseResponse(500, "Tên vật nuôi đã tồn tại", null);
            }
            pet.setStatus(Status.ACTIVE);
            pet.setCreateDate(DateUtil.getCurrenDateTime());
            pet.setUpdateHeathDate(DateUtil.getCurrenDateTime());
            pet.setLastDateUpdate(DateUtil.getCurrenDateTime());
            pet.setPregnantDateUpdate(DateUtil.getCurrenDateTime());
            if (pet.getUilness() == null || pet.getUilness().isBlank()) {
                pet.setPetCondition(2);
            } else {
                pet.setPetCondition(1);
            }
            petStatistic.syncDateOfBirthWithPetIds(List.of(pet));
            pet.setName(pet.getName());
            Pet savedPet = petRepository.save(pet);
            logger.info("Pet saved successfully with name: {}", savedPet.getName());
            logService.logAction(ELogType.CREATE_PET,"Thêm thông tin con vật "+ savedPet.getName()+ " chuồng "+ savedPet.getCage().getName()+" trại "+savedPet.getCage().getFarm().getName()+" thành công \n","success" );
            return new BaseResponse(200, "Thêm mới vật nuôi thành công", savedPet);
        } catch (Exception e) {
            logger.error("Error occurred while creating pet: {}", e.getMessage(), e);
            logService.logAction(ELogType.CREATE_PET,"Thêm thông tin con vật "+ pet.getName()+ " chuồng "+ pet.getCage().getName()+" trại "+pet.getCage().getFarm().getName()+" thất bại \n"+ e.getMessage(),"fail" );
            return new BaseResponse(500, "Có lỗi xảy ra khi thêm mới vật nuôi", null);
        }
    }

    @Override
    public BaseResponse updatePet(Pet pet) {
        Optional<Pet> entityMy = null;
        try {
            entityMy = petRepository.findById(pet.getId());
            Pet entity = entityMy.orElseThrow(() -> new RuntimeException("Pet not found"));
            Pet existingPet = petRepository.findByCageAndFarmAndName(pet.getName(), entity.getCage().getName(), entity.getCage().getFarm().getName());

            if (existingPet != null && !existingPet.getId().equals(pet.getId())) {
                return new BaseResponse(500, "Tên vật nuôi đã tồn tại", null);
            }

            ObjectMapperUtils.map(pet, entity);
            entity.setUpdateDate(DateUtil.getCurrenDateTime());
            entity.setUpdateHeathDate(DateUtil.getCurrenDateTime());
            entity.setLastDateUpdate(DateUtil.getCurrenDateTime());
            entity.setPregnantDateUpdate(DateUtil.getCurrenDateTime());
            entity.setCreateDate(entity.getCreateDate());
            entity.setCage(entity.getCage());
            petStatistic.syncDateOfBirthWithPetIds(List.of(entity));
            entity.setPetCondition(1);
            entity.setStatus(pet.getUilness() == null || pet.getUilness().isBlank() ? 2 : 1);

            // Save the updated pet
            Pet savedPet = petRepository.save(entity);

            // Log changes
            StringBuilder changeLog = new StringBuilder("Cập nhật: " + entity.getName() + " ");
            if (!entity.getName().equals(savedPet.getName())) {
                changeLog.append("Name: '").append(entity.getName()).append("' -> '").append(savedPet.getName()).append("'; ");
            }
            if (!entity.getCage().getName().equals(savedPet.getCage().getName())) {
                changeLog.append("Cage: '").append(entity.getCage().getName()).append("' -> '").append(savedPet.getCage().getName()).append("'; ");
            }
            if (!entity.getUilness().equals(savedPet.getUilness())) {
                changeLog.append("Bệnh: '").append(entity.getUilness()).append("' -> '").append(savedPet.getUilness()).append("'; ");
            }
            if (!(entity.getWeight() == savedPet.getWeight())) {
                changeLog.append("Cân nặng: '").append(entity.getWeight()).append("' -> '").append(savedPet.getWeight()).append("'; ");
            }
            if (!(Objects.equals(entity.getParentDad(), savedPet.getParentDad()))) {
                changeLog.append("Mã cha: '").append(entity.getParentDad()).append("' -> '").append(savedPet.getParentDad()).append("'; ");
            }
            if (!(Objects.equals(entity.getParentMon(), savedPet.getParentMon()))) {
                changeLog.append("Mã mẹ: '").append(entity.getParentMon()).append("' -> '").append(savedPet.getParentMon()).append("'; ");
            }

            logger.info(changeLog.toString());

            logService.logAction(ELogType.UPDATE_PET,
                    "Cập nhật thông tin con vật " + savedPet.getName() + " chuồng " + savedPet.getCage().getName() + " trại " + savedPet.getCage().getFarm().getName() + " thành công: Dữ liệu thay đổi" + changeLog,
                    "success");

            return new BaseResponse(200, "Cập nhật vật nuôi thành công", savedPet);
        } catch (Exception e) {
            logger.error("Error occurred while updating pet: {}", e.getMessage(), e);
            logService.logAction(ELogType.UPDATE_PET,
                    "Cập nhật thông tin con vật " + entityMy.get().getName() + " chuồng " + entityMy.get().getCage().getName() + " trại " + entityMy.get().getCage().getFarm().getName() + " thất bại" + e.getMessage(),
                    "fail");
            return new BaseResponse(500, "Có lỗi xảy ra khi cập nhật vật nuôi", null);
        }
    }

    @Override
    public BaseResponse updatePetWeight(UpdateWeightPetReq updateWeightPet) {
        List<Pet> pets = petRepository.findByIdIn(updateWeightPet.getPetIds());
        for (Pet pet : pets) {
            pet.setWeight(updateWeightPet.getWeight());
        }
        return new BaseResponse(200, "Cập nhật cân nặng vật nuôi thành công", petRepository.saveAll((pets)));
    }

    @Override
    public BaseResponse updatePetStatus(List<ChangeStatusPetReq> changeStatusPetReqs) {
        List<Pet> pets = new ArrayList<>();
        for (var changeStatusPetReq : changeStatusPetReqs) {
            Optional<Pet> pet = petRepository.findById(changeStatusPetReq.getPetId());
            if (pet.isPresent()) {
                pet.get().setStatus(changeStatusPetReq.getStatus());
                pet.get().setNote(changeStatusPetReq.getNote());
                pet.get().setUpdateDate(DateUtil.getCurrenDateTime());
                pet.get().setPregnantDateUpdate(changeStatusPetReq.getPregnantDateUpdate());
                pets.add(pet.get());
            }
        }
        return new BaseResponse(200, "Cập nhật trạng thái vật nuôi thành công", petRepository.saveAll((pets)));
    }

    @Override
    public BaseResponse updatePetHealthCondition(List<ChangeStatusPetReq> changeStatusPetReqs) {
        List<Pet> pets = new ArrayList<>();
        for (var changeStatusPetReq : changeStatusPetReqs) {
            Optional<Pet> pet = petRepository.findById(changeStatusPetReq.getPetId());
            if (pet.isPresent()) {
                pet.get().setUpdateDate(DateUtil.getCurrenDateTime());
                pet.get().setUpdateHeathDate(DateUtil.getCurrenDateTime());
                pet.get().setPetCondition(changeStatusPetReq.getPetCondition());
                pet.get().setUpdateHeathDate(DateUtil.getCurrenDateTime());
                pets.add(pet.get());
            }
        }
        return new BaseResponse(200, "Cập nhật trạng thái vật nuôi thành công", petRepository.saveAll((pets)));
    }

    @Override
    public Pet getMenPetByNumbersOfMonth(String month) {
        return petRepository.findMenPetByNunbersOfMonth(month);
    }

    @Override
    public Pet getWomenPetByNumbersOfMonth(String month) {
        return petRepository.findWomenPetByNunbersOfMonth(month);
    }
    @Override
    public BaseResponse findAllPet() {
        List<Pet> petList= petRepository.findAll().stream().filter(pet -> pet.getStatus()!=-1).collect(Collectors.toList());
        return new BaseResponse(200, "Lấy vật nuôi thành công",  petList);
    }

    @Override
    public BaseResponse findPetWithCageAndFarm(List<Long> cageIds, List<Long> farmIds, Long startDate, Long endDate) {
        List<Pet> pets = new ArrayList<>();
        List<Pet> resultPets = new ArrayList<>();
        if (!cageIds.isEmpty()) {
            for (var cageId : cageIds) {
                pets = petRepository.findByCageId(cageId, startDate, endDate);
                resultPets.addAll(pets);
            }
        } else {
            for (var farmId : farmIds) {
                pets = petRepository.findByFarmId(farmId, startDate, endDate);
                resultPets.addAll(pets);
            }
        }

        return new BaseResponse(200, "Lấy vật nuôi thành công", resultPets);
    }

    public String generateName(int sex) {
        String year = DateUtil.getYearNow();
        String month = DateUtil.getMonthNow();
        if (sex == 1) {
            Pet pet = this.getMenPetByNumbersOfMonth(year + month);
            if (pet == null) {
                return year + month + "001";
            } else {
                return year + month + getNumber(Integer.parseInt(String.valueOf(pet.getName().substring(5, 7))) + 2);
            }
        } else {
            Pet pet = this.getWomenPetByNumbersOfMonth(year + month);
            if (pet == null) {
                return year + month + "002";
            } else {
                return year + month + getNumber(Integer.parseInt(String.valueOf(pet.getName().substring(5, 7)))  + 2);
            }
        }
    }

    public String getNumber(int number) {
        if (number < 10) {
            return "00" + number;
        }

        if (number < 100) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    @Override
    public BaseResponse statisticQuantityUnilness(StatisticQuantityUnilessReq req) {
        String filter = getFilter(req);
        Node rootNode = new RSQLParser().parse(filter);
        Specification<Pet> spec = rootNode.accept(new CustomRsqlVisitor<>());
//        List<Pet> allData = petRepository.findAll(spec);
        List<Pet> allData = getStatisticData(petRepository.findAll(spec), req.getFromAge(), req.getToAge());
        List<Uilness> uilnessList = uilnessService.getAll();
        List<StatisticQuantityUnilessItemRes> itemResList = new ArrayList<>();
        for(int i = 0; i <= 6 ; i++) {
            StatisticQuantityUnilessItemRes itemRes = new StatisticQuantityUnilessItemRes();
            itemRes.setQuantity(0L);
            itemRes.setDiseaseLevel(i);
            itemResList.add(itemRes);
        }
        StatisticQuantityUnilessRes result = new StatisticQuantityUnilessRes();
        result.setTotalPet((long) allData.size());
        allData.stream().forEach(e -> {
            uilnessList.stream().forEach(uilness -> {
                if (e.getUilness().equals(uilness.getName())){
                    itemResList.get(uilness.getScore()).setQuantity(itemResList.get(uilness.getScore()).getQuantity() + 1L);
                }
            });
        });
        result.setStatisticQuantityUnilessItem(itemResList);
        return new BaseResponse().success(result);
    }

    @Override
    public BaseResponse statisticQuantityUnilnessType(StatisticQuantityUnilessReq req) {
        String filter = getFilter(req);
        Node rootNode = new RSQLParser().parse(filter);
        Specification<Pet> spec = rootNode.accept(new CustomRsqlVisitor<>());
//        List<Pet> allData = petRepository.findAll(spec);
        List<Pet> allData = getStatisticData(petRepository.findAll(spec), req.getFromAge(), req.getToAge());
        List<Uilness> uilnessList = uilnessService.getAll();
        Set<String> itemSet = new HashSet<>();

        for(int i = 0; i < uilnessList.size() ; i++) {
            if (uilnessList.get(i).getType() != null){
                itemSet.add(uilnessList.get(i).getType());
            }
        }
        List<StatisticQuantityUnilessTypeItemRes> itemResList = new ArrayList<>();
        List<String> stringList = new ArrayList<>(itemSet);
        for(int i = 0; i < stringList.size() ; i++) {
            StatisticQuantityUnilessTypeItemRes item = new StatisticQuantityUnilessTypeItemRes();
            item.setQuantity(0L);
            item.setDiseaseLevel(stringList.get(i));
            itemResList.add(item);
        }
        StatisticQuantityUnilessTypeRes result = new StatisticQuantityUnilessTypeRes();
        result.setTotalPet((long) allData.size());
        allData.stream().forEach(e -> {
            uilnessList.stream().forEach(uilness -> {
                if (e.getUilness().equals(uilness.getName()) && uilness.getType() != null){
                    itemResList.stream().forEach(type -> {
                        if (type.getDiseaseLevel().equals(uilness.getType())){
                            type.setQuantity(type.getQuantity() + 1L);
                        }
                    });
                }
            });
        });
        result.setStatisticQuantityUnilessTypeItem(itemResList);
        return new BaseResponse().success(result);
    }

    public List<Pet> getStatisticData(List<Pet> data, double fromAge, double toAge){
        List<Pet> result = new ArrayList<>();
        data.stream().forEach(e -> {
            if (calculateAge(e.getName()) <= toAge && calculateAge(e.getName()) >= fromAge){
                result.add(e);
            }
        });
        return result;
    }

    public String getFilter(StatisticQuantityUnilessReq req){
        String filter = "";
        if (req.getSex() != 999){
            filter = filter.concat("sex==" + req.getSex()).concat(";");
        }
        if (req.getStatusList() != null){
            if (!req.getStatusList().isEmpty()){
                AtomicReference<String> filterStatus = new AtomicReference<>("status=in=(");
                req.getStatusList().stream().forEach(e -> {
                    filterStatus.set(filterStatus.get() + e + ",");
                });
                filter = filter.concat(filterStatus.get().substring(0, filterStatus.get().length() - 1) + ")").concat(";");
            }
        }
        if(req.getCage() != null){
            filter = filter.concat("cage.id==" + req.getCage().getId()).concat(";");
        }

        if(req.getFarm() != null){
            SearchReq cageSearch = new SearchReq();
            cageSearch.setFilter(("farm.id==" + req.getFarm().getId()).concat(";status>-1"));
            cageSearch.setPage(0);
            cageSearch.setSize(9999999);
            cageSearch.setSort("id,asc");
            Node rootNode = new RSQLParser().parse(cageSearch.getFilter());
            Specification<Cage> spec = rootNode.accept(new CustomRsqlVisitor<>());
            List<Cage> cageList  = cageRepository.findAll(spec);
            if (!cageList.isEmpty()){
                AtomicReference<String> filterCage = new AtomicReference<>("cage.id=in=(");
                cageList.stream().forEach(e -> {
                    filterCage.set(filterCage.get() + e.getId() + ",");
                });
                filter = filter.concat(filterCage.get().substring(0, filterCage.get().length() - 1) + ")").concat(";");
            }
        }
        return filter.concat("status>-1").concat(";createDate>=" + req.getStartDate()).concat(";createDate<=" + req.getEndDate());
    }

    public double calculateAge(String yymm) throws DateTimeParseException {
        if (!checkRegex(yymm)){
            return 0.0;
        }
        // Tách yy và mm từ chuỗi yymm
        int yy = Integer.parseInt(yymm.substring(0, 2));
        int mm = Integer.parseInt(yymm.substring(2, 4));

        // Chuyển đổi yy thành năm đầy đủ
        int year;
        if (yy < 50) {
            year = 2000 + yy;
        } else {
            year = 1900 + yy;
        }

        // Tạo ngày sinh từ year và mm
        LocalDate birthDate = LocalDate.of(year, mm, 1);
        LocalDate currentDate = LocalDate.now();

        // Tính tuổi
        Period period = Period.between(birthDate, currentDate);

        // Chuyển đổi tuổi thành số thập phân (năm + tháng/12)
        double age = period.getYears() + period.getMonths()/12.0;
        return age;
    }

    public Boolean checkRegex(String input){
        // Regex để kiểm tra chuỗi có 7 ký tự và đều là số
        String regex = "^\\d{2}(0[1-9]|1[0-2])\\d{3}$";

        // Tạo Pattern từ regex
        Pattern pattern = Pattern.compile(regex);

        // Tạo Matcher từ chuỗi input
        Matcher matcher = pattern.matcher(input);

        // Kiểm tra xem chuỗi có khớp với regex không
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public BaseResponse changeCageByName(ChangeCageByNameReq req) {
        if (req.getCageName() == null || req.getPetName() == null || req.getFarmName() == null){
            return new BaseResponse().builder().code(501).message("Thất bại").result("Tên trại tên chuồng và tên pet không được để trống").build();
        }
        List<Farm> farmList = farmRepository.findAllByName(req.getFarmName());
        if (farmList.isEmpty()){
            return new BaseResponse().builder().code(502).message("Thất bại").result("Không có trại nào phù hợp với tên bạn nhập").build();
        }
        if (farmList.size() > 1){
            return new BaseResponse().builder().code(503).message("Thất bại").result("Có 2 trại ứng với tên bạn nhập").build();
        }
        List<Cage> cageList = cageRepository.findAllByNameAndFarm(req.getCageName(), farmList.get(0));
        if (cageList.isEmpty()){
            return new BaseResponse().builder().code(504).message("Thất bại").result("Không có chuồng nào phù hợp với tên bạn nhập").build();
        }
        if (cageList.size() > 1){
            return new BaseResponse().builder().code(505).message("Thất bại").result("Có 2 chuồng ứng với tên bạn nhập").build();
        }
        List<Pet> petList = new ArrayList<>();
        if (req.getCage() != null){
            petList = petRepository.findAllByNameAndCage(req.getPetName(), req.getCage());
        }

        if (req.getCage() == null){
            petList = petRepository.findAllByName( req.getPetName());
        }

        if (petList.isEmpty()){
            return new BaseResponse().builder().code(504).message("Thất bại").result("Không có pet nào phù hợp với tên bạn nhập").build();
        }
        if (petList.size() > 1){
            return new BaseResponse().builder().code(505).message("Thất bại").result("Có 2 pet ứng với tên bạn nhập").build();
        }
        if (petList.get(0).getStatus() < 0){
            return new BaseResponse().builder().code(504).message("Thất bại").result("Pet bạn nhập có thể đã chết").build();
        }
        Cage cage = cageList.get(0);
        Pet pet = petList.get(0);
        pet.setCage(cage);
        petRepository.save(pet);
        return new BaseResponse().success("Chuyển pet " + pet.getName() + " sang chuồng " + pet.getCage().getName() + " thành công");
    }
}
