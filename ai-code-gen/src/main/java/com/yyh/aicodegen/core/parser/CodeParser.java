package com.yyh.aicodegen.core.parser;

/**
 * 代码解析器策略接口
 * @param <T>
 */
public interface CodeParser<T> {

    /**
     * 解析代码内容
     * @param codeContent 原始内容
     * @return 解析后的结果对象
     */
    T codeParse(String codeContent);
}
