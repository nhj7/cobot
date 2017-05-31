package com.cobot.bat;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Options.FlushCachePolicy;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface BAT_DAO {
	
	
	
	@Select( " "
			+ "SELECT "
					+ "	CCD "
					+ "	, MIN_VOL "
					+ "	, TRUNCATE( SUM( RISE_VOL) / MIN_VOL * 100, 2) AS RISE_VOL_PER "
					+ "	, SUM( RISE_VOL) RISE_VOL "
					+ "	, SUM( RISE_PER ) RISE_PER "
					+ "	 "
					+ "	, SUBSTRING_INDEX (group_concat( RISE_PER order by SECTOR ASC, ',' ), ',', 1) RISE_PER_1 "
					+ "	 "
					+ "	, SUBSTRING_INDEX (group_concat( RISE_VOL order by SECTOR DESC, ',' ), ',', 4) CAT_RISE_VOL "
					+ "	, SUBSTRING_INDEX (group_concat( RISE_PER order by SECTOR DESC, ',' ), ',', 4) CAT_RISE_PER "
					+ "	, SUBSTRING_INDEX (group_concat( MAX_PRICE order by SECTOR DESC, ',' ), ',', 4) CAT_PRICE "
					+ "	, ( "
					+ "		SELECT LOW24HR FROM TB_CO_SNAP "
					+ "		WHERE 1=1 "
					+ "		AND CCD = UNION_TMP.CCD "
					+ "		AND UNIT_CID = 1 "
					+ "		ORDER BY REG_DTTM DESC "
					+ "		LIMIT 1 "
					+ "	) AS LOW24HR "
					+ "	, FLOOR( SUBSTRING_INDEX (group_concat( MAX_PRICE order by SECTOR ASC, ',' ), ',', 1) / ( "
					+ "		SELECT LOW24HR FROM TB_CO_SNAP "
					+ "		WHERE 1=1 "
					+ "		AND CCD = UNION_TMP.CCD "
					+ "		AND UNIT_CID = 1 "
					+ "		ORDER BY REG_DTTM DESC "
					+ "		LIMIT 1 "
					+ "	) * 100  ) - 100 AS RISE24PER "
					+ "FROM ( "
					+ " "
					+ "select  "
					+ "	@n := @n + 1 as NO "
					+ "	, '4' AS SECTOR "
					+ "	, CCD "
					+ "	, FLOOR(RISE_VOL) RISE_VOL "
					+ "	, FLOOR(RISE_PER) RISE_PER "
					+ "	, MIN_VOL "
					+ "	, MAX_VOL  	 "
					+ "	, MIN_PRICE "
					+ "	, MAX_PRICE "
					+ "	, min_dttm "
					+ "	, max_dttm "
					+ "from ( "
					+ "select  "
					+ "	 "
					+ "	ccd	 "
					+ "	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS min_vol "
					+ "	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) AS max_vol "
					+ "	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) - SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS RISE_VOL "
					+ "	, SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) AS min_price "
					+ "	, SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) AS max_price "
					+ "	, (( SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) /  SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) ) - 1 ) * 100  as RISE_PER "
					+ "	, min( reg_dttm ) min_dttm "
					+ "	, max( reg_dttm ) max_dttm "
					+ "	 "
					+ "from tb_co_snap "
					+ "where 1=1 "
					+ "and unit_cid = 1 "
					+ "and srch_dttm BETWEEN date_sub(now(), interval 4 MINUTE ) AND date_sub(now(), interval 3 MINUTE ) "
					+ "group by ccd "
					+ " "
					+ ") tmp  "
					+ " "
					+ "union  "
					+ " "
					+ "select  "
					+ "	@n := @n + 1 as NO "
					+ "	, '3' "
					+ "	, CCD "
					+ "	, FLOOR(RISE_VOL) RISE_VOL "
					+ "	, FLOOR(RISE_PER) RISE_PER "
					+ "	, MIN_VOL "
					+ "	, MAX_VOL  	 "
					+ "	, MIN_PRICE "
					+ "	, MAX_PRICE "
					+ "	, min_dttm "
					+ "	, max_dttm "
					+ "from ( "
					+ "select  "
					+ "	 "
					+ "	ccd	 "
					+ "	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS min_vol "
					+ "	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) AS max_vol "
					+ "	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) - SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS RISE_VOL "
					+ "	, SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) AS min_price "
					+ "	, SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) AS max_price "
					+ "	, (( SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) /  SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) ) - 1 ) * 100  as RISE_PER "
					+ "	, min( reg_dttm ) min_dttm "
					+ "	, max( reg_dttm ) max_dttm "
					+ "from tb_co_snap "
					+ "where 1=1 "
					+ "and unit_cid = 1 "
					+ "and srch_dttm BETWEEN date_sub(now(), interval 3 MINUTE ) AND date_sub(now(), interval 2 MINUTE ) "
					+ "group by ccd "
					+ " "
					+ "	 "
					+ ") tmp  "
					+ " "
					+ "union  "
					+ " "
					+ "select  "
					+ "	@n := @n + 1 as NO "
					+ "	, '2' "
					+ "	, CCD "
					+ "	, FLOOR(RISE_VOL) RISE_VOL "
					+ "	, FLOOR(RISE_PER) RISE_PER "
					+ "	, MIN_VOL "
					+ "	, MAX_VOL  	 "
					+ "	, MIN_PRICE "
					+ "	, MAX_PRICE "
					+ "	, min_dttm "
					+ "	, max_dttm "
					+ "from ( "
					+ "select  "
					+ "	 "
					+ "	ccd	 "
					+ "	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS min_vol "
					+ "	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) AS max_vol "
					+ "	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) - SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS RISE_VOL "
					+ "	, SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) AS min_price "
					+ "	, SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) AS max_price "
					+ "	, (( SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) /  SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) ) - 1 ) * 100  as RISE_PER "
					+ "	, min( reg_dttm ) min_dttm "
					+ "	, max( reg_dttm ) max_dttm "
					+ "from tb_co_snap "
					+ "where 1=1 "
					+ "and unit_cid = 1 "
					+ "and srch_dttm BETWEEN date_sub(now(), interval 2 MINUTE ) AND date_sub(now(), interval 1 MINUTE ) "
					+ "group by ccd "
					+ " "
					+ "	 "
					+ ") tmp  "
					+ " "
					+ "union  "
					+ " "
					+ "select  "
					+ "	@n := @n + 1 as NO "
					+ "	, '1' "
					+ "	, CCD "
					+ "	, FLOOR(RISE_VOL) RISE_VOL "
					+ "	, FLOOR(RISE_PER) RISE_PER "
					+ "	, MIN_VOL "
					+ "	, MAX_VOL  	 "
					+ "	, MIN_PRICE "
					+ "	, MAX_PRICE "
					+ "	, min_dttm "
					+ "	, max_dttm "
					+ "from ( "
					+ "select  "
					+ "	 "
		 		+ "	ccd	 "
					+ "	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS min_vol "
					+ "	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) AS max_vol "
					+ "	, SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm desc, ',' ), ',', 1) - SUBSTRING_INDEX (group_concat( base_vol order by reg_dttm asc, ',' ), ',', 1) AS RISE_VOL "
					+ "	, SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) AS min_price "
					+ "	, SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) AS max_price "
					+ "	, (( SUBSTRING_INDEX (group_concat( price order by reg_dttm desc, ',' ), ',', 1) /  SUBSTRING_INDEX (group_concat( price order by reg_dttm asc, ',' ), ',', 1) ) - 1 ) * 100  as RISE_PER "
					+ "	, min( reg_dttm ) min_dttm "
					+ "	, max( reg_dttm ) max_dttm "
					+ "from tb_co_snap "
					+ "where 1=1 "
					+ "and unit_cid = 1 "
					+ "and srch_dttm BETWEEN date_sub(now(), interval 1 MINUTE ) AND date_sub(now(), interval 0 MINUTE ) "
					+ "group by ccd "
					+ " "
					+ "	 "
					+ ") tmp  "
					+ " "
					+ ") UNION_TMP  "
					+ " "
					+ "WHERE 1=1 "
					+ "GROUP BY CCD "
					+ "HAVING SUM(RISE_VOL) > 20 "
					+ "AND SUM(RISE_PER) > 0 "
					+ "ORDER BY SUM(RISE_VOL) DESC "		
	)
	@Results(
			value = {}
		)
	public List<Map> selectCoSnapAnal();
	
	
	@Select(" SELECT * FROM tb_exch_co ")
	//@MapKey({"EID","CID"})
	@Results(
		value = {}
	)
	public List<Map> selectAll();
	
	@Insert({
		"<script>",
        "INSERT INTO TB_CO_SNAP (CID, EID, CCD, UNIT_CID, PRICE, PER_CH, BASE_VOL, QUOTE_VOL, HIGH24HR, LOW24HR, IS_FROZEN )",
        "VALUES ",
        "<foreach  collection='param' item='m' separator=','>",
        
        "( "
        + "  #{m.cid,jdbcType=INTEGER}"
        + ", #{m.eid,jdbcType=INTEGER}"
        + ", #{m.ccd,jdbcType=VARCHAR}"
        + ", #{m.unit_cid,jdbcType=VARCHAR}"
        
		+ ", #{m.price,jdbcType=VARCHAR}"
		+ ", #{m.per_ch,jdbcType=VARCHAR}"
		+ ", #{m.base_vol,jdbcType=VARCHAR}"
		+ ", #{m.quote_vol,jdbcType=VARCHAR}"
		+ ", #{m.high24hr,jdbcType=VARCHAR}"
		+ ", #{m.low24hr,jdbcType=VARCHAR}"
		+ ", #{m.is_frozen,jdbcType=VARCHAR}"
        + ")"
        ,
        
        "</foreach>",
        "</script>"
	})
	public int insertCoSnapBatch(@Param("param") List<Map> param);
	
	@Insert({
		"<script>",
        "INSERT INTO TB_AN_1 ( CCD, RISE_VOL, RISE_PER, RISE_PER_1, CAT_RISE_VOL, CAT_RISE_PER, CAT_PRICE, LOW24HR, RISE24PER, SCORE , MIN_VOL, RISE_VOL_PER  )",
        "VALUES ",
        "<foreach  collection='param' item='m' separator=','>",
        
        "( "
        + "  #{m.CCD,jdbcType=INTEGER}"
        + ", #{m.RISE_VOL,jdbcType=INTEGER}"
        + ", #{m.RISE_PER,jdbcType=INTEGER}"
        + ", #{m.RISE_PER_1,jdbcType=INTEGER}"
        + ", #{m.CAT_RISE_VOL,jdbcType=VARCHAR}"
        + ", #{m.CAT_RISE_PER,jdbcType=VARCHAR}"
        + ", #{m.CAT_PRICE,jdbcType=VARCHAR}"
        + ", #{m.LOW24HR,jdbcType=INTEGER}"
        + ", #{m.RISE24PER,jdbcType=INTEGER}"
        + ", #{m.SCORE,jdbcType=INTEGER}"
        + ", #{m.MIN_VOL,jdbcType=INTEGER}"
        + ", #{m.RISE_VOL_PER,jdbcType=INTEGER}"
        
        
        + ")"
        ,
        
        "</foreach>",
        "</script>"
	})
	public int insertAn1(@Param("param") List<Map> param);
	
	
	@Select(
					" SELECT  "
					+ " 	CCD "
					+ " 	, #{mm} as TMP "
					+ " 	, COUNT(*) as CNT "
					+ " 	, FLOOR(MIN_VOL) AS MIN_VOL "
					+ " 	, AVG( SCORE ) AVG_SCORE "
					+ " 	, MAX( RISE_VOL_PER ) RISE_VOL_PER "
					+ " 	, RISE24PER "
					+ " 	, ( SELECT PRICE FROM TB_CO_SNAP WHERE CCD = A.CCD AND UNIT_CID = 1 ORDER BY REG_DTTM DESC LIMIT 1 ) AS CUR_PRICE "
					+ " FROM tb_an_1 A "
					+ " WHERE srch_dttm > date_sub(now(), interval 10 MINUTE ) "
					+ " AND RISE24PER < 30 "					
					+ " GROUP BY CCD "
					+ " ORDER BY RISE_VOL_PER DESC "

			)
	//@MapKey({"EID","CID"})
	@Results( 
		value = {}
	)
	public List<Map> selectRcmList(String mm);
	
	
	
	
	@Select({"<script>  "
			 	 + "SELECT  "
				 + "	CCD "
				 + "	, PRICE  "
				 + "	, #{mm} AS TMP  "
				 + "FROM TB_CO_SNAP "
				 + "WHERE srch_dttm > date_sub(now(), interval 1 MINUTE ) "
				 + "AND EID = 1 "
				 + "AND UNIT_CID = 1 "
				 + "AND CCD IN "
				 , "<foreach item='item' index='index' collection='arr_ccd'"
				 , "open='(' separator=',' close=')'>"
				 , "#{item}"
				 , "</foreach>"
				 , "</script>  "	 	
	})
	//@MapKey({"EID","CID"})
	@Results( 
	value = {}
	)
	@Options(flushCache=FlushCachePolicy.TRUE) 
	
	public List<Map> selectCoSnapList(@Param(value="arr_ccd") String[] arr_ccd,@Param(value="mm") String mm );
	
		
}
