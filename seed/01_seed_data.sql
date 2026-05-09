-- =========================================================
-- ms-hotel-service — Seed data RESET (pegar en MySQL Workbench)
-- =========================================================
-- DB destino: hotel_db en MySQL (146.181.62.223:3306)
-- Pre-requisito: V2__add_imagen_url_departamento.sql YA aplicada
--                (la columna imagen_url debe existir en departamento)
--
-- Estrategia RESET: limpia TODAS las tablas data y carga fresh.
--                   Conserva intacto flyway_schema_history (Flyway sigue OK).
--
-- Resultado:
--   7 departamentos (regiones turísticas del Perú)
--   3 tipos de habitación (Simple, Doble, Suite)
--   14 hoteles (2 por departamento)
--   70 habitaciones (5 por hotel)
--
-- URLs de imágenes: picsum.photos con seed (gratis, estables, reemplazables).
-- =========================================================

USE hotel_db;

-- ============================================================
-- 0. RESET — borrar TODAS las tablas data (preservando flyway_schema_history)
-- ============================================================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE habitacion;
TRUNCATE TABLE hotel;
TRUNCATE TABLE tipo_habitacion;
TRUNCATE TABLE departamento;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 1. DEPARTAMENTOS (7 regiones turísticas del Perú)
--    Después del TRUNCATE, IDs autogenerados serán 1..7 en orden.
-- ============================================================
INSERT INTO departamento (nombre, detalle, imagen_url) VALUES
('Lima',        'Capital del Perú. Ciudad costera con rica historia colonial y gastronomía mundial.',  'https://picsum.photos/seed/lima/800/600'),
('Cusco',       'Antigua capital del imperio Inca, puerta de entrada a Machu Picchu.',                  'https://picsum.photos/seed/cusco/800/600'),
('Arequipa',    'La Ciudad Blanca, al pie del volcán Misti. Centro histórico patrimonio UNESCO.',       'https://picsum.photos/seed/arequipa/800/600'),
('La Libertad', 'Tierra de Chan Chan y las playas de Huanchaco. Capital: Trujillo.',                    'https://picsum.photos/seed/lalibertad/800/600'),
('Piura',       'Sol radiante todo el año, playas de Máncora y los manglares de Tumbes cerca.',         'https://picsum.photos/seed/piura/800/600'),
('Loreto',      'Corazón de la Amazonía peruana. Iquitos, río Amazonas, biodiversidad única.',          'https://picsum.photos/seed/loreto/800/600'),
('Puno',        'Folclore andino a orillas del lago Titicaca, el lago navegable más alto del mundo.',   'https://picsum.photos/seed/puno/800/600');

-- IDs resultantes (asumiendo TRUNCATE previo):
--   1=Lima, 2=Cusco, 3=Arequipa, 4=La Libertad, 5=Piura, 6=Loreto, 7=Puno

-- ============================================================
-- 2. TIPOS DE HABITACIÓN (IDs: 1=Simple, 2=Doble, 3=Suite)
-- ============================================================
INSERT INTO tipo_habitacion (nombre, descripcion, capacidad) VALUES
('Simple', 'Habitación individual con cama simple, baño privado y desayuno incluido.', 1),
('Doble',  'Habitación con cama doble o dos camas simples, ideal para 2 personas.',     2),
('Suite',  'Suite de lujo con sala de estar, jacuzzi y vista panorámica.',              4);

