package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getAll() {
        String sql = "SELECT" +
                "    f.ID," +
                "    f.NAME," +
                "    f.DESCRIPTION," +
                "    f.RELEASE_DATE," +
                "    f.DURATION," +
                "    f.MPA_ID," +
                "    r.NAME as MPA_NAME," +
                "    r.DESCRIPTION  as MPA_DESCRIPTION " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS r ON f.MPA_ID = r.id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film create(Film film) {
        String sql = "insert into films(name, description, release_date, duration, mpa_id) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            // По умолчанию mpa без возрастных ограничений
            stmt.setInt(5, film.getMpa() == null ? 1 : film.getMpa().getId());
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey().intValue() >= 0) {
            film.setId(keyHolder.getKey().intValue());
            return film;
        }
        return null;
    }

    @Override
    public Film update(Film film) {
        String sql = "insert into films(id, name, description, release_date, duration, mpa_id) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setInt(1, film.getId());
            stmt.setString(2, film.getName());
            stmt.setString(3, film.getDescription());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, film.getDuration());
            // По умолчанию mpa без возрастных ограничений
            stmt.setInt(6, film.getMpa() == null ? 1 : film.getMpa().getId());
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey().intValue() == film.getId()) {
            return film;
        }
        return null;
    }

    @Override
    public Film getById(Integer id) {
        String sql = "SELECT" +
                "    f.ID," +
                "    f.NAME," +
                "    f.DESCRIPTION," +
                "    f.RELEASE_DATE," +
                "    f.DURATION," +
                "    f.MPA_ID," +
                "    m.NAME as MPA_NAME," +
                "    m.DESCRIPTION  as MPA_DESCRIPTION, " +
                "    l.rate as RATE " +
                "FROM films AS f " +
                "INNER JOIN mpa AS m ON f.MPA_ID = m.id " +
                "INNER JOIN (SELECT count(*) as rate FROM LIKES where film_id = ?) AS l " +
                "WHERE f.id = ? ";
        try {
            Film film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), id, id);
            return film;
        } catch (DataAccessException error) {
            // Если фильм не найден по id
            return null;
        }

    }

    @Override
    public boolean delete(Integer id) {
        String sqlQuery = "delete from films where id = ?";
        try {
            return jdbcTemplate.update(sqlQuery, id) > 0;

        } catch (DataAccessException error) {
            // Если фильм не найден по id
            return false;
        }
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Integer duration = rs.getInt("duration");
        Integer rate = rs.getInt("rate");
        Integer mpaId = rs.getInt("mpa_id");
        String mpaName = rs.getString("mpa_name");
        String mpaDescription = rs.getString("mpa_description");
        Mpa mpa = new Mpa(mpaId, mpaName, mpaDescription);

        // TODO получать значения из других таблиц
        Set<Integer> likedUserIds = new HashSet<>();
        Set<Genre> genres = new HashSet<>();

        return new Film(
                id,
                name,
                description,
                releaseDate,
                duration,
                likedUserIds,
                rate,
                genres,
                mpa
        );
    }
}
