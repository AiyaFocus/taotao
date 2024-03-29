package com.aiyafocus.taotao.common.utils;

import java.util.Calendar;
import java.util.Random;

/**
 * 各种id生成策略
 * <p>Title: IDUtils</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.com</p> 
 * @author	入云龙
 * @date	2015年7月22日下午2:32:10
 * @version 1.0
 */
public class IDUtils {

	/**
	 * 图片名生成
	 */
	public static String genImageName() {
		//取当前时间的长整形值包含毫秒
		long millis = System.currentTimeMillis();
		//long millis = System.nanoTime();
		//加上三位随机数
		Random random = new Random();
		int end3 = random.nextInt(999);
		//如果不足三位前面补0
		String str = millis + String.format("%03d", end3);
		
		return str;
	}
	
	/**
	 * 商品id生成
	 */
	public static long genItemId() {
		//取当前时间的长整形值包含毫秒
		long millis = System.currentTimeMillis();
		//long millis = System.nanoTime();
		//加上两位随机数
		Random random = new Random();
		int end2 = random.nextInt(99);
		//如果不足两位前面补0
		String str = millis + String.format("%02d", end2);
		long id = new Long(str);
		return id;
	}

	/**
	 * 根据当前日期创建文件上传子路径
	 * @return 返回创建的文件上传子路径
	 */
	public static String createDateDir(){
		Calendar calendar = Calendar.getInstance();  // 获得Calendar对象
		int year = calendar.get(Calendar.YEAR);  // 获得当前时间的年份
		int month = calendar.get(Calendar.MONTH)+1;  // 获得当前时间的月份，注意Calendar得到的月份为0-11，所以需要手动加1
		int day = calendar.get(Calendar.DATE);  // 获得当前时间的日期

		return year+"/"+month+"/"+day+"/";
	}

}
