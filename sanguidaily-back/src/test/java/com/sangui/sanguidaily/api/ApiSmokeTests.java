package com.sangui.sanguidaily.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sangui.sanguidaily.service.JwtService;
import com.sangui.sanguidaily.service.PostService;
import com.sangui.sanguidaily.service.UploadCleanupService;
import com.sangui.sanguidaily.service.UploadService;
import com.sangui.sanguidaily.service.UserService;
import com.sangui.sanguidaily.service.WechatAuthService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ApiSmokeTests {

    private MockMvc mockMvc;

    private PostService postService;

    private JwtService jwtService;

    private UserService userService;

    private WechatAuthService wechatAuthService;

    private UploadService uploadService;

    private UploadCleanupService uploadCleanupService;

    @BeforeEach
    void setUp() {
        postService = org.mockito.Mockito.mock(PostService.class);
        jwtService = org.mockito.Mockito.mock(JwtService.class);
        userService = org.mockito.Mockito.mock(UserService.class);
        wechatAuthService = org.mockito.Mockito.mock(WechatAuthService.class);
        uploadService = org.mockito.Mockito.mock(UploadService.class);
        uploadCleanupService = org.mockito.Mockito.mock(UploadCleanupService.class);

        PostController postController = new PostController(postService, jwtService, userService);
        AuthController authController = new AuthController(wechatAuthService, userService, jwtService);
        UploadController uploadController = new UploadController(uploadService, jwtService, userService, "");
        UploadCleanupController cleanupController =
            new UploadCleanupController(uploadCleanupService, jwtService, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(
            postController,
            authController,
            uploadController,
            cleanupController
        ).build();

        when(jwtService.parseToken(any())).thenReturn(Optional.empty());
    }

    @Test
    void listPostsReturnsOk() throws Exception {
        when(postService.listFeedPosts(any(), any())).thenReturn(java.util.Collections.emptyList());
        mockMvc.perform(get("/api/posts"))
            .andExpect(status().isOk());
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

    @Test
    void cleanupOrphansForbiddenWhenNoAuth() throws Exception {
        mockMvc.perform(get("/api/uploads/orphans"))
            .andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/uploads/orphans")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"paths\":[]}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void listDeletedForbiddenWhenNoAuth() throws Exception {
        mockMvc.perform(get("/api/posts/deleted"))
            .andExpect(status().isForbidden());
    }

    @Test
    void deletePostForbiddenWhenNoAuth() throws Exception {
        mockMvc.perform(post("/api/posts/1/delete"))
            .andExpect(status().isMethodNotAllowed());
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .patch("/api/posts/1/delete"))
            .andExpect(status().isForbidden());
    }

    @Test
    void restorePostForbiddenWhenNoAuth() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .patch("/api/posts/1/restore"))
            .andExpect(status().isForbidden());
    }
}
