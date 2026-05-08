package com.JDBC.util;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();
    static {
        loadProperties();
    }
    private PropertiesUtil() {
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key); //Получаем PROPERTIES по ключу
    }

    private static void loadProperties() {
        try(var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("dp.properties")){
            PROPERTIES.load(inputStream);
            if (inputStream == null) {
                throw new RuntimeException("Файл не найден");
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла");
        }
    }
}
