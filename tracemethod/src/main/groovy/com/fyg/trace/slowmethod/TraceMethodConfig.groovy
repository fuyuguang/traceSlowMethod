package com.fyg.trace.slowmethod

import com.fyg.transform.base.BaseConfig;

/**
 * Created by fuyuguang on 2022/8/11 11:04 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class TraceMethodConfig extends BaseConfig{

    String output;
    boolean open;
    String traceConfigFile;
    boolean logTraceInfo;
    Long monitoringTimeThreshold;

    TraceMethodConfig() {
        open = true;
        output = "";
        logTraceInfo = false;
        monitoringTimeThreshold = 100l;
    }
}
