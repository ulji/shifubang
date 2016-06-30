package com.xskj.shifubang.tools;

  
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
  
  
  
public class HttpPostUploadUtil {  
   
    public static String formUpload(String urlStr, Map<String, String> textMap,Map<String, File> fileMap) {  
        String res = "";  
        HttpURLConnection conn = null;  
        String BOUNDARY = "---------------------------123";   
        try {  
            URL url = new URL(urlStr);  
            conn = (HttpURLConnection) url.openConnection();  
            conn.setConnectTimeout(5000);  
            conn.setReadTimeout(30000);  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setUseCaches(false);  
            conn.setRequestMethod("POST");  
            conn.setRequestProperty("Connection", "Keep-Alive");  
            conn.setRequestProperty("User-Agent",  
                            "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");  
            conn.setRequestProperty("Content-Type",  
                    "multipart/form-data; boundary=" + BOUNDARY);  
  
            OutputStream out = new DataOutputStream(conn.getOutputStream());  
            // text  
            if (textMap != null) {  
                StringBuffer strBuf = new StringBuffer();  
                Iterator iter = textMap.entrySet().iterator();  
                while (iter.hasNext()) {  
                    Map.Entry entry = (Map.Entry) iter.next();  
                    String inputName = (String) entry.getKey();  
                    String inputValue = (String) entry.getValue();  
                    if (inputValue == null) {  
                        continue;  
                    }  
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append(  
                            "\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""  
                            + inputName + "\"\r\n\r\n");  
                    strBuf.append(inputValue);  
                }  
                out.write(strBuf.toString().getBytes());  
            }  
  
            // file  
            if (fileMap != null) {  
            	Log.e("fileMap:", fileMap.size()+"");
                Iterator iter = fileMap.entrySet().iterator();  
                while (iter.hasNext()) {  
                    Map.Entry entry = (Map.Entry) iter.next();  
                    String inputName = (String) entry.getKey();  
                    String inputValue = (String) entry.getValue();  
                    if (inputValue == null) {  
                        continue;
                    } 
                   Log.e("inputValueinputValueinputValue:", inputValue);
                   compressPicture(inputValue, inputValue);
                    File file = new File(inputValue);
                    String filename = file.getName();  
                    String contentType = new MimetypesFileTypeMap().getContentType(file);  
                    if (filename.endsWith(".png")) {  
                        contentType = "image/png";  
                    }  
                    if (contentType == null || contentType.equals("")) {  
                        contentType = "application/octet-stream";  
                    }
  
                    StringBuffer strBuf = new StringBuffer();  
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append(  
                            "\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""  
                            + "oss" + "\"; filename=\"" + filename  
                            + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");  
  
                    out.write(strBuf.toString().getBytes());  
                  
                    DataInputStream in = new DataInputStream(  
                            new FileInputStream(file));  
                    int bytes = 0;   
                    byte[] bufferOut = new byte[1024];  
                    while ((bytes = in.read(bufferOut)) != -1) {  
                        out.write(bufferOut, 0, bytes);  
                    }  
                    in.close();  
                }  
            }  
  
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();  
            out.write(endData);  
            out.flush();  
            out.close();  
  
            // 璇诲彇杩斿洖鏁版嵁  
            StringBuffer strBuf = new StringBuffer();  
            BufferedReader reader = new BufferedReader(new InputStreamReader(  
                    conn.getInputStream()));  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
                strBuf.append(line).append("\n");  
            }  
            res = strBuf.toString();
            reader.close();  
            reader = null;  
        } catch (Exception e) {  
            System.out.println("发送POST请求出错。" + urlStr);  
            e.printStackTrace();
            res="2";
        } finally {
            if (conn != null) {  
                conn.disconnect();  
                conn = null;  
            }  
        }  
        return res;  
    }  
    
    /**
     * 功能：图片压缩并保存为流的形式的工具类
     * @param srcPath 要压缩的图片路径
     * @param desPath 压缩后保存的图片路径
     * compress中的第二个参数是压缩比例的设置，100的话图片是原始的大小，越小图片越压缩
     * 时间：2016-6-18
     * 姓名：赵爱民
     */
    public static void compressPicture(String srcPath, String desPath) {  
        FileOutputStream fos = null;  
        BitmapFactory.Options op = new BitmapFactory.Options();  
  
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        op.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, op);  
        op.inJustDecodeBounds = false;  
  
        // 缩放图片的尺寸  
        float w = op.outWidth;  
        float h = op.outHeight;  
       float hh = 1024f;//   设置图片的高度
        float ww = 1024f;//  设置图片的宽度
        // 最长宽度或高度1024  
       float be = 1.0f;
        if (w > h && w > ww) {  
            be = (float) (w / ww);  
        } else if (w < h && h > hh) {  
            be = (float) (h / hh);  
        }  
        if (be <= 0) {  
            be = 1.0f;  
        }  
        op.inSampleSize = (int) be;// 设置缩放比例,这个数字越大,图片大小越小.  
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
       bitmap = BitmapFactory.decodeFile(srcPath, op);  
      int desWidth = (int) (w / be);  
      int desHeight = (int) (h / be);  

        bitmap = Bitmap.createScaledBitmap(bitmap, desWidth, desHeight, true);  
        try {  
            fos = new FileOutputStream(desPath);  
            if (bitmap != null) {  
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);  
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        }  
    }  

  
}  