package com.aiyafocus.taotao.common.bo;

/**
 * 业务逻辑的基础类
 *
 * @author AiyaFocus
 * createDate 2020/5/31 15:47
 */
public class BaseResult {

    private Integer status; // 返回的状态码
    private Object data; // 返回的对象

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // 带参构造方法
    private BaseResult(Integer status, Object data) {
        this.status = status;
        this.data = data;
    }

    // 无参构造方法
    public BaseResult() {
    }

    // 写两个静态方法：ok和error供Controller直接调用
    public static BaseResult ok(Object object){
        return new BaseResult(200, object);
    }
    public static BaseResult error(Object object){
        return new BaseResult(400, object);
    }

}
