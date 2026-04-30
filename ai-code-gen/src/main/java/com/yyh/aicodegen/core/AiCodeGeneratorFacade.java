package com.yyh.aicodegen.core;

import com.yyh.aicodegen.ai.AiCodeGeneratorService;
import com.yyh.aicodegen.ai.model.HtmlCodeResult;
import com.yyh.aicodegen.ai.model.MultiFileCodeResult;
import com.yyh.aicodegen.core.parser.CodeParserExecutor;
import com.yyh.aicodegen.core.saver.CodeFileSaverExecutor;
import com.yyh.aicodegen.exception.BusinessException;
import com.yyh.aicodegen.exception.ErrorCode;
import com.yyh.aicodegen.model.enums.CodeGenType;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

@Service
@Slf4j
public class AiCodeGeneratorFacade {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    /**
     * 统一入口：生成并保存代码
     * @param userMessage
     * @param codeGenType
     * @return
     */
    public File generateAndSaveCode(String userMessage, CodeGenType codeGenType) {
        if (codeGenType == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "CodeGenType 类型为空");
        }
        return switch (codeGenType) {
            case HTML -> {
                HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generatorHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(htmlCodeResult, CodeGenType.HTML);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generatorMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(multiFileCodeResult, CodeGenType.MULTI_FILE);
            }
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的类型: " + codeGenType.getType());
        };
    }

    /**
     * 统一入口：生成并保存代码 （流式输出）
     * @param userMessage
     * @param codeGenType
     * @return
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenType codeGenType) {
        if (codeGenType == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "CodeGenType 类型为空");
        }
        return switch (codeGenType) {
            case HTML -> {
                Flux<String> res = aiCodeGeneratorService.generatorHtmlCodeStream(userMessage);
                yield processCodeStream(res, CodeGenType.HTML);
            }
            case MULTI_FILE -> {
                Flux<String> res = aiCodeGeneratorService.generatorMultiFileCodeStream(userMessage);
                yield processCodeStream(res, CodeGenType.MULTI_FILE);
            }
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的类型: " + codeGenType.getType());
        };
    }

    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenType codeGenType) {
        StringBuilder sb = new StringBuilder();
        return codeStream.doOnNext(chunk -> {
            // 实时拼接字符片段
            sb.append(chunk);
        })
        .doOnComplete(() -> {
            try {
                String completedCode = sb.toString();
                Object parseCodeResult = CodeParserExecutor.executeParser(completedCode, codeGenType);
                File savedFile = CodeFileSaverExecutor.executeSaver(parseCodeResult, codeGenType);
                log.info("保存成功, 文件路径: {}", savedFile.getAbsolutePath());
            } catch (Exception e) {
                log.error("保存失败: {}", e.getMessage());
            }
        });
    }
}
