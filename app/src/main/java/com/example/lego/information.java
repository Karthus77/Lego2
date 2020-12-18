package com.example.lego;

public class information {
    /**
     * code : 200
     * data : {"user":{"user_id":21,"account":"Karthus77","password":"Dilhumar1025","name":"user48581","sex":true,"info":"","head":""}}
     * msg : 获取成功
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
         * user : {"user_id":21,"account":"Karthus77","password":"Dilhumar1025","name":"user48581","sex":true,"info":"","head":""}
         */

        private UserBean user;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * user_id : 21
             * account : Karthus77
             * password : Dilhumar1025
             * name : user48581
             * sex : true
             * info :
             * head :
             */

            private int user_id;
            private String account;
            private String password;
            private String name;
            private boolean sex;
            private String info;
            private String head;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public boolean isSex() {
                return sex;
            }

            public void setSex(boolean sex) {
                this.sex = sex;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }
        }
    }
}
