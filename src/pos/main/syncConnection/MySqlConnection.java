package pos.main.syncConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.os.StrictMode;

public class MySqlConnection {

	
    /** The time it takes for our client to timeout */
    public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
 
    /** Single instance of our HttpClient */
    private static HttpClient mHttpClient;
 
/**
     * Get our single instance of our HttpClient object.
     *
     * @return an HttpClient object with connection parameters set
     */
    private static HttpClient getHttpClient() {
        if (mHttpClient == null) {
            mHttpClient = new DefaultHttpClient();
            final HttpParams params = mHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
            ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
        }
        return mHttpClient;
    }
 
    /**
     * Performs an HTTP Post request to the specified url with the
     * specified parameters.
     *
     * @param url The web address to post the request to
     * @param postParameters The parameters to send via the request
     * @return The result of the request
     * @throws Exception
     */
    
    
    public static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters) throws Exception {
    	
    	BufferedReader in = null;
    	System.out.println("Sendiong request...."+ url);
        try {
        	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			 
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            InputStreamReader is=new InputStreamReader(response.getEntity().getContent());
            in= new BufferedReader(is);
 
            StringBuffer sb = new StringBuffer("");
            String line = "";
         //   line= new String(raw, 0 , val);
            
           // String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
 
            String result = sb.toString();
            System.out.println("Result...."+result);
            return result;
        }catch (Exception e) {
        	e.printStackTrace();
        	System.out.println("catch request...."+ url);
			
		}finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            System.out.println("finally request...."+ url);
        }
        
        System.out.println("finish request...."+ url);
		return null;
    }
 
    /**
     * Performs an HTTP GET request to the specified url.
     *
     * @param url The web address to post the request to
     * @return The result of the request
     * @throws Exception
     */
    public static String executeHttpGet(String url) throws Exception {
        BufferedReader in = null;
        String result ="";
        try {
         HttpClient client = getHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
 
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            result = sb.toString();
            
            
            
            return result;
        } finally {
            if (in != null) {
                try {
                    in.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
