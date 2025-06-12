package com.kbhc.boot.user.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kbhc.boot.user.controller.dto.deserializer.OffsetDateTimeDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class WalkDto {
    @Getter
    @ToString
    public static class Request {
        private UUID recordkey;
        private Data data;
        private String type;
        @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
        private OffsetDateTime lastUpdate;
    }

    @ToString
    @Getter
    public static class Data {
        private String memo;
        private List<Entry> entries;
        private Source source;
    }

    @ToString
    @Getter
    public static class Entry {

        private BigDecimal steps;
        private Period period;
        private Distance distance;
        private Calories calories;
    }

    @ToString
    @Getter
    public static class Period {
        @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
        private OffsetDateTime from;
        @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
        private OffsetDateTime to;
    }

    @ToString
    @Getter
    public static class Distance {
        private BigDecimal value;
        private String unit;
    }

    @ToString
    @Getter
    public static class Calories {
        private BigDecimal value;
        private String unit;
    }

    @ToString
    @Getter
    public static class Source {
        private String type;
        private int mode;
        private String name;
        private Product product;
    }

    @ToString
    @Getter
    public static class Product {
        private String name;
        private String vender;
    }

}
