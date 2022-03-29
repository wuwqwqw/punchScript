package com.example.punchscript.utils;

import com.example.punchscript.constant.PunchEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class SeleniumUtilTest {

    @Autowired
    SeleniumUtil seleniumUtil;

    @Test
    void getCookie() {
        String cookie = seleniumUtil.getCookie(PunchEnum.ZHAO_KAI.userName, PunchEnum.ZHAO_KAI.password);
        log.info("获取的Cookie：{}",cookie);
        assertNotNull(cookie);
    }
}