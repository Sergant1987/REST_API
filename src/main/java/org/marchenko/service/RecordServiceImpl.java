package org.marchenko.service;

import org.marchenko.model.Record;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class RecordServiceImpl implements RecordService {
    @Override
    public List<Record> findAllRecords(Long userId, Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public Record createRecord(Long userId, String name, String phone) {
        return null;
    }

    @Override
    public Record findRecordById(Long userId, Long recordId) {
        return null;
    }

    @Override
    public Record findRecordByPhone(Long userId, String phone) {
        return null;
    }

    @Override
    public Record deleteRecord(Long userId, Long recordId) {
        return null;
    }

    @Override
    public Record updateRecord(Long userId, Long recordId, String name, String phone) {
        return null;
    }
}
