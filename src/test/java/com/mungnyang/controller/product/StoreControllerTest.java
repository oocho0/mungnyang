package com.mungnyang.controller.product;

import com.mungnyang.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("관리자 권한으로 페이지 요청")
    @WithMockUser(username = "admin@abc.com", roles = "ADMIN")
    void 관리자_권한으로_페이지를_요청하면_성공한다() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/store")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @DisplayName("셀러 권한으로 페이지 요청")
    @WithMockUser(username = "seller@abc.com", roles = "SELLER")
    void 셀러_권한으로_페이지를_요청하면_Forbidden처리가_된다() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/store")).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("사용자 권한으로 페이지 요청")
    @WithMockUser(username = "user@abc.com", roles = "USER")
    void 사용자_권한으로_페이지를_요청하면_Forbidden처리가_된다() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/store")).andDo(print()).andExpect(status().isForbidden());
    }
}