package org.marchenko.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.marchenko.exception.BadRequest;
import org.marchenko.exception.CreateException;
import org.marchenko.exception.NotFoundException;
import org.marchenko.model.Record;
import org.marchenko.model.User;
import org.marchenko.service.RecordService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class RecordControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RecordService recordService;

    @Test
    public void getRecords() throws Exception {
        User user = new User(1L, "user", "phone");

        List<Record> records = Arrays.asList(
                new Record(1L, "name1", "phone1"),
                new Record(2L, "name2", "phone2")
        );

        Mockito.when(recordService.findAllRecords(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(records);

        mvc.perform(MockMvcRequestBuilders.get("/users/{user_id}/records", user.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id", contains(1, 2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", contains("name1", "name2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].phone", contains("phone1", "phone2")));

        Mockito.verify(recordService).findAllRecords(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void createRecordWhenRecordNotExist() throws Exception {
        User user = new User(1L, "user", "phone");

        Record record = new Record(1L, "user1", "phone1");

        Mockito.when(recordService.createRecord(user.getId(), record.getName(), record.getPhone())).thenReturn(record);

        mvc.perform(MockMvcRequestBuilders.post("/users/{user_id}/records", user.getId())
                .param("name", record.getName())
                .param("phone", record.getPhone())
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(record.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("phone").value(record.getPhone()));

        Mockito.verify(recordService).createRecord(user.getId(), record.getName(), record.getPhone());
    }

    @Test
    public void createRecordWhenRecordExist() throws Exception {
        User user = new User(1L, "user", "phone");

        Record record = new Record(1L, "user1", "phone1");

        Mockito.when(recordService.createRecord(user.getId(), record.getName(), record.getPhone()))
                .thenThrow(CreateException.class);

        mvc.perform(MockMvcRequestBuilders.post("/users/{user_id}/records", user.getId())
                .param("name", record.getName())
                .param("phone", record.getPhone())
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(recordService).createRecord(user.getId(), record.getName(), record.getPhone());
    }

    @Test
    public void createRecordWhenNameIsEmpty() throws Exception {
        User user = new User(1L, "user", "phone");

        Record record = new Record(1L, "", "phone1");

        Mockito.when(recordService.createRecord(user.getId(), record.getName(), record.getPhone()))
                .thenThrow(BadRequest.class);

        mvc.perform(MockMvcRequestBuilders.post("/users/{user_id}/records", user.getId())
                .param("name", record.getName())
                .param("phone", record.getPhone())
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(recordService).createRecord(user.getId(), record.getName(), record.getPhone());
    }

    @Test
    public void createRecordWhenPhoneIsEmpty() throws Exception {
        User user = new User(1L, "user", "phone");

        Record record = new Record(1L, "name", "");

        Mockito.when(recordService.createRecord(user.getId(), record.getName(), record.getPhone()))
                .thenThrow(BadRequest.class);

        mvc.perform(MockMvcRequestBuilders.post("/users/{user_id}/records", user.getId())
                .param("name", record.getName())
                .param("phone", record.getPhone())
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(recordService).createRecord(user.getId(), record.getName(), record.getPhone());
    }


    @Test
    public void deleteRecordWhenRecordNotExist() throws Exception {
        User user = new User(1L, "user", "phone");

        Record record = new Record(1L, "user1", "phone1");

        Mockito.when(recordService.deleteRecord(user.getId(), record.getId()))
                .thenReturn(record);

        mvc.perform(MockMvcRequestBuilders.delete("/users/{user_id}/records/{record_id}", user.getId(), record.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(recordService).deleteRecord(user.getId(), record.getId());
    }

    @Test
    public void deleteRecordWhenRecordExist() throws Exception {
        User user = new User(1L, "user", "phone");

        Record record = new Record(1L, "user1", "phone1");

        Mockito.when(recordService.deleteRecord(user.getId(), record.getId()))
                .thenThrow(CreateException.class);

        mvc.perform(MockMvcRequestBuilders.delete("/users/{user_id}/records/{record_id}", user.getId(), record.getId()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(recordService).deleteRecord(user.getId(), record.getId());
    }

    @Test
    public void findRecordByIdWhenRecordExist() throws Exception {
        User user = new User(1L, "user", "phone");

        Record record = new Record(1L, "user1", "phone1");

        Mockito.when(recordService.findRecordById(user.getId(), record.getId()))
                .thenReturn(record);

        mvc.perform(MockMvcRequestBuilders.get("/users/{user_id}/records/{record_id}", user.getId(), record.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(record.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("phone").value(record.getPhone()));

        Mockito.verify(recordService).findRecordById(user.getId(), record.getId());
    }

    @Test
    public void findRecordByIdWhenRecordNotExist() throws Exception {
        User user = new User(1L, "user", "phone");

        Record record = new Record(1L, "user1", "phone1");

        Mockito.when(recordService.findRecordById(user.getId(), record.getId()))
                .thenThrow(NotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.get("/users/{user_id}/records/{record_id}", user.getId(), record.getId()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(recordService).findRecordById(user.getId(), record.getId());
    }


    @Test
    public void updateRecordWhenRecordNotExist() throws Exception {
        User user = new User(1L, "user", "phone");

        Record record = new Record(1L, "user1", "phone1");

        Mockito.when(recordService.updateRecord(user.getId(), record.getId(), record.getName(), record.getPhone()))
                .thenReturn(record);

        mvc.perform(MockMvcRequestBuilders.patch("/users/{user_id}/records/{record_id}", user.getId(), record.getId())
                .param("name", record.getName())
                .param("phone", record.getPhone()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(record.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("phone").value(record.getPhone()));

        Mockito.verify(recordService).updateRecord(user.getId(), record.getId(), record.getName(), record.getPhone());
    }

    @Test
    public void updateRecordWhenRecordExist() throws Exception {
        User user = new User(1L, "user", "phone");

        Record record = new Record(1L, "user1", "phone1");

        Mockito.when(recordService.updateRecord(user.getId(), record.getId(), record.getName(), record.getPhone()))
                .thenThrow(CreateException.class);

        mvc.perform(MockMvcRequestBuilders.patch("/users/{user_id}/records/{record_id}", user.getId(), record.getId())
                .param("name", record.getName())
                .param("phone", record.getPhone()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(recordService).updateRecord(user.getId(), record.getId(), record.getName(), record.getPhone());
    }

    @Test
    public void findRecordByPhone() throws Exception {
        User user = new User(1L, "user", "phone");

        Record record = new Record(1L, "user1", "phone1");

        Mockito.when(recordService.findRecordByPhone(user.getId(), record.getPhone())).thenReturn(record);

        mvc.perform(MockMvcRequestBuilders.get("/users/{user_id}/records/by_phone", user.getId())
                .param("phone", record.getPhone()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(record.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("phone").value(record.getPhone()));

        Mockito.verify(recordService).findRecordByPhone(user.getId(), record.getPhone());
    }

}