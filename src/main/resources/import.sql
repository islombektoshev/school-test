-- insert users
insert into users(username, password) values ('islom', '123456789');
insert into users(username, password) values ('behruz', '123456789');

-- insert roles
insert into role(rolename) values ('ROLE_USER');
insert into role(rolename) values ('ROLE_ADMIN');
insert into role(rolename) values ('GET_ONLY');
insert into role(rolename) values ('POST_ONLY');
insert into role(rolename) values ('PUT_ONLY');
insert into role(rolename) values ('PATCH_ONLY');
insert into role(rolename) values ('FULL_USER_ACCESS');

-- insert rels
insert into users_roles(roles_id, users_id) VALUES (1, 1); -- ROLE_USER
insert into users_roles(roles_id, users_id) VALUES (2, 1); -- ROLE_ADMIN
insert into users_roles(roles_id, users_id) VALUES (3, 1); -- GET_ONLY
insert into users_roles(roles_id, users_id) VALUES (7, 1); -- FULL_USER_ACCESS
insert into users_roles(roles_id, users_id) VALUES (1, 2); -- ROLE_USER
insert into users_roles(roles_id, users_id) VALUES (3, 2); -- GET_ONLY
insert into users_roles(roles_id, users_id) VALUES (7, 2); -- FULL_USER_ACCESS

-- insert scenter
insert into scenter (id, caption, name, author_id) values (1, 'nimadir', 'first', 1);
insert into scenter (id, caption, name, author_id) values (2, 'nimadir', 'second', 1);
insert into scenter (id, caption, name, author_id) values (3, 'nimadir', 'first', 2);
insert into scenter (id, caption, name, author_id) values (4, 'nimadir', 'second', 2);

-- insert subjects
insert into subject (name, scenter_id) values ('Matematika', 1);
insert into subject (name, scenter_id) values ('Fizika', 1);
insert into subject (name, scenter_id) values ('Ona tili', 1);

-- insert guruh
insert into guruh (id, name, scenter_id) values (1, 'A', 1);
insert into guruh (id, name, scenter_id) values (2, 'B', 1);
insert into guruh (id, name, scenter_id) values (3, 'C', 1);

-- insert students
insert into student (firstname, lastname, guruh_id, scenter_id) values ('islom', 'toshev', 1, 1);
insert into student (firstname, lastname, guruh_id, scenter_id) values ('behruz', 'mahmudov', 1, 1);
insert into student (firstname, lastname, guruh_id, scenter_id) values ('oybek', 'muhhidinov', 1, 1);

