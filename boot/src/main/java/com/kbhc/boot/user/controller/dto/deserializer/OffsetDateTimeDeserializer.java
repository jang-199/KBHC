package com.kbhc.boot.user.controller.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

    private static final List<DateTimeFormatter> SUPPORTED_FORMATS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    );

    /***
     * Input Data의 시간을 OffsetDateTime으로 변환합니다.
     */
    @Override
    public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().trim();

        for (DateTimeFormatter formatter : SUPPORTED_FORMATS) {
            try {
                if (formatter.toString().contains("'T'") || formatter.toString().contains("Z")) {
                    return OffsetDateTime.parse(value, formatter);
                } else {
                    LocalDateTime ldt = LocalDateTime.parse(value, formatter);
                    return OffsetDateTime.of(ldt, ZoneOffset.UTC);
                }
            } catch (DateTimeParseException ignored) {}
        }

        throw new InvalidFormatException(p, "지원하지 않는 날짜 형식입니다: " + value, value, OffsetDateTime.class);
    }
}
