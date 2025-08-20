;   --одна команда должны быть, если не будет запросов'

--INSERT INTO statusbooking (name) VALUES ('WAITING'), ('APPROVED'), ('REJECTED'), ('CANCELED') ON CONFLICT (name) DO NOTHING;
