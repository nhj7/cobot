package nhj.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;

public class coinoneWebdriver {

	public static void log(Object obj) {
		System.out.println(obj);
	}

	public static void main(String[] args) throws Throwable {

		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		WebConnectionWrapper wc = new WebConnectionWrapper(webClient) {
            public WebResponse getResponse(WebRequest request) throws IOException {
                WebResponse response = super.getResponse(request);
                /*
                if (request.getUrl().toExternalForm().startsWith("https://coinone.co.kr/chart/olhc/")) {
                    String content = response.getContentAsString();

                    //change content

                    WebResponseData data = new WebResponseData(content.getBytes(),
                            response.getStatusCode(), response.getStatusMessage(), response.getResponseHeaders());
                    response = new WebResponse(data, request, response.getLoadTime());
                }
                */
                
                return response;
            }
        };
            

		org.apache.log4j.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(org.apache.log4j.Level.FATAL);
		
		System.out.println("Wait ByPass CloudFlare DDOS Check... About 10 second");		
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		String url = "https://doc.coinone.co.kr/";
		HtmlPage htmlPage = webClient.getPage(url);
		webClient.waitForBackgroundJavaScript(10_000);
		CookieManager cm = webClient.getCookieManager();
		
		//System.out.println("cookies1 : " + cm.getCookies());
		//System.out.println(htmlPage.asText());
		// Thread.sleep(10000);
		
		htmlPage = webClient.getPage(url);
		webClient.waitForBackgroundJavaScript(10_000);
		//System.out.println(htmlPage.asText());

		cm = webClient.getCookieManager();

		Set cookies = cm.getCookies();

		//System.out.println("cookies2 : " + cm.getCookies());

		CookieStore cs = new BasicCookieStore();

		Map header = new HashMap();
		com.gargoylesoftware.htmlunit.util.Cookie cookie = null;
		for (java.util.Iterator it = cookies.iterator(); it.hasNext();) {
			cookie = (com.gargoylesoftware.htmlunit.util.Cookie) it.next();
			System.out.println(cookie);
			// cf_clearance

			System.out.println("getName() : " + cookie.getName() + ", " + cookie.getValue());
			header.put(cookie.getName(), cookie.getValue());
			org.apache.http.impl.cookie.BasicClientCookie aCookie = new org.apache.http.impl.cookie.BasicClientCookie(
					cookie.getName(), cookie.getValue());
			aCookie.setDomain(".coinone.co.kr");

			cs.addCookie(aCookie);

		}
		String apiUrl = "https://coinone.co.kr/chart/olhc/?site=coinone" + "&type=15m";

		WebRequest rq = new WebRequest( new URL(apiUrl) );
		rq.setAdditionalHeader("accept", "application/json, text/javascript, */*; q=0.01");
		rq.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		
		
		WebResponse rs = wc.getResponse(rq);
		//webClient.waitForBackgroundJavaScript(10_000);
		System.out.println(rs.getContentAsString());
		
		log("wait 5 sec");
		Thread.sleep(5000);
		
		rs = wc.getResponse(rq);
		//webClient.waitForBackgroundJavaScript(10_000);
		System.out.println(rs.getContentAsString());
		
		
		
		
		//coinoneTest.getAjax(apiUrl, new HashMap(), new HashMap(), cs);

		if (true)
			return;

		// cf_clearance=027804d190fef4ccdb46e7545c722ca57feb12a1-1498945791-14400

		HttpURLConnection huc = (HttpURLConnection) new URL(apiUrl).openConnection();
		huc.setDoInput(true);

		huc.setRequestMethod("GET");
		// huc.setRequestProperty("Host", "coinone.co.kr");
		huc.setRequestProperty("Connection", "keep-alive");
		huc.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		huc.setRequestProperty("X-NewRelic-ID", "UwIOUVNTGwEFUVZQAgMP");
		huc.addRequestProperty("User-Agent",
				"Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Mobile Safari/537.36");

		huc.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		// huc.setRequestProperty("Referer",
		// "https://coinone.co.kr/chart/?site=Coinone&unit_time=5m");
		huc.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
		huc.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");

		// __cfduid=d9674fe4f46200ca4a88456b846508f441495030926;
		// __zlcmid=gZgS5KCkJK8J9N;
		// AWSELB=C7AD393914EE358373525A5147F2BF8A676F7B4C5DE2CCF5E0DEA8ECE378FC19584CD1725EAA39D5EF5B395D30DA841792E4155CCE5D643AD15341743DFC1D4DE06989A79C;
		// csrftoken=AqHgqz5JaQdfeDn76v6TMyBgj6Nr8odn;
		// sessionid=gfh1hgfhrtm0yqomxlcd8wrafj1ucl91;
		// _ga=GA1.3.1251298305.1495030941; _gid=GA1.3.954285082.1498743920;
		// banner_lst_nt=[%EA%B3%B5%EC%A7%80]%20%EC%8B%A0%EA%B7%9C%ED%9A%8C%EC%9B%90%20%EB%8C%80%EC%83%81%20KRW%20%EC%9E%85%EA%B8%88%EC%9A%A9%20%EA%B0%80%EC%83%81%EA%B3%84%EC%A2%8C%20%EC%8B%A0%EA%B7%9C%EB%B0%9C%EA%B8%89%20%EC%9D%BC%EC%8B%9C%20%EC%A4%91%EB%8B%A8%20%EC%95%88%EB%82%B4%C2%A0;
		// cf_clearance=7b2724e5a4353f501cc4e7ac20282ab99c9772db-1498751322-14400
		// cf_clearance

		huc.connect();

		int status = huc.getResponseCode();

		InputStream in = null;
		if (status != 200) {
			in = huc.getErrorStream();
		} else {
			in = huc.getInputStream();
		}

		log(huc + " : " + huc.getContentEncoding());

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

		// HttpClient client =
		// HttpClientBuilder.create().setDefaultCookieStore(cs).build();

		// https://coinone.co.kr/chart/olhc/?site=coinone" + currencyPair +
		// "&type=15m

		// System.out.println("cm : " + cm);

		// webClient.

		// webClient.

	}
}
