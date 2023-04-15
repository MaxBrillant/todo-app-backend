package com.ndashimye.firstapp;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Converter(autoApply = true)
public class ZonedDateTimeAttributeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    static ZoneId utcZoneId = ZoneId.of("UTC");
    private static ZoneId defaultZoneId = ZoneId.of("UTC");

    public static void setDefaultZoneId(ZoneId zoneId) {
        defaultZoneId = zoneId;
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
//
//    public static ZonedDateTime convertToUserTimeZone(ZonedDateTime zonedDateTime, Integer userId) throws UserNotFoundException, UserSettingsNotFoundException {
//        UserService userService = new UserService();
//        return zonedDateTime.withZoneSameInstant(ZoneId.of(userService.getUserSettingsByUserId(userId).getTimeZone()));
//    }

    public static ZonedDateTime toDefaultZoneId(ZonedDateTime zonedDateTime){
        return zonedDateTime.withZoneSameInstant(defaultZoneId);
    }
}
