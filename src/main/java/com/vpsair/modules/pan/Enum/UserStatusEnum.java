package com.vpsair.modules.pan.Enum;

import lombok.Getter;

/**
 * 用户状态枚举类
 * @author Shen && syf0412@vip.qq.com
 * @date 2021/11/16 22:22
 */
@Getter
public enum UserStatusEnum {
    ADMIN(2, "管理员"),
    MEMBER(3, "注册用户"),
    BLOCKED(4, "封禁用户"),
    VIP(5, "超级会员"),
    ;
    private Integer code;
    private String desc;

    UserStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
