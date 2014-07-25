package com.osolve.thor.client;

import com.osolve.thor.BuildConfig;

import java.lang.reflect.Field;

/**
 * Created by Kros on 7/22/14.
 */
public class ThorConstant {

    public static final ServerType SERVER_TYPE;

    static {
        SERVER_TYPE = reflectServerType(ServerType.PRODUCTION);
    }

    private static final ServerType reflectServerType(final ServerType defaultValue) {
        try {
            final Field field = BuildConfig.class.getField("SERVER_TYPE");
            final Object value = field.get(null);
            if (value instanceof String) {
                return ServerType.valueOf((String) value);
            } else {
                throw new IllegalArgumentException("SERVER_TYPE field not String or null");
            }
        } catch (final NoSuchFieldException e) {
            return defaultValue;
        } catch (final IllegalAccessException e) {
            throw new RuntimeException("unexpected", e);
        }
    }
}
