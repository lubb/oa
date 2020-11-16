package com.lbb.oa.util;

public class GlobalConfig {

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

    public enum UserTypeEnum{
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
