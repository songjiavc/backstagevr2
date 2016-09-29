
/*
	※※※11/3更新的初始化数据主要是用来初始化超级管理员的初始权限，包括"用户模块"下的一系列功能
*/

/*
	初始化权限表数据
*/
insert  into `T_SDF_AUTHORITY`(`ID`,`CREATER`,`CREATER_TIME`,`MODIFY`,`MODIFY_TIME`,`AUTH_IMG`,`AUTH_NAME`,`CODE`,`PARANT_AUTH_ID`,`STATUS`,`URL`,`IS_DELETED`,`ISSYSTEM`) 
values ('1','admin','2015-10-15 09:35:14','admin','2015-10-15 09:35:14',NULL,'权限树根','1','','1',NULL,'1','1');
insert  into `T_SDF_AUTHORITY`(`ID`,`CREATER`,`CREATER_TIME`,`MODIFY`,`MODIFY_TIME`,`AUTH_IMG`,`AUTH_NAME`,`CODE`,`PARANT_AUTH_ID`,`STATUS`,`URL`,`IS_DELETED`,`ISSYSTEM`) 
values ('4028813a505fe87201505ff93aa80002','admin','2015-10-13 14:53:00','admin','2015-10-14 14:39:08','icon-nav','用户','1','1','1','','1','1');
insert  into `T_SDF_AUTHORITY`(`ID`,`CREATER`,`CREATER_TIME`,`MODIFY`,`MODIFY_TIME`,`AUTH_IMG`,`AUTH_NAME`,`CODE`,`PARANT_AUTH_ID`,`STATUS`,`URL`,`IS_DELETED`,`ISSYSTEM`) 
values ('4028813a506515ad01506519290a0001','admin','2015-10-14 14:45:59','admin','2015-10-14 15:21:26','icon-nav','账号管理','11','4028813a505fe87201505ff93aa80002','1','/menu/useraccount.action','1','1');
insert  into `T_SDF_AUTHORITY`(`ID`,`CREATER`,`CREATER_TIME`,`MODIFY`,`MODIFY_TIME`,`AUTH_IMG`,`AUTH_NAME`,`CODE`,`PARANT_AUTH_ID`,`STATUS`,`URL`,`IS_DELETED`,`ISSYSTEM`) 
values ('4028813a506515ad01506519e1720002','admin','2015-10-14 14:46:46','admin','2015-10-14 15:21:19','icon-nav','权限管理','12','4028813a505fe87201505ff93aa80002','1','/menu/authority.action','1','1');
insert  into `T_SDF_AUTHORITY`(`ID`,`CREATER`,`CREATER_TIME`,`MODIFY`,`MODIFY_TIME`,`AUTH_IMG`,`AUTH_NAME`,`CODE`,`PARANT_AUTH_ID`,`STATUS`,`URL`,`IS_DELETED`,`ISSYSTEM`) 
values ('ff808181513737320151373886c80000', 'agentMgr', '代理管理', '4028813a505fe87201505ff93aa80002', '/agent/agentmanager.action', 'icon-nav', '1', 'admin', '2015-11-24 10:00:30', 'admin', '2015-11-24 10:02:12', '1', '0');
insert  into `T_SDF_AUTHORITY`(`ID`,`CREATER`,`CREATER_TIME`,`MODIFY`,`MODIFY_TIME`,`AUTH_IMG`,`AUTH_NAME`,`CODE`,`PARANT_AUTH_ID`,`STATUS`,`URL`,`IS_DELETED`,`ISSYSTEM`) 
values ('4028813a506536910150653be3ed0001', 'auth5', '站点管理', '4028813a505fe87201505ff93aa80002', '/station/stationmanager.action', 'icon-nav', '1', 'admin', '2015-10-14 15:23:55', 'admin', '2015-10-14 15:23:55', '1', '1');
insert  into `T_SDF_AUTHORITY`(`ID`,`CREATER`,`CREATER_TIME`,`MODIFY`,`MODIFY_TIME`,`AUTH_IMG`,`AUTH_NAME`,`CODE`,`PARANT_AUTH_ID`,`STATUS`,`URL`,`IS_DELETED`,`ISSYSTEM`) 
values ('4028813a506a4de701506a79c6990000','admin','2015-10-15 15:49:37','admin','2015-10-15 15:49:37','11','角色管理','tole55','4028813a505fe87201505ff93aa80002','1','/menu/roleManage.action','1','1');



