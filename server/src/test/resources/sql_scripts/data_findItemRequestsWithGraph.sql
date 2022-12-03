INSERT INTO users (name, email)
VALUES ('testUser', 'test@mail.ru' ),
       ('test2User', 'test2@mail.ru' ),
       ('test3User', 'test3@mail.ru' );

INSERT INTO items_requests (description, user_id, created)
VALUES ('test1', 1, now()),
       ('test2', 2, now()),
       ('test3', 3, now());

INSERT INTO items (name, description, available, owner_id, item_request_id)
VALUES ('testte', 'descTestss', true, 1, 1),
       ('test2ff', 'desc2Test', false, 1, 1),
       ('test3ff', 'desc3Test', true, 2, 2),
       ('test4', 'desc4Testff', true, 3, 2),
       ('test5te', 'desc5ss', false, 2, 2),
       ('test6te', 'desc6ss', true, 1, 3);

