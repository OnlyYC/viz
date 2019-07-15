package com.liaoyb.viz.engine.queryadapter;

import java.io.File;

/**
 * @author liaoyanbo
 * @date 2019-07-11 14:16
 */
public interface ExportCallback {
    /**
     * 文件完成
     */
    void doComplete(File file);

    /**
     * 更新进度比率
     *
     * @param progressRatio 进度比率
     */
    void doUpdateProgress(float progressRatio);

    /**
     * 发生异常
     *
     * @param e 异常
     */
    void uncaughtException(Throwable e);
}
