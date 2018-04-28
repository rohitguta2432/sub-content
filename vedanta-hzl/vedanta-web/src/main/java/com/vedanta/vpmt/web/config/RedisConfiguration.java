package com.vedanta.vpmt.web.config;

//@Configuration
public class RedisConfiguration {

	/*
	 * @Value("${redis.port}") private int redisPort;
	 * 
	 * @Bean
	 * 
	 * @DependsOn("redisServerUtil") public JedisConnectionFactory
	 * jedisConnectionFactory() { JedisConnectionFactory factory = new
	 * JedisConnectionFactory(); factory.setHostName("localhost");
	 * factory.setPort(redisPort);
	 * 
	 * return factory; }
	 * 
	 * @Bean public RedisTemplate<String, Object> redisTemplate() { final
	 * RedisTemplate<String, Object> template = new RedisTemplate<String,
	 * Object>(); template.setConnectionFactory(jedisConnectionFactory());
	 * 
	 * template.setKeySerializer(new StringRedisSerializer());
	 * template.setHashKeySerializer(new StringRedisSerializer());
	 * template.setHashValueSerializer(new
	 * GenericToStringSerializer<T>(T.class)); template.setValueSerializer(new
	 * GenericToStringSerializer<T>(T.class));
	 * 
	 * 
	 * template.setHashValueSerializer(new
	 * GenericToStringSerializer<Object>(Object.class));type
	 * template.setValueSerializer(new
	 * GenericToStringSerializer<Object>(Object.class));
	 * 
	 * return template; }
	 */
}
