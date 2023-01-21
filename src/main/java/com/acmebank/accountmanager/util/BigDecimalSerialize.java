package com.acmebank.accountmanager.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

public final class BigDecimalSerialize extends JsonSerializer<Object> {
    private BigDecimalSerialize(){}

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        BigDecimal bigDecimal = translateToBigDecimal(value);
        if (Objects.nonNull(value) && Objects.nonNull(bigDecimal)) {

            gen.writeString(bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString());
        } else {
            gen.writeString("");
        }
    }

    public static BigDecimal translateToBigDecimal(Object object) {
        BigDecimal bigDecimal = null;
        if (object instanceof BigDecimal) {
            bigDecimal = (BigDecimal) object;
        }
        return bigDecimal;
    }
}
