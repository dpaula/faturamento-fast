package com.portoitapoa.faturamentofast.util;

import com.google.gson.*;

import java.time.LocalDateTime;

/**
 * Utility class for handling JSON serialization and deserialization using Gson library.
 * It provides methods to convert JSON strings to Java objects and vice versa.
 * This class is specifically configured to handle {@link LocalDateTime} objects during the serialization
 * and deserialization process, ensuring that LocalDateTime instances are properly managed.
 *
 * @author Weliton Villain
 */
public class GsonUtils {

    /**
     * Instance of {@link Gson} configured with type adapters for {@link LocalDateTime}.
     */
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                    new JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context) ->
                    LocalDateTime.parse(json.getAsString()))
            .create();

    /**
     * Deserializes the specified JSON into an object of the specified class.
     * This method is particularly useful when dealing with JSON data that represents a specific type of object.
     *
     * @param json  the string from which the object is to be deserialized
     * @param clazz the class of T
     * @param <T>   the type of the desired object
     * @return an object of type T from the string. Returns {@code null} if {@code json} is {@code null}.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

}
