insert
  into board
values (null, 
		'안녕', 
		'ㅎㅎ', 
		0, 
		now(), 
		(select ifnull(max(group_no), 0) + 1 from board a), 
		1, 
		0, 
		2);
 
select ifnull(max(group_no), 0) + 1 from board;

select a.no, 
	   title,
	   content,
	   hit, 
	   date_format(reg_date, '%Y-%m-%d %p %h:%i:%s'),
	   b.no,
	   b.name 
  from board a,
       user b
 where a.user_no = b.no limit 0, 10;
 	   
select * from user; 
