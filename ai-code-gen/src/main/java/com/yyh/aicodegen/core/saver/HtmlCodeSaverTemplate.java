package com.yyh.aicodegen.core.saver;

import cn.hutool.core.util.StrUtil;
import com.yyh.aicodegen.ai.model.HtmlCodeResult;
import com.yyh.aicodegen.exception.BusinessException;
import com.yyh.aicodegen.exception.ErrorCode;
import com.yyh.aicodegen.model.enums.CodeGenType;

public class HtmlCodeSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult>{

    @Override
    protected CodeGenType getCodeGenType() {
        return CodeGenType.HTML;
    }

    @Override
    protected void saveFile(HtmlCodeResult result, String filePath) {
        writeToFile(filePath, "index.html", result.getHtmlCode());
    }

    @Override
    protected void validate(HtmlCodeResult result) {
        super.validate(result);
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码内容为空");
        }
    }
}
