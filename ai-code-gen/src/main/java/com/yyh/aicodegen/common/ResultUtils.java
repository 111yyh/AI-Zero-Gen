package com.yyh.aicodegen.common;

import com.yyh.aicodegen.exception.ErrorCode;

public class ResultUtils {

    /**
     * 响应成功
     * @param data 数据
     * @return 响应
     * @param <T> 数据类型
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 响应失败
     * @param code
     * @param msg
     * @return
     */
    public static BaseResponse<?> error(int code, String msg) {
        return new BaseResponse<>(code, null, msg);
    }

    public static BaseResponse<?> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static BaseResponse<?> error(ErrorCode errorCode, String msg) {
        return new BaseResponse<>(errorCode.getCode(), null, msg);
    }
}
