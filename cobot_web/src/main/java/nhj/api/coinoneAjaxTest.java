package nhj.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.http.util.TextUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class coinoneAjaxTest {
	private static Pattern functionPattern = Pattern.compile("setTimeout\\(\\s*function\\s*\\(\\)\\s*\\{(.*)f\\.submit",
			Pattern.DOTALL);
	private static Pattern assignPattern = Pattern.compile("a\\.value =(.+?) \\+ .*");
	private static Pattern stripPattern = Pattern.compile("\\s{3,}[a-z](?: = |\\.).+");
	private static Pattern jsPattern = Pattern.compile("[\\n\\\\']");

	private static String transformFunction(String function) throws Exception {
		// We first extract the main javascript function body: function() { ---
		// this part --- }
		Matcher transformer = functionPattern.matcher(function);
		if (!transformer.find()) {
			throw new Exception("Cloudflare evaluation function body could not be extracted.");
		}
		function = transformer.group(1);

		// We then replace the final statement so it returns the correct answer
		// instead of assigning it
		transformer = assignPattern.matcher(function);
		if (!transformer.find()) {
			throw new Exception("Cloudflare function structure changed.");
		}
		function = transformer.replaceFirst("$1;");

		// We then remove unneeded lines that would mess with the execution
		transformer = stripPattern.matcher(function);
		if (!transformer.find()) {
			throw new Exception("Cloudflare variable names changed.");
		}
		function = transformer.replaceAll("");

		// If the function is not already a single line then convert it to that
		// format
		transformer = jsPattern.matcher(function);
		if (transformer.find()) {
			function = transformer.replaceAll("");
		}

		return function;
	}
	
	
	public static Map getCloudFlareParam(String html ) throws Throwable{
		Document dom = Jsoup.parse(html);
		Element form = dom.getElementById("challenge-form");
		String action = form.attr("action");

		String scriptStr = dom.select("head script").first().html();
		
		

		String varStr = transformFunction(scriptStr);

		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine engine = engineManager.getEngineByName("nashorn");
		engine.eval(varStr);
		
		String varName = varStr.substring( varStr.indexOf("f,")+2, varStr.indexOf("=") );
		String propName = varStr.substring( varStr.indexOf("\"")+1 , varStr.indexOf("\"", varStr.indexOf("\"")+1 ) );
		
		
		System.out.println( "varName : " + varName + ", propName : " + propName  );
		System.out.println( "value : " + engine.eval("print(" + varName + "." + propName + ")")  );
		
		//System.out.println( "index str : " + varName + "." + propName +  ", function : " + scriptStr );
		
		String indexStartStr = "document.getElementById('challenge-form');";
		
		String calcStr = scriptStr.substring( scriptStr.indexOf( indexStartStr ) + indexStartStr.length()  , scriptStr.indexOf("a.value") );
		//System.out.println( "calcStr : " + calcStr );
		
		engine.eval(calcStr);
		
		Object resultObject = engine.eval( "parseInt("+varName + "." + propName + ", 10);" );
		System.out.println("resultObject : " + resultObject);
		int resultInt = 0;
		if( resultObject instanceof Double ){
			resultInt = ((Double)resultObject).intValue();
			
		}
		// "https://"
		int answer = resultInt + 13 ;
		
		System.out.println( "answer : " + answer);
		String jschl_vc = form.getElementsByAttributeValue("name", "jschl_vc").attr("value");
		String pass = form.getElementsByAttributeValue("name", "pass").attr("value");
		
		Map param = new HashMap();
		param.put("jschl_vc", jschl_vc);
		param.put("pass", pass);
		param.put("jschl_answer", answer);
		
		return param;
	}
	
	static String scheme = "https://";
	static String baseUrl = "coinone.co.kr";
	
	private static String verify(Map param) throws Throwable {
		
		
		
		String answer = param.get("jschl_answer").toString();
		String jschl_vc = param.get("jschl_vc").toString();
		String pass = param.get("pass").toString();
		System.out.println( "value2 : " +  answer );
		System.out.println("jschl_vc : " + jschl_vc);
		
		String jschl_url = scheme + baseUrl + "/cdn-cgi/l/chk_jschl?";
		System.out.println("jschl_url : " + jschl_url);
		
		
		return "";
	}
	
	
	static final String COOKIES_HEADER = "Set-Cookie";

	static java.net.CookieManager msCookieManager = new java.net.CookieManager();

	public static Map getResponseTxt(String utl, String cf_clearance ) throws Throwable{
		
		Map rtnMap = new HashMap();
		HttpURLConnection huc = (HttpURLConnection) new URL(utl).openConnection();
		huc.setInstanceFollowRedirects(true);  //you still need to handle redirect manully.
		huc.setFollowRedirects(true);

		//huc.setDoInput(true);

		huc.setRequestMethod("GET");
		huc.addRequestProperty("User-Agent",
				"Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Mobile Safari/537.36");
		huc.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		huc.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
		if (msCookieManager.getCookieStore().getCookies().size() > 0) {
			
			List list = msCookieManager.getCookieStore().getCookies();
			String cookieStr = "";
			for(int i = 0; i < list.size();i++){
				cookieStr += list.get(i).toString() + ";";
			}
			
			log("cookieStr : " + cookieStr);
			
		    // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
			huc.setRequestProperty("Cookie", cookieStr );    
		}

		huc.connect();

		int status = huc.getResponseCode();
		rtnMap.put("status", status);
		
		log("[status] : "+status);
		
		
		Map<String, List<String>> headerFields = huc.getHeaderFields();
		List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
		if (cookiesHeader != null) {
		    for (String cookie : cookiesHeader) {
		        msCookieManager.getCookieStore().add(null,HttpCookie.parse(cookie).get(0));
		    }               
		}

		
		
		InputStream in = null;
		if (status != 200) {
			in = huc.getErrorStream();
		} else {
			in = huc.getInputStream();
		}

		log(utl + " : " + utl);

		if ("gzip".equals(huc.getContentEncoding())) {
			in = new GZIPInputStream(in);
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		if (status != 200) {
			log("error status : " + status + " out : " + sb);
		}
		rtnMap.put("txt", sb.toString());
		//log(sb);
		return rtnMap;
	}

	public static void main(String[] args) throws Throwable {
		
		String apiUrl = "https://coinone.co.kr/chart/olhc/?site=coinone" + "&type=15m";
		String cf_clearance = "";
		Map rtnMap = getResponseTxt(apiUrl, cf_clearance);
		if( 503 ==  (int) rtnMap.get("status")){
			log("Status 503 Thread.sleep(2000)!! ");
			Thread.sleep(2000);
			String txt = rtnMap.get("txt").toString();
			Map param = getCloudFlareParam(txt);
			log("param : " + param);
			
			String jschl_answer = param.get("jschl_answer").toString();
			String jschl_vc = param.get("jschl_vc").toString();
			String pass = param.get("pass").toString();
			
			
			
			System.out.println( "value2 : " +  jschl_answer );
			System.out.println("jschl_vc : " + jschl_vc);
			
			String jschl_url = scheme + baseUrl + "/cdn-cgi/l/chk_jschl?jschl_vc="+jschl_vc+"&pass="+pass+"&jschl_answer="+jschl_answer;
			
			rtnMap = getResponseTxt(jschl_url, cf_clearance);
		}
		
		// huc.setRequestProperty("Host", "coinone.co.kr");
		//huc.setRequestProperty("Connection", "keep-alive");
		//huc.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		//huc.setRequestProperty("X-NewRelic-ID", "UwIOUVNTGwEFUVZQAgMP");
		
		
		// huc.setRequestProperty("Referer",
		// "https://coinone.co.kr/chart/?site=Coinone&unit_time=5m");
		
		//huc.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
		
		
		
		// __cfduid=d9674fe4f46200ca4a88456b846508f441495030926; __zlcmid=gZgS5KCkJK8J9N; AWSELB=C7AD393914EE358373525A5147F2BF8A676F7B4C5DE2CCF5E0DEA8ECE378FC19584CD1725EAA39D5EF5B395D30DA841792E4155CCE5D643AD15341743DFC1D4DE06989A79C; csrftoken=AqHgqz5JaQdfeDn76v6TMyBgj6Nr8odn; sessionid=gfh1hgfhrtm0yqomxlcd8wrafj1ucl91; _ga=GA1.3.1251298305.1495030941; _gid=GA1.3.954285082.1498743920; banner_lst_nt=[%EA%B3%B5%EC%A7%80]%20%EC%8B%A0%EA%B7%9C%ED%9A%8C%EC%9B%90%20%EB%8C%80%EC%83%81%20KRW%20%EC%9E%85%EA%B8%88%EC%9A%A9%20%EA%B0%80%EC%83%81%EA%B3%84%EC%A2%8C%20%EC%8B%A0%EA%B7%9C%EB%B0%9C%EA%B8%89%20%EC%9D%BC%EC%8B%9C%20%EC%A4%91%EB%8B%A8%20%EC%95%88%EB%82%B4%C2%A0; cf_clearance=7b2724e5a4353f501cc4e7ac20282ab99c9772db-1498751322-14400
		

		
		
        
	}

	private static void log(Object string) {
		System.out.println(string);
		
	}
}
