
SELECT
	CCD
	, TRUNCATE( MIN_VOL, 2) AS MIN_VOL
	, TRUNCATE( MAX_VOL, 2) AS MAX_VOL
	, TRUNCATE(MAX_VOL - MIN_VOL , 2 ) AS RISE_VOL
	, TRUNCATE( (MAX_VOL - MIN_VOL) / MIN_VOL * 100, 3)  AS RISE_VOL_PER	
	, BEFORE_PRICE
	, AFTER_PRICE
	, TRUNCATE((AFTER_PRICE - BEFORE_PRICE) / BEFORE_PRICE * 100, 2) AS RISE_PRICE
	, TRUNCATE( AFTER_PRICE / LOW24HR * 100 ,2 ) - 100 AS RISE24PER	
	, TRUNCATE( AFTER_PRICE / HIGH24HR * 100 ,2 ) - 100 AS DOWN24PER	
	, HIGH24HR
FROM (

SELECT 
	CCD 
	, MAX( HIGH24HR ) AS HIGH24HR
	, MIN( LOW24HR ) AS LOW24HR
	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS min_vol
	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) AS max_vol
	, SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) AS BEFORE_PRICE
	, SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) AS AFTER_PRICE
FROM TB_CO_SNAP
WHERE 1=1
AND UNIT_CID = 1
AND srch_dttm > date_sub(now(), interval 3 MINUTE )
 
GROUP BY CCD
) TMP 
WHERE TRUNCATE(MAX_VOL - MIN_VOL , 2 ) > 5
AND TRUNCATE((AFTER_PRICE - BEFORE_PRICE) / BEFORE_PRICE * 100, 2) > 1
ORDER BY TRUNCATE((AFTER_PRICE - BEFORE_PRICE) / BEFORE_PRICE * 100, 2) DESC




;



SELECT * FROM TB_CO_SNAP
WHERE 1=1
AND CCD = 'XPM'
AND srch_dttm > date_sub(now(), interval 3 MINUTE )
ORDER BY REG_DTTM DESC
;

SELECT 
	CCD 
	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS min_vol
FROM TB_CO_SNAP
WHERE 1=1
AND srch_dttm > date_sub(now(), interval 3 MINUTE )
GROUP BY CCD

;


SELECT * FROM TB_AN_1
where 1=1
AND srch_dttm > date_sub(now(), interval 5 MINUTE )
ORDER BY SRCH_DTTM DESC


;



SELECT * FROM TB_AN_1
WHERE CCD = 'BTCD'



;

SELECT CCD , '15' as TMP , COUNT(*) as CNT , FLOOR(MIN_VOL) AS MIN_VOL , AVG( SCORE ) AVG_SCORE 
, MAX( RISE_VOL_PER ) RISE_VOL_PER , RISE24PER , ( SELECT PRICE FROM TB_CO_SNAP WHERE CCD = 
A.CCD AND UNIT_CID = 1 ORDER BY REG_DTTM DESC LIMIT 1 ) AS CUR_PRICE FROM tb_an_1 A WHERE srch_dttm 
> date_sub(now(), interval 10 MINUTE ) AND RISE24PER < 30 GROUP BY CCD ORDER BY RISE_VOL_PER 
DESC 





