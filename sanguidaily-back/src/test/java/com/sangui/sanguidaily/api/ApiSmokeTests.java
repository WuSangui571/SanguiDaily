package com.sangui.sanguidaily.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sangui.sanguidaily.service.JwtService;
import com.sangui.sanguidaily.service.PostService;
import com.sangui.sanguidaily.service.UploadService;
import com.sangui.sanguidaily.service.UserService;
import com.sangui.sanguidaily.service.WechatAuthService;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {PostController.class, AuthController.class, UploadController.class})
class ApiSmokeTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserService userService;

    @MockBean
    private WechatAuthService wechatAuthService;

    @MockBean
    private UploadService uploadService;

    @BeforeEach
    void setUp() {
        when(jwtService.parseToken(any())).thenReturn(Optional.empty());
    }

    @Test
    void listPostsReturnsOk() throws Exception {
        when(postService.listFeedPosts(any(), any())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/posts"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    void updatePostForbiddenWhenNoAuth() throws Exception {
        mockMvc.perform(put("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void wechatLoginBadRequestOnInvalidCode() throws Exception {
        when(wechatAuthService.exchangeCode(anyString()))
            .thenThrow(new IllegalArgumentException("bad"));
        mockMvc.perform(post("/api/auth/wechat")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\":\"bad\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void uploadImageForbiddenWhenNoAuth() throws Exception {
        MockMultipartFile file =
            new MockMultipartFile("file", "test.jpg", "image/jpeg", "1".getBytes());
        mockMvc.perform(multipart("/api/uploads/image").file(file))
            .andExpect(status().isForbidden());
    }
}
