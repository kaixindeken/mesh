package com.tanklab.mathless.pojo;

import com.alibaba.fastjson.JSONObject;
import com.tanklab.mathless.pojo.bo.LogBO;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class LogPages {
    private static final int PAGE_SIZE = 65536;
    private static final int MAP_MAXSIZE = 8192;

    private final Map<String, String> logsMap = new LinkedHashMap<>(MAP_MAXSIZE);

    private static final LogPages instance = new LogPages();

    private LogPages() {
    }

    public static LogPages getInstance() {
        return instance;
    }

    public JSONObject saveLog(String userName, String log) {
        Date date = new Date();
        String id = String.valueOf(userName.hashCode() & Integer.MAX_VALUE).concat(String.valueOf(date.getTime()));
        logsMap.put(id, log);
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("pages", (log.length() / PAGE_SIZE) + (log.length() % PAGE_SIZE == 0 ? 0 : 1));
        return object;
    }

    public JSONObject readLog(LogBO logBO) {
        JSONObject object = new JSONObject();
        if (!logsMap.containsKey(logBO.getId())) {
            object.put("error", -1);
            return object;
        }
        String log = logsMap.get(logBO.getId());
        int pageNum = logBO.getPageNum();
        object.put("log", log.substring(pageNum * PAGE_SIZE, Math.min(log.length(), (pageNum + 1) * PAGE_SIZE)));
        return object;
    }
}
