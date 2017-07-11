package nhj.api;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.google.gson.JsonObject;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Utils;

public class WebPushAPI {
	private static String CHROME_GCM_PUBLIC_KEY = "BLjkJUY4Padux-qoYj_qT5FgPM5YW9p19Y8JyWeN0zeAA2ESySpuHcaDImzEJBlxzkcXMa9FOZsKLP0WZ9O6AK4";
	private static String CHROME_GCM_PRIVATE_KEY = "OI1tQOlq0ZOdsjO2XIDZ1h5WrgpN7-vVPYFjQsQGhvg";
	private static String CHROME_BASE_URL = "https://fcm.googleapis.com/fcm/send/";
	private static PushService PUSH_SVC_CHROME = new PushService();
	
	static{
		Security.addProvider(new BouncyCastleProvider());
		PUSH_SVC_CHROME.setSubject("Cobot");
	    try {
			PUSH_SVC_CHROME.setPublicKey(Utils.loadPublicKey(CHROME_GCM_PUBLIC_KEY));
			PUSH_SVC_CHROME.setPrivateKey(Utils.loadPrivateKey(CHROME_GCM_PRIVATE_KEY));
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void init(){
		
	}
	public static void main(String[] args) throws Throwable {
		//testPushChromeVapid();
		
		JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("body", "Coinkorea-ETH 비탈릭 비지니스 라이프에도 출연하네요 by @nhj12311");
        jsonObject.addProperty("url", "https://steemit.com/kr/@kim066/eth");
        jsonObject.addProperty("img", "https://steemitimages.com/256x512/https://steemitimages.com/DQmNUVW4GsG2wTiEq39Zhbjn8wimzfJgz3LgWXnkMD5YkHy/image.png");
        
        long cur = System.currentTimeMillis();
        
        PushChrome(
        		"dC6eBGSesrs:APA91bEG5yF3fix50NZDfWjlmsqvd6wgp_CSuKNxx0anRX0tN-w4d8AOiYT2nHJbrnY6CW-d5dsNXxqU_UtQE7mGzQi-UJaSP_vAGV_ahrGypIboKFCafuaWRnCHjQgnkD__70bQUft9"
        		, "BDfFbittuZS6p5WNVw5VNqVCImOg0v5t-dJyF3114-wH73LVmiL2ioMiy84rC4k3cQFJBVgWcA2htOusVPdrzJg="
        		, "z8GKxkX83iWFLWgIfvautg=="
        		, jsonObject
        );
        
        long cur2 = System.currentTimeMillis();
        
        System.out.println( cur2 - cur + "ms" );
        
        PushChrome(
        		"dC6eBGSesrs:APA91bEG5yF3fix50NZDfWjlmsqvd6wgp_CSuKNxx0anRX0tN-w4d8AOiYT2nHJbrnY6CW-d5dsNXxqU_UtQE7mGzQi-UJaSP_vAGV_ahrGypIboKFCafuaWRnCHjQgnkD__70bQUft9"
        		, "BDfFbittuZS6p5WNVw5VNqVCImOg0v5t-dJyF3114-wH73LVmiL2ioMiy84rC4k3cQFJBVgWcA2htOusVPdrzJg="
        		, "z8GKxkX83iWFLWgIfvautg=="
        		, jsonObject
        );
        
        System.out.println( System.currentTimeMillis() - cur2 + "ms" );
        
	}
	
	public static void PushChrome(String _endpoint, String userPublicKey, String userAuth, JsonObject jsonObject ) throws Exception {
        String endpoint = CHROME_BASE_URL + _endpoint;

        // Base64 string user public key/auth
        //String userPublicKey = "BDfFbittuZS6p5WNVw5VNqVCImOg0v5t-dJyF3114-wH73LVmiL2ioMiy84rC4k3cQFJBVgWcA2htOusVPdrzJg=";
        //String userAuth = "z8GKxkX83iWFLWgIfvautg==";

        // Base64 string server public/private key
        
        byte[] jsonBytes = jsonObject.toString().getBytes("utf-8");
        //System.out.println("jsonBytes : "+jsonBytes);
        // Construct notification
        Notification notification = new Notification(endpoint, userPublicKey, userAuth, jsonBytes);
        // Construct push service
        // Send notification!
        HttpResponse httpResponse = PUSH_SVC_CHROME.send(notification);
        
        //System.out.println(httpResponse.getStatusLine().getStatusCode());
        //System.out.println(IOUtils.toString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8));
        
    }
	
	
	public static void testPushChromeVapid() throws Exception {
        String endpoint = "dC6eBGSesrs:APA91bEG5yF3fix50NZDfWjlmsqvd6wgp_CSuKNxx0anRX0tN-w4d8AOiYT2nHJbrnY6CW-d5dsNXxqU_UtQE7mGzQi-UJaSP_vAGV_ahrGypIboKFCafuaWRnCHjQgnkD__70bQUft9";

        // Base64 string user public key/auth
        String userPublicKey = "BDfFbittuZS6p5WNVw5VNqVCImOg0v5t-dJyF3114-wH73LVmiL2ioMiy84rC4k3cQFJBVgWcA2htOusVPdrzJg=";
        String userAuth = "z8GKxkX83iWFLWgIfvautg==";

        // Base64 string server public/private key
        

        // Construct notification
        Notification notification = new Notification(endpoint, userPublicKey, userAuth, getPayload());
        

        // Construct push service
        PushService pushService = new PushService();
        pushService.setSubject("Cobot");
        pushService.setPublicKey(Utils.loadPublicKey(CHROME_GCM_PUBLIC_KEY));
        pushService.setPrivateKey(Utils.loadPrivateKey(CHROME_GCM_PRIVATE_KEY));
        
        // Send notification!
        HttpResponse httpResponse = pushService.send(notification);

        //System.out.println(httpResponse.getStatusLine().getStatusCode());
        //System.out.println(IOUtils.toString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8));
    }
	
	/**
     * Some dummy payload (a JSON object)
     *
     * @return
     */
    private static byte[] getPayload() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("body", "Coinkorea-ETH 비탈릭 비지니스 라이프에도 출연하네요 by @nhj12311");
        jsonObject.addProperty("url", "https://steemit.com/kr/@kim066/eth");
        jsonObject.addProperty("img", "https://steemitimages.com/256x512/https://steemitimages.com/DQmNUVW4GsG2wTiEq39Zhbjn8wimzfJgz3LgWXnkMD5YkHy/image.png");
        return jsonObject.toString().getBytes();
    }
    
}
