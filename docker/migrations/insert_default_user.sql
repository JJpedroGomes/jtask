INSERT INTO users (id, name, email, password, birth_date, creation_date, is_active)
SELECT 1000, 'default', 'email@email.com', '$2a$10$3bFaVNzZ4H3EfsfueIugruwxMRFJ/Tanh6XOuUBBk.HODPpu1vw16', '2000-11-15', '1912-4-13', true
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE id = 1000
);