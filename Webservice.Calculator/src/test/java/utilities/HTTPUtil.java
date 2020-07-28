package utilities;

import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HTTPUtil {

	private static final String HTTP_HEADER_ACCEPT = "application/xml";
	private static final String HTTP_HEADER_CONTENT_TYPE = "application/xml";

	private static String HTTP_HEADER_AUTHORIZATION;

	public static String getHTTP_HEADER_AUTHORIZATION() {
		return HTTP_HEADER_AUTHORIZATION;
	}

	public static void setHTTP_HEADER_AUTHORIZATION(
			String hTTP_HEADER_AUTHORIZATION) {
		HTTP_HEADER_AUTHORIZATION = hTTP_HEADER_AUTHORIZATION;
	}

	final static Logger logger = LoggerFactory.getLogger(HTTPUtil.class);

	/**
	 * Call the API using HTTP GET and save the response
	 *
	 * @param protocol
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static void httpGet(String url, String protocol) {

		CloseableHttpClient httpclient;
		try {
			httpclient = (protocol.equalsIgnoreCase("HTTP")) ? HttpClients
					.createDefault() : createSSLHttpClientBuilder().build();
			System.out.println("URL " + url + " protocal " + protocol);
			final HttpGet httpGet = new HttpGet(url);
			// Set HTTP Headers
			httpGet.setHeader("Accept", HTTP_HEADER_ACCEPT);
			httpGet.setHeader("Authorization", getHTTP_HEADER_AUTHORIZATION());

			// Make the call and save results
			final CloseableHttpResponse response = httpclient.execute(httpGet);
			String responseString = null;
			try {
				logger.info("httpGet Status Line {}", response.getStatusLine());

				responseString = EntityUtils.toString(response.getEntity());
				logger.info("httpGet Response {}", responseString);
				System.out.println(responseString);

			} finally {
				response.close();
				httpclient.close();
			}

		} catch (KeyManagementException | NoSuchAlgorithmException
				| IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Call the API using HTTP POST and save the response
	 *
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	
	
	
	
	
	
	
	public static void httpXmlPost(String url, String protocol,StringWriter Request) {
		StringWriter httprequest = Request;
		
	try {
		final CloseableHttpClient httpclient = (protocol
				.equalsIgnoreCase("HTTP")) ? HttpClients.createDefault()
				: createSSLHttpClientBuilder().build();

		// Generate URI
		final HttpPost httpPost = new HttpPost(url);

		// Set HTTP Headers
		httpPost.setHeader("Accept", HTTP_HEADER_ACCEPT);
		httpPost.setHeader("Content-Type", HTTP_HEADER_CONTENT_TYPE);

		httpPost.setHeader("Authorization", getHTTP_HEADER_AUTHORIZATION());


		StringEntity request = new StringEntity((httprequest).toString());
		httpPost.setEntity(request);

		// Make the call and save results
		final CloseableHttpResponse response = httpclient.execute(httpPost);
		String responseString = null;
		try {
			logger.info("httpPost Status Line {}", response.getStatusLine());

			if (response.getStatusLine().getStatusCode() == 200) {
				responseString = EntityUtils.toString(response.getEntity());
				logger.info("httpPOST Response {}", responseString);
				
				/*
				 *This is specific to WriteBack to get the xpath due to the error :
		         *The default (no prefix) Namespace URI for XPath queries is always '' and it cannot be redefined to 'http://www.aviva.co.uk/PMI3rdPartyClaimUpdate'. 
				 */

				responseString = responseString.replace("xmlns=\"http://www.aviva.co.uk/PMI3rdPartyClaimUpdate\"", "");
				
				

				System.out.println(responseString);
				
			}
		} finally {
			response.close();
			httpclient.close();
		}

	} catch (KeyManagementException | NoSuchAlgorithmException
			| IOException e) {
		e.printStackTrace();
	}
}
	
	
	

	/**
	 * Accepts a user id and password and returns a base 64 encoded header
	 */
	public static String getAuthorizationHeadder(String user, String password) {

		final String creds = user + ":" + password;
		final String authHeader = "Basic "
				+ new String(Base64.encodeBase64((creds).getBytes()));
		logger.info("Auth headder {}, {}, {}", user, password, authHeader);

		return authHeader;
	}

	/**
	 * Create SSL context
	 *
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static SSLContext createSSLContext()
			throws NoSuchAlgorithmException, KeyManagementException {

		// Set up a TrustManager that trusts everything
		final SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, new TrustManager[] { new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}
		} }, new SecureRandom());

		return sslContext;
	}

	/**
	 * Create SSL context
	 *
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static HttpClientBuilder createSSLHttpClientBuilder()
			throws KeyManagementException, NoSuchAlgorithmException {

		// Set up SSL connection manager and http client builder
		final SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
				createSSLContext(),
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		
				
		
		

		final Registry<ConnectionSocketFactory> registry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("https", sslConnectionSocketFactory).build();
		final HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(
				registry);
		final HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(ccm);

		return builder;
	}


	
}
