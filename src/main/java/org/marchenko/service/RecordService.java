package org.marchenko.service;

import org.marchenko.model.Record;

import java.util.List;

public interface RecordService {
    List<Record> findAllRecords(Long userId, Integer pageNumber, Integer pageSize);

    Record createRecord(Long userId, String name, String phone);

    Record findRecordById(Long userId, Long recordId);

    Record findRecordByPhone(Long userId, String phone);

    Record deleteRecord(Long userId, Long recordId);

    Record updateRecord(Long userId, Long recordId, String name, String phone);

}
