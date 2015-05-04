insert into cas_im values(1,'123','123');
insert into cas_units(id, name, uri_name, parent_id, left_id, right_id, enable, category) values(11, '上海教育集团', '上海教育集团', null, 1, 22, true, 'Group');
insert into cas_units(id, name, uri_name, parent_id, left_id, right_id, enable, category) values(1, '七宝中学教育集团', '上海教育集团-七宝中学教育集团', 11, 2, 11, true, 'Group');
insert into cas_units(id, name, uri_name, parent_id, left_id, right_id, enable, category) values(2, '上海市七宝中学', '七宝中学教育集团-上海市七宝中学', 1, 3, 4, true, 'School');
insert into cas_units(id, name, uri_name, parent_id, left_id, right_id, enable, category) values(3, '上海七宝德怀特高级中学', '七宝中学教育集团-上海七宝德怀特高级中学', 1, 5, 6, true, 'School');
insert into cas_units(id, name, uri_name, parent_id, left_id, right_id, enable, category) values(4, '上海市七宝xx小学', '七宝中学教育集团-上海市七宝xx小学', 1, 7, 8, true, 'School');
insert into cas_units(id, name, uri_name, parent_id, left_id, right_id, enable, category) values(5, '上海市七宝xx中学', '七宝中学教育集团-上海市七宝xx中学', 1, 9, 10, true, 'School');
insert into cas_units(id, name, uri_name, parent_id, left_id, right_id, enable, category) values(6, '上海徐汇教育集团', '上海教育集团-上海徐汇教育集团', 11, 12, 21, true, 'Group');
insert into cas_units(id, name, uri_name, parent_id, left_id, right_id, enable, category) values(7, '上海市徐家汇中学', '上海徐汇教育集团-上海市徐家汇中学', 6, 13, 14, true, 'School');
insert into cas_units(id, name, uri_name, parent_id, left_id, right_id, enable, category) values(8, '上海徐家汇xx高级中学', '上海徐汇教育集团-上海瑜伽会xx高级中学', 6, 15, 16, true, 'School');
insert into cas_units(id, name, uri_name, parent_id, left_id, right_id, enable, category) values(9, '上海市徐家汇xx小学', '上海徐汇教育集团-上海市徐家汇xx小学', 6, 17, 18, true, 'School');
insert into cas_units(id, name, uri_name, parent_id, left_id, right_id, enable, category) values(10, '上海市徐家汇xx中学', '上海徐汇教育集团-上海市徐家汇xx中学', 6, 19, 20, true, 'School');
insert into cas_units(id, name, uri_name, parent_id, left_id, right_id, enable, category) values(11, '三河市燕郊xx中学', '三河市燕郊xx中学', null, 23, 24, true, 'School');
insert into cas_users(audit,enableim,id, code,name,email,enable,gender,mobile,category,password,position,location,address,notes,imid)values(false,true,1, 'zhang','斯蒂芬','huang@gxcm.com','TRUE','女','18616949668','外部','9cbf8a4dcb8e30682b927f352d6559a0','店长','上海市-上海市-浦东新区','燕郊','篮球',1);
insert into cas_users(audit,enableim,id, code,name,email,enable,gender,mobile,category,password,position,location,address,notes)values(false,true,2, 'dj','乐福','eisof@gxcm.com','TRUE','男','12584125412','内部','9cbf8a4dcb8e30682b927f352d6559a0','店长','上海市-上海市-徐汇区','燕郊','羽毛球');
insert into cas_users(audit,enableim,id, code,name,email,enable,gender,mobile,category,password,position,location,address,notes)values(false,true,3, 'hiphop','科比','4585465581@qq.com','TRUE','女','18965412365','外部','7374ce58be384f97fb15117dd99fba3c','店长','上海市-上海市-浦东新区','燕郊','棒球');
insert into cas_users(audit,enableim,id, code,name,email,enable,gender,mobile,category,password,position,location,address,notes)values(false,true,4, 'rnb','艾弗森','665848554@qq.com','TRUE','男','12345678912','内部','85862151eaed9bbc8b94373243e687cf','联采','上海市-上海市-浦东新区','燕郊','高尔夫');
insert into cas_users(audit,enableim,id, code,name,email,enable,gender,mobile,category,password,position,location,address,notes)values(false,true,5, 'king','霍华德','dsea@gxcm.com','TRUE','男','12345678952','外部','74a0c18637d1c7585a37b331c78d71a8','联采','上海市-上海市-松江区','燕郊','排球');
insert into cas_users(audit,enableim,id, code,name,email,enable,gender,mobile,category,password,position,location,address,notes)values(false,true,6, 'jason','哈登','dsf@gxcm.com','TRUE','男','12345874521','内部','9b476ed9ae35b34d43890d662bd1924a','联采','上海市-上海市-浦东新区','燕郊','铅球');
insert into cas_users(audit,enableim,id, code,name,email,enable,gender,mobile,category,password,position,location,address,notes)values(false,true,7, 'jack','哈登','dsf@gxcm.com','TRUE','男','12345874522','内部','9cbf8a4dcb8e30682b927f352d6559a0','联采','河北省-三河市','燕郊','乒乓球');
insert into cas_news(id,title,type,user_id) values(1,'hehe','slideshow','1');
insert into cas_news(id,title,type,user_id) values(2,'haha','slideshow','1');
insert into cas_news(id,title,type,user_id) values(3,'aaa','slideshow','1');
insert into cas_news(id,title,type,user_id) values(4,'bbb','listshow','1');
insert into cas_news(id,title,type,user_id) values(5,'ccc','listshow','1');
insert into cas_news(id,title,type,user_id) values(6,'ddd','listshow','1');
insert into cas_news(id,title,type,user_id) values(7,'eee','listshow','1');
insert into cas_store(id,store_name,address,phone,type,province) values(1,'a','a','1','slideshow','上海市');
insert into cas_store(id,store_name,address,phone,type,province) values(2,'b','b','2','slideshow','上海市');
insert into cas_store(id,store_name,address,phone,type,province) values(3,'c','c','3','listshow','上海市');
insert into cas_store(id,store_name,address,phone,type,province) values(4,'d','d','4','listshow','江苏省');
insert into cas_store(id,store_name,address,phone,type,province) values(5,'e','e','5','listshow','江苏省');
insert into CAS_USER_UNIT values(1,1);
insert into CAS_USER_UNIT values(1,2);
insert into CAS_USER_UNIT values(2,3);
insert into CAS_USER_UNIT values(2,4);
insert into CAS_USER_UNIT values(3,5);
insert into CAS_USER_UNIT values(3,6);
insert into CAS_USER_UNIT values(4,7);
insert into CAS_USER_UNIT values(5,8);
insert into CAS_USER_UNIT values(5,9);
insert into CAS_USER_UNIT values(6,10);
insert into CAS_USER_UNIT values(7,11);
/*角色种子数据:*/
Insert into cas_role(id,role) values(1,'clerk');
Insert into cas_role(id,role) values(2, 'admin');
Insert into cas_role(id,role) values(3, 'buyer');
Insert into cas_role(id,role) values(4, 'supplier');
Insert into cas_role(id,role) values(5, 'visitor');
/*权限种子数据:*/
Insert into cas_permission (id,per_name) values(1,'im');
Insert into cas_permission (id,per_name) values(2,'cloudOffice');
Insert into cas_permission (id,per_name) values(3,'contacts');
Insert into cas_permission (id,per_name) values(4,'groupAvail');
Insert into cas_permission (id,per_name) values(5,'newsCreate');
Insert into cas_permission (id,per_name) values(6,'newsAvail');
Insert into cas_permission (id,per_name) values(7,'storeCreate');
Insert into cas_permission (id,per_name) values(8,'storeAvail');
Insert into cas_permission (id,per_name) values(9,'crowdCreate');
Insert into cas_permission (id,per_name) values(10,'crowdAvail');
Insert into cas_permission (id,per_name) values(11,'infoCreate');
Insert into cas_permission (id,per_name) values(12,'infoAvail');
Insert into cas_permission (id,per_name) values(13,'purchasingCreate');
Insert into cas_permission (id,per_name) values(14,'purchasingAvail');
Insert into cas_permission (id,per_name) values(15,'staffSquareCreate');
Insert into cas_permission (id,per_name) values(16,'staffSquareAvail');
Insert into cas_permission (id,per_name) values(17,'supplierSquareCreate');
Insert into cas_permission (id,per_name) values(18,'supplierSquareAvail');
Insert into cas_permission (id,per_name) values(19,'saleInfoCreate');
Insert into cas_permission (id,per_name) values(20,'saleInfoAvail');
Insert into cas_permission (id,per_name) values(21,'staffTrainCreate');
Insert into cas_permission (id,per_name) values(22,'staffTrainAvail');
Insert into cas_permission (id,per_name) values(23,'applyForSupplier');
Insert into cas_permission (id,per_name) values(24,'audit');
Insert into cas_permission (id,per_name) values(25,'configurationAudit');
/*用户关联角色种子数据:*/
Insert into CAS_USER_ROLE values(1,1);
Insert into CAS_USER_ROLE values(1,2);
Insert into CAS_USER_ROLE values(2,2);
Insert into CAS_USER_ROLE values(3,3);
Insert into CAS_USER_ROLE values(4,4);
Insert into CAS_USER_ROLE values(5,1);
Insert into CAS_USER_ROLE values(6,5);
Insert into CAS_USER_ROLE values(7,3);
/*角色关联权限 种子数据:
店员权限*/
Insert into CAS_ROLE_PERMISSION values(1,1);
Insert into CAS_ROLE_PERMISSION values(1,2);
Insert into CAS_ROLE_PERMISSION values(1,3);
Insert into CAS_ROLE_PERMISSION values(1,4);
Insert into CAS_ROLE_PERMISSION values(1,6);
Insert into CAS_ROLE_PERMISSION values(1,8);
Insert into CAS_ROLE_PERMISSION values(1,12);
Insert into CAS_ROLE_PERMISSION values(1,16);
Insert into CAS_ROLE_PERMISSION values(1,22);
/*管理员权限*/
Insert into CAS_ROLE_PERMISSION values(2,1);
Insert into CAS_ROLE_PERMISSION values(2,2);
Insert into CAS_ROLE_PERMISSION values(2,3);
Insert into CAS_ROLE_PERMISSION values(2,4);
Insert into CAS_ROLE_PERMISSION values(2,5);
Insert into CAS_ROLE_PERMISSION values(2,6);
Insert into CAS_ROLE_PERMISSION values(2,7);
Insert into CAS_ROLE_PERMISSION values(2,8);
Insert into CAS_ROLE_PERMISSION values(2,9);
Insert into CAS_ROLE_PERMISSION values(2,10);
Insert into CAS_ROLE_PERMISSION values(2,11);
Insert into CAS_ROLE_PERMISSION values(2,12);
Insert into CAS_ROLE_PERMISSION values(2,15);
Insert into CAS_ROLE_PERMISSION values(2,16);
Insert into CAS_ROLE_PERMISSION values(2,17);
Insert into CAS_ROLE_PERMISSION values(2,18);
Insert into CAS_ROLE_PERMISSION values(2,19);
Insert into CAS_ROLE_PERMISSION values(2,20);
Insert into CAS_ROLE_PERMISSION values(2,21);
Insert into CAS_ROLE_PERMISSION values(2,22);
Insert into CAS_ROLE_PERMISSION values(2,25);
/*联采权限:*/
Insert into CAS_ROLE_PERMISSION values(3,1);
Insert into CAS_ROLE_PERMISSION values(3,2);
Insert into CAS_ROLE_PERMISSION values(3,3);
Insert into CAS_ROLE_PERMISSION values(3,4);
Insert into CAS_ROLE_PERMISSION values(3,6);
Insert into CAS_ROLE_PERMISSION values(3,8);
Insert into CAS_ROLE_PERMISSION values(3,10);
Insert into CAS_ROLE_PERMISSION values(3,12);
Insert into CAS_ROLE_PERMISSION values(3,13);
Insert into CAS_ROLE_PERMISSION values(3,14);
Insert into CAS_ROLE_PERMISSION values(3,16);
Insert into CAS_ROLE_PERMISSION values(3,22);
/*供应商权限:*/
Insert into CAS_ROLE_PERMISSION values(4,1);
Insert into CAS_ROLE_PERMISSION values(4,2);
Insert into CAS_ROLE_PERMISSION values(4,3);
Insert into CAS_ROLE_PERMISSION values(4,4);
Insert into CAS_ROLE_PERMISSION values(4,6);
Insert into CAS_ROLE_PERMISSION values(4,8);
Insert into CAS_ROLE_PERMISSION values(4,12);
Insert into CAS_ROLE_PERMISSION values(4,18);
Insert into CAS_ROLE_PERMISSION values(4,20);
Insert into CAS_ROLE_PERMISSION values(4,23);
/*广场数据*/
insert into cas_square (information,type,user_id,unit_id) values('sdfasd','员工',1,1);
insert into cas_square (information,type,user_id,unit_id) values('gaasdf','供应商',1,1);
insert into cas_square (information,type,user_id,unit_id) values('sdfgadsasd','联采',3,5);
insert into cas_square (information,type,user_id,unit_id) values('sdfgadsasd','联采',1,1);
insert into cas_square (information,type,user_id,unit_id) values('sdfgadsasd','联采',3,5);










