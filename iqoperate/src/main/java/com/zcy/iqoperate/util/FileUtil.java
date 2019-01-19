package com.zcy.iqoperate.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

/**
 * create date : 2019/1/19
 */
public class FileUtil {

    /**
     * 创建文件
     *
     * @param content  字符串内容
     * @param filePath 文件夹地址
     * @param fileName 文件名
     * @return 返回文件完整路径
     */
    public static String createFile(String content, String filePath, String fileName) {

        if (content == null || filePath == null || fileName == null) {
            throw new RuntimeException("创建文件缺少相应信息");
        }

        //转换分隔符
        filePath = filePath.replace("\\", "/");

        //文件的完整路径
        String fileFullPath = filePath + "/" + fileName;

        File file = new File(fileFullPath);

        // 如果父目录不存在，创建父目录
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        //如果文件存在，则先将其删除
        if (file.exists()) {
            file.delete();
        }

        Writer write = null;

        try {
            //创建文件
            file.createNewFile();

            // 将格式化后的字符串写入文件
            write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(content);
            write.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                write.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileFullPath;
    }

    /**
     * 将json生成.json格式文件
     */
    public static String createJsonFile(String jsonString, String filePath, String fileName) {

        if (jsonString.indexOf("'") != -1) {
            //将单引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
            jsonString = jsonString.replaceAll("'", "\\'");
        }
        if (jsonString.indexOf("\"") != -1) {
            //将双引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
            jsonString = jsonString.replaceAll("\"", "\\\"");
        }

        if (jsonString.indexOf("\r\n") != -1) {
            //将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
            jsonString = jsonString.replaceAll("\r\n", "\\u000d\\u000a");
        }
        if (jsonString.indexOf("\n") != -1) {
            //将换行转换一下，因为JSON串中字符串不能出现显式的换行
            jsonString = jsonString.replaceAll("\n", "\\u000a");
        }

        // 格式化json字符串
        jsonString = formatJson(jsonString);

        return createFile(jsonString, filePath, fileName);
    }

    /**
     * 读取文件成string字符串
     *
     * @param filePath 如："D:/iq/candles.json"
     * @return
     */
    public static String readFileToString(String filePath) {

        BufferedReader bufferedReader = null;// 读取原始json文件
        String content = "";
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            String s = null;
            while ((s = bufferedReader.readLine()) != null) {
                content = content + s;
            }

            // bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return content;
    }

    /**
     * 读取文件成Object
     * @param filePath
     * @param classType
     * @return
     */
    public static Object readFileToObject(String filePath, Class classType) {
        File file = new File("D:/iq/candles.json");
        ObjectMapper mapper = new ObjectMapper();

        //返回结果
        Object object = null;
        try {
            object = mapper.readValue(file, classType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    /**
     * 返回格式化JSON字符串。
     *
     * @param json 未格式化的JSON字符串。
     * @return 格式化的JSON字符串。
     */
    private static String formatJson(String json) {
        StringBuffer result = new StringBuffer();

        int length = json.length();
        int number = 0;
        char key = 0;

        // 遍历输入字符串。
        for (int i = 0; i < length; i++) {
            // 1、获取当前字符。
            key = json.charAt(i);

            // 2、如果当前字符是前方括号、前花括号做如下处理：
            if ((key == '[') || (key == '{')) {
                // （1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
                if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
                    result.append('\n');
                    result.append(indent(number));
                }

                // （2）打印：当前字符。
                result.append(key);

                // （3）前方括号、前花括号，的后面必须换行。打印：换行。
                result.append('\n');

                // （4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
                number++;
                result.append(indent(number));

                // （5）进行下一次循环。
                continue;
            }

            // 3、如果当前字符是后方括号、后花括号做如下处理：
            if ((key == ']') || (key == '}')) {
                // （1）后方括号、后花括号，的前面必须换行。打印：换行。
                result.append('\n');

                // （2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
                number--;
                result.append(indent(number));

                // （3）打印：当前字符。
                result.append(key);

                // （4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
                if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
                    result.append('\n');
                }

                // （5）继续下一次循环。
                continue;
            }

            // 4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
            /*if ((key == ',')) {
                result.append(key);
                result.append('\n');
                result.append(indent(number));
                continue;
            }*/

            // 5、打印：当前字符。
            result.append(key);
        }
        return result.toString();
    }

    /**
     * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
     *
     * @param number 缩进次数。
     * @return 指定缩进次数的字符串。
     */
    private static String indent(int number) {
        StringBuffer result = new StringBuffer();
        String SPACE = "   ";
        for (int i = 0; i < number; i++) {
            result.append(SPACE);
        }
        return result.toString();
    }
}
