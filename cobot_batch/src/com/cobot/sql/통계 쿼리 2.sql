select * from tb_post_info 
where date(reg_dttm ) = '2017-07-26'

;


SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(t.arr_tag_str, ',', n.n), ',', -1) value
  FROM tb_post_info t CROSS JOIN tb_post_info n
 WHERE n.n <= 1 + (LENGTH(t.arr_tag_str) - LENGTH(REPLACE(t.arr_tag_str, ',', '')))
 ORDER BY arr_tag_str
;

select
  tb_post_info.post_title,
  SUBSTRING_INDEX(SUBSTRING_INDEX(tb_post_info.arr_tag_str, ',', numbers.n), ',', -1) name
from
  (select 1 n union all
   select 2 union all select 3 union all
   select 4 union all select 5) 
	numbers INNER JOIN tb_post_info
		on CHAR_LENGTH(tb_post_info.arr_tag_str)
     		-CHAR_LENGTH(REPLACE(tb_post_info.arr_tag_str, ',', ''))>=numbers.n-1
      and date(tb_post_info.reg_dttm ) = '2017-07-26'
order by
  tb_post_info.post_title, n

;

select 
* from
(
	select
	  SUBSTRING_INDEX(SUBSTRING_INDEX(tb_post_info.arr_tag_str, ',', numbers.n), ',', -1) name
	  , count(*) cnt
	from
	  (select 1 n union all
	   select 2 union all select 3 union all
	   select 4 union all select 5) 
		numbers INNER JOIN tb_post_info
			on CHAR_LENGTH(tb_post_info.arr_tag_str)
	     		-CHAR_LENGTH(REPLACE(tb_post_info.arr_tag_str, ',', ''))>=numbers.n-1
	      and date(tb_post_info.reg_dttm ) = '2017-07-26'
	group by SUBSTRING_INDEX(SUBSTRING_INDEX(tb_post_info.arr_tag_str, ',', numbers.n), ',', -1)
	order by
	  count(*) desc
) tmp
where ( name = 'coinkorea' or instr(name, 'kr') > 0 )

;


select 
* from
(
	select
	  SUBSTRING_INDEX(SUBSTRING_INDEX(tb_post_info.arr_tag_str, ',', numbers.n), ',', -1) name
	  , count(*) cnt
	from
	  (select 1 n union all
	   select 2 union all select 3 union all
	   select 4 union all select 5) 
		numbers INNER JOIN tb_post_info
			on CHAR_LENGTH(tb_post_info.arr_tag_str)
	     		-CHAR_LENGTH(REPLACE(tb_post_info.arr_tag_str, ',', ''))>=numbers.n-1
	      and date(tb_post_info.reg_dttm ) = '2017-07-25'
	group by SUBSTRING_INDEX(SUBSTRING_INDEX(tb_post_info.arr_tag_str, ',', numbers.n), ',', -1)
	order by
	  count(*) desc
) tmp
where ( name = 'coinkorea' or instr(name, 'kr') > 0 )
    
  
;
select 
	substring_index(substring_index( a.arr_tag_str , ',', @run := @run + 1), ',', -1) tag_name
	, a.post_title
from 
	tb_post_info a
	, tb_post_info b
	, (select @run := 0) c 
where date(a.reg_dttm ) = '2017-07-26'

and a.arr_tag_str <> ''
and @run < length( a.arr_tag_str ) - length(replace( a.arr_tag_str , ',', '')) + 1; 



;

select count(*) from tb_post_info 
where date(reg_dttm ) = '2017-07-26'

;

select post_author, count(*) from tb_post_info 
where date(reg_dttm ) = '2017-07-26'
group by post_author
order by count(post_author) desc