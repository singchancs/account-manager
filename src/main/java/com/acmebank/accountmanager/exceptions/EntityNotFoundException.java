package com.acmebank.accountmanager.exceptions;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class EntityNotFoundException extends RuntimeException {

    private final String errorCode;

    public EntityNotFoundException(Class clazz, String errorCode, String... searchParamsMap) {
        super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), errorCode, toMap(String.class, String.class, searchParamsMap)));
        this.errorCode = errorCode;
    }

    private static String generateMessage(String entity, String errorCode, Map<String, String> searchParams) {
        return "[" + errorCode + "] " + StringUtils.capitalize(entity) +
                " was not found for parameters " +
                searchParams;
    }

    private static <K, V> Map<K, V> toMap(
            Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