-- ============================================================
-- 3. HOTELES (14 — 2 por departamento)
--    IDs resultantes: 1..14 en orden de inserción
-- ============================================================
INSERT INTO hotel (nombre, direccion, descripcion, telefono, email, imagen_url, departamento_id) VALUES
-- Lima (departamento_id=1)
('Hotel Lima Centro',           'Av. Arequipa 1234, Miraflores',          'Hotel moderno en el corazón de Miraflores con vista al océano.',          '01-4567890', 'reservas@limacentro.com',     'https://picsum.photos/seed/hotellima1/800/600',      1),
('Lima Boutique Barranco',      'Av. Pedro de Osma 567, Barranco',        'Hotel boutique en el barrio bohemio, cerca del puente de los suspiros.',  '01-2345678', 'info@limaboutique.com',       'https://picsum.photos/seed/hotellima2/800/600',      1),
-- Cusco (departamento_id=2)
('Hotel Cusco Imperial',        'Plaza de Armas 567, Cusco',              'Hotel colonial con vista a la Plaza de Armas. Tour a Machu Picchu.',      '084-234567', 'reservas@cuscoimperial.com',  'https://picsum.photos/seed/hotelcusco1/800/600',     2),
('Cusco Plaza Hotel',           'Calle Procuradores 234, Cusco',          'Construcción inca restaurada. Spa, restaurante gourmet andino.',          '084-345678', 'info@cuscoplaza.com',         'https://picsum.photos/seed/hotelcusco2/800/600',     2),
-- Arequipa (departamento_id=3)
('Hotel Arequipa Plaza',        'Calle Mercaderes 123, Arequipa',         'Hotel boutique en el centro histórico de la Ciudad Blanca.',              '054-345678', 'reservas@arequipaplaza.com',  'https://picsum.photos/seed/hotelarequipa1/800/600',  3),
('Arequipa Colonial Inn',       'Calle Santa Catalina 456, Arequipa',     'Casona colonial restaurada cerca del Monasterio de Santa Catalina.',      '054-456789', 'info@arequipacolonial.com',   'https://picsum.photos/seed/hotelarequipa2/800/600',  3),
-- La Libertad (departamento_id=4)
('Hotel Trujillo Grand',        'Jr. Pizarro 789, Trujillo',              'Hotel ejecutivo cerca de Chan Chan y la Huaca de la Luna.',               '044-567890', 'reservas@trujillogrand.com',  'https://picsum.photos/seed/hoteltrujillo1/800/600',  4),
('Huanchaco Beach Resort',      'Av. Larco 890, Huanchaco',               'Resort frente al mar, clases de surf y caballitos de totora.',            '044-678901', 'info@huanchacobeach.com',     'https://picsum.photos/seed/hoteltrujillo2/800/600',  4),
-- Piura (departamento_id=5)
('Piura Inn',                   'Av. Loreto 234, Piura',                  'Hotel familiar en el centro de Piura. Punto de partida a Máncora.',       '073-345678', 'reservas@piurainn.com',       'https://picsum.photos/seed/hotelpiura1/800/600',     5),
('Mancora Beach Hotel',         'Panamericana Norte, Máncora',            'Resort de playa todo incluido. Surf, pesca, kitesurfing.',                '073-456789', 'info@mancorabeach.com',       'https://picsum.photos/seed/hotelpiura2/800/600',     5),
-- Loreto (departamento_id=6)
('Iquitos Amazon Lodge',        'Av. La Marina 100, Iquitos',             'Lodge ecológico con vista al Amazonas. Tours a la selva incluidos.',      '065-234567', 'reservas@iquitoslodge.com',   'https://picsum.photos/seed/hoteliquitos1/800/600',   6),
('Hotel Río Iquitos',           'Calle Putumayo 345, Iquitos',            'Hotel céntrico, base ideal para excursiones al río Amazonas.',            '065-345678', 'info@rioiquitos.com',         'https://picsum.photos/seed/hoteliquitos2/800/600',   6),
-- Puno (departamento_id=7)
('Titicaca Lodge Puno',         'Jr. Lima 456, Puno',                     'Hotel a orillas del lago Titicaca. Tours a Uros y Taquile.',              '051-456789', 'reservas@titicacalodge.com',  'https://picsum.photos/seed/hotelpuno1/800/600',      7),
('Puno Plaza Hotel',            'Jr. Tacna 789, Puno',                    'Hotel moderno en la Plaza de Armas de Puno. Calefacción central.',        '051-567890', 'info@punoplaza.com',          'https://picsum.photos/seed/hotelpuno2/800/600',      7);

