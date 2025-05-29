-- 1) Base de datos y modo ANSI
CREATE DATABASE IF NOT EXISTS spotify
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci;
USE spotify;

-- 2) Roles (InnoDB para FK)
CREATE TABLE IF NOT EXISTS roles (
  id_rol        INT           AUTO_INCREMENT PRIMARY KEY,
  nombre_rol    VARCHAR(50)   NOT NULL,
  estado_rol    BOOLEAN       NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3) Suscripciones (InnoDB para FK)
CREATE TABLE IF NOT EXISTS suscripciones (
  id_suscripcion           INT         AUTO_INCREMENT PRIMARY KEY,
  nombre_suscripcion       VARCHAR(50) NOT NULL,
  beneficios_suscripcion   TEXT        NOT NULL,
  descripcion_suscripcion  TEXT        NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4) Datos iniciales de Roles y Suscripciones
INSERT IGNORE INTO roles (id_rol, nombre_rol)
VALUES
  (1, 'Administrador'),
  (2, 'Espectador');

INSERT IGNORE INTO suscripciones 
  (id_suscripcion, nombre_suscripcion, beneficios_suscripcion, descripcion_suscripcion)
VALUES
  (1, 'Gratuita',
    'Acceso a millones de canciones y podcasts, listas de reproducción seleccionadas, descubrimiento de nueva música.',
    'Escucha una amplia selección de música y podcasts de forma gratuita. Descubre nuevas canciones y artistas, aunque con algunas limitaciones como anuncios y reproducción aleatoria en dispositivos móviles.'
  ),
  (2, 'Premium',
    'Escucha música sin anuncios, descarga canciones para escuchar sin conexión, escucha canciones en cualquier orden, calidad de audio alta, escucha con amigos en tiempo real, organiza la cola de reproducción.',
    'Disfruta de una experiencia musical ininterrumpida y de alta calidad con la suscripción Premium de Spotify. Descarga tu música favorita para escucharla sin conexión y ten el control total de tu reproducción.'
  );

-- 5) Tabla de Usuarios
--    Le ponemos valores por defecto para rol=2 (Espectador) y suscripción=1 (Gratuita)
CREATE TABLE IF NOT EXISTS usuarios (
  id_usuario       INT           AUTO_INCREMENT PRIMARY KEY,
  nombre_usuario   VARCHAR(100)  NOT NULL,
  correo           VARCHAR(100)  NOT NULL UNIQUE,
  telefono         VARCHAR(20)   NULL,
  clave            VARCHAR(255)  NOT NULL,
  id_rol           INT           NOT NULL DEFAULT 2,
  id_suscripcion   INT           NOT NULL DEFAULT 1,
  CONSTRAINT uq_usuario_correo UNIQUE(correo),
  CHECK (correo LIKE '%@usantoto.edu.co'),
  FOREIGN KEY (id_rol)         REFERENCES roles       (id_rol)
                                 ON UPDATE CASCADE
                                 ON DELETE RESTRICT,
  FOREIGN KEY (id_suscripcion) REFERENCES suscripciones(id_suscripcion)
                                 ON UPDATE CASCADE
                                 ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6) Canciones, Artistas, Géneros (para tu simulación)
CREATE TABLE IF NOT EXISTS generos (
  id_genero         INT         AUTO_INCREMENT PRIMARY KEY,
  nombre_genero     VARCHAR(50) NOT NULL,
  descripcion_genero TEXT       NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS artistas (
  id_artista        INT         AUTO_INCREMENT PRIMARY KEY,
  nombre_artista    VARCHAR(100) NOT NULL,
  descripcion_artista TEXT      NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS canciones (
  id                INT         AUTO_INCREMENT PRIMARY KEY,
  titulo            VARCHAR(255),
  descripcion       TEXT,
  duracion          DOUBLE,
  id_artista        INT,
  id_genero         INT,
  portada           LONGBLOB,
  FOREIGN KEY (id_artista) REFERENCES artistas(id_artista)
                                ON UPDATE CASCADE
                                ON DELETE SET NULL,
  FOREIGN KEY (id_genero)   REFERENCES generos   (id_genero)
                                ON UPDATE CASCADE
                                ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7) Tabla de método de pago (tarjeta) vinculada al usuario
CREATE TABLE IF NOT EXISTS metodo_pago (
  id_pago           INT        AUTO_INCREMENT PRIMARY KEY,
  id_usuario        INT        NOT NULL,
  numero_tarjeta    CHAR(16)   NOT NULL,
  fecha_vencimiento CHAR(5)    NOT NULL,  -- MM/AA
  cvc               CHAR(3)    NOT NULL,
  creado_en         TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
                              ON UPDATE CASCADE
                              ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
