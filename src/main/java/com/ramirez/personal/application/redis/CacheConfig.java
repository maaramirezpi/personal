package com.ramirez.personal.application.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Value;
import io.vavr.control.Option;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

  public static final String CACHE_CUSTOMER = "personal::customer";

  @Bean
  @Primary
  public RedisCacheManager redisCacheManager(
      RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {

    objectMapper =
        objectMapper
            .copy()
            .activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);

    RedisCacheConfiguration cacheDefaultConfig =
        RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofSeconds(20))
            .disableCachingNullValues()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new JsonRedisSerializer(objectMapper)));

    return RedisCacheManager.builder(redisConnectionFactory)
        .withCacheConfiguration(CACHE_CUSTOMER, cacheDefaultConfig)
        .build();
  }

  /*@Bean
  @Qualifier("assetDiagnosticsQueryCustomCacheKeyGenerator")
  public KeyGenerator assetDiagnosticsQueryCustomCacheKeyGenerator() {
    return (o, method, objects) ->
        ((AssetDiagnosticsQuery) objects[0]).toString().replace(" ", "_");
  }*/

  @RequiredArgsConstructor
  static class JsonRedisSerializer implements RedisSerializer<Value<Object>> {

    private final ObjectMapper om;

    @Override
    public byte[] serialize(Value<Object> t) throws SerializationException {
      try {
        return om.writeValueAsBytes(t);
      } catch (JsonProcessingException e) {
        throw new SerializationException(e.getMessage(), e);
      }
    }

    @Override
    public Value<Object> deserialize(byte[] bytes) throws SerializationException {

      if (bytes == null) {
        return null;
      }

      try {
        Object o = om.readValue(bytes, Object.class);
        Option<Object> right = Option.of(o);
        return right;
      } catch (Exception e) {
        throw new SerializationException(e.getMessage(), e);
      }
    }
  }
}
