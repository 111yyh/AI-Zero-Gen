package com.yyh.aicodegen.core;

import com.yyh.aicodegen.model.enums.CodeGenType;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void generateAndSaveHTMLCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("生成一个简易计算器，不超过30行代码", CodeGenType.HTML);
        Assertions.assertNotNull(file);
    }

    @Test
    void generateAndSaveMultiFileCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("生成一个简易计算器，不超过30行代码", CodeGenType.MULTI_FILE);
        Assertions.assertNotNull(file);
    }

    @Test
    void generateAndSaveMultiFileCodeStream() {
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeStream("生成一个简易计算器，不超过30行代码", CodeGenType.MULTI_FILE);
        List<String> res = codeStream.collectList().block();
        Assertions.assertNotNull(res);
        String completed = String.join("", res);
        Assertions.assertNotNull(completed);
    }
}