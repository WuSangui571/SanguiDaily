package com.sangui.sanguidaily.repository;

import com.sangui.sanguidaily.dto.PostCreateRequest;
import com.sangui.sanguidaily.dto.PostUpdateRequest;
import com.sangui.sanguidaily.model.Post;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepository {

    private final JdbcTemplate jdbcTemplate;
    public PostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Post> listFeedPosts(String role, Integer type) {
        StringBuilder sql = new StringBuilder("select * from t_post where deleted_at is null");
        List<Object> args = new ArrayList<>();
        if (!isOwner(role)) {
            sql.append(" and status = 0");
        }
        if (type != null && type >= 0) {
            sql.append(" and type = ?");
            args.add(type);
        }
        sql.append(" order by is_pinned desc, pinned_at desc, created_at desc");
        return jdbcTemplate.query(sql.toString(), postRowMapper(), args.toArray());
    }

    public Optional<Post> findVisibleById(Long id, String role) {
        StringBuilder sql = new StringBuilder("select * from t_post where id = ? and deleted_at is null");
        List<Object> args = new ArrayList<>();
        args.add(id);
        if (!isOwner(role)) {
            sql.append(" and status = 0");
        }
        return jdbcTemplate.query(sql.toString(), postRowMapper(), args.toArray()).stream().findFirst();
    }

    public Post insert(PostCreateRequest request) {
        String sql = """
            insert into t_post (
                author_id, type, content_text, link_url, link_title, link_cover_url, link_site_name,
                video_url, video_cover_url, status, is_pinned, pinned_at, like_count, created_at, updated_at
            ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, null, 0, now(3), now(3))
            """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, request.authorId());
            ps.setObject(2, request.type());
            ps.setString(3, request.contentText());
            ps.setString(4, request.linkUrl());
            ps.setString(5, request.linkTitle());
            ps.setString(6, request.linkCoverUrl());
            ps.setString(7, request.linkSiteName());
            ps.setString(8, request.videoUrl());
            ps.setString(9, request.videoCoverUrl());
            ps.setObject(10, request.status());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        Long id = key != null ? key.longValue() : null;
        if (id == null) {
            throw new IllegalStateException("Failed to create post");
        }
        return findVisibleById(id, "OWNER").orElseThrow(() -> new IllegalStateException("Post not found"));
    }

    public Optional<Post> updatePost(Long id, PostUpdateRequest request) {
        String sql = """
            update t_post set
                type = ?,
                content_text = ?,
                link_url = ?,
                link_title = ?,
                link_cover_url = ?,
                link_site_name = ?,
                video_url = ?,
                video_cover_url = ?,
                status = ?,
                updated_at = now(3)
            where id = ?
            """;
        jdbcTemplate.update(
            sql,
            request.type(),
            request.contentText(),
            request.linkUrl(),
            request.linkTitle(),
            request.linkCoverUrl(),
            request.linkSiteName(),
            request.videoUrl(),
            request.videoCoverUrl(),
            request.status(),
            id
        );
        return findVisibleById(id, "OWNER");
    }

    public Optional<Post> updatePinned(Long id, boolean pinned) {
        jdbcTemplate.update(
            "update t_post set is_pinned = ?, pinned_at = ?, updated_at = now(3) where id = ?",
            pinned ? 1 : 0,
            pinned ? LocalDateTime.now() : null,
            id
        );
        return findVisibleById(id, "OWNER");
    }

    public Optional<Post> updateStatus(Long id, Integer status) {
        jdbcTemplate.update(
            "update t_post set status = ?, updated_at = now(3) where id = ?",
            status,
            id
        );
        return findVisibleById(id, "OWNER");
    }

    public int incrementLikeCount(Long postId) {
        return jdbcTemplate.update("update t_post set like_count = like_count + 1 where id = ?", postId);
    }

    public int decrementLikeCount(Long postId) {
        return jdbcTemplate.update(
            "update t_post set like_count = case when like_count > 0 then like_count - 1 else 0 end where id = ?",
            postId
        );
    }

    public Optional<Integer> findLikeCount(Long postId) {
        return jdbcTemplate.query(
            "select like_count from t_post where id = ?",
            (rs, rowNum) -> rs.getInt("like_count"),
            postId
        ).stream().findFirst();
    }

    private boolean isOwner(String role) {
        return "OWNER".equalsIgnoreCase(role);
    }

    private RowMapper<Post> postRowMapper() {
        return (rs, rowNum) -> mapPost(rs);
    }

    private Post mapPost(ResultSet rs) throws SQLException {
        return new Post(
            rs.getLong("id"),
            rs.getLong("author_id"),
            rs.getInt("type"),
            rs.getString("content_text"),
            rs.getString("link_url"),
            rs.getString("link_title"),
            rs.getString("link_cover_url"),
            rs.getString("link_site_name"),
            rs.getString("video_url"),
            rs.getString("video_cover_url"),
            rs.getInt("status"),
            rs.getInt("is_pinned"),
            rs.getObject("pinned_at", LocalDateTime.class),
            rs.getInt("like_count"),
            rs.getObject("created_at", LocalDateTime.class),
            rs.getObject("updated_at", LocalDateTime.class),
            rs.getObject("deleted_at", LocalDateTime.class)
        );
    }
}
