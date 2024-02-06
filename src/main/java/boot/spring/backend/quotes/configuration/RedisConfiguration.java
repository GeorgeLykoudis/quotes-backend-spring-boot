package boot.spring.backend.quotes.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.time.Duration;

@EnableCaching
@Configuration
public class RedisConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfiguration.class);

  @Value("${redis.hostname}")
  private String hostName;

  @Value("${redis.port}")
  private int port;

  @Bean
  public RedisConnectionFactory jedisConnectionFactory() {

    RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
    redisConfig.setHostName(hostName);
    redisConfig.setPort(port);

    final JedisClientConfiguration jedisConfig = JedisClientConfiguration.builder()
        .connectTimeout(Duration.ofSeconds(60))
        .build();

    JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisConfig, jedisConfig);
    LOGGER.info("Redis being initialized");
    return jedisConnectionFactory;
  }
}
