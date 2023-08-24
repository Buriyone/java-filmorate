DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS films_genres;
DROP TABLE IF EXISTS film;
DROP TABLE IF EXISTS mpa;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS mpa (
  id Integer AUTO_INCREMENT,
  name varchar(64) NOT NULL,
  CONSTRAINT mpa_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS genres (
  id Integer AUTO_INCREMENT,
  name varchar(64) NOT NULL,
  CONSTRAINT genres_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS film (
  id Integer AUTO_INCREMENT,
  name varchar(64) NOT NULL,
  description varchar(200),
  releaseDate timestamp,
  duration Integer,
  rate Integer,
  mpa_id Integer,
  CONSTRAINT film_pk PRIMARY KEY (id),
  CONSTRAINT film_fk_rating FOREIGN KEY (mpa_id) REFERENCES mpa(id)
);

CREATE TABLE IF NOT EXISTS users (
  id Integer NOT NULL AUTO_INCREMENT,
  login varchar(64) NOT NULL,
  name varchar(64),
  email varchar(64),
  birthday timestamp,
  CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS likes (
  id Integer AUTO_INCREMENT,
  film_id Integer,
  user_id Integer NOT NULL,
  CONSTRAINT likes_pk PRIMARY KEY (id),
  CONSTRAINT likes_fk_film_id FOREIGN KEY (film_id) REFERENCES film(id),
  CONSTRAINT likes_fk_user_id FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS friends (
  id Integer AUTO_INCREMENT,
  user_id Integer NOT NULL,
  friend_id Integer NOT NULL,
  CONSTRAINT friends_pk PRIMARY KEY (id),
  CONSTRAINT friends_fk_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT friends_fk_friend FOREIGN KEY (friend_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS films_genres (
  id Integer AUTO_INCREMENT,
  film_id Integer NOT NULL,
  genre_id integer NOT NULL,
  CONSTRAINT fg_pk PRIMARY KEY(id),
  CONSTRAINT fg_fk_film_id FOREIGN KEY (film_id) REFERENCES film(id),
  CONSTRAINT fg_fk_genre_id FOREIGN KEY (genre_id) REFERENCES genres(id)
);
