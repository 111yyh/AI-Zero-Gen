package com.yyh.aicodegen.model.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

@Getter
public enum UserRole {

    USER("用户", "user"),
    ADMIN("管理员", "admin");

    private final String text;
    private final String value;

    UserRole(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取用户角色
     * @param value
     * @return
     */
    public static UserRole getUserRole(String value) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        for (UserRole role : values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        return null;
    }
}
