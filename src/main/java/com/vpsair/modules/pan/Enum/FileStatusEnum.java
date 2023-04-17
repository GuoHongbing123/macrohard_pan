package com.vpsair.modules.pan.Enum;

import lombok.Getter;

/**
 * 状态：1正常 2隐藏 3历史版本 4封禁 5禁止下载 6损坏
 */
@Getter
public enum FileStatusEnum {
    OK(1,"正常"),
    HIDE(2,"隐藏"),
    HISTORY(3,"历史版本"),
    BLOCK(4,"封禁"),
    FORBIDDEN(5,"禁止下载"),
    BROKEN(6,"损坏"),
    ;
    private Integer code;
    private String desc;
    FileStatusEnum(Integer code, String desc){
        this.code=code;
        this.desc=desc;
    }
}
