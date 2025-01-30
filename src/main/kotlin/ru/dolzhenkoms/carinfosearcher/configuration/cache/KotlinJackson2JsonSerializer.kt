package ru.dolzhenkoms.carinfosearcher.configuration.cache

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.SerializationException

class KotlinJackson2JsonSerializer<T> : RedisSerializer<T> {

    private val objectMapper: ObjectMapper
    private val javaType: JavaType

    constructor(objectMapper: ObjectMapper, clazz: Class<T>) {
        this.objectMapper = objectMapper
        this.javaType = TypeFactory.defaultInstance().constructType(clazz)
    }

    override fun serialize(value: T?): ByteArray {
        if (value == null) {
            return EMPTY_ARRAY
        }
        try {
            return objectMapper.writeValueAsBytes(value)
        } catch (ex: Exception) {
            throw SerializationException("Cannot write value", ex)
        }
    }

    override fun deserialize(bytes: ByteArray?): T? {
        if (bytes == null || bytes.isEmpty()) {
            return null
        }
        try {
            return objectMapper.readValue(bytes, javaType)
        } catch (ex: Exception) {
            throw SerializationException("Cannot read value", ex)
        }
    }

    companion object {
        val EMPTY_ARRAY = ByteArray(0)
    }
}