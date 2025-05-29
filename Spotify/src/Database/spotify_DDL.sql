/*--------------------------------------------------------------------
  0)  BASE DE DATOS
  ------------------------------------------------------------------*/
CREATE DATABASE IF NOT EXISTS spotify
  CHARACTER SET utf8mb4
  COLLATE      utf8mb4_general_ci;
USE spotify;

/*--------------------------------------------------------------------
  1)  ROLES
  ------------------------------------------------------------------*/
CREATE TABLE IF NOT EXISTS roles (
  id_rol        INT          AUTO_INCREMENT PRIMARY KEY,
  nombre_rol    VARCHAR(50)  NOT NULL,
  estado_rol    BOOLEAN      NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*--------------------------------------------------------------------
  2)  SUSCRIPCIONES
  ------------------------------------------------------------------*/
CREATE TABLE IF NOT EXISTS suscripciones (
  id_suscripcion           INT         AUTO_INCREMENT PRIMARY KEY,
  nombre_suscripcion       VARCHAR(50) NOT NULL,
  beneficios_suscripcion   TEXT        NOT NULL,
  descripcion_suscripcion  TEXT        NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* Datos iniciales ---------------------------------------------------*/
INSERT IGNORE INTO roles (id_rol, nombre_rol) VALUES
  (1, 'Administrador'),
  (2, 'Espectador');

INSERT IGNORE INTO suscripciones (id_suscripcion, nombre_suscripcion,
                                  beneficios_suscripcion, descripcion_suscripcion)
VALUES
  (1, 'Free',
   'Millions of songs and podcasts, curated playlists, discovery features.',
   'Listen to a wide selection of music and podcasts for free. Ads included.'),
  (2, 'Premium',
   'Ad-free, offline downloads, on-demand playback, high-quality audio.',
   'Enjoy uninterrupted, high-quality music with full playback control.');

/*--------------------------------------------------------------------
  3)  USUARIOS
  ------------------------------------------------------------------*/
CREATE TABLE IF NOT EXISTS usuarios (
  id_usuario      INT          AUTO_INCREMENT PRIMARY KEY,
  nombre_usuario  VARCHAR(100) NOT NULL,
  correo          VARCHAR(100) NOT NULL UNIQUE,
  telefono        VARCHAR(20)  NULL,
  clave           VARCHAR(255) NOT NULL,
  id_rol          INT          NOT NULL DEFAULT 2,
  id_suscripcion  INT          NOT NULL DEFAULT 1,

  CHECK (correo LIKE '%@usantoto.edu.co'),

  FOREIGN KEY (id_rol)
      REFERENCES roles(id_rol)
      ON UPDATE CASCADE
      ON DELETE RESTRICT,

  FOREIGN KEY (id_suscripcion)
      REFERENCES suscripciones(id_suscripcion)
      ON UPDATE CASCADE
      ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*--------------------------------------------------------------------
  4)  GÉNEROS Y ARTISTAS
  ------------------------------------------------------------------*/
CREATE TABLE IF NOT EXISTS generos (
  id_genero          INT          AUTO_INCREMENT PRIMARY KEY,
  nombre_genero      VARCHAR(50)  NOT NULL,
  descripcion_genero TEXT         NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS artistas (
  id_artista          INT          AUTO_INCREMENT PRIMARY KEY,
  nombre_artista      VARCHAR(100) NOT NULL,
  descripcion_artista TEXT         NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/* Valores “placeholder” para evitar errores de FK -------------------*/
INSERT IGNORE INTO generos (id_genero, nombre_genero, descripcion_genero)
VALUES (1, 'Unknown Genre', 'placeholder');

INSERT IGNORE INTO artistas (id_artista, nombre_artista, descripcion_artista)
VALUES (1, 'Unknown Artist', 'placeholder');

/*--------------------------------------------------------------------
  5)  CANCIONES  (almacena MP3 en BLOB)
  ------------------------------------------------------------------*/
CREATE TABLE IF NOT EXISTS canciones (
  id            INT           AUTO_INCREMENT PRIMARY KEY,
  titulo        VARCHAR(255)  NOT NULL,
  descripcion   TEXT          NULL,
  duracion      DOUBLE        NOT NULL,      -- segundos
  id_artista    INT           NULL,
  id_genero     INT           NULL,
  portada       LONGBLOB      NULL,          -- carátula
  archivo_mp3   MEDIUMBLOB    NOT NULL,      -- MP3

  FOREIGN KEY (id_artista)
      REFERENCES artistas(id_artista)
      ON UPDATE CASCADE
      ON DELETE SET NULL,

  FOREIGN KEY (id_genero)
      REFERENCES generos(id_genero)
      ON UPDATE CASCADE
      ON DELETE SET NULL,

  INDEX idx_cancion_artista (id_artista),
  INDEX idx_cancion_genero  (id_genero)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*--------------------------------------------------------------------
  6)  MÉTODOS DE PAGO
  ------------------------------------------------------------------*/
CREATE TABLE IF NOT EXISTS metodo_pago (
  id_pago           INT        AUTO_INCREMENT PRIMARY KEY,
  id_usuario        INT        NOT NULL,
  numero_tarjeta    CHAR(16)   NOT NULL,
  fecha_vencimiento CHAR(5)    NOT NULL,  -- MM/AA
  cvc               CHAR(3)    NOT NULL,
  creado_en         TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,

  FOREIGN KEY (id_usuario)
      REFERENCES usuarios(id_usuario)
      ON UPDATE CASCADE
      ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*--------------------------------------------------------------------
  7)  ÍNDICES EXTRA
  ------------------------------------------------------------------*/
CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuarios(correo);
CREATE INDEX IF NOT EXISTS idx_pago_usuario  ON metodo_pago(id_usuario);