-- ============================================================
-- 4. HABITACIONES (5 por hotel — 70 total)
--    Patrón por hotel: 2 simples (S/80), 2 dobles (S/120), 1 suite (S/250)
--    tipo_id: 1=Simple, 2=Doble, 3=Suite
-- ============================================================
INSERT INTO habitacion (numero, estado, precio, capacidad, tipo_id, hotel_id) VALUES
-- Hotel 1 (Lima Centro)
('101','DISPONIBLE', 80.00,1,1, 1),('102','DISPONIBLE', 80.00,1,1, 1),('201','DISPONIBLE',120.00,2,2, 1),('202','DISPONIBLE',120.00,2,2, 1),('301','DISPONIBLE',250.00,4,3, 1),
-- Hotel 2 (Lima Boutique)
('101','DISPONIBLE', 80.00,1,1, 2),('102','DISPONIBLE', 80.00,1,1, 2),('201','DISPONIBLE',120.00,2,2, 2),('202','DISPONIBLE',120.00,2,2, 2),('301','DISPONIBLE',250.00,4,3, 2),
-- Hotel 3 (Cusco Imperial)
('101','DISPONIBLE', 80.00,1,1, 3),('102','DISPONIBLE', 80.00,1,1, 3),('201','DISPONIBLE',120.00,2,2, 3),('202','DISPONIBLE',120.00,2,2, 3),('301','DISPONIBLE',250.00,4,3, 3),
-- Hotel 4 (Cusco Plaza)
('101','DISPONIBLE', 80.00,1,1, 4),('102','DISPONIBLE', 80.00,1,1, 4),('201','DISPONIBLE',120.00,2,2, 4),('202','DISPONIBLE',120.00,2,2, 4),('301','DISPONIBLE',250.00,4,3, 4),
-- Hotel 5 (Arequipa Plaza)
('101','DISPONIBLE', 80.00,1,1, 5),('102','DISPONIBLE', 80.00,1,1, 5),('201','DISPONIBLE',120.00,2,2, 5),('202','DISPONIBLE',120.00,2,2, 5),('301','DISPONIBLE',250.00,4,3, 5),
-- Hotel 6 (Arequipa Colonial)
('101','DISPONIBLE', 80.00,1,1, 6),('102','DISPONIBLE', 80.00,1,1, 6),('201','DISPONIBLE',120.00,2,2, 6),('202','DISPONIBLE',120.00,2,2, 6),('301','DISPONIBLE',250.00,4,3, 6),
-- Hotel 7 (Trujillo Grand)
('101','DISPONIBLE', 80.00,1,1, 7),('102','DISPONIBLE', 80.00,1,1, 7),('201','DISPONIBLE',120.00,2,2, 7),('202','DISPONIBLE',120.00,2,2, 7),('301','DISPONIBLE',250.00,4,3, 7),
-- Hotel 8 (Huanchaco Beach)
('101','DISPONIBLE', 80.00,1,1, 8),('102','DISPONIBLE', 80.00,1,1, 8),('201','DISPONIBLE',120.00,2,2, 8),('202','DISPONIBLE',120.00,2,2, 8),('301','DISPONIBLE',250.00,4,3, 8),
-- Hotel 9 (Piura Inn)
('101','DISPONIBLE', 80.00,1,1, 9),('102','DISPONIBLE', 80.00,1,1, 9),('201','DISPONIBLE',120.00,2,2, 9),('202','DISPONIBLE',120.00,2,2, 9),('301','DISPONIBLE',250.00,4,3, 9),
-- Hotel 10 (Mancora Beach)
('101','DISPONIBLE', 80.00,1,1,10),('102','DISPONIBLE', 80.00,1,1,10),('201','DISPONIBLE',120.00,2,2,10),('202','DISPONIBLE',120.00,2,2,10),('301','DISPONIBLE',250.00,4,3,10),
-- Hotel 11 (Iquitos Amazon Lodge)
('101','DISPONIBLE', 80.00,1,1,11),('102','DISPONIBLE', 80.00,1,1,11),('201','DISPONIBLE',120.00,2,2,11),('202','DISPONIBLE',120.00,2,2,11),('301','DISPONIBLE',250.00,4,3,11),
-- Hotel 12 (Hotel Río Iquitos)
('101','DISPONIBLE', 80.00,1,1,12),('102','DISPONIBLE', 80.00,1,1,12),('201','DISPONIBLE',120.00,2,2,12),('202','DISPONIBLE',120.00,2,2,12),('301','DISPONIBLE',250.00,4,3,12),
-- Hotel 13 (Titicaca Lodge)
('101','DISPONIBLE', 80.00,1,1,13),('102','DISPONIBLE', 80.00,1,1,13),('201','DISPONIBLE',120.00,2,2,13),('202','DISPONIBLE',120.00,2,2,13),('301','DISPONIBLE',250.00,4,3,13),
-- Hotel 14 (Puno Plaza)
('101','DISPONIBLE', 80.00,1,1,14),('102','DISPONIBLE', 80.00,1,1,14),('201','DISPONIBLE',120.00,2,2,14),('202','DISPONIBLE',120.00,2,2,14),('301','DISPONIBLE',250.00,4,3,14);

-- ============================================================
-- 5. VERIFICACIÓN
-- ============================================================
SELECT 'departamentos:' AS tabla, COUNT(*) AS rows FROM departamento
UNION ALL SELECT 'tipos_habitacion:', COUNT(*) FROM tipo_habitacion
UNION ALL SELECT 'hoteles:',          COUNT(*) FROM hotel
UNION ALL SELECT 'habitaciones:',     COUNT(*) FROM habitacion;
-- Esperado:
--   departamentos:    7
--   tipos_habitacion: 3
--   hoteles:          14
--   habitaciones:     70
