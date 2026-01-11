package com.sangui.sanguidaily.repository;

import com.sangui.sanguidaily.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findById(Long id) {
        return jdbcTemplate.query(
            "select * from t_user where id = ?",
            userRowMapper(),
            id
        ).stream().findFirst();
    }

    public Optional<User> findByOpenid(String openid) {
        return jdbcTemplate.query(
            "select * from t_user where openid = ?",
            userRowMapper(),
            openid
        ).stream().findFirst();
    }

    public Optional<User> findFirst() {
        return jdbcTemplate.query(
            "select * from t_user order by id asc limit 1",
            userRowMapper()
        ).stream().findFirst();
    }

    public Optional<User> insert(
        String openid,
        String role,
        String nickname,
        String avatarUrl,
        LocalDateTime lastLoginAt
    ) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "insert into t_user (openid, role, nickname, avatar_url, last_login_at) values (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, openid);
            ps.setString(2, role);
            ps.setString(3, nickname);
            ps.setString(4, avatarUrl);
            ps.setObject(5, lastLoginAt);
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key == null) {
            return Optional.empty();
        }
        return findById(key.longValue());
    }

    public void updateLoginProfile(
        Long id,
        String nickname,
        String avatarUrl,
        LocalDateTime lastLoginAt
    ) {
        jdbcTemplate.update(
            "update t_user set last_login_at = ?, nickname = coalesce(?, nickname), avatar_url = coalesce(?, avatar_url) where id = ?",
            lastLoginAt,
            nickname,
            avatarUrl,
            id
        );
    }

    public void updateRole(Long id, String role) {
        jdbcTemplate.update(
            "update t_user set role = ? where id = ?",
            role,
            id
        );
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> mapUser(rs);
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
            rs.getLong("id"),
            rs.getString("openid"),
            rs.getString("role"),
            rs.getString("nickname"),
            rs.getString("avatar_url"),
            rs.getObject("created_at", LocalDateTime.class),
            rs.getObject("updated_at", LocalDateTime.class),
            rs.getObject("last_login_at", LocalDateTime.class)
        );
    }
}
