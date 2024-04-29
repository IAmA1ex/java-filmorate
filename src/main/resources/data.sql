INSERT INTO friend_status (status_id, status)
SELECT 1, 'confirmed'
WHERE NOT EXISTS (SELECT 1 FROM friend_status WHERE status_id = 1);

INSERT INTO friend_status (status_id, status)
SELECT 2, 'unconfirmed'
WHERE NOT EXISTS (SELECT 1 FROM friend_status WHERE status_id = 2);

INSERT INTO genre (genre_id, genre)
SELECT 1, 'Комедия'
WHERE NOT EXISTS (SELECT 1 FROM genre WHERE genre_id = 1);

INSERT INTO genre (genre_id, genre)
SELECT 2, 'Драма'
WHERE NOT EXISTS (SELECT 1 FROM genre WHERE genre_id = 2);

INSERT INTO genre (genre_id, genre)
SELECT 3, 'Мультфильм'
WHERE NOT EXISTS (SELECT 1 FROM genre WHERE genre_id = 3);

INSERT INTO genre (genre_id, genre)
SELECT 4, 'Триллер'
WHERE NOT EXISTS (SELECT 1 FROM genre WHERE genre_id = 4);

INSERT INTO genre (genre_id, genre)
SELECT 5, 'Документальный'
WHERE NOT EXISTS (SELECT 1 FROM genre WHERE genre_id = 5);

INSERT INTO genre (genre_id, genre)
SELECT 6, 'Боевик'
WHERE NOT EXISTS (SELECT 1 FROM genre WHERE genre_id = 6);

INSERT INTO rating_mpa (rating_mpa_id, rating_mpa)
SELECT 1, 'G'
WHERE NOT EXISTS (SELECT 1 FROM rating_mpa WHERE rating_mpa_id = 1);

INSERT INTO rating_mpa (rating_mpa_id, rating_mpa)
SELECT 2, 'PG'
WHERE NOT EXISTS (SELECT 1 FROM rating_mpa WHERE rating_mpa_id = 2);

INSERT INTO rating_mpa (rating_mpa_id, rating_mpa)
SELECT 3, 'PG-13'
WHERE NOT EXISTS (SELECT 1 FROM rating_mpa WHERE rating_mpa_id = 3);

INSERT INTO rating_mpa (rating_mpa_id, rating_mpa)
SELECT 4, 'R'
WHERE NOT EXISTS (SELECT 1 FROM rating_mpa WHERE rating_mpa_id = 4);

INSERT INTO rating_mpa (rating_mpa_id, rating_mpa)
SELECT 5, 'NC-17'
WHERE NOT EXISTS (SELECT 1 FROM rating_mpa WHERE rating_mpa_id = 5);
