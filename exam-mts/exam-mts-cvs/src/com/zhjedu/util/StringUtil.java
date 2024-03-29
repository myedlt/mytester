﻿package com.zhjedu.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 此类中封装一些常用的字符串操作。
 * 所有方法都是静态方法，不需要生成此类的实例，
 * 为避免生成此类的实例，构造方法被申明为private类型的。
 * @since  0.1
 */

public class StringUtil {
  /**
   * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
   */
  private StringUtil() {
  }

  private static final BitSet allowed_query;
  private static MessageDigest digest = null;

  static
    {
        allowed_query = new BitSet(256);
        for(int i = 48; i <= 57; i++)
        {
            allowed_query.set(i);
        }

        for(int i = 97; i <= 122; i++)
        {
            allowed_query.set(i);
        }

        for(int i = 65; i <= 90; i++)
        {
            allowed_query.set(i);
        }

        allowed_query.set(45);
        allowed_query.set(95);
        allowed_query.set(46);
        allowed_query.set(33);
        allowed_query.set(126);
        allowed_query.set(42);
        allowed_query.set(39);
        allowed_query.set(40);
        allowed_query.set(41);
    }

  /**
   * 此方法将给出的字符串source使用delim划分为单词数组。
   * @param source 需要进行划分的原字符串
   * @param delim 单词的分隔字符串
   * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组，
   *         如果delim为null则使用逗号作为分隔字符串。
   * @since  0.1
   */
  public static String[] split(String source, String delim) {
    String[] wordLists;
    if (source == null) {
      wordLists = new String[1];
      wordLists[0] = source;
      return wordLists;
    }
    if (delim == null) {
      delim = ",";
    }
    StringTokenizer st = new StringTokenizer(source, delim);
    int total = st.countTokens();
    wordLists = new String[total];
    for (int i = 0; i < total; i++) {
      wordLists[i] = st.nextToken();
    }
    return wordLists;
  }

  /**
   * 此方法将给出的字符串source使用delim划分为单词数组。
   * @param source 需要进行划分的原字符串
   * @param delim 单词的分隔字符
   * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组。
   * @since  0.2
   */
  public static String[] split(String source, char delim) {
    return split(source, String.valueOf(delim));
  }

  /**
   * 此方法将给出的字符串source使用逗号划分为单词数组。
   * @param source 需要进行划分的原字符串
   * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组。
   * @since  0.1
   */
  public static String[] split(String source) {
    return split(source, ",");
  }

  /**
   * 循环打印字符串数组。
   * 字符串数组的各元素间以指定字符分隔，如果字符串中已经包含指定字符则在字符串的两端加上双引号。
   * @param strings 字符串数组
   * @param delim 分隔符
   * @param out 打印到的输出流
   * @since  0.4
   */
  public static void printStrings(String[] strings, String delim,
                                  OutputStream out) {
    try {
      if (strings != null) {
        int length = strings.length - 1;
        for (int i = 0; i < length; i++) {
          if (strings[i] != null) {
            if (strings[i].indexOf(delim) > -1) {
              out.write( ("\"" + strings[i] + "\"" + delim).getBytes());
            }
            else {
              out.write( (strings[i] + delim).getBytes());
            }
          }
          else {
            out.write("null".getBytes());
          }
        }
        if (strings[length] != null) {
          if (strings[length].indexOf(delim) > -1) {
            out.write( ("\"" + strings[length] + "\"").getBytes());
          }
          else {
            out.write(strings[length].getBytes());
          }
        }
        else {
          out.write("null".getBytes());
        }
      }
      else {
        out.write("null".getBytes());
      }
      out.write(Constants.LINE_SEPARATOR.getBytes());
    }
    catch (IOException e) {

    }
  }

  /**
   * 循环打印字符串数组到标准输出。
   * 字符串数组的各元素间以指定字符分隔，如果字符串中已经包含指定字符则在字符串的两端加上双引号。
   * @param strings 字符串数组
   * @param delim 分隔符
   * @since  0.4
   */
  public static void printStrings(String[] strings, String delim) {
    printStrings(strings, delim, System.out);
  }

