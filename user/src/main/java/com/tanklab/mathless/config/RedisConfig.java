package com.tanklab.mathless.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableCaching
public class RedisConfig {

		// 自定义一个redisTemplate
		@Bean(name = "redisTemplate")
		public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
				RedisTemplate<String, Object> template = new RedisTemplate<>();
				//序列化
				FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
				// value值的序列化采用fastJsonRedisSerializer
				template.setValueSerializer(fastJsonRedisSerializer);
				template.setHashValueSerializer(fastJsonRedisSerializer);
				// 可以指明小范围白名单
				// ParserConfig.getGlobalInstance().addAccept("");
				// key的序列化采用StringRedisSerializer
				StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
				template.setKeySerializer(stringRedisSerializer);
				template.setHashValueSerializer(stringRedisSerializer);
				template.setConnectionFactory(redisConnectionFactory);
				return template;
		}
}

/**
 * Value 序列化
 * @param <T>
 */
class FastJsonRedisSerializer<T> implements RedisSerializer<T>{

		private final Class<T> clazz;

		FastJsonRedisSerializer(Class<T> clazz) {
				this.clazz = clazz;
		}

		//添加白名单
		static {
				ParserConfig.getGlobalInstance().addAccept("com.tanklab.mathless.pojo");
		}

		@Override
		public byte[] serialize(T t){
				if(t == null){
						return new byte[0];
				}
				return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(StandardCharsets.UTF_8);
		}

		@Override
		public T deserialize(byte[] bytes) {
				if(bytes == null || bytes.length <= 0){
						return null;
				}
				String str = new String(bytes, StandardCharsets.UTF_8);
				return JSON.parseObject(str, clazz);
		}
}

/**
 * 重写序列化器
 */
class StringRedisSerializer implements RedisSerializer<Object>{

		private final Charset charset;

		StringRedisSerializer() {
				this(StandardCharsets.UTF_8);
		}

		private StringRedisSerializer(Charset charset) {
				Assert.notNull(charset, "Charset must not be null!");
				this.charset = charset;
		}

		@Override
		public String deserialize(byte[] bytes) {
				return (bytes == null ? null : new String(bytes, charset));
		}

		@Override
		public byte[] serialize(Object object) {
				String string = JSON.toJSONString(object);
				if (StringUtils.isBlank(string)) {
						return null;
				}
				string = string.replace("\"", "");
				return string.getBytes(charset);
		}
}