package com.yyh.aicodegen.core.saver;

import cn.hutool.core.util.StrUtil;
import com.yyh.aicodegen.ai.model.HtmlCodeResult;
import com.yyh.aicodegen.ai.model.MultiFileCodeResult;
import com.yyh.aicodegen.exception.BusinessException;
import com.yyh.aicodegen.exception.ErrorCode;
import com.yyh.aicodegen.model.enums.CodeGenType;

public class MultiFileCodeSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult>{
    @Override
    protected CodeGenType getCodeGenType() {
        return CodeGenType.MULTI_FILE;
    }

    @Override
    protected void saveFile(MultiFileCodeResult result, String filePath) {
        writeToFile(filePath, "index.html", result.getHtmlCode());
        writeToFile(filePath, "script.js", result.getJsCode());
        writeToFile(filePath, "style.css", result.getCssCode());
    }

    @Override
    protected void validate(MultiFileCodeResult result) {
        super.validate(result);
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码内容为空");
        }
    }
}
