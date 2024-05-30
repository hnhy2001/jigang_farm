package com.example.jingangfarmmanagement.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryHealthReq {
    private String type;
    private String unit;
    private String result;
    private Long checkingDate;
}
