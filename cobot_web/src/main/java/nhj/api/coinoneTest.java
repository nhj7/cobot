package nhj.api;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class coinoneTest {
	private final static Pattern OPERATION_PATTERN = Pattern.compile("setTimeout\\(function\\(\\)\\{\\s+(var s,t,o,p,b,r,e,a,k,i,n,g,f,.+?\\r?\\n[\\s\\S]+?a\\.value =.+?)\\r?\\n");
    private final static Pattern PASS_PATTERN = Pattern.compile("name=\"pass\" value=\"(.+?)\"");
    private final static Pattern CHALLENGE_PATTERN = Pattern.compile("name=\"jschl_vc\" value=\"(\\w+)\"");

    
    public static void main(String[] args) throws Throwable {
		String url = "";
		HttpResponse res = getPage("https://coinone.co.kr/exchange/", null, null, null);
		
		String HTML = EntityUtils.toString(res.getEntity());
		System.out.println(HTML);
	}
    
    
    public static HttpResponse getPage(String url, Map headers, Map param, CookieStore cs) throws IOException{
    	CloseableHttpClient httpclient = HttpClients.createDefault();
    	
    	HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cs).build();
    	
    	HttpContext localContext = new BasicHttpContext();
    	localContext.setAttribute(HttpClientContext.COOKIE_STORE, cs);
    	HttpGet httpget;
    	if( param != null ){
    		List<NameValuePair> params = new LinkedList<NameValuePair>();
        	for(Iterator it = param.keySet().iterator();it.hasNext();){
        		
        		Object key = it.next();
        		
        		params.add(new BasicNameValuePair( key.toString() , String.valueOf(param.get(key.toString()))));    		
        	}
        	String paramString = URLEncodedUtils.format(params, "utf-8");
        	httpget = new HttpGet( url + paramString);
    	}else{
    		httpget = new HttpGet( url);
    	}
        
    	if( headers != null)
        for(Iterator it = headers.keySet().iterator();it.hasNext();){
    		Object obj = it.next();
    		if( obj == null ) continue;
    		String key = obj.toString();
    		httpget.addHeader(key, headers.get(key).toString());
    	}
        
    	System.out.println("Executing request " + httpget.getRequestLine());

    	
    	HttpResponse hrs = client.execute(httpget, localContext);
        
    	System.out.println("----------------------------------------");
    	System.out.println(hrs.toString());
    	
        return hrs;
        
        
        
    	
    }
    
    public static HttpResponse getAjax(String url, Map headers, Map param, CookieStore cs) throws IOException{
    	CloseableHttpClient httpclient = HttpClients.createDefault();
    	
    	HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cs).build();
    	
    	HttpContext localContext = new BasicHttpContext();
    	localContext.setAttribute(HttpClientContext.COOKIE_STORE, cs);

    	
    	List<NameValuePair> params = new LinkedList<NameValuePair>();
    	for(Iterator it = param.keySet().iterator();it.hasNext();){
    		
    		Object key = it.next();
    		
    		params.add(new BasicNameValuePair( key.toString() , String.valueOf(param.get(key.toString()))));    		
    	}
    	

        String paramString = URLEncodedUtils.format(params, "utf-8");
        
        HttpGet httpget = new HttpGet( url + paramString);
        
        for(Iterator it = headers.keySet().iterator();it.hasNext();){
    		Object obj = it.next();
    		if( obj == null ) continue;
    		String key = obj.toString();
    		httpget.addHeader(key, headers.get(key).toString());
    	}
        
        httpget.addHeader("accept", "application/json");
        
    	System.out.println("Executing request " + httpget.getRequestLine());

    	
    	
    	HttpResponse hrs = client.execute(httpget, localContext);
        
    	System.out.println("----------------------------------------");
    	HttpEntity he = hrs.getEntity();
    	System.out.println(he.getContentLength());
    	
        return hrs;
        
        
        
    	
    }
    
    //abstract public CookieStore getCookieStore();

    
    
}
