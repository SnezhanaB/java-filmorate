package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getAll() {
        String sql = "SELECT id, email, name, login, birthday FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User create(User user) {
        String sql = "insert into users(email, name, login, birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey().intValue() > 0) {
            user.setId(keyHolder.getKey().intValue());
            return user;
        }
        return null;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users set email=?, name=?, login=?, birthday=? WHERE id=?";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
                stmt.setString(1, user.getEmail());
                stmt.setString(2, user.getName());
                stmt.setString(3, user.getLogin());
                stmt.setDate(4, Date.valueOf(user.getBirthday()));
                stmt.setInt(5, user.getId());
                return stmt;
            }, keyHolder);
            if (keyHolder.getKey().intValue() == user.getId()) {
                return user;
            }
            return null;
        } catch (DataAccessException | NullPointerException error) {
            // Если пользователь не найден по id
            return null;
        }
    }

    @Override
    public User getById(Integer id) {
        String sql = "SELECT id, email, name, login, birthday " +
                "FROM users " +
                "WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUser(rs), id);
            return user;
        } catch (DataAccessException error) {
            // Если пользователь не найден по id
            return null;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sqlQuery = "delete from users where id = ?";
        try {
            return jdbcTemplate.update(sqlQuery, id) > 0;

        } catch (DataAccessException error) {
            // Если фильм не найден по id
            return false;
        }
    }

    private User makeUser(ResultSet rs) throws SQLException {
        // id, email, name, login, birthday
        Integer id = rs.getInt("id");
        String email = rs.getString("email");
        String name = rs.getString("name");
        String login = rs.getString("login");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }
}
