create schema `db1`;
create table db1.account (
	id int auto_increment primary key,
    username varchar(10) not null,
    password varchar(200) not null,
    unique index idx_username (username)
);
ALTER TABLE db1.account CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
create schema `db2`;
create table db2.account (
	id int auto_increment primary key,
    username varchar(10) not null,
    password varchar(200) not null,
    unique index idx_username (username)
);
ALTER TABLE db2.account CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
create table db1.check_in (
	id int auto_increment primary key,
    user_id int not null,
    check_in_time datetime not null,
    foreign key (user_id) references db1.account(id)
);
create table db1.check_in_stats (
	id int auto_increment primary key,
    user_id int not null,
    time_difference int not null,
    foreign key (user_id) references db1.account(id)
);