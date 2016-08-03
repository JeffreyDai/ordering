package com.wzqj.ordering.common.exception;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class BusinessException extends RuntimeException {

    private long errCode;
    private String errMsg;
    private long innerErrCode;  //用于记录外部调用的错误码
    private String innerErrMsg; //用于记录外部调用的错误信息
    private Exception targetException;

    protected BusinessException() {
    }

    protected BusinessException(long errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    protected BusinessException(long errCode, String errMsg, long innerErrCode, String innerErrMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.innerErrCode = innerErrCode;
        this.innerErrMsg = innerErrMsg;
    }

    protected BusinessException(long errCode, String errMsg, Exception e) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.targetException = e;
    }

    public static BusinessException createInstance(BusinessErrorType type) {
        return new BusinessException(type.getErrCode(), type.getErrMsg());
    }

    public static BusinessException createInstance(BusinessErrorType type,
                                                   Exception e) {
        return new BusinessException(type.getErrCode(), type.getErrMsg(), e);
    }

    public static BusinessException createInstance(BusinessErrorType type,
                                                   String errMsg) {
        return new BusinessException(type.getErrCode(), type.getErrMsg() + "["
                + errMsg + "]");
    }

    public static BusinessException createInstance(long errorCode,
                                                   String errorMsg) {
        return new BusinessException(errorCode, errorMsg);
    }

    public static BusinessException createInstance(long errCode, String errMsg, long innerErrCode, String innerErrMsg) {
        return new BusinessException(errCode, errMsg, innerErrCode, innerErrMsg);
    }

    public static BusinessException createInstance(int errCode, String errMsg, int innerErrCode, String innerErrMsg) {
        return new BusinessException(errCode, errMsg, innerErrCode, innerErrMsg);
    }

    public static BusinessException createInstance(int errorCode,
                                                   String errorMsg, Exception e) {
        return new BusinessException(errorCode, errorMsg, e);
    }

    public static BusinessException createEmptyInstance() {
        return new BusinessException();
    }

    public String toErrMsg() {
        return errCode + ":" + errMsg;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public void setErrCode(long errCode) {
        this.errCode = errCode;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public void setInnerErrCode(long innerErrCode) {
        this.innerErrCode = innerErrCode;
    }

    public void setInnerErrMsg(String innerErrMsg) {
        this.innerErrMsg = innerErrMsg;
    }

    public void setTargetException(Exception targetException) {
        this.targetException = targetException;
    }

    public long getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public long getInnerErrCode() {
        return innerErrCode;
    }

    public String getInnerErrMsg() {
        return innerErrMsg;
    }

    public Exception getTargetException() {
        return targetException;
    }
}
