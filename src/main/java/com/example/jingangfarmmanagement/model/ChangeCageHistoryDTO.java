package com.example.jingangfarmmanagement.model;

import com.example.jingangfarmmanagement.repository.entity.ChangeCageHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCageHistoryDTO {
    Long createDate;
    List<ChangeCageHistory> changeCageHistories;
}
