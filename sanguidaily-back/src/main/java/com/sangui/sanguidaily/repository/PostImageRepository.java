package com.sangui.sanguidaily.repository;

import com.sangui.sanguidaily.model.PostImage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PostImageRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PostImageRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<PostImage> listByPostIds(List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Collections.emptyList();
        }
        String sql = "select * from t_post_image where post_id in (:postIds) order by post_id asc, sort_order asc";
        return namedParameterJdbcTemplate.query(sql, Map.of("postIds", postIds), postRowMapper());
    }

    public void insertBatch(List<PostImage> images) {
        if (images == null || images.isEmpty()) {
            return;
        }
        String sql = """
            insert into t_post_image
            (post_id, image_url, sort_order, width, height, size_bytes, created_at, updated_at)
            values (?, ?, ?, ?, ?, ?, now(3), now(3))
            """;
        jdbcTemplate.batchUpdate(sql, images, images.size(), (ps, image) -> {
            ps.setLong(1, image.postId());
            ps.setString(2, image.imageUrl());
            ps.setInt(3, image.sortOrder());
            ps.setObject(4, image.width());
            ps.setObject(5, image.height());
            ps.setObject(6, image.sizeBytes());
        });
    }

    public void deleteByPostId(Long postId) {
        if (postId == null) {
            return;
        }
        jdbcTemplate.update("delete from t_post_image where post_id = ?", postId);
    }

    public List<String> listAllImageUrls() {
        return jdbcTemplate.query(
            "select image_url from t_post_image",
            (rs, rowNum) -> rs.getString("image_url")
        );
    }

    private RowMapper<PostImage> postRowMapper() {
        return (rs, rowNum) -> mapPostImage(rs);
    }

    private PostImage mapPostImage(ResultSet rs) throws SQLException {
        return new PostImage(
            rs.getLong("id"),
            rs.getLong("post_id"),
            rs.getString("image_url"),
            rs.getInt("sort_order"),
            rs.getInt("width"),
            rs.getInt("height"),
            rs.getInt("size_bytes"),
            rs.getObject("created_at", LocalDateTime.class),
            rs.getObject("updated_at", LocalDateTime.class)
        );
    }
}
