insert into user(id, login, password, role, active) values (1, "user1", "$2a$08$Ijutdok/02EsUs3JXKcvnefYErhej6q1CMfD/nQ2XH8qVUWUkppz2", "TOURIST", 1);
insert into user(id, login, password, role, active) values (2, "admin", "$2a$08$Ijutdok/02EsUs3JXKcvnefYErhej6q1CMfD/nQ2XH8qVUWUkppz2", "ADMIN", 1);
insert into user(id, login, password, role, active) values (3, "user3", "$2a$08$Ijutdok/02EsUs3JXKcvnefYErhej6q1CMfD/nQ2XH8qVUWUkppz2", "VOLUNTEER", 1);
INSERT INTO `wtscdb`.`confirmation_token` (`token_id`, `activated`, `confirmation_token`, `user_id`) VALUES ("1", b'1', "133", "1");
INSERT INTO `wtscdb`.`confirmation_token` (`token_id`, `activated`, `confirmation_token`, `user_id`) VALUES ("2", b'1', "133", "3");