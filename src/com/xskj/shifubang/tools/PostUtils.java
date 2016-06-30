package com.xskj.shifubang.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

import com.xskj.shifubang.view.ProgersssDialogTools;

public class PostUtils {
	
	public ProgersssDialogTools progersssDialogTools;
	    /**
		 * 向指定URL发送POST方法的请求
		 * 
		 * @param url
		 *            发送请求的URL
		 * @param params
		 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
		 * @return URL所代表远程资源的响应
		 */
		public static String sendPost(String url, String params) {
			PrintWriter out = null;
			BufferedReader in = null;
			String result = "";
			try {
				URL realUrl = new URL(url);
				// 打开和URL之间的连接
				URLConnection conn = realUrl.openConnection();
				
				Log.e("日志", "进入---------");
				// 设置通66用的请求属性
				conn.setRequestProperty("accept", "*/*");
				conn.setRequestProperty("connection", "Keep-Alive");
				conn.setRequestProperty("user-agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
				// 发送POST请求必须设置如下两行
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.connect();
				// 获取URLConnection对象对应的输出流
				out = new PrintWriter(conn.getOutputStream());
				Log.e("params------:", out.toString());
				// 发送请求参数

				Log.e("params------:", params);
				out.print(params); // ②	
				
				// flush输出流的缓冲
				out.flush();
				// 定义BufferedReader输入流来读取URL的响应
				in = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				Log.e("params------:", in.readLine());
				String line;
				
				while ((line = in.readLine()) != null) {
					result += "\n" + line;
				}
			} catch (Exception e) {						
				System.out.println("发送POST请求出现异常！" + e);
				Log.e("异常-------:", "发送POST请求出现异常！");
				e.printStackTrace();
				return "error";
			}
			// 使用finally块来关闭输出流、输入流
			finally {
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			Log.e("result-------:", result);
			return result;
		}

}




