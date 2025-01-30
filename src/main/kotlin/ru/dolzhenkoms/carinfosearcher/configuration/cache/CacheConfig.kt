package ru.dolzhenkoms.carinfosearcher.configuration.cache

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.serializer.RedisSerializationContext
import ru.dolzhenkoms.carinfosearcher.configuration.properties.RedisCacheProperties
import ru.dolzhenkoms.carinfosearcher.model.dto.CarInfoDto

@Configuration
@EnableCaching
class CacheConfig(
    private val redisCacheProperties: RedisCacheProperties,
    private val objectMapper: ObjectMapper
) {

    @Bean
    fun redisCacheManager(
        lettuceConnectionFactory: LettuceConnectionFactory
    ): CacheManager {
        val customizer = RedisCacheManagerBuilderCustomizer { builder ->
            builder.disableCreateOnMissingCache()
                .addBdiHistoryCache()
                .addAuctionHistoryCache()
        }

        val builder = RedisCacheManagerBuilder.fromConnectionFactory(lettuceConnectionFactory)
        customizer.customize(builder)
        return builder.build()
    }

    private fun RedisCacheManagerBuilder.addBdiHistoryCache() =
        this.withCacheConfiguration(
            BDI_HISTORY,
            defaultRedisCacheConfiguration()
                .serializeValuesWith(
                    constructSerializationPair(
                        CarInfoDto::class.java,
                        objectMapper
                    )
                )
        )

    private fun RedisCacheManagerBuilder.addAuctionHistoryCache() =
        this.withCacheConfiguration(
            AUCTION_HISTORY,
            defaultRedisCacheConfiguration()
                .serializeValuesWith(
                    constructSerializationPair(
                        CarInfoDto::class.java,
                        objectMapper
                    )
                )
        )

    private fun defaultRedisCacheConfiguration() =
        RedisCacheConfiguration
            .defaultCacheConfig()
            .run {
                if (redisCacheProperties.allowNullValues) {
                    this
                } else {
                    this.disableCachingNullValues()
                }
            }
            .entryTtl(redisCacheProperties.expireTime)

    private fun <T> constructSerializationPair(
        clazz: Class<T>,
        objectMapper: ObjectMapper
    ): RedisSerializationContext.SerializationPair<T> = RedisSerializationContext.SerializationPair
        .fromSerializer(KotlinJackson2JsonSerializer(objectMapper, clazz))
}