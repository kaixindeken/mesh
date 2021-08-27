package com.tanklab.mathless.utils;

/**
 * 生成 Redis 的 key
 */
public class RedisKeyUtil {

		private static final String SPLIT = ":";
		private static final String PREFIX_KAPTCHA = "kaptcha"; // 验证码
		private static final String PREFIX_TOKEN = "token"; // 登录凭证
		private static final String PREFIX_USER = "user"; // 用户信息凭证
		private static final String PREFIX_FILE = "file"; // 缓存用户信息

		// 登录凭证
		public static String getTokenKey(String token){
				return PREFIX_TOKEN + SPLIT + token;
		}

		// 登录验证码
		public static String getKaptchaKey(String owner){
				return PREFIX_KAPTCHA + SPLIT + owner;
		}

		// 用户信息缓存
		public static String getUserKey(String userId){
				return PREFIX_USER + SPLIT + userId;
		}

		// 用户文件缓存
		public static String getFileKey(String user, String path){
				return PREFIX_FILE + SPLIT + user + SPLIT + path;
		}

}
