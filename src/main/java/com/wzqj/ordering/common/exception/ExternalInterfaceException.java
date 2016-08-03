package com.wzqj.ordering.common.exception;

/**
 * 调用外部接口时出现的异常, 包括SOA, http, udp等外界的服务
 */
public class ExternalInterfaceException extends RuntimeException {

    private String invokeResult = "";    //stub.invoke(req,resp)的返回值
    private String respCode = "";        //AO|http等外部服务返回的响应码

    /**
     * 构造函数
     *
     * @param invokeResult stub.invoke(req,resp)的返回值, 非idl时设为0
     * @param respCode     resp.result或http的respcode
     */
    public ExternalInterfaceException(int invokeResult, long respCode) {
        super();
        this.invokeResult = String.valueOf(invokeResult);
        this.respCode = String.valueOf(respCode);
    }

    /**
     * 构造函数
     *
     * @param invokeResult stub.invoke(req,resp)的返回值, 非idl时设为0
     * @param respCode     resp.result或http的respcode
     * @param message      resp.errMsg或其它自定义信息
     */
    public ExternalInterfaceException(int invokeResult, long respCode, String message) {
        super(message);
        this.invokeResult = String.valueOf(invokeResult);
        this.respCode = String.valueOf(respCode);
    }

    public ExternalInterfaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExternalInterfaceException(Throwable cause) {
        super(cause);
    }


    public String getInvokeResult() {
        return invokeResult;
    }

    public void setInvokeResult(String invokeResult) {
        this.invokeResult = invokeResult;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }
}
