package com.wzqj.ordering.common.exception;


// 不建议在此处重新定义ErrCode，会导致不好管理，推荐使用ErrConstant
//此处使用ErrConstant中的errCode。
//@Deprecated注释影响原有代码
public enum BusinessErrorType {

    SUCCESSFUL(0, "successful");
    private long errCode;
    private String errMsg;

    private BusinessErrorType(long errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public long getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

}
