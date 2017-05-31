set @n := 0 

;

SELECT
	CCD
	, MIN_VOL
	, SUM( RISE_VOL) RISE_VOL
	, SUM( RISE_VOL) / MIN_VOL * 100 AS RISE_VOL_PER
	, SUM( RISE_PER ) RISE_PER	
	, SUBSTRING_INDEX (group_concat( RISE_PER order by SECTOR ASC, ',' ), ',', 1) RISE_PER_1	
	, SUBSTRING_INDEX (group_concat( RISE_VOL order by SECTOR DESC, ',' ), ',', 4) CAT_RISE_VOL
	, SUBSTRING_INDEX (group_concat( RISE_PER order by SECTOR DESC, ',' ), ',', 4) CAT_RISE_PER
	, SUBSTRING_INDEX (group_concat( MAX_PRICE order by SECTOR DESC, ',' ), ',', 4) CAT_PRICE
	, (
		SELECT LOW24HR FROM TB_CO_SNAP
		WHERE 1=1
		AND CCD = UNION_TMP.CCD
		AND UNIT_CID = 1
		ORDER BY REG_DTTM DESC
		LIMIT 1
	) AS LOW24HR
	, FLOOR( SUBSTRING_INDEX (group_concat( MAX_PRICE order by SECTOR ASC, ',' ), ',', 1) / (
		SELECT LOW24HR FROM TB_CO_SNAP
		WHERE 1=1
		AND CCD = UNION_TMP.CCD
		AND UNIT_CID = 1
		ORDER BY REG_DTTM DESC
		LIMIT 1
	) * 100  ) - 100 AS RISE24PER
FROM (

select 
	@n := @n + 1 as NO
	, '4' AS SECTOR
	, CCD
	, FLOOR(RISE_VOL) RISE_VOL
	, FLOOR(RISE_PER) RISE_PER
	, MIN_VOL
	, MAX_VOL  	
	, MIN_PRICE
	, MAX_PRICE
	, min_dttm
	, max_dttm
from (
select 
	
	ccd	
	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS min_vol
	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) AS max_vol
	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) - SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS RISE_VOL
	, SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) AS min_price
	, SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) AS max_price
	, (( SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) /  SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) ) - 1 ) * 100  as RISE_PER
	, min( reg_dttm ) min_dttm
	, max( reg_dttm ) max_dttm
	
from tb_co_snap
where 1=1
and unit_cid = 1
and srch_dttm BETWEEN date_sub(now(), interval 60 MINUTE ) AND date_sub(now(), interval 45 MINUTE )
group by ccd

) tmp 

union 

select 
	@n := @n + 1 as NO
	, '3'
	, CCD
	, FLOOR(RISE_VOL) RISE_VOL
	, FLOOR(RISE_PER) RISE_PER
	, MIN_VOL
	, MAX_VOL  	
	, MIN_PRICE
	, MAX_PRICE
	, min_dttm
	, max_dttm
from (
select 
	
	ccd	
	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS min_vol
	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) AS max_vol
	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) - SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS RISE_VOL
	, SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) AS min_price
	, SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) AS max_price
	, (( SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) /  SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) ) - 1 ) * 100  as RISE_PER
	, min( reg_dttm ) min_dttm
	, max( reg_dttm ) max_dttm
from tb_co_snap
where 1=1
and unit_cid = 1
and srch_dttm BETWEEN date_sub(now(), interval 45 MINUTE ) AND date_sub(now(), interval 30 MINUTE )
group by ccd

	
) tmp 

union 

select 
	@n := @n + 1 as NO
	, '2'
	, CCD
	, FLOOR(RISE_VOL) RISE_VOL
	, FLOOR(RISE_PER) RISE_PER
	, MIN_VOL
	, MAX_VOL  	
	, MIN_PRICE
	, MAX_PRICE
	, min_dttm
	, max_dttm
from (
select 
	
	ccd	
	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS min_vol
	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) AS max_vol
	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) - SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS RISE_VOL
	, SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) AS min_price
	, SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) AS max_price
	, (( SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) /  SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) ) - 1 ) * 100  as RISE_PER
	, min( reg_dttm ) min_dttm
	, max( reg_dttm ) max_dttm
from tb_co_snap
where 1=1
and unit_cid = 1
and srch_dttm BETWEEN date_sub(now(), interval 30 MINUTE ) AND date_sub(now(), interval 15 MINUTE )
group by ccd

	
) tmp 

union 

select 
	@n := @n + 1 as NO
	, '1'
	, CCD
	, FLOOR(RISE_VOL) RISE_VOL
	, FLOOR(RISE_PER) RISE_PER
	, MIN_VOL
	, MAX_VOL  	
	, MIN_PRICE
	, MAX_PRICE
	, min_dttm
	, max_dttm
from (
select 
	
	ccd	
	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS min_vol
	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) AS max_vol
	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) - SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS RISE_VOL
	, SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) AS min_price
	, SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) AS max_price
	, (( SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) /  SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) ) - 1 ) * 100  as RISE_PER
	, min( reg_dttm ) min_dttm
	, max( reg_dttm ) max_dttm
from tb_co_snap
where 1=1
and unit_cid = 1
and srch_dttm BETWEEN date_sub(now(), interval 15 MINUTE ) AND date_sub(now(), interval 0 MINUTE )
group by ccd

	
) tmp 

) UNION_TMP 

WHERE 1=1
GROUP BY CCD
HAVING SUM(RISE_VOL) > 10
AND SUM(RISE_PER) > 0
ORDER BY SUM(RISE_VOL) DESC