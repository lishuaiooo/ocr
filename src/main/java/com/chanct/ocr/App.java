package com.chanct.ocr;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;

/**
 * Hello world!
 *
 */
public class App {


	//设置APPID/AK/SK
	public static final String APP_ID = "11590161";
	public static final String API_KEY = "VQeh2xjvfQzWu29iP9eDw1Gj";
	public static final String SECRET_KEY = "WHuhOejMwkFh6lh0ACWtCZ5Z6mjUsGyS";

	int[] coordinate = new int[4];

	
	public void snapShot() {
		try {
			// *** 核心代码 *** 拷贝屏幕到一个BufferedImage对象screenshot
			BufferedImage screenshot = (new Robot()).createScreenCapture(new Rectangle(990, 135, 297, 66));
			// 将screenshot对象写入图像文件
//			ImageIO.write(screenshot, "png", f);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	

	public static void main( String[] args ) {
		//	adb shell screencap -p /sdcard/picname.png
		//	adb pull /sdcard/picname.png D:/
		long start = System.currentTimeMillis();


		
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		// 初始化一个AipOcr
		AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
		// 调用接口
		JSONObject res = client.basicGeneral(out.toByteArray(), new HashMap<String, String>());
		//获取题目
		JSONArray jsonArray = res.getJSONArray("words_result");
		StringBuffer buffer = new StringBuffer();
		for (Object obj : jsonArray) {
			JSONObject jsonObject = (JSONObject) obj;
			buffer.append(jsonObject.getString("words"));
		}
		System.out.println(buffer.toString());
		
		
		
		long end = System.currentTimeMillis();
		System.out.println(end -start);
	}


}
