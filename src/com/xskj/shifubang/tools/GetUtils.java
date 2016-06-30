package com.xskj.shifubang.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class GetUtils {

	public static String sendGet(String url, String url1) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = url + "?" + url1;
			URL realUrl = new URL(urlName);
			// 打开和Url之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0(compatible;MSIE 6.0;Windows NT 5.1;SV1)");
			// 建立实际的连接
			conn.connect();

			// 获取所有响应头字段
			Map<String, List<String>> map = conn.getHeaderFields();
			// 遍历所有响应的头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}

			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			System.out.println("发送Get请求出现的异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		Log.e("aa", "-----------------------" + result);
		return result;
	}
}