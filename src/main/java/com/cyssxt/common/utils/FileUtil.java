package com.cyssxt.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cyssxt.common.constant.ErrorMessage;
import com.cyssxt.common.exception.ValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

public class FileUtil {

    private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private final static String SQL_RESULT = "sql_result";
    private final static String XLSX_RESULT = "xlsx_result";
    public static final String TMP_DASHBOARD_CONTEXT_PATH = "/tmp/dashboard/context_path";

    public static List<Map<String, Object>> getData(String tomcatBaseDirectory, String fileId) throws ValidException {
        try {
            File file = new File(getJSONPath(tomcatBaseDirectory, fileId));
            StringBuffer sb = getFileContent(file);
            logger.info("data={}",sb.toString());
            List<Map<String, Object>> resultList = JSON.parseObject(sb.toString(), new TypeReference<List<Map<String, Object>>>() {
            });
            return resultList;
        } catch (Exception e) {
            logger.error("get data error fileId={}", fileId);
            throw new ValidException(ErrorMessage.LOAD_SQL_DATA_ERROR.getMessageInfo());
        }
    }

    public static StringBuffer getFileContent(File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        bufferedReader.close();
        return sb;
    }

    public static String getTmpDashboardContextPath(String tomcatBaseDirectory) {
        if (StringUtils.isEmpty(tomcatBaseDirectory)) {
            return TMP_DASHBOARD_CONTEXT_PATH;
        }
        return tomcatBaseDirectory;
    }

    public static String getJSONPath(String tomcatBaseDirectory, String fileId) {
        return getTmpDashboardContextPath(tomcatBaseDirectory) + File.separator + "sql_result" + File.separator + fileId + ".json";
    }

    public static String getExcelPath(String tomcatBaseDirectory, String fileId) {
        return getTmpDashboardContextPath(tomcatBaseDirectory) + File.separator + "xlsx_result" + File.separator + fileId + ".xlsx";
    }

    public static String getExcelParentPath(String tomcatBaseDirectory) {
        return getTmpDashboardContextPath(tomcatBaseDirectory) + File.separator + "xlsx_result";
    }

    public static String getJSONParentPath(String tomcatBaseDirectory) {
        return getTmpDashboardContextPath(tomcatBaseDirectory) + File.separator + "sql_result";
    }

    public static String decodeSpecialCharsWhenLikeUseBackslash(String content) {
        // 单引号是oracle字符串的边界,oralce中用2个单引号代表1个单引号
        String afterDecode = content.replaceAll("'", "''");
        // 由于使用了/作为ESCAPE的转义特殊字符,所以需要对该字符进行转义
        // 这里的作用是将"a/a"转成"a//a"
        afterDecode = afterDecode.replaceAll("/", "//");
        // 使用转义字符 /,对oracle特殊字符% 进行转义,只作为普通查询字符，不是模糊匹配
        afterDecode = afterDecode.replaceAll("%", "/%");
        // 使用转义字符 /,对oracle特殊字符_ 进行转义,只作为普通查询字符，不是模糊匹配
        afterDecode = afterDecode.replaceAll("_", "/_");
        return afterDecode;
    }
}