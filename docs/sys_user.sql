/*
SQLyog Ultimate v11.11 (32 bit)
MySQL - 5.6.12-log 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `sys_user` (
	`id` int (11),
	`username` varchar (60),
	`passwd` varchar (96),
	`sys_user_type` tinyint (4),
	`status` tinyint (4),
	`privilege` varchar (300),
	`remark` varchar (300),
	`gmt_create` timestamp ,
	`gmt_modified` timestamp 
); 
insert into `sys_user` (`id`, `username`, `passwd`, `sys_user_type`, `status`, `privilege`, `remark`, `gmt_create`, `gmt_modified`) values('1','test','8af4defdc62234faa80b02fa6ff2bea7','0','1','1',NULL,'2014-11-04 15:38:20','2014-11-04 15:38:20');
insert into `sys_user` (`id`, `username`, `passwd`, `sys_user_type`, `status`, `privilege`, `remark`, `gmt_create`, `gmt_modified`) values('2','test1','c96ef47007e3b0ebd89cdf54030a1fb8','1','1','1','ceshi','2014-11-05 17:45:50','2014-11-05 17:45:50');
