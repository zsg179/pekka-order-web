package com.pekka.order.alipayconfig;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016101500690808";

	// 商户私钥，您的PKCS8格式RSA2私钥
	public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCuOAVoD2/+ldGDZTLx3bjqpiH8nxtzZiR83VZW9cg7qLDe7DrLLgf8KJ3xsT1O07T3sV1rsBVQXS6/AWHdJwLI2298q63yb8DyjJ7HhwzrMkgeMW6g0GKvnwWnjU+7CWZN5+tNjjuisYY009NnGUb0F88GelcdRiVGtysxK6rT4TNB1CBkbVyQcBs9wSHJEnkRiEOuLzG+gchFu4tQkdpK3sXi9w4MA2aNoQ5GA+nPVGnqNXYbUYa0RepxV1SklSUfDvri3fS0tR8tX4ThoA/ge/c7t1HxoHNFP3ygEkOrLLwJNIrJHOrbr0B9VDOMIwcYlcBRu1AuInF8g6lMGtDzAgMBAAECggEASAA1E82VfoTSYthXFqiIvMlOMIZdJPMoz0X2YdgQGb/c4TSGE+SBPlBW/kU+zi4mk1uE/XV3SZBJOn1KwchWmUY/LCoTqKdQ+azuDg4dY7KTJY9OZj3ZGzXAc3+uXOFEm5vmnlXTBakSixCkRO6oaUbRs56qFBoiW7hlrFucrRWtfWjXLDACCdmyLJsAsdwmVzCaKQt0nbAJZCwGEk1hg/PWZVJeFOEtMEUxTmzTqyjD39NwxptWgw6tPrD+s9dR4aMGbNjuHrzeoTaG1mt/vinGuYsp8x+Hziu242GJwUkrJxugUCsmJc4CROE9PoLjo7+WmENXR0glAEOhFnM3kQKBgQDeKwp96uNt77c8gj6+9xVlkS5mzKkCiX2Fy0vHx8/QiNpg35UDAqF/aS1j7CTCATCIiO+V24iZqfCclYrEg0UKpjOZoeGqk3EwNvO07o+P1STJNHI30ZJa4KCZz0sVBg1gJayflcu0ru6xg/vHeddg9dqdFwAgXpqNLbWzJwpCiQKBgQDIv7vvAU+mkbcfCgXsP+njJKQ3WzC+dy6X3PnyG/bdk83pWtK5B/jAdjI5xnFCZwvMDSF2VSnhcHnq0eaeITMDfGSNXcM9QBjUgaTCK1sSn/bKyq0MJyE+wqt8DjNnNxrpmI1vQKFeZHC1qTV374y5ATaJPkkgrpjWCpN2IT5ImwKBgQCBJcIOmomksbHx4cC54AY3KaMwrQ4eZHBsY1ZlBzk9/x/7Kl2EB3Z8C1cQfAiOIoTlCb1Efv60XP56989YFvr+isj6KQl20Aj/PyKCbh9UPFILCIrQ+em7HFMftknuKbuCxcPIbRqiqQImfRPSzHgwbBquMN2o83WiVg0G3M3xCQKBgAPFTjNkE+nuDMGBf7D7VlpU4tzDUrwRIwfpN6yrh8yLrS937ENbefmrfrAFITV9GASiLoG5gcrhQPj/LlTEDOBA4uag3zz0SkMbmEupYtGLvaf7fJqg1YgHQg9XePG0YEYI/ocbcKyEOHXBEucI0dzbv/l9+QOrI+XJVxG9+ByHAoGAM+vmfdSmD0WRKwFcF9IkGvpUDD4dq/VOWGgB5XMe0mPy0uR0ziLg78Eg1cZyyJoGhRbo+u4dvaHYin/mGNil5ULzGGr/iMqODrKLq+/ye/FhBWCOjXNjTV5RhrLCjqBbF2VqATjbc8LQJ59SyyI47R9DlQeAEFIrEflBVUM2/6o=";

	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm
	// 对应APPID下的支付宝公钥。
	public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnqEPBCl3mwRaFS4UCYDzNnTU/b/GyKBnUdwwkCREERz/b7G0TPHroc0L8kwJL3mnrXxhjNHeSfJQT9PNi/y5nny2F2ISGSUMGgTBpu1EPWIqLVDpsPzcv5W/HA77lBFMSo7WforD9DDwpJhIZvOrQ5URy6GJME6xYBkhDUpclercpy0PJEfe6BFPHa4CevayiLFgUL1JM4XrgT+t5UytKlgZkvDMILHRY9UjKXO5qz695L80FYt51EKd3dBIAlJ5mcAM3TFqZIBM4KDuBajOHSIQR02cKOkeQhh1Bgj/WYiEYvebVNkjIIYVD5jRJu0U7dd7yvlGRVvBNpNO4ZQKGwIDAQAB";

	// 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://localhost:8091/alipayNotifyNotice.action";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://localhost:8091/alipayReturnNotice.action";

	// 签名方式
	public static String sign_type = "RSA2";

	// 字符编码格式
	public static String charset = "utf-8";

	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

	// 支付宝网关
	public static String log_path = "D:\\";

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	/**
	 * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
	 * 
	 * @param sWord
	 *            要写入日志里的文本内容
	 */
	public static void logResult(String sWord) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
			writer.write(sWord);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
