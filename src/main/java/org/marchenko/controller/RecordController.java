package org.marchenko.controller;

import org.marchenko.model.Record;
import org.marchenko.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecordController {
    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/users/{user_id}/records")
    @ResponseStatus(HttpStatus.OK)
    public List<Record> getRecords(@PathVariable("user_id") Long userId,
                                   @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                   @RequestParam(required = false, defaultValue = "3") Integer pageSize) {
        return recordService.findAllRecords(userId, pageNumber, pageSize);
    }

    @PostMapping("/users/{user_id}/records")
    @ResponseStatus(HttpStatus.CREATED)
    public Record createRecord(@PathVariable("user_id") Long userId,
                               @RequestParam String name,
                               @RequestParam String phone) {
        return recordService.createRecord(userId, name, phone);
    }

    @DeleteMapping("/users/{user_id}/records/{record_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRecord(@PathVariable("user_id") Long userId,
                             @PathVariable("record_id") Long recordId
    ) {
        recordService.deleteRecord(userId, recordId);
    }

    @GetMapping("/users/{user_id}/records/{record_id}")
    @ResponseStatus(HttpStatus.OK)
    public Record findRecordById(@PathVariable("user_id") Long userId,
                                 @PathVariable("record_id") Long recordId) {
        return recordService.findRecordById(userId, recordId);
    }

    @PatchMapping("/users/{user_id}/records/{record_id}")
    @ResponseStatus(HttpStatus.OK)
    public Record updateRecord(@PathVariable("user_id") Long userId,
                               @PathVariable("record_id") Long recordId,
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false) String phone) {
        return recordService.updateRecord(userId, recordId, name, phone);
    }

    @GetMapping("/users/{user_id}/records/by_phone")
    @ResponseStatus(HttpStatus.OK)
    public Record findRecordByPhone(@PathVariable("user_id") Long userId,
                                    @RequestParam String phone) {
        return recordService.findRecordByPhone(userId, phone);
    }

}
