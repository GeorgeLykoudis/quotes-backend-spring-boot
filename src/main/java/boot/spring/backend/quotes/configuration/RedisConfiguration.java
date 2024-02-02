package boot.spring.backend.quotes.configuration;

import boot.spring.backend.quotes.model.QuoteEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@EnableCaching
@Configuration
public class RedisConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfiguration.class);

  @Value("${redis.hostname}")
  private String hostName;

  @Value("${redis.port}")
  private int port;

  @Value("${redis.ttl.in.minutes}")
  private int ttl;

  @Bean
  public RedisConnectionFactory jedisConnectionFactory() {

    RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
    redisConfig.setHostName(hostName);
    redisConfig.setPort(port);

    final JedisClientConfiguration jedisConfig = JedisClientConfiguration.builder()
        .connectTimeout(Duration.ofSeconds(60))
//        .usePooling()
        .build();

    JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisConfig, jedisConfig);
    LOGGER.info("Redis being initialized");
    return jedisConnectionFactory;
  }

  @Bean
  public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
    return RedisCacheManager.builder(redisConnectionFactory)
        .cacheDefaults(RedisCacheConfiguration
            .defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(ttl))
            .disableCachingNullValues())
        .build();
  }

  @Bean
  public RedisTemplate<String, QuoteEntity> redisTemplate() {
    RedisTemplate<String, QuoteEntity> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(jedisConnectionFactory());
    return redisTemplate;
  }
}
