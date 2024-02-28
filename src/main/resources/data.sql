MERGE INTO genres (id, name) VALUES
    (1, 'Боевик'),
    (2, 'Комедия'),
    (3, 'Ужас'),
    (4, 'Триллер'),
    (5, 'Фантастика');

MERGE INTO mpa (id, name, description) VALUES
    (1, 'G', 'у фильма нет возрастных ограничений'),
    (2, 'PG', 'детям рекомендуется смотреть фильм с родителями'),
    (3, 'PG-13', 'детям до 13 лет просмотр не желателен'),
    (4, 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
    (5, 'NC-17', 'лицам до 18 лет просмотр запрещён');

-- Закоментировано, чтобы тесты в постмане отрабатывали
--MERGE INTO users (id, email, name, login, birthday) VALUES
--    (1, 'example@mail.ru', 'John Smith', 'smith', '1990-01-10'),
--    (2, 'connor@mail.ru', 'Sarah Connor', 'connor', '1995-11-15'),
--    (3, 'ivanov@mail.ru', 'Ваня Иванов', 'ivanov', '2005-07-02');

--MERGE INTO films (id, name, description, release_date, duration, mpa_id) VALUES
--    (1, 'Иван Васильевич меняет профессию', 'Советская фантастическая комедия', '1973-01-01', 88, 1),
--    (2, 'Назад в будущее', 'Американский научно-фантастический фильм режиссёра Роберта Земекиса',
--      '1985-01-01', 116, 1);

--MERGE INTO likes (id, user_id, film_id) VALUES
--    (1, 1, 2),
--    (2, 2, 2),
--    (3, 3, 1);

--MERGE INTO film_genres (id, film_id, genre_id) VALUES
--    (1, 1, 2),
--    (2, 2, 2),
--    (3, 2, 5);

--MERGE INTO friends (id, user_id, friend_id) VALUES
--    (1, 1, 2),
--    (2, 2, 1),
--    (3, 1, 3);