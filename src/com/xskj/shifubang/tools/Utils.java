package com.xskj.shifubang.tools;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by Administrator on 2016-03-04.
 */
public class Utils {
    /**
     * 屏幕宽
     *
     * @param context
     * @return
     */
    public static int getWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 屏幕高
     *
     * @param context
     * @return
     */
    public static int getHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormater1 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss+SSSZ");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    
    /**
     * 验证身份证号是否符合规则
     * @param text 身份证号
     * @return
     */
     public static boolean personIdValidation(String text) {
    	
          String regx = "[0-9]{17}x";
          String reg1 = "[0-9]{15}";
          String regex = "[0-9]{18}";
          return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "刚刚";
        }
        String ftime = "";
        Calendar calNow = Calendar.getInstance();
        calNow.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(calNow.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((calNow.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0) {
                ftime = Math.max(
                        (calNow.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            } else if (hour > 0) {
                ftime = hour + "小时前";
            } else {
                ftime = "刚刚";
            }
            if (ftime.length() > 0 && ftime.contains("-")) {
                ftime = ftime.substring(1);
            }
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = calNow.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((calNow.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (calNow.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 30) {
            ftime = days + "天前";
        } else if (days > 30) {
            ftime = dateFormater2.get().format(time);
        } else {
            ftime = "刚刚";
        }
        return ftime;
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            if (sdate.contains("+0000")) {
                Date time = dateFormater.get().parse(sdate);
                time = new Date(time.getTime() + 8 * 3600 * 1000);
                return time;
            }
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }
    // 时间转成String
    public static String time2String(int time) {
        String s;
        if (time != 0) {
            int sec = time / 1000;
            int min = (sec / 60) % 60;
            sec %= 60;
            StringBuilder sb = new StringBuilder();
            String s2;
            if (min <= 9)
                s2 = (new StringBuilder("0")).append(min).toString();
            else
                s2 = min + "";
            sb = sb.append(s2).append(":");
            if (sec <= 9)
                s2 = (new StringBuilder("0")).append(sec).toString();
            else
                s2 = sec + "";
            s = sb.append(s2).toString();
        } else {
            s = "00:00";
        }
        return s;
    }

    // 转换信息时间
    public static String getInfoTime(int time) {
        int month = time / 1000000;
        int day = (time / 10000) % 100;
        int hour = (time / 100) % 100;
        int minute = time % 100;
        // String stime = (month<10?"0":"")+Integer.toString(month) + "月" +
        // (day<10?"0":"")+Integer.toString(day) + "日 "
        // + (hour<10?"0":"")+Integer.toString(hour) + ":" +
        // (minute<10?"0":"")+Integer.toString(minute);
        String stime = (month < 10 ? "0" : "") + Integer.toString(month) + "-"
                + (day < 10 ? "0" : "") + Integer.toString(day) + " "
                + (hour < 10 ? "0" : "") + Integer.toString(hour) + ":"
                + (minute < 10 ? "0" : "") + Integer.toString(minute);
        return stime;
    }
    /**

     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss

     *

     * @param strDate

     * @return

     */
    public static Date strToDateLong(String strDate) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ParsePosition pos = new ParsePosition(0);

        Date strtodate = formatter.parse(strDate, pos);

        return strtodate;

    }
    /**
     * 检测网络是否连接
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager)context. getSystemService(Context.CONNECTIVITY_SERVICE);
        // 去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            return manager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    // 返回两个经纬度的距离
    public static double getDistance(double latitude1, double longitude1,
                                     double latitude2, double longitude2) {
        double EARTH_RADIUS = 6378137.0;
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 获取map 缓存和读取目录
     */
    public static  String getSdCacheDir(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            java.io.File fExternalStorageDirectory = Environment
                    .getExternalStorageDirectory();
            java.io.File autonaviDir = new java.io.File(
                    fExternalStorageDirectory, "amapsdk");
            boolean result = false;
            if (!autonaviDir.exists()) {
                result = autonaviDir.mkdir();
            }
            java.io.File minimapDir = new java.io.File(autonaviDir,
                    "offlineMap");
            if (!minimapDir.exists()) {
                result = minimapDir.mkdir();
            }
            return minimapDir.toString() + "/";
        } else {
            return "";
        }
    }

    public static void show(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static void show(Context context, int info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    //将网络数据存入文件
    public static void saveData(String str,String file,Context context) {
        try {
            FileOutputStream output = context.openFileOutput(file, context.MODE_PRIVATE);
            byte b[] = str.getBytes();
            output.write(b);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //从文件中读取
    public static  String getData(String file,Context context) {
        String content = "";
        try {
            FileInputStream input = context.openFileInput(file);
            if (input != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content += line;
                }
                input.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}
