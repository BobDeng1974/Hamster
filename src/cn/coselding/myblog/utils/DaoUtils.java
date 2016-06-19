package cn.coselding.myblog.utils;

import java.util.UUID;

public class DaoUtils {

	/**
	 * 获取一个UUID字符串
	 *
	 * @return返回的UUID字符串
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
}
