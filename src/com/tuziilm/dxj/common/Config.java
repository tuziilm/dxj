package com.tuziilm.dxj.common;

import com.google.common.collect.Sets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

/**
 * 集中定义一些常量
 * @author <a href="tuziilm@163.com">tuziilm</a>
 *
 */
public final class Config {
	public final static String SECURITLY_KEY = "5adxj";
    private final static Random rand = new Random(System.currentTimeMillis());
	public final static Properties props=new Properties();
	static{
		try {
			props.load(Resources.getResource("config.properties").openStream());
		} catch (IOException e) {
			System.exit(1);
		}
	}
	/** 文件上传根路径*/
	public final static String UPLOAD_ROOT_DIR=props.getProperty("upload.root.dir");
    public final static String APK_DECODE_DIR=UPLOAD_ROOT_DIR+"/"+".apk";
	public final static String DOMAIN=props.getProperty("domain");
    public final static String HOST_URL="http://"+DOMAIN;
	public final static String APP_NAME=props.getProperty("app.name");
    public static String[] DOWNLOAD_URL=props.getProperty("download.url").split("\\s+");
    public final static Set<String> TEST_IDENTITIES= Sets.newHashSet("460019002606393","460028174372880","460028998577977");

    public final static String randomDownloadURL(){
        if (DOWNLOAD_URL==null || DOWNLOAD_URL.length<1){
            return HOST_URL;
        }
        return DOWNLOAD_URL[rand.nextInt(DOWNLOAD_URL.length)];
    }

    public final static String randomDownloadURL(String prefix, String path){
        if(path==null){
            return "";
        }
        return randomDownloadURL()+prefix+path;
    }
    /**
     * 获得配置文件中某个参数的值
     * @author tuziilm
     * @param paramName
     * @return
     * 2015年2月6日
     */
    public static String getParam(String paramName){
		return props.getProperty(paramName);
	}
}
