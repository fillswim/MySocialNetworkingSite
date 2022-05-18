INSERT INTO users(id, created, updated, username, first_name, last_name, password, email, role, status)
VALUES (1, now(), now(), 'user', 'UserFirstname', 'UserLastname',
        '$2a$10$r5tZGzprBSg0nZOwQ3Gj8.n9BtWbHCQgaC603vODTV7iXEpSGSCsu',
        'user@mail.ru', 'ROLE_USER', 'ACTIVE'),
       (2, now(), now(), 'admin', 'AdminFirstname', 'AdminLastname',
        '$2a$10$PyqPmTitRJGlQwsYn5NQm.PNMu3njcxWgMRbCp3Ujhf04jBsTtS8u',
        'admin@mail.ru', 'ROLE_ADMIN', 'ACTIVE');

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

INSERT INTO posts(id, created, updated, caption, likes, location, title, user_id)
VALUES (1, now(), now(), 'Caption1', 1, 'Location1', 'Title1', 1),
       (2, now(), now(), 'Caption2', 1, 'Location2', 'Title2', 1),
       (3, now(), now(), 'Caption3', 1, 'Location3', 'Title3', 2),
       (4, now(), now(), 'Caption4', 1, 'Location4', 'Title4', 2);

SELECT setval('posts_id_seq', (SELECT MAX(id) FROM posts));

INSERT INTO comments(id, created, updated, message, post_id, user_id)
VALUES (1, now(), now(), 'Comment Text 1', 1, 1),
       (2, now(), now(), 'Comment Text 2', 1, 2),
       (3, now(), now(), 'Comment Text 3', 2, 2),
       (4, now(), now(), 'Comment Text 4', 2, 1);

SELECT setval('comments_id_seq', (SELECT MAX(id) FROM comments));

INSERT INTO post_liked_users(post_id, liked_user)
VALUES (1, 'user'),
       (2, 'admin'),
       (3, 'admin'),
       (4, 'user');

