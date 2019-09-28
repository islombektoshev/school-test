-- insert users
insert into users(username, password) values ('islom', '1');
insert into users(username, password) values ('behruz', '1');

-- insert roles
insert into role(rolename) values ('ROLE_USER');
insert into role(rolename) values ('ROLE_ADMIN');

-- insert rels
insert into role_users(roles_id, users_id) VALUES (1, 1);
insert into role_users(roles_id, users_id) VALUES (2, 1);
insert into role_users(roles_id, users_id) VALUES (1, 2);

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

