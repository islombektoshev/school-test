-- insert users
insert into users(username, password, payment_expired_date, firstname, lastname) values ('islom', '123456789', to_timestamp('2099-09-21 21:21:32','YYYY-MM-DD HH24:MI:SS'), 'Islombek', 'Toshev');
insert into users(username, password, payment_expired_date, firstname, lastname) values ('behruz', '123456789', to_timestamp('2019-11-10 21:21:32','YYYY-MM-DD HH24:MI:SS'), 'Mahmudov', 'Behruz');

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
insert into scenter (caption, name, author_id) values ('nimadir', 'first'  , 1);
insert into scenter (caption, name, author_id) values ('nimadir', 'second' , 1);
insert into scenter (caption, name, author_id) values ('nimadir', 'first'  , 2);
insert into scenter (caption, name, author_id) values ('nimadir', 'second' , 2);

-- insert subjects
insert into subject (name, scenter_id) values ('Matematika', 1);
insert into subject (name, scenter_id) values ('Fizika', 1);
insert into subject (name, scenter_id) values ('Ona tili', 1);

-- insert guruh
insert into guruh (name, scenter_id) values ('A', 1);
insert into guruh (name, scenter_id) values ('B', 1);
insert into guruh (name, scenter_id) values ('C', 1);

-- insert students
insert into student (firstname, lastname, guruh_id, scenter_id) values ('islom', 'toshev', 1, 1);
insert into student (firstname, lastname, guruh_id, scenter_id) values ('behruz', 'mahmudov', 1, 1);
insert into student (firstname, lastname, guruh_id, scenter_id) values ('oybek', 'muhhidinov', 1, 1);

