-- Agregar campo finalizado a la tabla partidos
ALTER TABLE partidos ADD COLUMN finalizado BOOLEAN DEFAULT FALSE NOT NULL;

-- Actualizar partidos existentes que ya tienen resultado como finalizados
UPDATE partidos 
SET finalizado = TRUE 
WHERE goles_local IS NOT NULL AND goles_visitante IS NOT NULL;
