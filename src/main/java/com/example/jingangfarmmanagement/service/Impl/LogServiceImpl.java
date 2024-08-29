package com.example.jingangfarmmanagement.service.Impl;

import com.example.jingangfarmmanagement.repository.BaseRepository;
import com.example.jingangfarmmanagement.repository.LogRepository;
import com.example.jingangfarmmanagement.repository.entity.Enum.ELogType;
import com.example.jingangfarmmanagement.repository.entity.Log;
import com.example.jingangfarmmanagement.repository.entity.Materials;
import com.example.jingangfarmmanagement.repository.entity.MaterialsBOL;
import com.example.jingangfarmmanagement.service.LogService;
import com.example.jingangfarmmanagement.service.MaterialsBOLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends BaseServiceImpl<Log> implements LogService {
    @Autowired
    private LogRepository logRepository;
    @Override
    protected BaseRepository<Log> getRepository() {
        return logRepository;
    }

    public void logAction(ELogType type, String logMessage, String result) {
        Log logEntry = new Log();
        logEntry.setStatus(1);
        logEntry.setType(type);
        logEntry.setLog(logMessage);
        logEntry.setFileName(null);
        logEntry.setTimeLog(System.currentTimeMillis());
        logEntry.setResult(result);
        logRepository.save(logEntry);
    }
}