  /**
   * 循环打印字符串数组。
   * 字符串数组的各元素间以逗号分隔，如果字符串中已经包含逗号则在字符串的两端加上双引号。
   * @param strings 字符串数组
   * @param out 打印到的输出流
   * @since  0.2
   */
  public static void printStrings(String[] strings, OutputStream out) {
    printStrings(strings, ",", out);
  }

  /**
   * 循环打印字符串数组到系统标准输出流System.out。
   * 字符串数组的各元素间以逗号分隔，如果字符串中已经包含逗号则在字符串的两端加上双引号。
   * @param strings 字符串数组
   * @since  0.2
   */
  public static void printStrings(String[] strings) {
    printStrings(strings, ",", System.out);
  }

  /**
   * 将字符串中的变量使用values数组中的内容进行替换。
   * 替换的过程是不进行嵌套的，即如果替换的内容中包含变量表达式时不会替换。
   * @param prefix 变量前缀字符串
   * @param source 带参数的原字符串
   * @param values 替换用的字符串数组
   * @return 替换后的字符串。
   *         如果前缀为null则使用“%”作为前缀；
   *         如果source或者values为null或者values的长度为0则返回source；
   *         如果values的长度大于参数的个数，多余的值将被忽略；
   *         如果values的长度小于参数的个数，则后面的所有参数都使用最后一个值进行替换。
   * @since  0.2
   */
  public static String getReplaceString(String prefix, String source,
                                        String[] values) {
    String result = source;
    if (source == null || values == null || values.length < 1) {
      return source;
    }
    if (prefix == null) {
      prefix = "%";
    }

    for (int i = 0; i < values.length; i++) {
      String argument = prefix + Integer.toString(i + 1);
      int index = result.indexOf(argument);
      if (index != -1) {
        String temp = result.substring(0, index);
        if (i < values.length) {
          temp += values[i];
        }
        else {
          temp += values[values.length - 1];
        }
        temp += result.substring(index + 2);
        result = temp;
      }
    }
    return result;
  }

  /**
   * 将字符串中的变量（以“%”为前导后接数字）使用values数组中的内容进行替换。
   * 替换的过程是不进行嵌套的，即如果替换的内容中包含变量表达式时不会替换。
   * @param source 带参数的原字符串
   * @param values 替换用的字符串数组
   * @return 替换后的字符串
   * @since  0.1
   */
  public static String getReplaceString(String source, String[] values) {
    return getReplaceString("%", source, values);
  }

