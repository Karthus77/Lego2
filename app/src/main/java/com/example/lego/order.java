package com.example.lego;

import java.util.List;

public class order {
    /**
     * code : 200
     * data : {"orders":[{"ID":2,"CreatedAt":"2020-12-10T11:08:31Z","UpdatedAt":"2020-12-10T11:08:31Z","DeletedAt":null,"user_id":4,"good_id":1,"goods_count":1,"goods_price":12},{"ID":3,"CreatedAt":"2020-12-10T11:10:48Z","UpdatedAt":"2020-12-10T11:10:48Z","DeletedAt":null,"user_id":4,"good_id":2,"goods_count":3,"goods_price":138}]}
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
        private List<OrdersBean> orders;

        public List<OrdersBean> getOrders() {
            return orders;
        }

        public void setOrders(List<OrdersBean> orders) {
            this.orders = orders;
        }

        public static class OrdersBean {
            /**
             * ID : 2
             * CreatedAt : 2020-12-10T11:08:31Z
             * UpdatedAt : 2020-12-10T11:08:31Z
             * DeletedAt : null
             * user_id : 4
             * good_id : 1
             * goods_count : 1
             * goods_price : 12
             */

            private int ID;
            private String CreatedAt;
            private String UpdatedAt;
            private Object DeletedAt;
            private int user_id;
            private int good_id;
            private int goods_count;
            private int goods_price;

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public String getCreatedAt() {
                return CreatedAt;
            }

            public void setCreatedAt(String CreatedAt) {
                this.CreatedAt = CreatedAt;
            }

            public String getUpdatedAt() {
                return UpdatedAt;
            }

            public void setUpdatedAt(String UpdatedAt) {
                this.UpdatedAt = UpdatedAt;
            }

            public Object getDeletedAt() {
                return DeletedAt;
            }

            public void setDeletedAt(Object DeletedAt) {
                this.DeletedAt = DeletedAt;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getGood_id() {
                return good_id;
            }

            public void setGood_id(int good_id) {
                this.good_id = good_id;
            }

            public int getGoods_count() {
                return goods_count;
            }

            public void setGoods_count(int goods_count) {
                this.goods_count = goods_count;
            }

            public int getGoods_price() {
                return goods_price;
            }

            public void setGoods_price(int goods_price) {
                this.goods_price = goods_price;
            }
        }
    }
}
