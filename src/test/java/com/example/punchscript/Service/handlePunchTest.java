package com.example.punchscript.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class HandlePunchTest {

    @Autowired
    HandlePunch handlePunch;

    @Test
    void handlePunch() throws IOException {
        handlePunch.handle();
    }
}