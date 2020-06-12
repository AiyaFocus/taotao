package com.aiyafocus.taotao.common.utils;

import java.io.InputStream;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class SFtpUtils {
	private static Session session = null;
	private static Channel channel = null;

	//建立连接
	private static ChannelSftp getChannel(String hostname, int port, String username, String password, int timeout)
			throws JSchException {
		JSch jsch = new JSch(); // 创建JSch对象
		session = jsch.getSession(username, hostname, port); // 根据用户名，主机ip，端口获取一个Session对象
		session.setPassword(password); // 设置密码
		
		//设置不进行秘钥检查
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		
		session.setTimeout(timeout); // 设置timeout时间
		session.connect(); // 通过Session建立链接
		channel = session.openChannel("sftp"); // 打开SFTP通道
		channel.connect(); // 建立SFTP通道的连接
		return (ChannelSftp) channel;
	}

	//关闭连接
	private static void closeChannel() {
		if (channel != null) {
			channel.disconnect();
		}
		if (session != null) {
			session.disconnect();
		}
	}
	
	/**
	 * 上传文件
	 * @param hostname 远程文件服务器主机IP，注意：不是主机名称
	 * @param port SFTP连接使用的端口号（SFTP协议与SSH协议共用1个端口号，即22）
	 * @param username 远程文件服务器的连接用户名
	 * @param password 远程文件服务器的连接用户的密码
	 * @param timeout 设置连接超时的时间，注意：以毫秒为单位（ms）
	 * @param input 设置上传的文件的字节输入流
	 * @param filePath 设置文件上传的路径（
	 *                    注意上传的文件路径由两部分组成：
	 *                    1.父路径已经规定好，即Nginx服务器上的/docker/nginx/html；
	 *                    2.子路径是根据当前日期创建的文件上传的子路径，例如：当前日期为2020年6月8号，则子路径为“2020/6/8”）
	 * @param fileName 设置上传的文件名称（
	 *                    注意上传的文件名称由两部分组成：
	 *                    1.文件名称生成方法（当前时间的毫秒数+3位随机数）；
	 *                    2.上传的文件的后缀名）
	 * @throws Exception 出现异常，向上声明
	 */
	public static void uploadFile(String hostname, int port, String username, String password, int timeout,
			InputStream input, String filePath, String fileName) throws Exception {
		ChannelSftp con = getChannel(hostname, port, username, password, timeout);
		createDir(filePath, con);
		con.put(input, filePath+fileName);
		closeChannel();
	}
	//创建文件夹
	private static void createDir(String createpath, ChannelSftp sftp) {
		try {
			if (isDirExist(createpath, sftp)) {
				sftp.cd(createpath);
				return;
			}
			String[] pathArry = createpath.split("/");
			StringBuilder filePath = new StringBuilder("/");
			for (String path : pathArry) {
				if (path.equals("")) {
					continue;
				}
				filePath.append(path).append("/");
				if (isDirExist(filePath.toString(), sftp)) {
					sftp.cd(filePath.toString());
				} else {
					// 建立目录
					sftp.mkdir(filePath.toString());
					// 进入并设置为当前目录
					sftp.cd(filePath.toString());
				}
			}
				sftp.cd(createpath);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
	//判断文件夹是否存在
	private static boolean isDirExist(String directory, ChannelSftp sftp) {
		boolean isDirExistFlag = false;
		try {
			SftpATTRS sftpATTRS = sftp.lstat(directory);
			isDirExistFlag = true;
			return sftpATTRS.isDir();
		} catch (Exception e) {
			if (e.getMessage().toLowerCase().equals("no such file")) {
				isDirExistFlag = false;
			}
		}
		return isDirExistFlag;
	}

}