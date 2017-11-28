package me.xujichang.hybirdbase.module.web.interfaces;

/**
 * Des:
 *
 * @author xjc
 *         Created on 2017/11/25 17:00.
 */

public interface IWebLoading {
    /**
     * 开始Loading
     */
    void start();

    /**
     * 停止Loading
     */
    void stop();

    /**
     * 显示进度
     *
     * @param progress 进度
     */
    void progress(int progress);
}
