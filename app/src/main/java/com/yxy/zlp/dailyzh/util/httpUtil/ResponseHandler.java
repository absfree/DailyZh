package com.yxy.zlp.dailyzh.util.httpUtil;

/**
 * Created by Administrator on 2016/6/2.
 */
public interface ResponseHandler {
    void onSuccess(byte[] result);
    void onFailure();
}