  /**
   * 字符串数组中是否包含指定的字符串。
   * @param strings 字符串数组
   * @param string 字符串
   * @param caseSensitive 是否大小写敏感
   * @return 包含时返回true，否则返回false
   * @since  0.4
   */
  public static boolean contains(String[] strings, String string,
                                 boolean caseSensitive) {
    for (int i = 0; i < strings.length; i++) {
      if (caseSensitive == true) {
        if (strings[i].equals(string)) {
          return true;
        }
      }
      else {
        if (strings[i].equalsIgnoreCase(string)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 字符串数组中是否包含指定的字符串。大小写敏感。
   * @param strings 字符串数组
   * @param string 字符串
   * @return 包含时返回true，否则返回false
   * @since  0.4
   */
  public static boolean contains(String[] strings, String string) {
    return contains(strings, string, true);
  }

  /**
   * 不区分大小写判定字符串数组中是否包含指定的字符串。
   * @param strings 字符串数组
   * @param string 字符串
   * @return 包含时返回true，否则返回false
   * @since  0.4
   */
  public static boolean containsIgnoreCase(String[] strings, String string) {
    return contains(strings, string, false);
  }

  /**
   * 将字符串数组使用指定的分隔符合并成一个字符串。
   * @param array 字符串数组
   * @param delim 分隔符，为null的时候使用""作为分隔符（即没有分隔符）
   * @return 合并后的字符串
   * @since  0.4
   */
  public static String combineStringArray(String[] array, String delim) {
    int length = array.length - 1;
    if (delim == null) {
      delim = "";
    }
    StringBuffer result = new StringBuffer(length * 8);
    for (int i = 0; i < length; i++) {
      result.append(array[i]);
      result.append(delim);
    }
    result.append(array[length]);
    return result.toString();
  }

  /**
   * 将指定字符串插入到数组每个字符串的头部。
   * @param array
   * @param headString
   */
  public static String[] insertHeadToArray(String[] array, String headString) {
      StringBuffer sb;
    for (int i=0;i<array.length;i++) {
        sb = new StringBuffer(headString.length()+16);
        sb.append(headString);
        sb.append(File.separator);
        sb.append(array[i]);
        array[i] = sb.toString();
    }
    return array;
  }

  /**
   * 去除所有的回车。
   * @param source
   * @param String
   */
  public static String replaceEnter(String source) {
      return source.replace('\n',' ');
  }
  
  /**
   * 转换所有的回车为HTML中的分行。
   * @param source
   * @param String
   */
  public static String replaceEnterToBr(String source) {
      return source.replaceAll("\n","<br />");
  }
  /**
   * 把字符串数据转化成Base64的格式
   * @param data String
   * @return String
   */
  public static String encodeBase64(String data)
  {
      byte bytes[] = null;
      try
      {
          bytes = data.getBytes("ISO-8859-1");
      }
      catch(UnsupportedEncodingException uee)
      {
          System.out.println(uee.getMessage());
      }
      return encodeBase64(bytes);
  }

  /**
   * 把btye数组数据转化成Base64的格式
   * @param data byte[]
   * @return String
   */
  public static String encodeBase64(byte data[])
  {
      int len = data.length;
      StringBuffer ret = new StringBuffer((len / 3 + 1) * 4);
      for(int i = 0; i < len; i++)
      {
          int c = data[i] >> 2 & 0x3f;
          ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(c));
          c = data[i] << 4 & 0x3f;
          if(++i < len)
          {
              c |= data[i] >> 4 & 0xf;
          }
          ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(c));
          if(i < len)
          {
              c = data[i] << 2 & 0x3f;
              if(++i < len)
              {
                  c |= data[i] >> 6 & 3;
              }
              ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(c));
          } else
          {
              i++;
              ret.append('=');
          }
          if(i < len)
          {
              c = data[i] & 0x3f;
              ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(c));
          } else
          {
              ret.append('=');
          }
      }

      return ret.toString();
  }

  /**
   * 把Base64的格式字符串数据解成原始数据
   * @param data String
   * @return String
   */
  public static String decodeBase64(String data)
  {
      byte bytes[] = null;
      try
      {
          bytes = data.getBytes("ISO-8859-1");
      }
      catch(UnsupportedEncodingException uee)
      {
    	  System.out.println(uee.getMessage());
      }
      return decodeBase64(bytes);
  }

  /**
   * 把Base64的格式btye数组数据解成原始数据 
   * @param data byte[]
   * @return String
   */
  public static String decodeBase64(byte data[])
  {
      int len = data.length;
      StringBuffer ret = new StringBuffer((len * 3) / 4);
      for(int i = 0; i < len; i++)
      {
          int c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf(data[i]);
          i++;
          int c1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf(data[i]);
          c = c << 2 | c1 >> 4 & 3;
          ret.append((char)c);
          if(++i < len)
          {
              c = data[i];
              if(61 == c)
              {
                  break;
              }
              c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf(c);
              c1 = c1 << 4 & 0xf0 | c >> 2 & 0xf;
              ret.append((char)c1);
          }
          if(++i >= len)
          {
              continue;
          }
          c1 = data[i];
          if(61 == c1)
          {
              break;
          }
          c1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".indexOf(c1);
          c = c << 6 & 0xc0 | c1;
          ret.append((char)c);
      }

      return ret.toString();
  }

  /**
   * 把指定URL字符串按照指定的编码转换
   * @param original 指定的URL字符串
   * @param charset 指定的编码方式
   * @return 转换后的URL
   * @throws UnsupportedEncodingException
   */
  public static String URLEncode(String original, String charset)
      throws UnsupportedEncodingException
  {
      if(original == null)
      {
          return null;
      }
      byte octets[];
      try
      {
          octets = original.getBytes(charset);
      }
      catch(UnsupportedEncodingException error)
      {
          throw new UnsupportedEncodingException();
      }
      StringBuffer buf = new StringBuffer(octets.length);
      for(int i = 0; i < octets.length; i++)
      {
          char c = (char)octets[i];
          if(allowed_query.get(c))
          {
              buf.append(c);
          } else
          {
              buf.append('%');
              byte b = octets[i];
              char hexadecimal = Character.forDigit(b >> 4 & 0xf, 16);
              buf.append(Character.toUpperCase(hexadecimal));
              hexadecimal = Character.forDigit(b & 0xf, 16);
              buf.append(Character.toUpperCase(hexadecimal));
          }
      }

      return buf.toString();
    }

