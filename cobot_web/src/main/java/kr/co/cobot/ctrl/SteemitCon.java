package kr.co.cobot.ctrl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import kr.co.cobot.bot.DATA;
import kr.co.cobot.conf.HibernateCfg;
import kr.co.cobot.entity.TbPostMarketInfo;
import kr.co.cobot.entity.TbPostMarketReply;
import nhj.util.DateUtil;
import nhj.util.Paging;
import nhj.util.PrintUtil;

/**
 * Handles requests for the application home page.
 */

@Controller
public class SteemitCon {

	private static final Logger logger = LoggerFactory.getLogger(SteemitCon.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = { "/ff33" } )
	public @ResponseBody Object exam(Model model, @RequestParam Map ioMap, ServletRequest req) {

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

		String formattedDate = dateFormat.format(date);		

		model.addAttribute("serverTime", formattedDate);
		model.addAttribute("ioMap", ioMap);
		
		System.out.println("ioMap : "+ioMap);
		
		PrintUtil.printMap(ioMap);
		 
		Transaction tx = null;
		try {
			
			/*
			Session session = HibernateCfg.getCurrentSession();
			// 트랜잭션 시작
			tx = session.getTransaction();
			
			TbExchange te = new TbExchange();
			
			te.setEid(1);
			te.setEcd("PLNX");
			te.setEnm("Poloniex");
			te.setEnmKo("폴로닉스..");
			//te.setRegDttm(date);
			//te.setModDttm(date);
			
			session.beginTransaction();
			session.merge(te);
			//tx.
			
			tx.commit(); // 커밋
			
			*/
			
			
			// DBIO_Cubrid_Test.main(null);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			tx.rollback();
			e.printStackTrace();
		}finally{
			HibernateCfg.closeSession();
		}
		
		
		return (ioMap);
	}
	
	@RequestMapping(value = { "/steemit/steem-mart", "/steemit/steem-mart/*" })
	public String krmarket(Model model, @RequestParam Map ioMap, HttpServletRequest req) {

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

		String formattedDate = dateFormat.format(date);
		
		String ua= req.getHeader("User-Agent").toLowerCase();
		
		String url = "steem-mart";
		
		if (ua.matches(".*(android|avantgo|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")||ua.substring(0,4).matches("1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|e\\-|e\\/|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\\-|2|g)|yas\\-|your|zeto|zte\\-")){
			url = "steem-mart-m";
		}

		return url;
	}
	
