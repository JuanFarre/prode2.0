-- Agregar columna empezada a la tabla fechas
ALTER TABLE fechas ADD COLUMN empezada BOOLEAN DEFAULT FALSE;

-- Actualizar todas las fechas existentes para que empezada sea false por defecto
UPDATE fechas SET empezada = FALSE WHERE empezada IS NULL;

-- Hacer que la columna sea NOT NULL
ALTER TABLE fechas ALTER COLUMN empezada SET NOT NULL;