    /**
     * 哈希数据
     * @param data 原始数据
     * @return 哈希后的数据
     */
    public static final synchronized String hash(String data)
    {
        if(digest == null)
        {
            try
            {
                digest = MessageDigest.getInstance("MD5");
            }
            catch(NoSuchAlgorithmException nsae)
            {
            	System.out.println("Failed to load the MD5 MessageDigest. LMS will be unable to function normally."+nsae.getMessage());
            }
        }
        try
        {
            digest.update(data.getBytes("utf-8"));
        }
        catch(UnsupportedEncodingException e)
        {
        	System.out.println(e.getMessage());
        }
        return encodeHex(digest.digest());
    }

    /**
     *
     * @param bytes byte[]
     * @return String
     */
    public static final String encodeHex(byte bytes[])
    {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for(int i = 0; i < bytes.length; i++)
        {
            if((bytes[i] & 0xff) < 16)
            {
                buf.append("0");
            }
            buf.append(Long.toString(bytes[i] & 0xff, 16));
        }

        return buf.toString();
    }

    /**
     *
     * @param hex String
     * @return byte[]
     */
    public static final byte[] decodeHex(String hex)
    {
        char chars[] = hex.toCharArray();
        byte bytes[] = new byte[chars.length / 2];
        int byteCount = 0;
        for(int i = 0; i < chars.length; i += 2)
        {
            int newByte = 0;
            newByte |= hexCharToByte(chars[i]);
            newByte <<= 4;
            newByte |= hexCharToByte(chars[i + 1]);
            bytes[byteCount] = (byte)newByte;
            byteCount++;
        }

        return bytes;
    }

    /**
     * 把16位字符转换成字节吗
     * @param ch 16位字符
     * @return 字节
     */
    private static final byte hexCharToByte(char ch)
    {
        switch(ch)
        {
        case 48: // '0'
            return 0;

        case 49: // '1'
            return 1;

        case 50: // '2'
            return 2;

        case 51: // '3'
            return 3;

        case 52: // '4'
            return 4;

        case 53: // '5'
            return 5;

        case 54: // '6'
            return 6;

        case 55: // '7'
            return 7;

        case 56: // '8'
            return 8;

        case 57: // '9'
            return 9;

        case 97: // 'a'
            return 10;

        case 98: // 'b'
            return 11;

        case 99: // 'c'
            return 12;

        case 100: // 'd'
            return 13;

        case 101: // 'e'
            return 14;

        case 102: // 'f'
            return 15;

        case 58: // ':'
        case 59: // ';'
        case 60: // '<'
        case 61: // '='
        case 62: // '>'
        case 63: // '?'
        case 64: // '@'
        case 65: // 'A'
        case 66: // 'B'
        case 67: // 'C'
        case 68: // 'D'
        case 69: // 'E'
        case 70: // 'F'
        case 71: // 'G'
        case 72: // 'H'
        case 73: // 'I'
        case 74: // 'J'
        case 75: // 'K'
        case 76: // 'L'
        case 77: // 'M'
        case 78: // 'N'
        case 79: // 'O'
        case 80: // 'P'
        case 81: // 'Q'
        case 82: // 'R'
        case 83: // 'S'
        case 84: // 'T'
        case 85: // 'U'
        case 86: // 'V'
        case 87: // 'W'
        case 88: // 'X'
        case 89: // 'Y'
        case 90: // 'Z'
        case 91: // '['
        case 92: // '\\'
        case 93: // ']'
        case 94: // '^'
        case 95: // '_'
        case 96: // '`'
        default:
            return 0;
        }
    }

