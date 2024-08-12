package com.example.jingangfarmmanagement.repository.entity;

import com.example.jingangfarmmanagement.repository.entity.Enum.ELogType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "log")
public class Log extends BaseEntity{
    @Column(name = "time_log")
    private Long timeLog;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ELogType type;
    @Column(name = "log")
    private String log;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "result")
    private String result;
}
