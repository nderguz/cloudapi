INSERT INTO users (id, password, login)
VALUES (1, '$2a$12$AwTGWNs/RAWPv6yPbvnK9.U26Gaffa8L35Kfry23CU9YuGaXP8kIG', 'test_user1');
INSERT INTO users (id, password, login)
VALUES (2, '$2a$12$RzipMYO0aCP5CafQ1PdPP.m3XejJ92mPTPiDq6PNS0GkuDT7q71PS', 'test_user2');
INSERT INTO users (id, password, login)
VALUES (3, '$2a$12$gYshAjxnVMUFuZBUGi/72eUJmUdbdKkogJMfl349vTduUezkEXuTy', 'admin');

-- INSERT INTO users (id, password, login)
-- VALUES (4, '$2a$12$jUDDgfrK0QWzrJIpaJbQ5.vWDYUovoC4mdCnq7uJKv4LKsBmrNv0O', 'user');

INSERT INTO user_roles (user_id, role)
VALUES (1, 'ROLE_USER');
INSERT INTO user_roles (user_id, role)
VALUES (2, 'ROLE_USER');
INSERT INTO user_roles (user_id, role)
VALUES (3, 'ROLE_ADMIN');


-- WITH seq_data AS (SELECT pg_get_serial_sequence('users', 'id') AS seq_name)
-- SELECT setval(seq_name, 4, FALSE) FROM seq_data;
--
-- WITH seq_data AS (SELECT pg_get_serial_sequence('post', 'id') AS seq_name)
-- SELECT setval(seq_name, 1, FALSE) FROM seq_data;
--
-- WITH seq_data AS (SELECT pg_get_serial_sequence('subscription', 'id') AS seq_name)
-- SELECT setval(seq_name, 1, FALSE) FROM seq_data;