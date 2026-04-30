package com.yyh.aicodegen.core.parser;

import com.yyh.aicodegen.exception.BusinessException;
import com.yyh.aicodegen.exception.ErrorCode;
import com.yyh.aicodegen.model.enums.CodeGenType;

public class CodeParserExecutor {

    private static final HtmlCodeParser htmlParser = new HtmlCodeParser();
    private static final MultiFileCodeParser multiFileParser = new MultiFileCodeParser();

    /**
     * 代码解析器执行
     * @param content 原始内容
     * @param codeGenType 代码生成类型
     * @return 解析结果
     */
    public static Object executeParser(String content, CodeGenType codeGenType) {
        return switch (codeGenType) {
            case HTML -> htmlParser.codeParse(content);
            case MULTI_FILE -> multiFileParser.codeParse(content);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的类型: " + codeGenType.getType());
        };
    }
}
