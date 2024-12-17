package com.example.jingangfarmmanagement.repository;

import com.example.jingangfarmmanagement.repository.entity.MealVoucher;
import com.example.jingangfarmmanagement.repository.entity.TreatmentCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealVoucherRepository extends BaseRepository<MealVoucher> {
    @Query(value = "SELECT tc.* " +
            "FROM meal_voucher tc JOIN meal_voucher_pet tcp ON tc.id = tcp.meal_voucher_id " +
            "JOIN pet p ON p.id = tcp.pet_id " +
            "WHERE p.id IN :petIds",
            countQuery = "SELECT COUNT(*) " +
                    "FROM meal_voucher tc JOIN meal_voucher_pet tcp ON tc.id = tcp.meal_voucher_id " +
                    "JOIN pet p ON p.id = tcp.pet_id " +
                    "WHERE p.id IN :petIds",
            nativeQuery = true)
    Page<MealVoucher> findMealVoucherByPet(@Param("petIds") List<Long> petIds, Pageable pageable);

}
