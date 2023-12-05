package com.nebula.oauth2.authentication.config.mvc;

import com.nebula.oauth2.authentication.mvc.response.Response;
import com.nebula.oauth2.authentication.exception.BusiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Set;

/**
 * 全局异常处理
 *
 * @author feifeixia
 * 2018/12/25 17:39
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size:1M}")
    private String maxFileSize;

    /**
     * 业务异常统一处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler( {BusiException.class})
    public ResponseEntity<Response> handleBusiException(BusiException e) {
        return ResponseEntity.ok(Response.failure(e.getMessage()));
    }

    /**
     * 参数验证 异常处理
     *
     * @param ex ConstraintViolationException
     * @return
     */
    @ExceptionHandler( {ConstraintViolationException.class})
    public ResponseEntity<Response> handleCiException(ConstraintViolationException ex) {
        final ResponseEntity<Response> result = ResponseEntity.ok(Response.failure(null));
        final Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            if (!StringUtils.isEmpty(violation.getMessage())) {
                final Response resultBody = result.getBody();
                assert resultBody != null;
                resultBody.setErrorMessage(violation.getMessage());
                break;
            }
        }
        return result;
    }

    /**
     * 兜底异常处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler( {Exception.class})
    public ResponseEntity<Response> handle(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        final Response response = Response.failure("系统错误");
        if (ex instanceof HttpMessageNotReadableException
                || ex instanceof MethodArgumentTypeMismatchException) {
            response.setErrorMessage("参数解析失败");
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            response.setErrorMessage("不支持当前请求方法");
            status = HttpStatus.METHOD_NOT_ALLOWED;
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            response.setErrorMessage("不支持当前媒体类型");
            status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        } else if (ex instanceof SQLException) {
            response.setErrorMessage("服务运行SQL异常");
        } else if (ex instanceof MissingServletRequestParameterException) {
            status = HttpStatus.BAD_REQUEST;
            response.setErrorMessage("请求参数不全");
        } else if (ex instanceof MaxUploadSizeExceededException) {
            status = HttpStatus.PAYLOAD_TOO_LARGE;
            response.setErrorMessage("上传文件总大小不允许超过" + maxFileSize);
        } else if (ex instanceof MethodArgumentNotValidException) {
            status = HttpStatus.BAD_REQUEST;
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            if (bindingResult.hasErrors()) {
                final FieldError fieldError = bindingResult.getFieldError();
                response.setErrorMessage(fieldError.getDefaultMessage());
            }
        } else if (ex instanceof BindException) {
            BindingResult bindingResult = ((BindException) ex).getBindingResult();
            if (bindingResult.hasErrors()) {
                final FieldError fieldError = bindingResult.getFieldError();
                response.setErrorMessage(fieldError.getDefaultMessage());
            }
        }
        log.error("兜底异常", ex);
        return ResponseEntity.status(status).body(response);
    }

}
