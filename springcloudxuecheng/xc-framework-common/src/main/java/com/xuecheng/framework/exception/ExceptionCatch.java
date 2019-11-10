package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.Response;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕获类
 */
@ControllerAdvice
public class ExceptionCatch {

    public static final Logger logger = LoggerFactory.getLogger(ExceptionCatch.class);
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException customException){
        logger.error("catch exception:{}\r\nexception:",customException.getMessage(),customException);
        //ResponseResult responseResult = new ResponseResult();
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }
}
