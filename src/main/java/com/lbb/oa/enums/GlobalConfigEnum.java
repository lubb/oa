package com.lbb.oa.enums;

public class GlobalConfigEnum {

    public enum ResponseCode {
        SUCCESS(0, "成功"),
        ERROR(1, "错误"),
        UNLOGIN(2,"用户未登录"),
        ONLINE(3,"用户已经登录");

        private final int code;
        private final String desc;

        ResponseCode(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum  RoleStatusEnum {
        DISABLE(0),
        AVAILABLE(1);

        private int statusCode;
        RoleStatusEnum(int statusCode) {
            this.statusCode = statusCode;
        }
        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
    }

    /**
     * 用户状态
     * @Author lubingbing
     * @Date 2020/5/29 12:29
     * @Version 1.0
     **/
    public enum  UserStatusEnum {

        DISABLE(0),
        AVAILABLE(1);

        private int statusCode;

        UserStatusEnum(int statusCode) {
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
    }

    /**
     * 用户类型
     * @Author lubingbing
     * @Date 2020/5/29 12:25
     * @Version 1.0
     **/
    public enum UserTypeEnum {

        SYSTEM_ADMIN(0),//系统管理员admin

        SYSTEM_USER(1);//系统的普通用户

        private int typeCode;

        UserTypeEnum(int typeCode) {
            this.typeCode = typeCode;
        }

        public int getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(int typeCode) {
            this.typeCode = typeCode;
        }
    }
}