/*
	初始化角色表数据
*/
insert  into `T_SDF_ROLES`(`ID`,`CREATER`,`CREATER_TIME`,`IS_DELETED`,`MODIFY`,`MODIFY_TIME`,`CODE`,`NAME`,`PARENT_ROLE`,`PARENT_ROLENAME`,`ISSYSTEM`) 
values ('0','admin','2015-10-13 14:53:00','1','admin','2015-10-20 10:20:38','1','超级管理员','','','1');
insert  into `T_SDF_ROLES`(`ID`,`CREATER`,`CREATER_TIME`,`IS_DELETED`,`MODIFY`,`MODIFY_TIME`,`CODE`,`NAME`,`PARENT_ROLE`,`PARENT_ROLENAME`,`ISSYSTEM`) 
values ('1','admin','2015-10-13 14:53:00','1','admin','2015-10-20 10:20:38','2','财务管理员','0','超级管理员','1');
insert  into `T_SDF_ROLES`(`ID`,`CREATER`,`CREATER_TIME`,`IS_DELETED`,`MODIFY`,`MODIFY_TIME`,`CODE`,`NAME`,`PARENT_ROLE`,`PARENT_ROLENAME`,`ISSYSTEM`) 
values ('2','admin','2015-10-13 14:53:00','1','admin','2015-10-20 10:20:38','3','代理','0','超级管理员','1');
/*新添加两个初始化角色数据，省中心和市中心，用于在发布应用公告和应用广告还有通告时使用*/
insert  into `T_SDF_ROLES`(`ID`,`CREATER`,`CREATER_TIME`,`IS_DELETED`,`MODIFY`,`MODIFY_TIME`,`CODE`,`NAME`,`PARENT_ROLE`,`PARENT_ROLENAME`,`ISSYSTEM`) 
values ('3','admin','2015-10-13 14:53:00','1','admin','2015-10-20 10:20:38','4','省中心','0','超级管理员','1');
insert  into `T_SDF_ROLES`(`ID`,`CREATER`,`CREATER_TIME`,`IS_DELETED`,`MODIFY`,`MODIFY_TIME`,`CODE`,`NAME`,`PARENT_ROLE`,`PARENT_ROLENAME`,`ISSYSTEM`) 
values ('4','admin','2015-10-13 14:53:00','1','admin','2015-10-20 10:20:38','5','市中心','0','超级管理员','1');
/*2016-8-9日添加两个角色，“公司补录员”，“对外补录员”*/
insert  into `T_SDF_ROLES`(`ID`,`CREATER`,`CREATER_TIME`,`IS_DELETED`,`MODIFY`,`MODIFY_TIME`,`CODE`,`NAME`,`PARENT_ROLE`,`PARENT_ROLENAME`,`ISSYSTEM`) 
values ('5','admin','2016-08-06 14:53:00','1','admin','2015-10-20 10:20:38','6','公司补录员','0','超级管理员','1');
insert  into `T_SDF_ROLES`(`ID`,`CREATER`,`CREATER_TIME`,`IS_DELETED`,`MODIFY`,`MODIFY_TIME`,`CODE`,`NAME`,`PARENT_ROLE`,`PARENT_ROLENAME`,`ISSYSTEM`) 
values ('6','admin','2015-08-06 14:53:00','1','admin','2015-10-20 10:20:38','7','对外补录员','0','超级管理员','1');

