package com.tanklab.mathless.constant;

/**
 * 登录所需要使用的常量
 */
public class RedisTimeConstant {

		// 默认验证码Cookie保存时间，两分钟
		public static int DEFAULT_COOKIE_EXPIRED_SECONDS = 60 * 5;

		// 一个token的有效时间
		public static int DEFAULT_TOKEN_ACCESS_SECONDS = 60000;

		// 默认的登录凭证超时时间（6小时）
		public static int DEFAULT_EXPIRED_SECONDS = 3600 * 6;

		// 记住我状态下的凭证超时时间（3天）
		public static int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 3;

		// 缓存用户信息的时间
		public static int USER_CACHE_SECONDS = 3600;
}
