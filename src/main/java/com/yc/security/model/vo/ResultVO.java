package com.yc.security.model.vo;

import com.yc.security.base.enums.ResultCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResultVO<T> {

    /**
     * 返回码
     */
    private final String code;

    /**
     * 描述
     */
    private final String message;

    /**
     * 数据
     */
    private final T detail;

    public static <T> ResultVO<T> ofSuccess(T detail) {
        return of(ResultCode.SUCCESS, detail);
    }

    public static <T> ResultVO<T> ofSuccess() {
        return of(ResultCode.SUCCESS);
    }

    public static <T> ResultVO<T> of(ResultCode resultCode, T detail) {
        return new ResultVO<>(resultCode.getCode(), resultCode.getMessage(), detail);
    }

    public static <T> ResultVO<T> of(ResultCode resultCode) {
        return new ResultVO<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public boolean isSuccess() {
        return ResultCode.SUCCESS.name().equals(code);
    }
}