    /**
     * 更改文件的扩展名
     * @param filename 文件名
     * @param suffix 新的扩展名
     * @return 新的文件名
     */
    public static String changeFileNameSuffixTo(String filename, String suffix)
    {
        int dotPos = filename.lastIndexOf('.');
        if(dotPos != -1)
        {
            return filename.substring(0, dotPos + 1) + suffix;
        } else
        {
            return filename;
        }
    }

    /**
     * 转换javascript中的符号
     * @param source 代码
     * @return 转换后的代码
     */
    public static String escapeJavaScript(String source)
    {
        source = substitute(source, "\\", "\\\\");
        source = substitute(source, "\"", "\\\"");
        source = substitute(source, "'", "\\'");
        source = substitute(source, "\r\n", "\\n");
        source = substitute(source, "\n", "\\n");
        return source;
    }

    /**
     * 消除字符串为Pattern,是一个正则表达式变为一个非正则表达式
     * @param source 正则表达式字符串
     * @return 转换后的字符串
     */
    public static String escapePattern(String source)
    {
        if(source == null)
        {
            return null;
        }
        StringBuffer result = new StringBuffer(source.length() * 2);
        for(int i = 0; i < source.length(); i++)
        {
            char ch = source.charAt(i);
            switch(ch)
            {
            case 92: // '\\'
                result.append("\\\\");
                break;

            case 47: // '/'
                result.append("\\/");
                break;

            case 36: // '$'
                result.append("\\$");
                break;

            case 94: // '^'
                result.append("\\^");
                break;

            case 46: // '.'
                result.append("\\.");
                break;

            case 42: // '*'
                result.append("\\*");
                break;

            case 43: // '+'
                result.append("\\+");
                break;

            case 124: // '|'
                result.append("\\|");
                break;

            case 63: // '?'
                result.append("\\?");
                break;

            case 123: // '{'
                result.append("\\{");
                break;

            case 125: // '}'
                result.append("\\}");
                break;

            case 91: // '['
                result.append("\\[");
                break;

            case 93: // ']'
                result.append("\\]");
                break;

            case 40: // '('
                result.append("\\(");
                break;

            case 41: // ')'
                result.append("\\)");
                break;

            default:
                result.append(ch);
                break;
            }
        }

        return new String(result);
    }

    /**
     * 把text文本中的attribute属性抽取出来,其中返回的map中包含两个元素,一个是键值text,一个是键值value.返回的text键值中的值将不再包含attribute
     * @param text text文本
     * @param attribute text中的属性
     * @param defValue 默认属性值
     * @return 其中返回的map中包含两个元素,一个是键值text,一个是键值value
     */
    public static Map extendAttribute(String text, String attribute, String defValue)
    {
        Map retValue = new HashMap();
        retValue.put("text", text);
        retValue.put("value", "'" + defValue + "'");
        if(text != null && text.toLowerCase().indexOf(attribute.toLowerCase()) >= 0)
        {
            String quotation = "'";
            int pos1 = text.toLowerCase().indexOf(attribute.toLowerCase());
            int pos2 = text.indexOf(quotation, pos1);
            int test = text.indexOf("\"", pos1);
            if(test > -1 && (pos2 == -1 || test < pos2))
            {
                quotation = "\"";
                pos2 = test;
            }
            int pos3 = text.indexOf(quotation, pos2 + 1);
            String newValue = quotation + defValue + text.substring(pos2 + 1, pos3 + 1);
            String newText = text.substring(0, pos1);
            if(pos3 < text.length())
            {
                newText = newText + text.substring(pos3 + 1);
            }
            retValue.put("text", newText);
            retValue.put("value", newValue);
        }
        return retValue;
    }

    /**
     * 把HTML中的body部分提取出来
     * @param content HTML文本内容
     * @return HTML中的body内容
     */
    public static String extractHtmlBody(String content)
    {
        Matcher startMatcher = C_BODY_START_PATTERN.matcher(content);
        Matcher endMatcher = C_BODY_END_PATTERN.matcher(content);
        int start = 0;
        int end = content.length();
        if(startMatcher.find())
        {
            start = startMatcher.end();
        }
        if(endMatcher.find(start))
        {
            end = endMatcher.start();
        }
        return content.substring(start, end);
    }

