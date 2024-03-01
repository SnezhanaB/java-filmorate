package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                "    r.DESCRIPTION  as MPA_DESCRIPTION," +
                "    l.rate as RATE " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS r ON f.MPA_ID = r.id " +
                "LEFT JOIN " +
                "   (SELECT count(*) as rate, film_id FROM LIKES GROUP BY film_id) AS l " +
                "ON l.film_id = f.id";
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
        Integer savedId = keyHolder.getKey().intValue();
        if (savedId >= 0) {
            film.setId(savedId);
            film.setGenres(saveGenresForFilmId(savedId, film.getGenres()));
            return film;
        }
        return null;
    }

    @Override
    public Film update(Film film) {
        String sql = "update films set name=?, description=?, release_date=?, duration=?, mpa_id=? where id=?";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                stmt.setInt(4, film.getDuration());
                // По умолчанию mpa без возрастных ограничений
                stmt.setInt(5, film.getMpa() == null ? 1 : film.getMpa().getId());
                stmt.setInt(6, film.getId());
                return stmt;
            }, keyHolder);
            if (keyHolder.getKey().intValue() == film.getId()) {
                film.setGenres(saveGenresForFilmId(film.getId(), film.getGenres()));
                return film;
            }
            return null;
        } catch (DataAccessException | NullPointerException error) {
            // Если пользователь не найден по id
            return null;
        }
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

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT" +
                "    ID," +
                "    NAME," +
                "FROM genres ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre getGenre(Integer id) throws NotFoundException {
        String sql = "SELECT" +
                "    ID," +
                "    NAME " +
                " FROM genres " +
                " WHERE id = ? ";
        try {
            Genre genre = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeGenre(rs), id);
            return genre;
        } catch (DataAccessException error) {
            // Если жанр не найден по id
            throw new NotFoundException("Жанр с id=" + id + " не найден");
        }
    }

    @Override
    public Mpa getMpa(int id) throws NotFoundException {
        String sql = "SELECT " +
                "    id," +
                "    name," +
                "    description " +
                "FROM mpa " +
                "WHERE id = ? ";
        try {
            Mpa mpa = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeMpa(rs), id);
            return mpa;
        } catch (DataAccessException error) {
            // Если рейтинг не найден по id
            throw new NotFoundException("Рейтинг с id=" + id + " не найден");
        }
    }

    @Override
    public List<Mpa> getMpas() {
        String sql = "SELECT" +
                "    id, " +
                "    name, " +
                "    description " +
                " FROM mpa ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public void addLike(Integer filmId, Integer userId) throws NotFoundException {
        try {
            jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?);", filmId, userId);
        } catch (DataAccessException e) {
            throw new NotFoundException("Фильм или пользователь не найден");
        }
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) throws NotFoundException {
        if (jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? AND user_id = ? ;", filmId, userId) <= 0) {
            throw new NotFoundException("Фильм или пользователь не найден");
        }
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");

        return new Genre(id, name);
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");

        return new Mpa(id, name, description);
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
        List<Genre> genres = getGenresByFilmId(id);

        return new Film(
                id,
                name,
                description,
                releaseDate,
                duration,
                rate,
                genres,
                mpa
        );
    }

    private List<Genre> getGenresByFilmId(int filmId) {
        return jdbcTemplate.query("SELECT " +
                "fg.genre_id as id, " +
                "g.name as name " +
                "FROM FILM_GENRES AS fg " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.id " +
                "WHERE fg.film_id = ? " +
                "GROUP BY fg.genre_id;", (rset, rowNum) -> makeGenre(rset), filmId);
    }

    private List<Genre> saveGenresForFilmId(int filmId, List<Genre> genres) {
        try {
            // Очищаем от старых значений
            String sqlQuery = "DELETE FROM film_genres WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery, filmId);
            // Сохраняем новые
            if (genres != null) {
                Set<Integer> genreIds = new HashSet<>();
                List<Genre> filteredFromDublicates = genres.stream().filter(genre -> {
                    boolean alreadyExists = genreIds.contains(genre.getId());
                    if (alreadyExists) {
                        return false;
                    } else {
                        genreIds.add(genre.getId());
                        return true;
                    }
                }).collect(Collectors.toList());
                genreIds.forEach(genreId -> {
                    jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)", filmId, genreId);
                });
                return filteredFromDublicates;
            }
            return null;

        } catch (DataAccessException error) {
            // Если фильм не найден по id
            throw new NotFoundException("Фильм с id=" + filmId + " не найден");
        }
    }
}
