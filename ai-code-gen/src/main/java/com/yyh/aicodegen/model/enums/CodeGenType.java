package com.yyh.aicodegen.model.enums;

import lombok.Getter;

@Getter
public enum CodeGenType {
    HTML("单HTML模式", "html"),
    MULTI_FILE("多文件模式", "multi_file");

    private String name;
    private final String type;

    CodeGenType(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public static CodeGenType getCodeGenType(String type) {
        if (type == null) {
            return null;
        }
        for (CodeGenType code : CodeGenType.values()) {
            if (type.equals(code.type)) {
                return code;
            }
        }
        return null;
    }
}