insert  into `T_SDF_ROLES`(`ID`,`CREATER`,`CREATER_TIME`,`IS_DELETED`,`MODIFY`,`MODIFY_TIME`,`CODE`,`NAME`,`PARENT_ROLE`,`PARENT_ROLENAME`,`ISSYSTEM`) 
values ('ff808181514698fb015146a02eaf0000', 'admin', '2015-11-27 09:48:01', '1', 'admin', '2015-11-27 09:48:01', 'SC_ZJ', '市场总监', '0', '超级管理员', '0');
insert  into `T_SDF_ROLES`(`ID`,`CREATER`,`CREATER_TIME`,`IS_DELETED`,`MODIFY`,`MODIFY_TIME`,`CODE`,`NAME`,`PARENT_ROLE`,`PARENT_ROLENAME`,`ISSYSTEM`) 
values ('ff808181514698fb015146a085460001', 'admin', '2015-11-27 09:48:23', '1', 'admin', '2015-12-03 15:51:45', 'SC_ZY', '市场专员', 'ff808181514698fb015146a02eaf0000', '市场总监', '0');




/*
	初始化用户表数据
 */

insert  into `T_SDF_USERS`(`ID`,`CREATER`,`CREATER_TIME`,`MODIFY`,`MODIFY_TIME`,`ADDRESS`,`CITY_CODE`,`CODE`,`NAME`,`PARENT_UID`,`PASSWORD`,`PROVINCE_CODE`,`REGION_CODE`,`IS_DELETED`,`status`,`TELEPHONE`,`roles`) 
values ('0','admin','2015-10-29 13:09:12','admin','2015-10-29 13:09:12',NULL,NULL,'admin','admin',NULL,'1',NULL,NULL,'1','1','',NULL);

/*
	初始化用户角色关联表数据
*/
INSERT  INTO `RELA_SDF_USER_ROLE`(`ID`,`ROLE_ID`,`USER_ID`) 
VALUES ('4028813a50b2024b0150b203b0b90000','0','0');


/*
	初始化角色权限关联表数据
*/
insert  into `RELA_SDF_AUTHORITY_ROLE`(`ROLE_ID`,`authority_ID`) 
values ('0','1');/*超级管理员权限*/
insert  into `RELA_SDF_AUTHORITY_ROLE`(`ROLE_ID`,`authority_ID`) 
values ('0','4028813a505fe87201505ff93aa80002');/*超级管理员权限*/
insert  into `RELA_SDF_AUTHORITY_ROLE`(`ROLE_ID`,`authority_ID`) 
values ('0','4028813a506a4de701506a79c6990000');/*超级管理员权限*/
insert  into `RELA_SDF_AUTHORITY_ROLE`(`ROLE_ID`,`authority_ID`) 
values ('0','4028813a506536910150653be3ed0001');/*超级管理员权限*/
insert  into `RELA_SDF_AUTHORITY_ROLE`(`ROLE_ID`,`authority_ID`) 
values ('0','4028813a506515ad01506519e1720002');/*超级管理员权限*/
insert  into `RELA_SDF_AUTHORITY_ROLE`(`ROLE_ID`,`authority_ID`) 
values ('0','4028813a506515ad01506519290a0001');/*超级管理员权限*/
insert into `RELA_SDF_AUTHORITY_ROLE` (`ROLE_ID`, `authority_ID`)
values('1','4028813a506515ad01506519e1720002');/*财务管理员权限*/


/*
 * 初始化审批站点发布应用广告状态表数据
 */
