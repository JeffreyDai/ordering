package com.wzqj.ordering.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static Map<Character, String> toEscapeChar = new HashMap<Character, String>();    //html转义字符映射

    static {
        toEscapeChar.put(Character.valueOf('<'), "&lt;");
        toEscapeChar.put(Character.valueOf('>'), "&gt;");
        toEscapeChar.put(Character.valueOf('\''), "&#39;");
        toEscapeChar.put(Character.valueOf('"'), "&quot;");
        toEscapeChar.put(Character.valueOf('&'), "&amp;");
        toEscapeChar.put(Character.valueOf('='), "&#61;");
        toEscapeChar.put(Character.valueOf('`'), "&#96;");
    }

    /**
     * 是不是手机号. 代替以前的MobileUtil(新手机号判断错误)
     */
    public static boolean isMobileNO(String no) {
        return no == null ? false : no.matches("1[0-9]{10}");
    }

    /**
     * 隐藏手机号码中间4位
     */
    public static String maskMobile(String mobile) {
        if (mobile != null && mobile.length() > 7) {
            return mobile.substring(0, 3) + "****" + mobile.substring(7);
        } else {
            return mobile;
        }
    }

    /**
     * 换算ip地址, 规则:
     * 127.1.2.3
     * =127*256*256*256
     * +1*256*256
     * +2*256
     * +3
     */
    public static long convertIp(String ip) {
        try {
            String[] arr = ip.split("[.]");
            long sum = 0;
            for (int i = 0, j = arr.length - 1; i < arr.length; i++, j--) {
                sum += Integer.parseInt(arr[i]) * Math.pow(256, j);
            }
            return sum;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 关闭多个流
     */
    public static void closeStream(Closeable... closeable) {
        for (Closeable c : closeable) {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 请求json流,在服务端接收转化为json对象
     */
    public static JSONObject ConvertRequestToJSON(HttpServletRequest request) throws IOException {
        InputStream requestInputStream = request.getInputStream();
        String requestString = IOUtils.toString(requestInputStream, "UTF-8");
        IOUtils.closeQuietly(requestInputStream);
        return JSONObject.parseObject(requestString);
    }

    /**
     * 检查对象是否是默认值,数字--0;字符--"";对象--{}/[]/null
     */
    public static boolean checkDefaultValue(Object value) {
        if (null == value || String.valueOf(value).equals("") || String.valueOf(value).equals("{}")
                || String.valueOf(value).equals("[]") || String.valueOf(value).equals("0")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查字符串是否是数字
     *
     * @return 是数字 true;不是数字 false
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]+[.]*[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * readTxt
     */
    public static String readTxt(String classpathFile) {
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(classpathFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    "utf-8"));
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * readFile
     */
    public static String readFile(String filePath) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将String数组转换为Int数组
     */
    public static int[] toIntArray(String[] s) {
        int[] ints = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            ints[i] = Integer.parseInt(s[i]);
        }
        return ints;
    }

    /**
     * 将String数组转换为Int数组
     */
    public static long[] toLongArray(String[] s) {
        long[] longs = new long[s.length];
        for (int i = 0; i < s.length; i++) {
            longs[i] = Long.parseLong(s[i]);
        }
        return longs;
    }

    /**
     * toIntList
     */
    public static List<Integer> toIntList(String[] s) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < s.length; i++) {
            list.add(Integer.parseInt(s[i]));
        }
        return list;
    }

    /**
     * arrToList
     */
    public static List<Integer> arrToList(int[] s) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < s.length; i++) {
            list.add(s[i]);
        }
        return list;
    }

    /**
     * 将字符串集合分隔拼接成一条字符串
     */
    public static String join(String[] arr, String splitter) {
        return join(Arrays.asList(arr), splitter, null);
    }

    /**
     * join
     */
    public static String join(String[] arr, String splitter, String replacement) {
        return join(Arrays.asList(arr), splitter, replacement);
    }

    /**
     * 将字符串集合分隔拼接成一条字符串
     *
     * @param list     集合
     * @param splitter 分隔符
     */
    public static String join(List<String> list, String splitter) {
        return join(list, splitter, null);
    }

    /**
     * 将字符串集合分隔拼接成一条字符串
     *
     * @param list        集合
     * @param splitter    分隔符
     * @param replacement 将与分隔符有冲突的字符串替换
     */
    public static String join(List<String> list, String splitter, String replacement) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (replacement != null && list.get(i) != null) {
                sb.append(list.get(i).replace(splitter, replacement));
            } else {
                sb.append(list.get(i));
            }
            if (i < list.size() - 1) {
                sb.append(splitter);
            }
        }
        return sb.toString();
    }

    /**
     * toVector
     */
    public static <T> Vector<T> toVector(T[] arr) {
        Vector<T> v = new Vector<T>();
        for (T t : arr) {
            v.add(t);
        }
        return v;
    }

    /**
     * 生成visitkey, 和ip,port,秒时间相关
     * (( ip最后一部分<<48 ) | ( port<<32 )) & (0xffffffff00000000) | time
     */
    public static long genVisitkey(String ip, int port) {
        String[] arr = ip.split("[.]");
        long lastIp = Long.parseLong(arr[arr.length - 1]);
        long portL = port;
        long time = System.currentTimeMillis() / 1000;
        return ((lastIp << 48) | (portL << 32)) & 0xffffffff00000000L | time;
    }

    /**
     * isEmpty
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /**
     * isNotEmpty
     */
    public static boolean isNotEmpty(String s) {
        return s != null && !"".equals(s);
    }

    /**
     * 把ip4转成long
     */
    public static long iP2Long(String ip4) {
        String[] ipArray = ip4.split("[.]");
        if (ipArray.length != 4) {
            return 0;
        }
        long ipLong = 0;
        for (int i = 3; i >= 0; --i) {
            long section = Long.valueOf(ipArray[i]);
            ipLong = (ipLong << 8) + section;
        }
        return ipLong;
    }

    /**
     * 把Util.iP2Long(String ip4)的结果还原成IP4字符串
     */
    public static String long2Ip4(long ip) {
        return new StringBuilder()
                .append(ip & 0xFF).append(".")
                .append((ip >> 8) & 0xFF).append(".")
                .append((ip >> 16) & 0xFF).append(".")
                .append((ip >> 24) & 0xFF)
                .toString();
    }

    /**
     * s是否在list中
     */
    public static boolean contains(List<String> list, String s) {
        return new HashSet<String>(list).contains(s);    //使用hashset做快速查询
    }

    /**
     * contains
     */
    public static boolean contains(String[] list, String s) {
        return contains(Arrays.asList(list), s);
    }

    /**
     * 把request.getParameterMap()转成字符串, 适用于查看POST参数
     *
     * @param charset 是否要把参数urlencode
     */
    public static String paramMapToString(Map paramMap, String charset) {
        if (paramMap.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : paramMap.entrySet()) {
            Entry e = (Entry) o;
            if (e.getValue() instanceof String[]) {
                for (String val : (String[]) e.getValue()) {
                    if (charset != null) {
                        try {
                            val = URLEncoder.encode(val, charset);
                        } catch (UnsupportedEncodingException ex) {
                            ex.printStackTrace();
                        }
                    }
                    sb.append(e.getKey()).append("=").append(val).append("&");
                }
            } else {
                String value = (String) e.getValue();
                if (charset != null) {
                    try {
                        value = URLEncoder.encode(value, charset);
                    } catch (UnsupportedEncodingException ex) {
                        ex.printStackTrace();
                    }
                }
                sb.append(e.getKey()).append("=").append(value).append("&");
            }
        }
        if (sb.toString().endsWith("&")) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * paramMapToString
     */
    public static String paramMapToString(Map paramMap) {
        return paramMapToString(paramMap, null);
    }

    /**
     * 把a=1&a=2&b=2&c=3这种url参数反解成map
     */
    public static Map<String, String[]> deserializeQueryStr(String queryString) {
        Map<String, String[]> map = new HashMap<String, String[]>();
        for (String s : queryString.split("&")) {
            try {
                String[] entry = s.split("=", 2);
                if (map.containsKey(entry[0])) {
                    String[] o = map.get(entry[0]);
                    String[] n = new String[o.length + 1];
                    System.arraycopy(o, 0, n, 0, o.length);
                    n[o.length] = entry[1];
                    map.put(entry[0], n);
                } else {
                    map.put(entry[0], new String[]{entry[1]});
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 从数组里随机筛选一个数
     */
    public static <T> T random(T... arr) {
        return arr[new Random().nextInt(arr.length)];
    }

    /**
     * MD5加密
     *
     * @param cont 要加密的字节数组
     * @return String 加密后的字符串
     */
    public static String toMd5(byte[] cont) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(cont);
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer();
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();                        //32位加密
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成文件的md5码
     */
    public static String toMd5(File file) throws IOException {
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int len = 0;
            MessageDigest md = MessageDigest.getInstance("MD5");
            while ((len = fis.read(buf)) > 0) {
                md.update(buf, 0, len);
            }
            byte[] bytes = md.digest();

            char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            StringBuffer sb = new StringBuffer(2 * bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                char c0 = hex[(bytes[i] & 0xf0) >> 4];// 取字节中高 4 位的数字转换
                // 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
                char c1 = hex[bytes[i] & 0xf];// 取字节中低 4 位的数字转换
                sb.append(c0).append(c1);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发起网络请求, 获得本机IP.
     * 直接使用InetAddress.getLocalHost()的话, 在服务器上会报UnknownHostException
     */
    public static String getLocalIp() {
        String localip = "";
        String netip = "";
        InetAddress ip = null;
        boolean finded = false;
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            while ((netInterfaces.hasMoreElements()) && (!finded)) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = (InetAddress) address.nextElement();
                    if ((!ip.isSiteLocalAddress()) && (!ip.isLoopbackAddress()) 
                            && (ip.getHostAddress().indexOf(":") == -1)) {
                        netip = ip.getHostAddress();
                        finded = true;
                        break;
                    }
                    if ((ip.isSiteLocalAddress()) && (!ip.isLoopbackAddress()) 
                            && (ip.getHostAddress().indexOf(":") == -1)) {
                        localip = ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return isNotEmpty(netip) ? netip : localip;
    }

    /**
     * html转义,防止XSS攻击
     */
    public static String htmlEscape(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            String replace = (String) toEscapeChar.get(Character.valueOf(c));
            sb.append(replace == null ? c : replace);
        }
        return sb.toString();
    }

    /**
     * html转义
     *
     * @param s           待处理的字符
     * @param filterChars 自定义需要被过滤的字符
     */
    public static String htmlEscape(String s, char... filterChars) {
        if (s == null) {
            return null;
        }
        Arrays.sort(filterChars);
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Arrays.binarySearch(filterChars, c) >= 0) {
                sb.append("&#").append((int) c).append(";");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * urlEncode
     */
    public static String urlEncode(String s, String charset) {
        if (s == null) {
            return null;
        }
        try {
            return URLEncoder.encode(s, charset);
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    /**
     * urlDecode
     */
    public static String urlDecode(String s, String charset) {
        if (s == null) {
            return null;
        }
        try {
            return URLDecoder.decode(s, charset);
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    /**
     * toString
     */
    public static String toString(byte[] bs, String charset) {
        try {
            return new String(bs, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * getBytes
     */
    public static byte[] getBytes(String s, String charset) {
        try {
            return s.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * writeBuf
     */
    public static void writeBuf(InputStream is, OutputStream os) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = bis.read(buf)) != -1) {
            bos.write(buf, 0, len);
        }
        bos.flush();
    }

    /**
     * 从url中截取域名或ip
     */
    public static String parseHost(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            int slash = url.lastIndexOf("/");
            return slash > -1 ? url.substring(0, slash) : url;
        }
        Matcher m = Pattern.compile("(http://|https://)(.*?)(:|/|$)", Pattern.CASE_INSENSITIVE).matcher(url);
        if (m.find()) {
            return m.group(2);
        }
        return null;
    }

    /**
     * 从url中截取域名+端口或ip+端口
     *
     * @return 1:域名或ip 2:端口
     */
    public static String[] parseHostIp(String url) {
        Matcher m = Pattern.compile("(http://|https://)(.*?):?(\\d*)(/|$)", Pattern.CASE_INSENSITIVE).matcher(url);
        String[] arr = new String[2];
        if (m.find()) {
            arr[0] = m.group(2);
            if (Util.isNotEmpty(m.group(3))) {
                arr[1] = m.group(3);
            }
        }
        return arr;
    }

    /**
     * 去掉url中的参数串
     */
    public static String truncateParam(String url) {
        int i = url.indexOf("?");
        return i > 0 ? url.substring(0, i) : url;
    }

    /**
     * 判断是否为局域网IP
     * <ul>
     *     <li>127开头</li>
     *     <li>10.0.0.0~10.255.255.255</li>
     *     <li>172.16.0.0~172.31.255.255</li>
     *     <li>192.168.0.0~192.168.255.255</li>
     * </ul>
     */
    public static boolean isIntraNet(String ip) {
        String reg = "(10|127|172|192)\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})"
                + "\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})"
                + "\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})";
        return ip.matches(reg);
    }

    /**
     * 统计substr在s中出现的次数
     */
    public static int appearCount(String s, String substr) {
        int total = 0;
        for (String tmp = s; tmp != null && tmp.length() >= substr.length(); ) {
            if (tmp.indexOf(substr) == 0) {
                total++;
            }
            tmp = tmp.substring(1);
        }
        return total;
    }

    /**
     * 生成几个随机小写字母
     */
    public static String randChar(int count) {
        byte[] arr = new byte[count];
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (byte) (rand.nextInt(26) + 97);
        }
        return new String(arr);
    }
}
