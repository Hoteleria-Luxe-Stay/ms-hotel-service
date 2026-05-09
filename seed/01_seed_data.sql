-- =========================================================
-- ms-hotel-service — Seed data (NO migration, pegar manual)
-- =========================================================
-- DB destino: hotel_db en MySQL (146.181.62.223:3306)
-- Pre-requisito: V2__add_imagen_url_departamento.sql aplicada
--                (la columna imagen_url debe existir en departamento)
--
-- Estrategia: 7 departamentos × 2 hoteles × 5 habitaciones = 70 hab.
--             URLs de imágenes en picsum.photos (estables, gratuitas,
--             reemplazables después por Cloudinary / S3 / etc).
--
-- IMPORTANTE: si la DB ya tiene datos (vos cargaste 3 hoteles antes),
--             descomentá las líneas TRUNCATE para reset limpio.
--             Si querés MERGE (mantener lo que hay + agregar), comentá
--             el TRUNCATE y MySQL te avisará si hay duplicates.
-- =========================================================

USE hotel_db;

-- ---------- RESET (descomentá si querés borrar todo y empezar fresco) ----------
SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE habitacion;
-- TRUNCATE TABLE hotel;
-- TRUNCATE TABLE tipo_habitacion;
-- TRUNCATE TABLE departamento;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 1. DEPARTAMENTOS (7 regiones turísticas del Perú)
-- ============================================================
INSERT INTO departamento (nombre, detalle, imagen_url) VALUES
('Lima',        'Capital del Perú. Ciudad costera con rica historia colonial y gastronomía mundial.',  'https://picsum.photos/seed/lima/800/600'),
('Cusco',       'Antigua capital del imperio Inca, puerta de entrada a Machu Picchu.',                  'https://picsum.photos/seed/cusco/800/600'),
('Arequipa',    'La Ciudad Blanca, al pie del volcán Misti. Centro histórico patrimonio UNESCO.',       'https://picsum.photos/seed/arequipa/800/600'),
('La Libertad', 'Tierra de Chan Chan y las playas de Huanchaco. Capital: Trujillo.',                    'https://picsum.photos/seed/lalibertad/800/600'),
('Piura',       'Sol radiante todo el año, playas de Máncora y los manglares de Tumbes cerca.',         'https://picsum.photos/seed/piura/800/600'),
('Loreto',      'Corazón de la Amazonía peruana. Iquitos, río Amazonas, biodiversidad única.',          'https://picsum.photos/seed/loreto/800/600'),
('Puno',        'Folclore andino a orillas del lago Titicaca, el lago navegable más alto del mundo.',   'https://picsum.photos/seed/puno/800/600');

-- ============================================================
-- 2. TIPOS DE HABITACIÓN
-- ============================================================
INSERT INTO tipo_habitacion (nombre, descripcion, capacidad) VALUES
('Simple', 'Habitación individual con cama simple, baño privado y desayuno incluido.', 1),
('Doble',  'Habitación con cama doble o dos camas simples, ideal para 2 personas.',     2),
('Suite',  'Suite de lujo con sala de estar, jacuzzi y vista panorámica.',              4);

-- ============================================================
-- 3. HOTELES (2 por departamento — 14 total)
-- ============================================================
-- Variables auxiliares para no depender de IDs hardcoded
SET @lima        = (SELECT id FROM departamento WHERE nombre = 'Lima');
SET @cusco       = (SELECT id FROM departamento WHERE nombre = 'Cusco');
SET @arequipa    = (SELECT id FROM departamento WHERE nombre = 'Arequipa');
SET @lalibertad  = (SELECT id FROM departamento WHERE nombre = 'La Libertad');
SET @piura       = (SELECT id FROM departamento WHERE nombre = 'Piura');
SET @loreto      = (SELECT id FROM departamento WHERE nombre = 'Loreto');
SET @puno        = (SELECT id FROM departamento WHERE nombre = 'Puno');

INSERT INTO hotel (nombre, direccion, descripcion, telefono, email, imagen_url, departamento_id) VALUES
('Hotel Lima Centro',           'Av. Arequipa 1234, Miraflores',          'Hotel moderno en el corazón de Miraflores con vista al océano.',     '01-4567890', 'reservas@limacentro.com',     'https://picsum.photos/seed/hotellima1/800/600',     @lima),
('Lima Boutique Barranco',      'Av. Pedro de Osma 567, Barranco',        'Hotel boutique en el barrio bohemio, cerca del puente de los suspiros.','01-2345678', 'info@limaboutique.com',       'https://picsum.photos/seed/hotellima2/800/600',     @lima),

('Hotel Cusco Imperial',        'Plaza de Armas 567, Cusco',              'Hotel colonial con vista a la Plaza de Armas. Tour a Machu Picchu incluido.','084-234567', 'reservas@cuscoimperial.com',  'https://picsum.photos/seed/hotelcusco1/800/600',    @cusco),
('Cusco Plaza Hotel',           'Calle Procuradores 234, Cusco',          'Construcción inca restaurada. Spa, restaurante gourmet andino.',     '084-345678', 'info@cuscoplaza.com',         'https://picsum.photos/seed/hotelcusco2/800/600',    @cusco),

