package com.yc.security.base.aop;

import com.yc.security.base.enums.ResultCode;
import com.yc.security.base.exception.BizException;
import com.yc.security.model.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 业务异常
     */
    @ExceptionHandler({BizException.class})
    public ResponseEntity<ResultVO> bizExceptionHandler(Exception ex) {
        LOGGER.error("paramsException => ", ex);
        BizException biz = (BizException) ex;
        if (biz.getDetail() == null) {
            return new ResponseEntity<>(ResultVO.of(biz.getCode()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResultVO.of(biz.getCode(), biz.getDetail()), HttpStatus.OK);
        }
    }

    /**
     * 内部异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultVO> exceptionHandler(Exception ex) {
        LOGGER.error("unknownException => ", ex);
        return new ResponseEntity<>(ResultVO.of(ResultCode.BIZ_SYSTEM_EXECUTE_ERROR), HttpStatus.OK);
    }

}
