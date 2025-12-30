package com.yyh.aicodegen.model.dto.app;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 初始化应用的prompt
     */
    private String initPrompt;
}
