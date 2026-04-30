package com.yyh.aicodegen.ai;

import com.yyh.aicodegen.ai.model.HtmlCodeResult;
import com.yyh.aicodegen.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generatorHtmlCode() {
        HtmlCodeResult res = aiCodeGeneratorService.generatorHtmlCode("帮我用20行代码生成一个简易计算器");
        Assertions.assertNotNull(res);
    }

    @Test
    void generatorMultiFileCode() {
        MultiFileCodeResult res = aiCodeGeneratorService.generatorMultiFileCode("帮我用20行代码生成一个简易留言板");
        Assertions.assertNotNull(res);
    }
}