package ru.dolzhenkoms.carinfosearcher.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

sealed class CacheProperties {
    open var allowNullValues: Boolean = true
    open var expireTime: Duration = Duration.ofDays(1)
}

@ConfigurationProperties("cache.redis")
class RedisCacheProperties : CacheProperties()