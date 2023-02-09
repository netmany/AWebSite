SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

create database myweb;
use myweb;

create table `user` 
(`id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, 
 `name` varchar(32) not null, 
 `age` int(10) default '0', 
 `gender` int(2) default '0', 
 `height` float(6,3), 
 `country` varchar(32) default null, 
 `phonenum` bigint not null);
 
alter table user add unique key `phonenum` (`phonenum`);

insert into user (name, age, gender, height, country, phonenum) values ("henry", 18, 1, 2.34, "france", 14325345443);

commit;
