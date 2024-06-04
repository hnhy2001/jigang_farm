package com.example.jingangfarmmanagement.model.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StatisticPetWithAge {
//    @Pattern(regexp="^\\d{14}$", message = "Ngày bắt đầu không hợp lệ yêu cầu truyền theo định dạng yyyyMMddHHmmss")
    String startDate;
//    @Pattern(regexp="^\\d{14}$", message = "Ngày kết thúc không hợp lệ yêu cầu truyền theo định dạng yyyyMMddHHmmss")
    String endDate;
}

