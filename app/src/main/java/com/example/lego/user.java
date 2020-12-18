package com.example.lego;

public class user {
    /**
     * code : 200
     * data : {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOjAsImV4cCI6MTYwNzc1ODA1OSwiaWF0IjoxNjA3MTUzMjU5LCJpc3MiOiJUYW9CYW9TZXJ2ZXIiLCJzdWIiOiJ1c2VyIHRva2VuIn0.Wq6bA0oQemiAzISWLMCZOtMvpOLXImoKXc905_YZHkA"}
     * msg : 注册成功
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOjAsImV4cCI6MTYwNzc1ODA1OSwiaWF0IjoxNjA3MTUzMjU5LCJpc3MiOiJUYW9CYW9TZXJ2ZXIiLCJzdWIiOiJ1c2VyIHRva2VuIn0.Wq6bA0oQemiAzISWLMCZOtMvpOLXImoKXc905_YZHkA
         */

        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
