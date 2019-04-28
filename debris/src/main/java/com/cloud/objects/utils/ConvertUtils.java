package com.cloud.objects.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import com.cloud.objects.ObjectJudge;
import com.cloud.objects.logs.Logger;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ConvertUtils {

    public static byte[] toBase64Dec(String src) {
        return Base64.decode(src, Base64.DEFAULT);
    }

    public static String toBase64Enc(byte[] b) {
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap toBitmap(View view, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight,
                Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    /**
     * 图片文件转bitmap
     *
     * @param filePath 图片文件
     * @return bitmap对象
     */
    public static Bitmap toBitmap(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }

    /**
     * 图片文件转bitmap
     *
     * @param options  图片options对象
     * @param filePath 图片文件
     * @return bitmap对象
     */
    public static Bitmap toBitmap(BitmapFactory.Options options, String filePath) {
        options.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }

    /**
     * 字节转bitmap
     *
     * @param bytes 字节
     * @return Bitmap
     */
    public static Bitmap toBitmap(byte[] bytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    /**
     * 数字Object对象转int型
     * <p>
     * param obj          要转换的对象
     * param defaultvlaue 默认值
     * return
     */
    public static int toInt(Object obj, int defaultvlaue) {
        int val = defaultvlaue;
        try {
            if (obj != null) {
                String objstr = obj.toString().trim();
                if (!TextUtils.isEmpty(objstr)) {
                    Number number = NumberFormat.getNumberInstance().parse(objstr);
                    val = number.intValue();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            val = defaultvlaue;
        }
        return val;
    }

    /**
     * 数字Object对象转int型(默认值0)
     * <p>
     * param obj 要转换的对象
     * return
     */
    public static int toInt(Object obj) {
        return toInt(obj, 0);
    }

    public static int toInt(long value) {
        return (int) value;
    }

    /**
     * 数字Object对象转long型
     * <p>
     * param obj          要转换的对象
     * param defaultvlaue 默认值
     * return
     */
    public static long toLong(Object obj, int defaultvlaue) {
        long val = defaultvlaue;
        try {
            if (obj != null) {
                String objstr = obj.toString().trim();
                if (!TextUtils.isEmpty(objstr)) {
                    Number number = NumberFormat.getNumberInstance().parse(objstr);
                    val = number.longValue();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            val = defaultvlaue;
        }
        return val;
    }

    /**
     * 数字Object对象转long型
     * <p>
     * param obj 要转换的对象
     * return
     */
    public static long toLong(Object obj) {
        return toLong(obj, 0);
    }

    /**
     * 数字Object对象转double型
     * <p>
     * param obj          要转换的对象
     * param defaultvlaue 默认值
     * return
     */
    public static double toDouble(Object obj, double defaultvlaue) {
        double val = defaultvlaue;
        try {
            if (obj != null) {
                String objstr = obj.toString().trim();
                if (!TextUtils.isEmpty(objstr)) {
                    Number number = NumberFormat.getNumberInstance().parse(objstr);
                    val = number.doubleValue();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            val = defaultvlaue;
        }
        return val;
    }

    /**
     * 数字Object对象转double型(默认值1.0)
     * <p>
     * param obj 要转换的对象
     * return
     */
    public static double toDouble(Object obj) {
        return toDouble(obj, 0);
    }

    /**
     * 数字Object对象转float型
     * <p>
     * param obj          要转换的对象
     * param defaultvlaue 默认值
     * return
     */
    public static float toFloat(Object obj, float defaultvlaue) {
        float val = defaultvlaue;
        try {
            if (obj != null) {
                String objstr = obj.toString().trim();
                if (!TextUtils.isEmpty(objstr)) {
                    Number number = NumberFormat.getNumberInstance().parse(objstr);
                    val = number.floatValue();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            val = defaultvlaue;
        }
        return val;
    }

    /**
     * 数字Object对象转float型(默认值1.0)
     * <p>
     * param obj 要转换的对象
     * return
     */
    public static float toFloat(Object obj) {
        return toFloat(obj, 0);
    }

    /**
     * 资源图片转换到Bitmap图片
     * <p>
     * param context
     * param resid
     * return
     */
    public static Bitmap toBitmap(Context context, int resid) {
        return BitmapFactory.decodeResource(context.getResources(), resid);
    }

    /**
     * Bitmap转换到字节数组
     * <p>
     * param bitmap
     * return
     */
    public static byte[] toByteArray(Bitmap bitmap) throws IOException {
        byte[] result = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, output);
        bitmap.recycle();
        result = output.toByteArray();
        output.close();
        return result;
    }

    /**
     * 转字符串
     * <p>
     * param value
     * return
     */
    public static String toString(Object value) {
        return value != null ? value.toString() : "";
    }

    /**
     * 转换时间至秒
     * <p>
     * param saveTime 时间
     * param timeUnit 时间单位
     * return
     */
    public static int toSeconds(long saveTime, TimeUnit timeUnit) {
        int time = 0;
        if (timeUnit == TimeUnit.NANOSECONDS) {
            time = (int) timeUnit.toSeconds(saveTime);
        } else if (timeUnit == TimeUnit.MICROSECONDS) {
            time = (int) timeUnit.toSeconds(saveTime);
        } else if (timeUnit == TimeUnit.MILLISECONDS) {
            time = (int) timeUnit.toSeconds(saveTime);
        } else if (timeUnit == TimeUnit.SECONDS) {
            time = (int) timeUnit.toSeconds(saveTime);
        } else if (timeUnit == TimeUnit.MINUTES) {
            time = (int) timeUnit.toSeconds(saveTime);
        } else if (timeUnit == TimeUnit.HOURS) {
            time = (int) timeUnit.toSeconds(saveTime);
        } else if (timeUnit == TimeUnit.DAYS) {
            time = (int) timeUnit.toSeconds(saveTime);
        }
        return time;
    }

    /**
     * 转换时间至毫秒
     * <p>
     * param saveTime 时间
     * param timeUnit 时间单位
     * return
     */
    public static long toMilliseconds(long saveTime, TimeUnit timeUnit) {
        long time = 0;
        if (timeUnit == TimeUnit.NANOSECONDS) {
            time = timeUnit.toMillis(saveTime);
        } else if (timeUnit == TimeUnit.MICROSECONDS) {
            time = timeUnit.toMillis(saveTime);
        } else if (timeUnit == TimeUnit.MILLISECONDS) {
            time = timeUnit.toMillis(saveTime);
        } else if (timeUnit == TimeUnit.SECONDS) {
            time = timeUnit.toMillis(saveTime);
        } else if (timeUnit == TimeUnit.MINUTES) {
            time = timeUnit.toMillis(saveTime);
        } else if (timeUnit == TimeUnit.HOURS) {
            time = timeUnit.toMillis(saveTime);
        } else if (timeUnit == TimeUnit.DAYS) {
            time = timeUnit.toMillis(saveTime);
        }
        return time;
    }

    /**
     * 将字符串转成全角字符
     *
     * @param input 要转换的内容
     * @return 全角字符
     */
    public static String toDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        String s = new String(c);
        return s;
    }

    public static String toAmount(String format, double amount) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(amount);
    }

    /**
     * 将内容通过特定的字符分隔成列表
     *
     * @param content 要处理的内容
     * @param split   分隔符
     * @return 处理后列表
     */
    public static List<String> toList(String content, String split) {
        List<String> lst = new ArrayList<String>();
        if (!TextUtils.isEmpty(content)) {
            if (TextUtils.isEmpty(split)) {
                lst.add(content);
            } else {
                content = content.trim();
                String[] args = content.split(split);
                if (!ObjectJudge.isNullOrEmpty(args)) {
                    for (String arg : args) {
                        if (!TextUtils.isEmpty(arg)) {
                            lst.add(arg);
                        }
                    }
                }
            }
        }
        return lst;
    }

    /**
     * 将列表根据split作为分隔符进行连接
     *
     * @param lst   要连接的集合
     * @param split 连接分隔符
     * @return 已拼接字符串
     */
    public static <T> String toJoin(List<T> lst, String split) {
        if (ObjectJudge.isNullOrEmpty(lst)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (T t : lst) {
            if (t == null) {
                continue;
            }
            builder.append(t).append(split);
        }
        if (builder.length() > 0) {
            builder.delete(builder.length() - split.length(), builder.length());
        }
        return builder.toString();
    }

    /**
     * 将列表根据split作为分隔符进行连接
     * key-value中间用=号连接
     *
     * @param lst   要连接的集合
     * @param split 连接分隔符
     * @return 已拼接字符串
     */
    public static <K, V> String toJoin(Map<K, V> lst, String split) {
        if (ObjectJudge.isNullOrEmpty(lst)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<K, V> entry : lst.entrySet()) {
            if (entry == null) {
                continue;
            }
            builder.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append(split);
        }
        if (builder.length() > 0) {
            builder.delete(builder.length() - split.length(), builder.length());
        }
        return builder.toString();
    }

    /**
     * 将列表根据split作为分隔符进行连接
     *
     * @param lst   要连接的集合
     * @param split 连接分隔符
     * @return 已拼接字符串
     */
    public static <T> String toJoin(T[] lst, String split) {
        if (ObjectJudge.isNullOrEmpty(lst)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (T t : lst) {
            if (t == null) {
                continue;
            }
            builder.append(t).append(split);
        }
        if (builder.length() > 0) {
            builder.delete(builder.length() - split.length(), builder.length());
        }
        return builder.toString();
    }

    /**
     * 将列表根据split作为分隔符进行连接
     *
     * @param lst   要连接的集合
     * @param split 连接分隔符
     * @return 拼接后的字符串
     */
    public static <T> String toJoin(Set<T> lst, String split) {
        if (ObjectJudge.isNullOrEmpty(lst)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (T t : lst) {
            if (t == null) {
                continue;
            }
            builder.append(t).append(split);
        }
        if (builder.length() > 0) {
            builder.delete(builder.length() - split.length(), builder.length());
        }
        return builder.toString();
    }

    /**
     * 根据split连接params参数
     *
     * @param split  连接分隔符
     * @param params 要连接的内容
     * @return 拼接后内容
     */
    public static <T> String toJoin(String split, T... params) {
        if (ObjectJudge.isNullOrEmpty(params)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (T t : params) {
            if (t == null) {
                continue;
            }
            builder.append(t).append(split);
        }
        if (builder.length() > 0) {
            builder.delete(builder.length() - split.length(), builder.length());
        }
        return builder.toString();
    }

    /**
     * 将列表每一项用startSplit和endSplit包裹进行连接
     *
     * @param lst        要连接的集合
     * @param startSplit 每一项开始连接符
     * @param endSplit   每一项开始连接符
     * @return 拼接后的内容
     */
    public static String toJoin(List<String> lst, String startSplit, String endSplit) {
        if (ObjectJudge.isNullOrEmpty(lst)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String param : lst) {
            builder.append(startSplit).append(param).append(endSplit);
        }
        return builder.toString().trim();
    }

    /**
     * 泛型可变参数转成list
     *
     * @param params 泛型可变参数
     * @param <T>    参数类型
     * @return list数组
     */
    public static <T> List<T> toList(T... params) {
        if (ObjectJudge.isNullOrEmpty(params)) {
            return new ArrayList<T>();
        }
        return Arrays.asList(params);
    }

    /**
     * 添加字符串 到可变参数集合中
     *
     * @param object 字符串对象
     * @param params 参数集合
     * @return 已合并的参数集
     */
    public static String[] toJoinArray(String object, String... params) {
        if (object == null || ObjectJudge.isNullOrEmpty(params)) {
            return null;
        }
        String[] array = new String[params.length + 1];
        for (int i = 0; i < params.length; i++) {
            String param = params[i];
            array[i] = param;
        }
        array[params.length] = object;
        return params;
    }

    /**
     * 转换rgb到16进制颜色
     *
     * @param r rgb颜色
     * @param g rgb颜色
     * @param b rgb颜色
     * @return 返回16进制颜色
     */
    public static String toRGBHex(int r, int g, int b) {
        String rFString, rSString, gFString, gSString,
                bFString, bSString, result;
        int red, green, blue;
        int rred, rgreen, rblue;
        red = r / 16;
        rred = r % 16;
        if (red == 10) rFString = "A";
        else if (red == 11) rFString = "B";
        else if (red == 12) rFString = "C";
        else if (red == 13) rFString = "D";
        else if (red == 14) rFString = "E";
        else if (red == 15) rFString = "F";
        else rFString = String.valueOf(red);

        if (rred == 10) rSString = "A";
        else if (rred == 11) rSString = "B";
        else if (rred == 12) rSString = "C";
        else if (rred == 13) rSString = "D";
        else if (rred == 14) rSString = "E";
        else if (rred == 15) rSString = "F";
        else rSString = String.valueOf(rred);

        rFString = rFString + rSString;

        green = g / 16;
        rgreen = g % 16;

        if (green == 10) gFString = "A";
        else if (green == 11) gFString = "B";
        else if (green == 12) gFString = "C";
        else if (green == 13) gFString = "D";
        else if (green == 14) gFString = "E";
        else if (green == 15) gFString = "F";
        else gFString = String.valueOf(green);

        if (rgreen == 10) gSString = "A";
        else if (rgreen == 11) gSString = "B";
        else if (rgreen == 12) gSString = "C";
        else if (rgreen == 13) gSString = "D";
        else if (rgreen == 14) gSString = "E";
        else if (rgreen == 15) gSString = "F";
        else gSString = String.valueOf(rgreen);

        gFString = gFString + gSString;

        blue = b / 16;
        rblue = b % 16;

        if (blue == 10) bFString = "A";
        else if (blue == 11) bFString = "B";
        else if (blue == 12) bFString = "C";
        else if (blue == 13) bFString = "D";
        else if (blue == 14) bFString = "E";
        else if (blue == 15) bFString = "F";
        else bFString = String.valueOf(blue);

        if (rblue == 10) bSString = "A";
        else if (rblue == 11) bSString = "B";
        else if (rblue == 12) bSString = "C";
        else if (rblue == 13) bSString = "D";
        else if (rblue == 14) bSString = "E";
        else if (rblue == 15) bSString = "F";
        else bSString = String.valueOf(rblue);
        bFString = bFString + bSString;
        result = "#" + rFString + gFString + bFString;
        return result;
    }

    /**
     * 字节数组转为文件
     *
     * @param targetFile 目标文件
     * @param bytes      字节
     */
    public static void toFile(File targetFile, byte[] bytes) {
        if (targetFile == null || bytes == null || !targetFile.exists()) {
            return;
        }
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (IOException e) {
            Logger.error(e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    Logger.error(e);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Logger.error(e);
                }
            }
        }
    }

    /**
     * 流转为文件
     *
     * @param targetFile 目标文件
     * @param stream     流对象
     */
    public static void toFile(File targetFile, InputStream stream) {
        if (targetFile == null || stream == null || !targetFile.exists()) {
            return;
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(targetFile);
            int bytesRead = 0;
            byte[] buffer = new byte[4096];
            while ((bytesRead = stream.read(buffer, 0, 4096)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            Logger.error(e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Logger.error(e);
                }
            }
            try {
                stream.close();
            } catch (IOException e) {
                Logger.error(e);
            }
        }
    }

    /**
     * 转换json字符串为JSONArray
     *
     * @param jsonArray json字符串
     * @return JSONArray
     */
    public static JSONArray toJSONArray(String jsonArray) {
        if (TextUtils.isEmpty(jsonArray) || !ObjectJudge.isJsonArray(jsonArray)) {
            //json字符串为空或非json数组
            return null;
        }
        try {
            JSONArray array = new JSONArray(jsonArray);
            return array;
        } catch (JSONException e) {
            return null;
        }
    }
}
