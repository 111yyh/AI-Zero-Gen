package com.yyh.aicodegen.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.yyh.aicodegen.exception.BusinessException;
import com.yyh.aicodegen.exception.ErrorCode;
import com.yyh.aicodegen.model.enums.CodeGenType;

import java.io.File;
import java.nio.charset.StandardCharsets;

public abstract class CodeFileSaverTemplate<T> {

    // 文件根目录
    private static final String ROOT_DIR = System.getProperty("user.dir") + "/tmp/output";

    /**
     * 模板方法: 保存代码的标准流程
     * @param result 代码结果对象
     * @return 保存的文件
     */
    public final File saveCode(T result) {
        // 校验 result
        validate(result);
        // 创建唯一标识目录 （雪花ID）
        String baseDir = createUniqueFile();
        // 保存文件
        saveFile(result, baseDir);
        return new File(baseDir);
    }

    /**
     * 验证代码结果对象(可被子类覆写)
     * @param result 代码结果对象
     */
    protected void validate(T result){
        if (result == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "结果对象不能为空");
        }
    };

    // 创建唯一标识目录 （雪花ID）
    private String createUniqueFile() {
        String codeType = getCodeGenType().getType();
        String uniqueName = StrUtil.format("{}_{}", codeType, IdUtil.getSnowflakeNextIdStr());
        String baseDir = ROOT_DIR + File.separator + uniqueName;
        FileUtil.mkdir(baseDir);
        return baseDir;
    }

    // 写入单个文件
    protected final void writeToFile(String fileDir, String fileName, String content) {
        String filePath = fileDir + File.separator + fileName;
        FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
    }

    /**
     * 获取代码类型
     * @return
     */
    protected abstract CodeGenType getCodeGenType();

    /**
     * 保存文件
     * @param result
     * @param filePath
     */
    protected abstract void saveFile(T result, String filePath);
}
