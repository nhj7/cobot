package nhj.api;

import java.io.BufferedInputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import nhj.util.DateUtil;

public class HTMLParsingAPI implements Runnable {
	
	public static String reqToStringForBithumb( String url ) throws Throwable, Throwable {

		HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
		huc.setRequestMethod("GET");

		huc.addRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Whale/0.7.33.5 Safari/537.36");

		huc.addRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
		// huc.addRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");
		huc.addRequestProperty("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
		huc.addRequestProperty("Connection", "keep-alive");
		huc.addRequestProperty("X-Requested-With", "XMLHttpRequest");

		huc.connect();

		BufferedInputStream bis = new BufferedInputStream(huc.getInputStream());
		ByteArrayBuffer baf = new ByteArrayBuffer(50);
		int read = 0;
		int bufSize = 512;
		byte[] buffer = new byte[bufSize];
		while (true) {
			read = bis.read(buffer);
			if (read == -1) {
				break;
			}
			baf.append(buffer, 0, read);
		}
		String queryResult = new String(baf.toByteArray());
		// System.out.println("queryResult : " + queryResult);

		return queryResult.toString();

	}
	
	public static int TOTAL_PAGE = 0;
	public static BigDecimal TOTAL_ETHER = BigDecimal.ZERO;
	public static String LAST_DTTM = "";
	public static long LAP_TM = 0;
	
	public static void main(String[] args) throws Throwable {
		sumEtherScanTran();
		
		System.out.println("[LAST] Total Page : " + TOTAL_PAGE + ", Total Ether : " + TOTAL_ETHER+ ", tm : " + LAP_TM  + "ms, 작업일시 : " + LAST_DTTM  );
		
	}
	
	public static void sumEtherScanTran() throws Throwable {
		
		try {
			int curPage = 1;
			int totalCnt = 1;
			BigDecimal totalEther = BigDecimal.ZERO;
			long cur = System.currentTimeMillis();
			while( curPage <= totalCnt ){
				
				long loop_cur = System.currentTimeMillis();
				
				String url = "https://etherscan.io/txs?a=0xBbc79794599b19274850492394004087cBf89710&p=" + curPage;
				String html = "";
				boolean loopFlag = true;
				while(loopFlag){
					try {
						html = reqToStringForBithumb(url);
						loopFlag = false;
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("다시 시도!!");
					}
				}
				
				
				Document dom = Jsoup.parse(html);
				Element pre_element = 
						 dom.getElementById("ContentPlaceHolder1_HyperLinkPrev");
				
				Element span_element = pre_element.nextElementSibling();
				
				//int totalCnt = 
				Elements bTag = span_element.getElementsByTag("b");
				
				totalCnt = Integer.parseInt(((Element)bTag.get(1)).text());
				
				Elements tableElements = dom.getElementsByClass("table-responsive").get(0).getElementsByTag("table");
				Element tableElement = (Element)tableElements.get(0);
				Elements trElements = tableElement.getElementsByTag("tr");
				
				
				for(int i = 1 ; i < trElements.size();i++){
					String text = trElements.get(i).getElementsByTag("td").get(6).text();
					if( text.indexOf("wei") > -1 ){
						continue;
					}
					String ether = text.replaceAll(" ", "").replaceAll("Ether", "").replaceAll(",", "");
					//System.out.println(ether);
					try {
						totalEther = totalEther.add(new BigDecimal(Double.parseDouble(ether))).setScale(8, BigDecimal.ROUND_DOWN);
					} catch (Exception e) {
						
						System.out.println("text : " + text );
						throw e;
						
					}
				}
				
				//System.out.println("page : " + curPage + ", Total Ether : " + totalEther + ", tm : " + ( System.currentTimeMillis() - loop_cur ) + "ms" );
				curPage++;
			}
			long lap_tm = ( System.currentTimeMillis() - cur );
			System.out.println("[HTMLParsingAPI.sumEtherScanTran] Total Page : " + curPage + ", Total Ether : " + totalEther+ ", tm : " + lap_tm  + "ms"  );
			
			TOTAL_PAGE = curPage;
			TOTAL_ETHER = totalEther;
			LAP_TM = lap_tm;
			LAST_DTTM = DateUtil.getCurDate("YYYY-MM-DD kk:mm:ss");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true){
			try {
				sumEtherScanTran();
				Thread.sleep(60000);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		
		System.out.println("[HTMLParsingAPI.finalize()] . Call !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}

	public static void init() {
		new Thread(new HTMLParsingAPI()).start();
	}
}
