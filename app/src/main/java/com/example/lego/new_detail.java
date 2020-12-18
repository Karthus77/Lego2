package com.example.lego;


public class new_detail {
    /**
     * code : 200
     * data : {"good":{"good_id":1,"user_id":1,"quantity":83,"name":"些法为下","price":12,"info":"dolore eu cupidatat ullamco","img":"http://dummyimage.com/400x400"}}
     * msg : 查询成功
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
         * good : {"good_id":1,"user_id":1,"quantity":83,"name":"些法为下","price":12,"info":"dolore eu cupidatat ullamco","img":"http://dummyimage.com/400x400"}
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
             * good_id : 1
             * user_id : 1
             * quantity : 83
             * name : 些法为下
             * price : 12
             * info : dolore eu cupidatat ullamco
             * img : http://dummyimage.com/400x400
             */

            private int good_id;
            private int user_id;
            private int quantity;
            private String name;
            private String price;
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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
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

