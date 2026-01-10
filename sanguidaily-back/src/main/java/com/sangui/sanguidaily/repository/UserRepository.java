package com.sangui.sanguidaily.repository;

import com.sangui.sanguidaily.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
