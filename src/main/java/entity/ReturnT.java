package entity;

/*
* 返回类
* */
public class ReturnT {
    private static final int SUCCESS_CODE = 200;
    private static final int ERROR_CODE = 500;
    private static final String SUCCESS_MSG ="SUCCESS";

    private int code;
    private String msg;
    private Object data;

    private ReturnT(){
    }
    private ReturnT(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
    private ReturnT(int code,String msg,Object data){
        this.msg=msg;
        this.code=code;
        this.data=data;
    }

    public ReturnT ok(){
        return new ReturnT(SUCCESS_CODE,SUCCESS_MSG);
    }
    public ReturnT ok(String msg){
        return new ReturnT(SUCCESS_CODE,msg);
    }
    public ReturnT ok(Object data){
        return new ReturnT(SUCCESS_CODE,SUCCESS_MSG, data);
    }
    public ReturnT ok(int code,String msg){
        return new ReturnT(code,msg);
    }

    public ReturnT error(){
        return new ReturnT(ERROR_CODE,"ERROR");
    }
    public ReturnT error(String msg){
        return new ReturnT(ERROR_CODE,msg);
    }
    public ReturnT error(int code,String msg){
        return new ReturnT(code,msg);
    }
    public ReturnT error(Object data){
        return new ReturnT(ERROR_CODE,"ERROR",data);
    }

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return code;
    }
    public void setMsg(String msg){
        this.msg=msg;
    }
    public String getMsg(){
        return msg;
    }
    public void setData(Object data){
        this.data=data;
    }
    public Object getData(){
        return data;
    }
}
