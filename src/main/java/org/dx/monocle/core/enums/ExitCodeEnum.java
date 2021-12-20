package org.dx.monocle.core.enums;

/**
 * @author Jimmy.li
 * @date 2021/12/9 11:03
 */
public enum ExitCodeEnum {
    CONFIG_ERROR(10001, "配置加载错误");

    ExitCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private int code;

    private String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
