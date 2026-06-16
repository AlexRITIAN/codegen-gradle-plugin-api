package io.github.alexritian.codegen.converter;

import org.jetbrains.annotations.Nullable;
import org.jooq.Converter;
import org.jooq.impl.AbstractConverter;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author Too_young
 */
public class TimestampInstantConverter extends AbstractConverter<Timestamp, Instant> {

    public TimestampInstantConverter(Class<Timestamp> fromType, Class<Instant> toType) {
        super(fromType, toType);
    }

    @Override
    @Nullable
    public Instant from(@Nullable Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toInstant();
    }

    @Override
    @Nullable
    public Timestamp to(@Nullable Instant instant) {
        if (instant == null) {
            return null;
        }
        return Timestamp.from(instant);
    }

}
