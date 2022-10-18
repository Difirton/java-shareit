INSERT INTO users (id, name, email)
VALUES (1, 'testUser', 'test@mail.ru' ),
       (2, 'test2User', 'test2@mail.ru' ),
       (3, 'test3User', 'test3@mail.ru' );

INSERT INTO items (id, name, description, available, user_id)
VALUES (1, 'testte', 'descTestss', true, 1),
       (2, 'test2ff', 'desc2Test', false, 1),
       (3, 'test3ff', 'desc3Test', true, 2),
       (4, 'test4', 'desc4Testff', true, 3),
       (5, 'test5te', 'desc5ss', false, 2),
       (6, 'test6te', 'desc6ss', true, 1);
