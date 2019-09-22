insert into user(username, password) values ("islom", "1");
insert into user(username, password) values ("behruz", "1");

insert into role(rolename) values ("ROLE_USER");
insert into role(rolename) values ("ROLE_ADMIN");

insert into role_users(roles_id, users_id) VALUES (1, 1);
insert into role_users(roles_id, users_id) VALUES (2, 1);
insert into role_users(roles_id, users_id) VALUES (1, 2);


