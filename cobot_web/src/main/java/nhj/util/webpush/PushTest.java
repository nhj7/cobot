//package nhj.util.webpush;
//
//import java.nio.charset.StandardCharsets;
//import java.security.KeyPair;
//import java.security.Security;
//
//import org.apache.commons.io.IOUtils;
//import org.apache.http.HttpResponse;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import com.google.gson.Gson;
//
//import nl.martijndwars.webpush.Notification;
//import nl.martijndwars.webpush.PushService;
//import nl.martijndwars.webpush.Subscription;
//
//public class PushTest {
//    @BeforeClass
//    public static void addSecurityProvider() {
//        Security.addProvider(new BouncyCastleProvider());
//    }
//
//    @Test
//    public void testPush() throws Exception {
//        Gson gson = new Gson();
//
//        // Deserialize subscription object
//        Subscription subscription = gson.fromJson(
//                "{\"endpoint\":\"https://fcm.googleapis.com/fcm/send/efI2iY2iI7g:APA91bFJMK9cNaCh9dDyQ8X3kuXEzVYlHGEJ2BLKG57n7H_NCjTyjJ87wczJKkAV8wfqo5iZRFnTJf1LgaqZ5NsNhGX2PTQQM5pPaCS41ogYfSY9KpfKZJTY410sUQG6yEDGjSuXrtbP\",\"keys\":{\"p256dh\":\"BHj7LOv2ARShKqY_RXP5zoSSpvAevF-VTzJFm9dXfTtnFg5wHVqei_74UOF8vr8kzY-3hR-wgdhGQOw10AxkmBI=\",\"auth\":\"cJN5ZAvblDfOo_Y_ibFZSg==\"}}",
//                Subscription.class
//        );
//
//        // Construct notification
//        Notification notification = new Notification(
//                subscription.endpoint,
//                subscription.keys.p256dh,
//                subscription.keys.auth,
//                "Hello, world!"
//        );
//
//        // Construct push service
//        KeyPair keyPair = TestUtils.readVapidKeys();
//
//        PushService pushService = new PushService();
//        pushService.setKeyPair(keyPair);
//        pushService.setSubject("mailto:admin@domain.com");
//
//        // Send notification!
//        HttpResponse httpResponse = pushService.send(notification);
//
//        System.out.println(httpResponse.getStatusLine().getStatusCode());
//        System.out.println(IOUtils.toString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8));
//    }
//}
