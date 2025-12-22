package com.yyh.aicodegen.core.saver;

import com.yyh.aicodegen.ai.model.HtmlCodeResult;
import com.yyh.aicodegen.ai.model.MultiFileCodeResult;
import com.yyh.aicodegen.exception.BusinessException;
import com.yyh.aicodegen.exception.ErrorCode;
import com.yyh.aicodegen.model.enums.CodeGenType;

import java.io.File;

public class CodeFileSaverExecutor {

    private static final HtmlCodeSaverTemplate htmlCodeSaver = new HtmlCodeSaverTemplate();
    private static final MultiFileCodeSaverTemplate multiFileCodeSaver = new MultiFileCodeSaverTemplate();

    /**
     * 代码保存执行
     * @param result 代码结果对象
     * @param codeGenType 代码生成类型
     * @return 文件保存目录
     */
    public static File executeSaver(Object result, CodeGenType codeGenType) {
        return switch (codeGenType) {
            case HTML -> htmlCodeSaver.saveCode((HtmlCodeResult) result);
            case MULTI_FILE -> multiFileCodeSaver.saveCode((MultiFileCodeResult) result);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的类型: " + codeGenType.getType());
        };
    }
}
