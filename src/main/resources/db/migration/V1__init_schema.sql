-- =========================================================
-- ms-hotel-service: Baseline schema
-- =========================================================
-- Refleja el estado actual de las entidades JPA:
--   Departamento, TipoHabitacion, Hotel, Habitacion
-- A partir de aquí, cada cambio de schema = nuevo script V{n}__descripcion.sql
-- =========================================================

CREATE TABLE departamento (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    nombre   VARCHAR(255),
    detalle  VARCHAR(255),
    CONSTRAINT pk_departamento PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE tipo_habitacion (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(255),
    descripcion VARCHAR(255),
    capacidad   INT          NOT NULL,
    CONSTRAINT pk_tipo_habitacion PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE hotel (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(255),
    direccion       VARCHAR(255),
    descripcion     VARCHAR(255),
    telefono        VARCHAR(255),
    email           VARCHAR(255),
    imagen_url      VARCHAR(255),
    departamento_id BIGINT,
    CONSTRAINT pk_hotel PRIMARY KEY (id),
    CONSTRAINT fk_hotel_departamento FOREIGN KEY (departamento_id) REFERENCES departamento (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE INDEX idx_hotel_departamento_id ON hotel (departamento_id);

CREATE TABLE habitacion (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    numero     VARCHAR(255),
    estado     VARCHAR(255),
    precio     DOUBLE       NOT NULL,
    capacidad  INT          NOT NULL,
    tipo_id    BIGINT,
    hotel_id   BIGINT,
    CONSTRAINT pk_habitacion PRIMARY KEY (id),
    CONSTRAINT fk_habitacion_tipo  FOREIGN KEY (tipo_id)  REFERENCES tipo_habitacion (id),
    CONSTRAINT fk_habitacion_hotel FOREIGN KEY (hotel_id) REFERENCES hotel (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE INDEX idx_habitacion_tipo_id  ON habitacion (tipo_id);
CREATE INDEX idx_habitacion_hotel_id ON habitacion (hotel_id);
