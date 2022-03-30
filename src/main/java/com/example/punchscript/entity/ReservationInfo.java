package com.example.punchscript.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationInfo {
    private String placeId;
    private String type;
    private String subDate;
    private String subTime;
    private String endTime;
    private String subHours;
    private String title;
}
