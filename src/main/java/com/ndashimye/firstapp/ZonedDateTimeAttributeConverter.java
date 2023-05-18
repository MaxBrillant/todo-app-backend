package com.ndashimye.firstapp;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Converter(autoApply = true)
public class ZonedDateTimeAttributeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    static ZoneId utcZoneId = ZoneId.of("UTC");
    static ZoneId defaultZoneId = ZoneId.systemDefault();
    public static ZoneId setDefaultZoneId(ZoneId zoneId) {
        defaultZoneId = zoneId;
        return zoneId;
    }

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        // Store always in UTC
        return (zonedDateTime == null ? null :
                Timestamp.valueOf(toUtcZoneId(zonedDateTime).toLocalDateTime()));
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
        // Read from database (stored in UTC) and return with the system default.
        return (sqlTimestamp == null ? null :
                toDefaultZoneId(sqlTimestamp.toLocalDateTime().atZone(utcZoneId)));
    }

    public static ZonedDateTime toUtcZoneId(ZonedDateTime zonedDateTime){
        return zonedDateTime.withZoneSameInstant(utcZoneId);
    }

    public static ZonedDateTime toDefaultZoneId(ZonedDateTime zonedDateTime){
        return zonedDateTime.withZoneSameInstant(defaultZoneId);
    }
}
