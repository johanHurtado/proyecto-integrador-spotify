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

/*==========================================================
  EJEMPLOS (10 registros por tabla)
  Ejecuta todo de una vez en tu BD "spotify"
==========================================================*/

USE spotify;
SET FOREIGN_KEY_CHECKS = 0;

/*----------------------------------------------------------
  1)  ROLES   (ya tenías 1=Admin, 2=Espectador)
----------------------------------------------------------*/
-- (Nada que añadir)

/*----------------------------------------------------------
  2)  SUSCRIPCIONES  (1 = Free | 2 = Premium)
----------------------------------------------------------*/
-- (Ya insertadas)

/*----------------------------------------------------------
  3)  GÉNEROS  (sumamos 9 más, id_1 “Unknown Genre” ya existe)
----------------------------------------------------------*/
INSERT INTO generos (id_genero, nombre_genero, descripcion_genero) VALUES
 (2,'Pop'        ,'Popular contemporary music'),
 (3,'Rock'       ,'Rock and roll & sub-genres'),
 (4,'Hip-Hop'    ,'Rap / hip-hop'),
 (5,'Electrónica','EDM, house, techno'),
 (6,'Reggaetón'  ,'Latin urban'),
 (7,'Jazz'       ,'Smooth & classic jazz'),
 (8,'Clásica'    ,'Obras sinfónicas y barrocas'),
 (9,'Country'    ,'Country & folk'),
(10,'Metal'      ,'Heavy & power metal');

/*----------------------------------------------------------
  4)  ARTISTAS  (id_1 “Unknown Artist” ya existe)
----------------------------------------------------------*/
INSERT INTO artistas (id_artista,nombre_artista,descripcion_artista) VALUES
 (2,'The Echoes'         ,'Indie pop band'),
 (3,'DJ Nova'            ,'EDM/House producer'),
 (4,'Ritmo Urbano'       ,'Proyecto de reggaetón'),
 (5,'Metal Forge'        ,'Heavy-metal quartet'),
 (6,'Sonia Rodríguez'    ,'Cantautora latina'),
 (7,'Classic Ensemble'   ,'Orquesta sinfónica'),
 (8,'Flow Masters'       ,'Hip-hop colectivo'),
 (9,'Midnight County'    ,'Country duo'),
(10,'Blue Notes Trio'    ,'Jazz instrumental');

/*----------------------------------------------------------
  5)  USUARIOS  (10 usuarios ficticios)
      clave sin hash solo para demo
----------------------------------------------------------*/
INSERT INTO usuarios
(nombre_usuario, correo, telefono, clave, id_rol, id_suscripcion)
VALUES
('ana_p','ana@usantoto.edu.co'       ,'3001111111','passAna',2,1), -- Free
('beto_r','beto@usantoto.edu.co'     ,'3002222222','passBeto',2,2), -- Premium
('caro_g','caro@usantoto.edu.co'     ,'3003333333','passCaro',2,1),
('david_m','david@usantoto.edu.co'   ,'3004444444','passDavid',2,2),
('elena_q','elena@usantoto.edu.co'   ,'3005555555','passElena',2,1),
('felix_s','felix@usantoto.edu.co'   ,'3006666666','passFelix',2,2),
('gina_t','gina@usantoto.edu.co'     ,'3007777777','passGina',2,1),
('hector_v','hector@usantoto.edu.co' ,'3008888888','passHec' ,2,2),
('irene_w','irene@usantoto.edu.co'   ,'3009999999','passIre' ,2,1),
('jorge_z','jorge@usantoto.edu.co'   ,'3010000000','passJor' ,2,2);

/*----------------------------------------------------------
  6)  CANCIONES  (10 canciones; portada y mp3 = NULL demo)
----------------------------------------------------------*/
INSERT INTO canciones
(titulo, descripcion, duracion, id_artista, id_genero, portada, archivo_mp3)
VALUES
('Shining Lights'     ,'Lead single',          210, 2, 2 ,NULL,NULL),
('Night Drive'        ,'Club mix',             255, 3, 5 ,NULL,NULL),
('Baila Conmigo'      ,'Summer reggaetón hit', 198, 4, 6 ,NULL,NULL),
('Forged in Fire'     ,'Debut metal track',    305, 5,10 ,NULL,NULL),
('Luna Llena'         ,'Acoustic ballad',      230, 6, 2 ,NULL,NULL),
('Symphony No. 7'     ,'2nd movement',         540, 7, 8 ,NULL,NULL),
('Mic Drop'           ,'Rap cypher',           226, 8, 4 ,NULL,NULL),
('Dusty Roads'        ,'Country classic',      244, 9, 9 ,NULL,NULL),
('Smooth Sailing'     ,'Jazz lounge',          312,10, 7 ,NULL,NULL),
('Echoes (Reprise)'   ,'Indie pop reprise',    190, 2, 2 ,NULL,NULL);

/*----------------------------------------------------------
  7)  MÉTODOS DE PAGO  (solo para usuarios Premium)
      — id_usuario coincide con inserción anterior —
----------------------------------------------------------*/
INSERT INTO metodo_pago
(id_usuario, numero_tarjeta, fecha_vencimiento, cvc)
VALUES
(2 ,'4111111111111111','12/29','123'),
(4 ,'5500000000000004','08/27','456'),
(6 ,'4000123412341234','01/30','789'),
(8 ,'340000000000009' ,'05/28','159'),
(10,'6011000000000004','11/26','753');

SET FOREIGN_KEY_CHECKS = 1;

