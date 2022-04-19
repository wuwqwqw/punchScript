package com.example.punchscript.Service;

import com.example.punchscript.entity.ReservationInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class VenueReservationService {

    @Autowired
    AsyncService asyncService;

//    @Scheduled(cron = "0 0/25 * * * ?")
//    @Scheduled(cron = "50 59 6 31 12 ?")
    public void handle() throws UnirestException, JsonProcessingException {
        ReservationInfo build = ReservationInfo.builder()
                .placeId("1639988691662689")
                .type("2")
                .subDate("2022-04-01")
                .subTime("19:00")
                .endTime("21:00")
                .subHours("1")
                .title("1号场地")
                .build();
        asyncService.doHandle(build);
    }
}
