package com.liaoyb.viz.engine.domain;

import lombok.Data;

/**
 * 导出参数
 *
 * @author liaoyanbo
 */
@Data
public class ExportParam {
    /**
     * 导出任务id
     */
    private String exportTaskId;
    /**
     * 文件名
     */
    private String filename;

    /**
     * 导出类型(csv、excel)
     */
    private String exportType;
}