    /**
     * 得到XML文件头部标示的编码方式
     * @param content XML文本内容
     * @return 编码方式
     */
    public static String extractXmlEncoding(String content)
    {
        String result = null;
        Matcher xmlHeadMatcher = C_XML_HEAD_REGEX.matcher(content);
        if(xmlHeadMatcher.find())
        {
            String xmlHead = xmlHeadMatcher.group();
            Matcher encodingMatcher = C_XML_ENCODING_REGEX.matcher(xmlHead);
            if(encodingMatcher.find())
            {
                String encoding = encodingMatcher.group();
                int pos1 = encoding.indexOf('=') + 2;
                String charset = encoding.substring(pos1, encoding.length() - 1);
                if(Charset.isSupported(charset))
                {
                    result = charset;
                }
            }
        }
        return result;
    }

    /**
     * 转换运行时间长度,把运行时长的毫秒数转换成时分秒
     * @param runtime 运行时间
     * @return 转换后的字符串(格式为00:00:00)
     */
    public static String formatRuntime(long runtime)
    {
        long seconds = (runtime / 1000L) % 60L;
        long minutes = (runtime / 60000L) % 60L;
        long hours = (runtime / 0x36ee80L) % 24L;
        long days = runtime / 0x5265c00L;
        StringBuffer strBuf = new StringBuffer();
        if(days > 0L)
        {
            if(days < 10L)
            {
                strBuf.append('0');
            }
            strBuf.append(days);
            strBuf.append(':');
        }
        if(hours < 10L)
        {
            strBuf.append('0');
        }
        strBuf.append(hours);
        strBuf.append(':');
        if(minutes < 10L)
        {
            strBuf.append('0');
        }
        strBuf.append(minutes);
        strBuf.append(':');
        if(seconds < 10L)
        {
            strBuf.append('0');
        }
        strBuf.append(seconds);
        return strBuf.toString();
    }

    /**
     * 判断字符串是否为空
     * @param value 字符串
     * @return 判断结果
     */
    public static boolean isEmpty(String value)
    {
        return value == null || value.length() == 0;
    }

    /**
     * 判断字符串去掉空白是否为空
     * @param value 字符串
     * @return 判断结果
     */
    public static boolean isEmptyOrWhitespaceOnly(String value)
    {
        return isEmpty(value) || value.trim().length() == 0;
    }

    /**
     * 判断字符串是否不为空
     * @param value 字符串
     * @return 判断结果
     */
    public static boolean isNotEmpty(String value)
    {
        return value != null && value.length() != 0;
    }
    
    /**
     * 判断字符串去掉空白是否不为空
     * @param value 字符串
     * @return 判断结果
     */
    public static boolean isNotEmptyOrWhitespaceOnly(String value)
    {
        return value != null && value.trim().length() > 0;
    }

    /**
     * 判断className是否符合java命名规则
     * @param className 类名
     * @return 判断结果
     */
    public static boolean isValidJavaClassName(String className)
    {
        if(isEmpty(className))
        {
            return false;
        }
        int length = className.length();
        boolean nodot = true;
        for(int i = 0; i < length; i++)
        {
            char ch = className.charAt(i);
            if(nodot)
            {
                if(ch == '.')
                {
                    return false;
                }
                if(Character.isJavaIdentifierStart(ch))
                {
                    nodot = false;
                } else
                {
                    return false;
                }
                continue;
            }
            if(ch == '.')
            {
                nodot = true;
                continue;
            }
            if(Character.isJavaIdentifierPart(ch))
            {
                nodot = false;
            } else
            {
                return false;
            }
        }

        return true;
    }

   /**
    * 根据分隔符分割字符串成字符串数组
    * @param source 字符串
    * @param delimiter 分隔符
    * @return 字符串数组
    */
    public static String[] splitAsArray(String source, char delimiter)
    {
        List result = splitAsList(source, delimiter);
        return (String[])result.toArray(new String[result.size()]);
    }

   /**
    * 根据分隔符分割字符串成字符串数组
    * @param source 字符串
    * @param delimiter 分隔符
    * @return 字符串数组
    */
    public static String[] splitAsArray(String source, String delimiter)
    {
        List result = splitAsList(source, delimiter);
        return (String[])result.toArray(new String[result.size()]);
    }

