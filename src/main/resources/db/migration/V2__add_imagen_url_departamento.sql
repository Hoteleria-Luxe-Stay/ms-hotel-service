-- =========================================================
-- V2: Agregar columna imagen_url a la tabla departamento
-- =========================================================
-- Motivo: el frontend necesita mostrar imagenes para departamentos
--         (igual que ya las muestra para hoteles).
-- Estrategia: NULLABLE (compatible con datos existentes).
-- =========================================================

ALTER TABLE departamento
    ADD COLUMN imagen_url VARCHAR(500) NULL AFTER detalle;
