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
		 * ��ָ��URL����POST����������
		 * 
		 * @param url
		 *            ���������URL
		 * @param params
		 *            ����������������Ӧ����name1=value1&name2=value2����ʽ��
		 * @return URL������Զ����Դ����Ӧ
		 */
		public static String sendPost(String url, String params) {
			PrintWriter out = null;
			BufferedReader in = null;
			String result = "";
			try {
				URL realUrl = new URL(url);
				// �򿪺�URL֮�������
				URLConnection conn = realUrl.openConnection();
				
				Log.e("��־", "����---------");
				// ����ͨ66�õ���������
				conn.setRequestProperty("accept", "*/*");
				conn.setRequestProperty("connection", "Keep-Alive");
				conn.setRequestProperty("user-agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
				// ����POST�������������������
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.connect();
				// ��ȡURLConnection�����Ӧ�������
				out = new PrintWriter(conn.getOutputStream());
				Log.e("params------:", out.toString());
				// �����������

				Log.e("params------:", params);
				out.print(params); // ��	
				
				// flush������Ļ���
				out.flush();
				// ����BufferedReader����������ȡURL����Ӧ
				in = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				Log.e("params------:", in.readLine());
				String line;
				
				while ((line = in.readLine()) != null) {
					result += "\n" + line;
				}
			} catch (Exception e) {						
				System.out.println("����POST��������쳣��" + e);
				Log.e("�쳣-------:", "����POST��������쳣��");
				e.printStackTrace();
				return "error";
			}
			// ʹ��finally�����ر��������������
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




