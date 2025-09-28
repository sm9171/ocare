package com.health.app.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.app.adapter.in.web.dto.RegisterUserRequest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Test
    void registerUser_ValidRequest_Success() throws Exception {
        // given
        RegisterUserRequest request =
                new RegisterUserRequest("test@example.com", "password123", "Test User");

        // when & then
        mockMvc.perform(
                        post("/api/v1/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.recordKey").exists())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void registerUser_InvalidEmail_BadRequest() throws Exception {
        // given
        RegisterUserRequest request =
                new RegisterUserRequest("invalid-email", "password123", "Test User");

        // when & then
        mockMvc.perform(
                        post("/api/v1/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
    }

    @Test
    void registerUser_ShortPassword_BadRequest() throws Exception {
        // given
        RegisterUserRequest request =
                new RegisterUserRequest("test@example.com", "short", "Test User");

        // when & then
        mockMvc.perform(
                        post("/api/v1/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
    }

    @Test
    void registerUser_DuplicateEmail_BadRequest() throws Exception {
        // given
        RegisterUserRequest request =
                new RegisterUserRequest("test@example.com", "password123", "Test User");

        // 첫 번째 사용자 등록
        mockMvc.perform(
                        post("/api/v1/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // when & then - 같은 이메일로 두 번째 등록 시도
        mockMvc.perform(
                        post("/api/v1/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_ARGUMENT"));
    }
}