    /**
     * 根据分隔符分割字符串成List数组
     * @param source 字符串
     * @param delimiter 分隔符
     * @return 字符串数组
     */
    public static List splitAsList(String source, char delimiter)
    {
        return splitAsList(source, delimiter, false);
    }

    /**
     * 根据分隔符分割字符串成List数组
     * @param source 字符串
     * @param delimiter 分隔符
     * @return 字符串数组
     */
    public static List splitAsList(String source, String delimiter)
    {
        return splitAsList(source, delimiter, false);
    }
    
    /**
     * 根据分隔符分割字符串成List数组
     * @param source 字符串
     * @param delimiter 分隔符
     * @param trim 是否去除空白
     * @return 字符串数组
     */
    public static List splitAsList(String source, char delimiter, boolean trim)
    {
        List result = new ArrayList();
        int index = 0;
        for(int next = source.indexOf(delimiter); next != -1; next = source.indexOf(delimiter, index))
        {
            String item = source.substring(index, next);
            if(trim)
            {
                result.add(item.trim());
            } else
            {
                result.add(item);
            }
            index = next + 1;
        }

        if(trim)
        {
            result.add(source.substring(index).trim());
        } else
        {
            result.add(source.substring(index));
        }
        return result;
    }

    /**
     * 根据分隔符分割字符串成List数组
     * @param source 字符串
     * @param delimiter 分隔符
     * @param trim 是否去除空白
     * @return 字符串数组
     */
    public static List splitAsList(String source, String delimiter, boolean trim)
    {
        int len = delimiter.length();
        if(len == 1)
        {
            return splitAsList(source, delimiter.charAt(0), trim);
        }
        List result = new ArrayList();
        int index = 0;
        for(int next = source.indexOf(delimiter); next != -1; next = source.indexOf(delimiter, index))
        {
            String item = source.substring(index, next);
            if(trim)
            {
                result.add(item.trim());
            } else
            {
                result.add(item);
            }
            index = next + len;
        }

        if(trim)
        {
            result.add(source.substring(index).trim());
        } else
        {
            result.add(source.substring(index));
        }
        return result;
    }

    /**
     * 搜索content内容字符串中serarchString的字符串,并且把它替换成replaceItem字符串的内容
     * @param content 内容字符串
     * @param searchString 要搜索的字符串
     * @param replaceItem 要替换成的字符串
     * @return 处理后的字符串
     */
    public static String substitute(String content, String searchString, String replaceItem)
    {
        if(content == null)
        {
            return null;
        }
        int stringLength = content.length();
        int findLength;
        if(searchString == null || (findLength = searchString.length()) == 0)
        {
            return content;
        }
        if(replaceItem == null)
        {
            replaceItem = "";
        }
        int replaceLength = replaceItem.length();
        int length;
        if(findLength == replaceLength)
        {
            length = stringLength;
        } else
        {
            int count = 0;
            int end;
            for(int start = 0; (end = content.indexOf(searchString, start)) != -1; start = end + findLength)
            {
                count++;
            }

            if(count == 0)
            {
                return content;
            }
            length = stringLength - count * (findLength - replaceLength);
        }
        int start = 0;
        int end = content.indexOf(searchString, start);
        if(end == -1)
        {
            return content;
        }
        StringBuffer sb = new StringBuffer(length);
        for(; end != -1; end = content.indexOf(searchString, start))
        {
            sb.append(content.substring(start, end));
            sb.append(replaceItem);
            start = end + findLength;
        }

        end = stringLength;
        sb.append(content.substring(start, end));
        return sb.toString();
    }

    /**
     * 验证资源文件名是否符合规范
     * @param 文件名
     * @return 返回结果
     */
    public static boolean validateResourceName(String name)
    {
        if(name == null)
        {
            return false;
        }
        int l = name.length();
        if(l == 0)
        {
            return false;
        }
        if(name.length() != name.trim().length())
        {
            return false;
        }
        for(int i = 0; i < l; i++)
        {
            char ch = name.charAt(i);
            switch(ch)
            {
            case 47: // '/'
                return false;

            case 92: // '\\'
                return false;

            case 58: // ':'
                return false;

            case 42: // '*'
                return false;

            case 63: // '?'
                return false;

            case 34: // '"'
                return false;

            case 62: // '>'
                return false;

            case 60: // '<'
                return false;

            case 124: // '|'
                return false;
            }
            if(Character.isISOControl(ch))
            {
                return false;
            }
            if(!Character.isDefined(ch))
            {
                return false;
            }
        }

        return true;
    }