('Hotel Arequipa Plaza',        'Calle Mercaderes 123, Arequipa',         'Hotel boutique en el centro histórico de la Ciudad Blanca.',         '054-345678', 'reservas@arequipaplaza.com',  'https://picsum.photos/seed/hotelarequipa1/800/600', @arequipa),
('Arequipa Colonial Inn',       'Calle Santa Catalina 456, Arequipa',     'Casona colonial restaurada cerca del Monasterio de Santa Catalina.', '054-456789', 'info@arequipacolonial.com',   'https://picsum.photos/seed/hotelarequipa2/800/600', @arequipa),

('Hotel Trujillo Grand',        'Jr. Pizarro 789, Trujillo',              'Hotel ejecutivo cerca de Chan Chan y la Huaca de la Luna.',          '044-567890', 'reservas@trujillogrand.com',  'https://picsum.photos/seed/hoteltrujillo1/800/600', @lalibertad),
('Huanchaco Beach Resort',      'Av. Larco 890, Huanchaco',               'Resort frente al mar, con clases de surf y paseos en caballitos de totora.','044-678901', 'info@huanchacobeach.com',   'https://picsum.photos/seed/hoteltrujillo2/800/600', @lalibertad),

('Piura Inn',                   'Av. Loreto 234, Piura',                  'Hotel familiar en el centro de Piura. Buen punto de partida a Máncora.','073-345678', 'reservas@piurainn.com',       'https://picsum.photos/seed/hotelpiura1/800/600',    @piura),
('Mancora Beach Hotel',         'Av. Antigua Panamericana Norte, Máncora','Resort de playa todo incluido. Surf, pesca, kitesurfing.',          '073-456789', 'info@mancorabeach.com',       'https://picsum.photos/seed/hotelpiura2/800/600',    @piura),

('Iquitos Amazon Lodge',        'Av. La Marina 100, Iquitos',             'Lodge ecológico con vista al Amazonas. Tours a la selva incluidos.', '065-234567', 'reservas@iquitoslodge.com',   'https://picsum.photos/seed/hoteliquitos1/800/600',  @loreto),
('Hotel Río Iquitos',           'Calle Putumayo 345, Iquitos',            'Hotel céntrico, base ideal para excursiones al río Amazonas.',       '065-345678', 'info@rioiquitos.com',         'https://picsum.photos/seed/hoteliquitos2/800/600',  @loreto),

('Titicaca Lodge Puno',         'Jr. Lima 456, Puno',                     'Hotel a orillas del lago Titicaca. Tours a Uros y Taquile.',         '051-456789', 'reservas@titicacalodge.com',  'https://picsum.photos/seed/hotelpuno1/800/600',     @puno),
('Puno Plaza Hotel',            'Jr. Tacna 789, Puno',                    'Hotel moderno en la Plaza de Armas de Puno. Calefacción, oxígeno disponible.','051-567890', 'info@punoplaza.com',          'https://picsum.photos/seed/hotelpuno2/800/600',     @puno);

-- ============================================================
-- 4. HABITACIONES (5 por hotel — 70 total)
--    2 simples (S/80), 2 dobles (S/120), 1 suite (S/250)
-- ============================================================
SET @t_simple = (SELECT id FROM tipo_habitacion WHERE nombre = 'Simple');
SET @t_doble  = (SELECT id FROM tipo_habitacion WHERE nombre = 'Doble');
SET @t_suite  = (SELECT id FROM tipo_habitacion WHERE nombre = 'Suite');

-- Genera habitaciones para cada hotel con un loop de stored procedure
DELIMITER //
DROP PROCEDURE IF EXISTS seed_habitaciones //
CREATE PROCEDURE seed_habitaciones()
BEGIN
    DECLARE v_hotel_id BIGINT;
    DECLARE done INT DEFAULT FALSE;
    DECLARE cur CURSOR FOR SELECT id FROM hotel ORDER BY id;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;
    read_loop: LOOP
        FETCH cur INTO v_hotel_id;
        IF done THEN LEAVE read_loop; END IF;

        INSERT INTO habitacion (numero, estado, precio, capacidad, tipo_id, hotel_id) VALUES
        ('101', 'DISPONIBLE',  80.00, 1, @t_simple, v_hotel_id),
        ('102', 'DISPONIBLE',  80.00, 1, @t_simple, v_hotel_id),
        ('201', 'DISPONIBLE', 120.00, 2, @t_doble,  v_hotel_id),
        ('202', 'DISPONIBLE', 120.00, 2, @t_doble,  v_hotel_id),
        ('301', 'DISPONIBLE', 250.00, 4, @t_suite,  v_hotel_id);
    END LOOP;
    CLOSE cur;
END //
DELIMITER ;

CALL seed_habitaciones();
DROP PROCEDURE seed_habitaciones;

-- ============================================================
-- VERIFICACIÓN
-- ============================================================
SELECT 'departamentos:' AS tabla, COUNT(*) AS rows FROM departamento
UNION ALL SELECT 'tipos_habitacion:',  COUNT(*) FROM tipo_habitacion
UNION ALL SELECT 'hoteles:',           COUNT(*) FROM hotel
UNION ALL SELECT 'habitaciones:',      COUNT(*) FROM habitacion;
-- Esperado:
--   departamentos:    7
--   tipos_habitacion: 3
--   hoteles:          14
--   habitaciones:     70
