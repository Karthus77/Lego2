package com.example.lego;

public class good_creat {
    /**
     * code : 200
     * data : {"good":{"good_id":55,"user_id":4,"quantity":50,"name":"县求受得","price":42,"info":"tempor","img":"http://dummyimage.com/400x400"}}
     * msg : 创建成功
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
         * good : {"good_id":55,"user_id":4,"quantity":50,"name":"县求受得","price":42,"info":"tempor","img":"http://dummyimage.com/400x400"}
         */

        private GoodBean good;

        public GoodBean getGood() {
            return good;
        }

        public void setGood(GoodBean good) {
            this.good = good;
        }

        public static class GoodBean {
            /**
             * good_id : 55
             * user_id : 4
             * quantity : 50
             * name : 县求受得
             * price : 42
             * info : tempor
             * img : http://dummyimage.com/400x400
             */

            private int good_id;
            private int user_id;
            private int quantity;
            private String name;
            private int price;
            private String info;
            private String img;

            public int getGood_id() {
                return good_id;
            }

            public void setGood_id(int good_id) {
                this.good_id = good_id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }
    }
}