insert into `T_BS_STATION_AD_STATUS` (`ID`, `isParent`, `parentStatus`, `statusId`, `statusName`) values('1','1',NULL,'00','站点发布应用广告');
insert into `T_BS_STATION_AD_STATUS` (`ID`, `isParent`, `parentStatus`, `statusId`, `statusName`) values('2','0','1','01','站点保存应用广告');
insert into `T_BS_STATION_AD_STATUS` (`ID`, `isParent`, `parentStatus`, `statusId`, `statusName`) values('3','0','1','02','市中心驳回应用广告');
insert into `T_BS_STATION_AD_STATUS` (`ID`, `isParent`, `parentStatus`, `statusId`, `statusName`) values('4','1',NULL,'10','市中心审批站点应用广告');
insert into `T_BS_STATION_AD_STATUS` (`ID`, `isParent`, `parentStatus`, `statusId`, `statusName`) values('5','0','4','11','提交市中心审批');
insert into `T_BS_STATION_AD_STATUS` (`ID`, `isParent`, `parentStatus`, `statusId`, `statusName`) values('6','1',NULL,'20','审批完成');
insert into `T_BS_STATION_AD_STATUS` (`ID`, `isParent`, `parentStatus`, `statusId`, `statusName`) values('7','0','6','21','审批完成发布站点广告');
insert into `T_BS_STATION_AD_STATUS` (`ID`, `isParent`, `parentStatus`, `statusId`, `statusName`) values('8','1',NULL,'30','不通过');
insert into `T_BS_STATION_AD_STATUS` (`ID`, `isParent`, `parentStatus`, `statusId`, `statusName`) values('9','0','8','31','审批不通过');


/*
 * 初始化审批站点发布应用广告状态流程表数据
 */
insert into `T_BS_STATION_AD_NEXT_STATUS` (`ID`, `current_status_id`, `direction_flag`, `next_status_id`) values('1','01','1','11');
insert into `T_BS_STATION_AD_NEXT_STATUS` (`ID`, `current_status_id`, `direction_flag`, `next_status_id`) values('2','02','1','11');
insert into `T_BS_STATION_AD_NEXT_STATUS` (`ID`, `current_status_id`, `direction_flag`, `next_status_id`) values('3','11','1','21');
insert into `T_BS_STATION_AD_NEXT_STATUS` (`ID`, `current_status_id`, `direction_flag`, `next_status_id`) values('4','11','0','02');


/*
 * 初始化审批专家发布图谜字谜状态表数据
 */
INSERT INTO `T_BYL_FIGURE_AND_PUZZLE_APP_STATUS` (`ID`, `IS_PARENT`, `PARENT_STATUS`, `STATUS_ID`, `STATUS_NAME`) VALUES('1','1',NULL,'00','专家发布图谜字谜');
INSERT INTO `T_BYL_FIGURE_AND_PUZZLE_APP_STATUS` (`ID`, `IS_PARENT`, `PARENT_STATUS`, `STATUS_ID`, `STATUS_NAME`) VALUES('2','0','1','01','专家保存图谜字谜');
INSERT INTO `T_BYL_FIGURE_AND_PUZZLE_APP_STATUS` (`ID`, `IS_PARENT`, `PARENT_STATUS`, `STATUS_ID`, `STATUS_NAME`) VALUES('3','0','1','02','公司驳回专家图谜字谜');
INSERT INTO `T_BYL_FIGURE_AND_PUZZLE_APP_STATUS` (`ID`, `IS_PARENT`, `PARENT_STATUS`, `STATUS_ID`, `STATUS_NAME`) VALUES('4','1',NULL,'10','公司审批专家图谜字谜');
INSERT INTO `T_BYL_FIGURE_AND_PUZZLE_APP_STATUS` (`ID`, `IS_PARENT`, `PARENT_STATUS`, `STATUS_ID`, `STATUS_NAME`) VALUES('5','0','4','11','提交公司审批');
INSERT INTO `T_BYL_FIGURE_AND_PUZZLE_APP_STATUS` (`ID`, `IS_PARENT`, `PARENT_STATUS`, `STATUS_ID`, `STATUS_NAME`) VALUES('6','1',NULL,'20','审批完成');
INSERT INTO `T_BYL_FIGURE_AND_PUZZLE_APP_STATUS` (`ID`, `IS_PARENT`, `PARENT_STATUS`, `STATUS_ID`, `STATUS_NAME`) VALUES('7','0','6','21','审批完成发布图谜字谜');
INSERT INTO `T_BYL_FIGURE_AND_PUZZLE_APP_STATUS` (`ID`, `IS_PARENT`, `PARENT_STATUS`, `STATUS_ID`, `STATUS_NAME`) VALUES('8','1',NULL,'30','不通过');
INSERT INTO `T_BYL_FIGURE_AND_PUZZLE_APP_STATUS` (`ID`, `IS_PARENT`, `PARENT_STATUS`, `STATUS_ID`, `STATUS_NAME`) VALUES('9','0','8','31','审批不通过');


