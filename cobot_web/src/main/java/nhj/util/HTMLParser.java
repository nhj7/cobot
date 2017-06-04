package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLParser {       
	final public static String GET = "Set-Cookie";
	
	public static boolean isHTML = false;
	public static String BR_STR = "<br />";
	 
	
	public static String printHTML(String url ) throws Throwable, Throwable{
		
		
		HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
		huc.setRequestMethod("GET");
		huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		
		huc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
		huc.connect();

		
		BufferedReader br = new BufferedReader( new InputStreamReader(huc.getInputStream(), "UTF-8"));
		
//		Map m = huc.getHeaderFields();
//		
//		
//		String cookie = "";
//        if(m.containsKey(GET)) {
//                Collection c =(Collection)m.get(GET);
//                for(Iterator i = c.iterator(); i.hasNext(); ) {
//                        cookie = (String)i.next();
//                }
//                print("server response cookie:" + cookie);
//        }
		String line = null;
		
		StringBuilder sb = new StringBuilder();
		
		while( (line = br.readLine()) != null ){
			
			//print(line);
			sb.append(line);
		}
		
		br.close();
		
		return sb.toString();
		
	}
	
	static String[][] codeStr = new String[][]{
		{"sgm","싱글남"}
		,{"sgf","싱글녀"}
		,{"uni","대학생"}
		,{"tee","청소년"}
		,{"hwf","주부"}
		,{"mwk","직장인"}
		,{"inv","재테크족"}
	};
	
	public static String codeMap( String code ){
		
		
		for(int i = 0; i < codeStr.length;i++){
			
			if( codeStr[i][0].equals(code) ){
				
				return codeStr[i][1]; 
			}
			
		}
		return code;
	}
	
	
	public static void main(String[] args) throws Throwable {
		 
		String html = printHTML("http://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ie=utf8&query=");
		
		if( "".equals(html)){
			html = printHTML("https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&ie=utf8&query=");
		}
		
		//System.out.println("html : "+html);
		
		Document naverSearchDoc = Jsoup.parse(html);
		Elements titles = naverSearchDoc.select("div._list.realtime_srch").select("ol.lst_realtime_srch").select("li");
 
		//print all titles in main page
		
		print("■ 실시간 인기검색어 : ");
		
		for(Element e: titles){
			//print("text: " +e.text());
			//print("html: "+ e.html());
			
			Elements num = e.select("em.num");
			Elements searchTxt = e.select("span.tit");
			Elements newTxt = e.select("em.rank.new");
			print(num.text() + " : " + searchTxt.text() + " " + newTxt.text());
			
		}	
 
		Elements scripts = naverSearchDoc.select("script[type=text/javascript]");
		
		for(Element e: scripts){
			//print("text: " +e.text());
			for (DataNode node : e.dataNodes()) {	            
				
	            if( node.getWholeData().indexOf("$Element(\"nxfr_ugrank\")") > 0 ){	            	
	            	//print(node.getWholeData());
	            	String scriptStr = node.getWholeData();	            	
	            	String kwds = scriptStr.substring(scriptStr.indexOf("var aKwds") + 11 , scriptStr.indexOf("var aCds")-2);  
	            	//print("" + kwds);
	            	
	            	JSONParser jp = new JSONParser();
	            	JSONArray ja = (JSONArray)jp.parse(kwds);
	            	
	            	for(int i = 0 ; i < ja.size();i++){
	            		
	            		//print("value : " + ja.get(i));
	            		
	            		JSONObject jo = (JSONObject)ja.get(i);
	            		print("");
	            		//print("jo : " + jo);
	            		print("■ 그룹별 인기검색어 : " + codeMap(jo.get("code").toString()));
	            		
	            		
	            		JSONArray keywords = (JSONArray)jo.get("keywords");
	            		JSONArray percent = (JSONArray)jo.get("percent");
	            		
	            		for(int j = 0; j < keywords.size();j++){
	            			
	            			print((j +1) +". " + keywords.get(j) + " " + percent.get(j) + "" );
	            			
	            			
	            		}
	            	}
	            	
	            	
	            	
	            }
	        }   
		}
		
		
		//print all available links on page
//		Elements links = doc.select("a[href]");
//		for(Element l: links){
//			print("link: " +l.attr("abs:href"));
//		}
//		
		
		
		// http://n.search.naver.com/jsonp/hottopic.js?_callback=window.__jindo2_callback._$3361_0&hour=8&hl=
		print("");
		
		printNaverHotTopic("8");
		
		print("");
		
		printNaverHotTopic("11");
		
		print("");
		
		printNaverHotTopic("14");
		
		print("");
		
		printNaverHotTopic("17");
		
		print("");
		
		printNaverHotTopic("20");
		
		print("");
		
		printNaverHotTopic("23");
		
		print("");
	}
	
	private static Writer out;
	
	
	public static void setPrintWriter( Writer ps ){
		
		
		out = ps;
	}
	
	private static void print( String str ){
		
		if( out != null ){
			try {
				out.write(str + ( isHTML ? BR_STR : "" ));
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(str);
		}else{
			
			System.out.println(str);
			
		}
		
	}
	
	private static void printNaverHotTopic(String hour) throws Throwable{
		
		String url = NAVER_HOT_TOPIC_URL.replaceAll(":hour", hour);
		//print("url : " + url );
		String hotTopic = printHTML(url);
		
		
		if( hotTopic.length() < 100 ) return;
		
		//print("hotTopic : "+ hotTopic + " " + hotTopic.length());
		
		hotTopic = hotTopic.substring(hotTopic.indexOf("list_innerHTML") + 18 , hotTopic.lastIndexOf("</div>")+6).replaceAll("\\\\", "");
		
		//print("hotTopic : "+ hotTopic);
		
		Document hotTopicDoc = Jsoup.parse(hotTopic);
		
		Elements hotTopicLi = hotTopicDoc.select("li");
		
		
		//print(hotTopicLi.toString());
		
		print("■ 시간대 별 인기검색어 "+hour+"시 : ");
		for(Element e: hotTopicLi){
			//print("text: " +e.text());
			Elements num = e.select("em.num");
			Elements searchTxt = e.select("span.tit");
			Elements newTxt = e.select("em.rank.new");
			print(num.text() + " : " + searchTxt.text() + " " + newTxt.text());
			
			//e.getElementsByAttribute("");
		
		}
	}
	static String NAVER_HOT_TOPIC_URL = "http://n.search.naver.com/jsonp/hottopic.js?_callback=window.__jindo2_callback._$3361_0&hour=:hour&hl=";
	static String[] NAVER_HOT_TOPIC_HOURS = new String[]{
		"8","11","14","17","20","23"	
	};
}
