package com.sangui.sanguidaily.repository;

import com.sangui.sanguidaily.model.PostLike;
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
public class PostLikeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PostLikeRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<PostLike> listByUserAndPostIds(Long userId, List<Long> postIds) {
        if (userId == null) {
            return Collections.emptyList();
        }
        if (postIds == null || postIds.isEmpty()) {
            String sql = "select * from t_post_like where user_id = ? order by created_at desc";
            return jdbcTemplate.query(sql, likeRowMapper(), userId);
        }
        String sql = "select * from t_post_like where user_id = :userId and post_id in (:postIds)";
        return namedParameterJdbcTemplate.query(sql, Map.of("userId", userId, "postIds", postIds), likeRowMapper());
    }

    public boolean insertLike(Long postId, Long userId) {
        int changed = jdbcTemplate.update(
            "insert ignore into t_post_like (post_id, user_id, created_at, updated_at) values (?, ?, now(3), now(3))",
            postId,
            userId
        );
        return changed > 0;
    }

    public boolean deleteLike(Long postId, Long userId) {
        int changed = jdbcTemplate.update(
            "delete from t_post_like where post_id = ? and user_id = ?",
            postId,
            userId
        );
        return changed > 0;
    }

    private RowMapper<PostLike> likeRowMapper() {
        return (rs, rowNum) -> mapPostLike(rs);
    }

    private PostLike mapPostLike(ResultSet rs) throws SQLException {
        return new PostLike(
            rs.getLong("id"),
            rs.getLong("post_id"),
            rs.getLong("user_id"),
            rs.getObject("created_at", LocalDateTime.class),
            rs.getObject("updated_at", LocalDateTime.class)
        );
    }
}
