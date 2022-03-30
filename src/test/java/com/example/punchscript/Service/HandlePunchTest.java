package com.example.punchscript.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class HandlePunchTest {

    @Autowired
    HandlePunch handlePunch;

    @Autowired
    VenueReservationService venueReservationService;

    @Test
    void handlePunch() throws IOException {
        handlePunch.handle();
    }

    @Test
    void handle() throws UnirestException, JsonProcessingException {
        venueReservationService.handle();
    }
}