/*
 * 初始化审批专家发布图谜字谜状态流程表数据
 */
INSERT INTO `T_BYL_FIGURE_AND_PUZZLE_NEXT_STATUS` (`ID`, `CURRENT_STATUS_ID`, `DIRECTION_FLAG`, `NEXT_STATUS_ID`) VALUES('1','01','1','11');
INSERT INTO `T_BYL_FIGURE_AND_PUZZLE_NEXT_STATUS` (`ID`, `CURRENT_STATUS_ID`, `DIRECTION_FLAG`, `NEXT_STATUS_ID`) VALUES('2','02','1','11');
INSERT INTO `T_BYL_FIGURE_AND_PUZZLE_NEXT_STATUS` (`ID`, `CURRENT_STATUS_ID`, `DIRECTION_FLAG`, `NEXT_STATUS_ID`) VALUES('3','11','1','21');
INSERT INTO `T_BYL_FIGURE_AND_PUZZLE_NEXT_STATUS` (`ID`, `CURRENT_STATUS_ID`, `DIRECTION_FLAG`, `NEXT_STATUS_ID`) VALUES('4','11','0','02');

/*
 *初始化代理提交订单状态流程表数据
 */
insert  into `T_SDF_ORDER_NEXT_STATUS`(`id`,`current_status_id`,`next_status_id`,`direction_flag`) values (1,'01','11','1');
insert  into `T_SDF_ORDER_NEXT_STATUS`(`id`,`current_status_id`,`next_status_id`,`direction_flag`) values (2,'02','11','1');
insert  into `T_SDF_ORDER_NEXT_STATUS`(`id`,`current_status_id`,`next_status_id`,`direction_flag`) values (3,'11','21','1');
insert  into `T_SDF_ORDER_NEXT_STATUS`(`id`,`current_status_id`,`next_status_id`,`direction_flag`) values (4,'11','02','0');

/*
 *初始化代理提交订单状态表数据 
 */

insert  into `T_SDF_ORDER_STATUS`(`id`,`statusId`,`statusName`,`isParent`,`parentStatus`) values (1,'00','代理录入订单',1,NULL);
insert  into `T_SDF_ORDER_STATUS`(`id`,`statusId`,`statusName`,`isParent`,`parentStatus`) values (2,'01','代理保存订单',0,'1');
insert  into `T_SDF_ORDER_STATUS`(`id`,`statusId`,`statusName`,`isParent`,`parentStatus`) values (3,'02','财务管理员驳回',0,'1');
insert  into `T_SDF_ORDER_STATUS`(`id`,`statusId`,`statusName`,`isParent`,`parentStatus`) values (4,'10','财务管理员审批',1,NULL);
insert  into `T_SDF_ORDER_STATUS`(`id`,`statusId`,`statusName`,`isParent`,`parentStatus`) values (5,'11','提交财务管理员审批',0,'4');
insert  into `T_SDF_ORDER_STATUS`(`id`,`statusId`,`statusName`,`isParent`,`parentStatus`) values (6,'20','审批完成',1,NULL);
insert  into `T_SDF_ORDER_STATUS`(`id`,`statusId`,`statusName`,`isParent`,`parentStatus`) values (7,'21','审批完成归档',0,'6');
insert  into `T_SDF_ORDER_STATUS`(`id`,`statusId`,`statusName`,`isParent`,`parentStatus`) values (8,'30','不通过',1,NULL);
insert  into `T_SDF_ORDER_STATUS`(`id`,`statusId`,`statusName`,`isParent`,`parentStatus`) values (9,'31','审批不通过',0,'8');




