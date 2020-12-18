package com.example.lego;

public class good_img {
    /**
     * code : 200
     * data : {"hash":"df54baba5db2f4bc264a091f7c4e1e36"}
     * msg : 上传图片成功
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
         * hash : df54baba5db2f4bc264a091f7c4e1e36
         */

        private String hash;

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }
    }
}
