package com.aiyafocus.taotao.common.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {

	public static String doGet(String url) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse response = httpClient.execute(get);
		HttpEntity entity = response.getEntity();
	    String result = EntityUtils.toString(entity, "utf-8");
		response.close();
		httpClient.close();
		return result;
	}
	
	public static String doGet(String url,List<NameValuePair> list) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		URIBuilder builder = new URIBuilder(url);
		builder.setParameters(list);
		HttpGet get = new HttpGet(builder.build());
		CloseableHttpResponse response = httpClient.execute(get);
		HttpEntity entity = response.getEntity();
	    String result = EntityUtils.toString(entity, "utf-8");
		response.close();
		httpClient.close();
		return result;
	}
	
	public static String doPost(String url) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		CloseableHttpResponse response = httpClient.execute(post);
		HttpEntity entity = response.getEntity();
	    String result = EntityUtils.toString(response.getEntity(), "utf-8");
		response.close();
		httpClient.close();
		return result;
	}
	
	public static String doPost(String url,List<NameValuePair> list) throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		StringEntity entity = new UrlEncodedFormEntity(list, "utf-8");
		post.setEntity(entity);
		CloseableHttpResponse response = httpClient.execute(post);
		String result = EntityUtils.toString(response.getEntity(), "utf-8");	
		response.close();
		httpClient.close();
		return result;
	}
	
	public static String doPostJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}
}
