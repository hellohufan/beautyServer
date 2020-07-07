package com.irontower.gcbdriver.app.utils;

/**
 * <p>
 * Copyright: (c) 2016,　福建简悦信息科技有限公司 All rights reserved。
 * </p>
 * <p>
 * 文件名称：DownloadFileCallback.java
 * </p>
 * <p>
 * 描 述：下载回调
 * </p>
 * 
 * @author jhf
 * @date 2016-5-20
 * @version 1.0
 ******************************************************************************** 
 *          <p>
 *          修改记录
 *          </p>
 *          <p>
 *          修改时间：
 *          </p>
 *          <p>
 *          修 改 人：
 *          </p>
 *          <p>
 *          修改内容：
 *          </p>
 */
public interface DownloadFileCallback {
    void downloadError(String msg);
}
