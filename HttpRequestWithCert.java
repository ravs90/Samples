
import java.io.InputStream;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;


public class HttpRequestWithCert {

	private static class DefaultTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	private static void getCertificate() {
		HttpsURLConnection conn = null;

		try {
			// configure the SSLContext with a TrustManager
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
			SSLContext.setDefault(ctx);

			URL url = new URL("https://10.xxx.xxx.xxx");
			conn = (HttpsURLConnection) url.openConnection();
			conn.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});
			System.out.println(conn.getResponseCode());

		} catch (Exception ex) {

		}
	}
	public static void main(String[] args) {
		getCertificate();
		HttpClient httpclient = new HttpClient();
				
		String name = "admin";
        String password = "admin123";
        String authString = name + ":" + password;
        byte[] byteArr = authString.getBytes();
        String authStringEnc = org.apache.commons.codec.binary.Base64.encodeBase64String(byteArr);
        System.out.println("Base64 encoded auth string: " + authStringEnc);
        String requestUrl = "https://10.xxx.xxx.xxx/";
        System.out.println("Making Api call "+requestUrl);
        GetMethod method = new GetMethod(requestUrl);		
	
		method.addRequestHeader("Authorization","Basic "+authStringEnc);		
		method.addRequestHeader("Content-Type", "application/json");		
		try {			
			int statuscode = httpclient.executeMethod(method);
			System.out.println("statuscode is "+statuscode);
			String retVal2=null;
			InputStream inpStream = method.getResponseBodyAsStream();
			retVal2  = convertStreamToString(inpStream);
			System.out.println(retVal2);
			inpStream.close();
		} catch (Exception e) { // TODO Auto-generated catch block
									// e.printStackTrace(); } finally {
			e.printStackTrace();
			method.releaseConnection();
		}

	}
	static String convertStreamToString(java.io.InputStream is) {
	    @SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}

}