	private static Object[] getArrayConvert(Object[] arrayObj, Class targetClass ){
		
		Object[] rtnArray = new Object[arrayObj.length];
		for(int i = 0; i < arrayObj.length;i++){
			if( targetClass == Short.class){
				rtnArray[i] = Short.parseShort( arrayObj[i].toString() );
			}
		}
		return rtnArray;
	}
	Gson gson = new Gson();
	/*
	 *  Search Market Data
	 */
	@RequestMapping(value = { "/kr-market/Search" } , produces="application/json;charset=UTF-8")
	public @ResponseBody Object krmarketSearch(Model model, @RequestBody Map ioMap , ServletRequest req) {
		
		System.out.println("[SteemitCon.krmarketSearch] : "+ioMap);
		//String alarmID = ioMap.get("alarmID").toString();		
		try {
			Session session = HibernateCfg.getCurrentSession();			
			
			
			
			// select query make.
			StringBuilder cntSb = new StringBuilder("select count(*) from TbPostMarketInfo market where 1=1 ");
			StringBuilder selSb = new StringBuilder();			
			selSb.append(" from TbPostMarketInfo market ");
			selSb.append(" join TbPostInfo post on market.postId = post.postId ");
			selSb.append(" where 1=1 ");
			
			String arrSeller = ioMap.get("arrSeller").toString();
			String arrMethod = ioMap.get("arrMethod").toString();
			String arrCategory = ioMap.get("arrCategory").toString();
			String arrStatus = ioMap.get("arrStatus").toString();
			String arrActionDate = ioMap.get("arrActionDate").toString();
			String sortDvcd = ioMap.get("sortDvcd").toString();
			
			
			//where query make.
			{
				
				StringBuilder where = new StringBuilder();
				if( !"".equals(arrSeller) ){
					where.append(" and market.author in (:arrSeller) ");
				}
				
				if( !"".equals(arrMethod) ){
					where.append(" and market.method in (:arrMethod) ");
				}
				
				if( !"".equals(arrCategory) ){
					where.append(" and market.category in (:arrCategory) ");
				}
				
				
				if( !"".equals(arrStatus) ){
					where.append(" and market.status in (:arrStatus) ");
				}
				
				
				cntSb.append( where );
				selSb.append( where );
			}
			
			
			// order by
			{
				if( sortDvcd == null || "".equals(sortDvcd) || "descAuctionDate".equals(sortDvcd) ){
					selSb.append(" ORDER BY AUCTION_END_DTTM ASC ");
				}else if( "descRegDate".equals(sortDvcd) ){
					selSb.append(" ORDER BY POST_REG_DTTM DESC ");
				}else if( "ascRealAmt".equals(sortDvcd) ){
					selSb.append(" ORDER BY REAL_AMT ASC ");
				}else if( "descRealAmt".equals(sortDvcd) ){
					selSb.append(" ORDER BY REAL_AMT DESC ");
				}else if( "descReply".equals(sortDvcd) ){
					selSb.append(" ORDER BY REPLY_CNT DESC ");
				}
				
			}
			
			// param set!
			Query countQuery = session.createQuery(cntSb.toString());
			Query selectQuery = session.createQuery(selSb.toString());
			
			{
				// param arrStatus
				if( !"".equals(arrSeller) ){
					List list = Arrays.asList( arrSeller.split(","));
					countQuery.setParameter("arrSeller",  list);
					selectQuery.setParameter("arrSeller",  list);
				}
				// param arrStatus
				if( !"".equals(arrStatus) ){
					List list = Arrays.asList( getArrayConvert(arrStatus.split(","), Short.class));
					countQuery.setParameter("arrStatus",  list);
					selectQuery.setParameter("arrStatus",  list);
				}
				// arrMethod
				if( !"".equals(arrMethod) ){
					List list = Arrays.asList( getArrayConvert(arrMethod.split(","), Short.class));
					countQuery.setParameter("arrMethod",  list);
					selectQuery.setParameter("arrMethod",  list);
				}
				// arrCategory
				if( !"".equals(arrCategory) ){
					List list = Arrays.asList( getArrayConvert(arrCategory.split(","), Short.class));
					countQuery.setParameter("arrCategory",  list);
					selectQuery.setParameter("arrCategory",  list);
				}
			}
			
			int curPage = 0;
			int pageSize = 10;
			int postSize = 10;
			try{
				curPage = Integer.parseInt(ioMap.get("curPage").toString());
				pageSize = Integer.parseInt(ioMap.get("pageSize").toString());
				postSize = Integer.parseInt(ioMap.get("postSize").toString());
			}catch(Throwable e){
				
			}
			
			
			selectQuery.setFirstResult( (curPage-1) * pageSize );
			selectQuery.setMaxResults( pageSize );
			
			int count = Math.toIntExact((Long)countQuery.getSingleResult());
			List markets = selectQuery.getResultList();
			
			JsonArray ja = gson.toJsonTree(markets).getAsJsonArray();
			
			JsonObject main = new JsonObject();
			// int w_size, int p_size, int writing_Count, int cur_Page
			Paging pg = new Paging( postSize , pageSize, count, curPage);
			main.add("pg", gson.fromJson(gson.toJson(pg), JsonObject.class));			
			
			if( !"".equals(DATA.USD_KRW) 
					&& !"".equals(DATA.PLNX_USD_BITCOIN) 
					&& !"".equals(DATA.PLNX_BTC_SBD) 
			)
			{
				//System.out.println("DATA.PLNX_USD_BITCOIN : "+DATA.PLNX_USD_BITCOIN);
				//System.out.println("DATA.PLNX_BTC_SBD : "+DATA.PLNX_BTC_SBD);
				//System.out.println("DATA.USD_KRW : "+DATA.USD_KRW);
				
				BigDecimal USD_BITCOIN = new BigDecimal(DATA.PLNX_USD_BITCOIN);
				BigDecimal BTC_SBD = new BigDecimal(DATA.PLNX_BTC_SBD);
				BigDecimal USD_KRW = new BigDecimal(DATA.USD_KRW);
				
				//System.out.println("divide : "+USD_BITCOIN.multiply(BTC_SBD).setScale(2, BigDecimal.ROUND_DOWN));
				
				
				BigDecimal KRW_SBD = USD_BITCOIN.multiply(BTC_SBD).setScale(2, BigDecimal.ROUND_DOWN).multiply(USD_KRW).setScale(0, BigDecimal.ROUND_DOWN);
				
				//System.out.println("KRW_SBD : "+KRW_SBD);
				main.add("KRW_SBD", new JsonPrimitive(KRW_SBD));
			}
			
			JsonArray itemList = new JsonArray();
			
			main.add("itemList", itemList);
			for( int i = 0; i < ja.size();i++ ){
				JsonObject market = ja.get(i).getAsJsonArray().get(0).getAsJsonObject();
				JsonObject post = ja.get(i).getAsJsonArray().get(1).getAsJsonObject();
				
				TbPostMarketInfo marketInfo = (TbPostMarketInfo)((Object[])markets.get(i))[0];
				
				int postId = Integer.parseInt(market.get("postId").toString());
				
				market.add("strAuctionEndDttm"
						, new JsonPrimitive(DateUtil.getDateToString(marketInfo.getAuctionEndDttm() , "yyyy-MM-dd HH:mm:ss")));
				
				List<TbPostMarketReply> replys = session.createQuery("from TbPostMarketReply where postId = :postId order by auction_amt desc ")
					.setParameter("postId", postId).list();
				
				JsonArray replysJson = gson.toJsonTree(replys).getAsJsonArray();				
								
				market.add("reply", replysJson);
				market.add("postImgUrl", post.get("postImgUrl"));
				market.add("postUrl", post.get("postUrl"));
				itemList.add(market);
			}
			
			//System.out.println("ja : "+ja.toString());			
			// 트랜잭션 시작
			//TbWebPushM webPushM = session.get(TbWebPushM.class, alarmID);
			return main.toString();
		} catch (Throwable e) {			
			e.printStackTrace();
		}finally{
			HibernateCfg.closeSession();
		}		
		return "";
	}
}