    public static String replaceNum(int num){
		String Num = "一二三四五六七八九十";
		
		if(num <= 10){
			return Num.substring(num, num + 1);
		}else{
			Num = (num + "").replaceAll("1", "一");
			Num = (num + "").replaceAll("2", "二");
			Num = (num + "").replaceAll("3", "三");
			Num = (num + "").replaceAll("4", "四");
			Num = (num + "").replaceAll("5", "五");
			Num = (num + "").replaceAll("6", "六");
			Num = (num + "").replaceAll("7", "七");
			Num = (num + "").replaceAll("8", "八");
			Num = (num + "").replaceAll("9", "九");
			return Num;
		}
  }
    
	/**
	 * 提供小数位四舍五入处理。
	 * @param d 需要四舍五入的数字 
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果
	 */
    public static double round(double d, int scale) {
        long temp=1;
        for (int i=scale; i>0; i--) {
                temp*=10;
        }
        d*=temp;
        long dl=Math.round(d);
        return (double)(dl)/temp;
}
    
	public static String uuid() {
		return UUIDHexGenerator.getInstance().generate();
	}
    
    public static void main(String argv[]){

    }
    
    
    
    public static final String C_BODY_END_REGEX = "<\\s*/\\s*body[^>]*>";
    public static final String C_BODY_START_REGEX = "<\\s*body[^>]*>";
    public static final String C_LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String C_TABULATOR = "  ";
    private static final Pattern C_BODY_END_PATTERN = Pattern.compile(C_BODY_END_REGEX, 2);
    private static final Pattern C_BODY_START_PATTERN = Pattern.compile(C_BODY_START_REGEX, 2);
    private static final long C_DAYS = 0x5265c00L;
    private static final long C_HOURS = 0x36ee80L;
    private static final long C_MINUTES = 60000L;
    private static final long C_SECONDS = 1000L;
    private static final Pattern C_XML_ENCODING_REGEX = Pattern.compile("encoding\\s*=\\s*[\"'].+[\"']", 2);
    private static final Pattern C_XML_HEAD_REGEX = Pattern.compile("<\\s*\\?.*\\?\\s*>", 2);
    private static String m_contextReplace;
    private static String m_contextSearch;

}

class UUIDHexGenerator {

	private String sep = "";

	private static final int IP;

	private static short counter = (short) 0;

	private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

	private static UUIDHexGenerator uuidgen = new UUIDHexGenerator();

	static {
		int ipadd;
		try {
			ipadd = toInt(InetAddress.getLocalHost().getAddress());
		} catch (Exception e) {
			ipadd = 0;
		}
		IP = ipadd;
	}

	public static UUIDHexGenerator getInstance() {
		return uuidgen;
	}

	public static int toInt(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}

	protected String format(int intval) {
		String formatted = Integer.toHexString(intval);
		StringBuffer buf = new StringBuffer("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	protected String format(short shortval) {
		String formatted = Integer.toHexString(shortval);
		StringBuffer buf = new StringBuffer("0000");
		buf.replace(4 - formatted.length(), 4, formatted);
		return buf.toString();
	}

	protected int getJVM() {
		return JVM;
	}

	protected synchronized short getCount() {
		if (counter < 0) {
			counter = 0;
		}
		return counter++;
	}

	protected int getIP() {
		return IP;
	}

	protected short getHiTime() {
		return (short) (System.currentTimeMillis() >>> 32);
	}

	protected int getLoTime() {
		return (int) System.currentTimeMillis();
	}

	public String generate() {
		return new StringBuffer(36)
				.append(format(getIP()))
				.append(sep)
				.append(format(getJVM()))
				.append(sep)
				.append(format(getHiTime()))
				.append(sep)
				.append(format(getLoTime()))
				.append(sep)
				.append(format(getCount()))
				.toString();
	}
}
