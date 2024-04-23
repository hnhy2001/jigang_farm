package com.example.jingangfarmmanagement.uitl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.github.sisyphsu.dateparser.DateParserUtils;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {
    @SneakyThrows
    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        if (StringUtils.isEmpty(jsonParser.getText())) {
            return null;
        }
        return DateParserUtils.parseDateTime(jsonParser.getText()).atZone(ZoneId.systemDefault());
    }
}
