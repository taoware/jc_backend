package com.irengine.commons;

import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.Test;

public class ResizeImage {

	/**
	 * @param im
	 *            原始图像
	 * @param resizeTimes
	 *            需要缩小的倍数，缩小2倍为原来的1/2 ，这个数值越大，返回的图片越小
	 * @return 返回处理后的图像
	 */
	public BufferedImage resizeImage(BufferedImage im, float resizeTimes) {
		/* 原始图像的宽度和高度 */
		int width = im.getWidth();
		int height = im.getHeight();

		/* 调整后的图片的宽度和高度 */
		int toWidth = (int) (Float.parseFloat(String.valueOf(width)) / resizeTimes);
		int toHeight = (int) (Float.parseFloat(String.valueOf(height)) / resizeTimes);

		/* 新生成结果图片 */
		BufferedImage result = new BufferedImage(toWidth, toHeight,
				BufferedImage.TYPE_INT_RGB);

		result.getGraphics().drawImage(
				im.getScaledInstance(toWidth, toHeight,
						java.awt.Image.SCALE_SMOOTH), 0, 0, null);
		return result;
	}

	/**
	 * @param im
	 *            原始图像
	 * @param resizeTimes
	 *            倍数,比如0.5就是缩小一半,0.98等等double类型
	 * @return 返回处理后的图像
	 */
	public BufferedImage zoomImage(BufferedImage im, float resizeTimes) {
		/* 原始图像的宽度和高度 */
		int width = im.getWidth();
		int height = im.getHeight();

		/* 调整后的图片的宽度和高度 */
		int toWidth = (int) (Float.parseFloat(String.valueOf(width)) * resizeTimes);
		int toHeight = (int) (Float.parseFloat(String.valueOf(height)) * resizeTimes);

		/* 新生成结果图片 */
		BufferedImage result = new BufferedImage(toWidth, toHeight,
				BufferedImage.TYPE_INT_RGB);

		result.getGraphics().drawImage(
				im.getScaledInstance(toWidth, toHeight,
						java.awt.Image.SCALE_SMOOTH), 0, 0, null);
		return result;
	}

	/**
	 * @param path
	 *            要转化的图像的文件夹,就是存放图像的文件夹路径
	 * @param type
	 *            图片的后缀名组成的数组
	 * @return
	 */
	public List<BufferedImage> getImageList(String path, String[] type)
			throws IOException {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		for (String s : type) {
			map.put(s, true);
		}
		List<BufferedImage> result = new ArrayList<BufferedImage>();
		File[] fileList = new File(path).listFiles();
		for (File f : fileList) {
			if (f.length() == 0)
				continue;
			if (map.get(getExtension(f.getName())) == null)
				continue;
			// 将file注入到BufferedImage类中
			result.add(javax.imageio.ImageIO.read(f));
		}
		return result;
	}

	/**
	 * 把图片写到磁盘上
	 * 
	 * @param im
	 * @param path
	 *            eg: C://home// 图片写入的文件夹地址
	 * @param fileName
	 *            DCM1987.jpg 写入图片的名字
	 * @return
	 */
	public boolean writeToDisk(BufferedImage im, String path, String fileName) {
		File f = new File(path + fileName);
		String fileType = getExtension(fileName);
		if (fileType == null)
			return false;
		try {
			ImageIO.write(im, fileType, f);
			im.flush();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean writeHighQuality(String name,BufferedImage im, String fileFullPath) {
		try {
			/* 输出到文件流 */
			FileOutputStream newimage = new FileOutputStream(fileFullPath
					+ "\\" + name);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
			JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(im);
			/* 压缩质量 */
			jep.setQuality(1f, true);
			encoder.encode(im, jep);
			/* 近JPEG编码 */
			newimage.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 返回文件的文件后缀名
	 * 
	 * @param fileName
	 * @return
	 */
	public String getExtension(String fileName) {
		try {
			return fileName.split("\\.")[fileName.split("\\.").length - 1];
		} catch (Exception e) {
			return null;
		}
	}

	@Test
	public void test02(){
		String fileUrl="C:\\Users\\Administrator\\Desktop\\1\\12042022121790811[1].png";
		//12042022121790811[1]t.png
		String thumbnail=fileUrl.substring(fileUrl.lastIndexOf("\\")+1, fileUrl.lastIndexOf("."))+"t"+fileUrl.substring(fileUrl.lastIndexOf("."));
		System.out.println(thumbnail);
	}
	@Test
	public void test01() throws IOException {
		ResizeImage r = new ResizeImage();
		String fileUrl="C:\\Users\\Administrator\\Desktop\\1\\12042022121790811[1].png";
		String thumbnailName=fileUrl.substring(fileUrl.lastIndexOf("\\")+1, fileUrl.lastIndexOf("."))+"t"+fileUrl.substring(fileUrl.lastIndexOf("."));
		File file=new File(fileUrl);
		String outputFolder="C:\\Users\\Administrator\\Desktop\\2";
		String thumbnail=fileUrl.substring(fileUrl.lastIndexOf("\\"), fileUrl.lastIndexOf("."));
		BufferedImage image=javax.imageio.ImageIO.read(file);
		int toWidth=300;
		int toHight=(int) (image.getHeight()/(image.getWidth()/300.0));
		BufferedImage result = new BufferedImage(toWidth, toHight,
				BufferedImage.TYPE_INT_RGB);
		result.getGraphics().drawImage(
				image.getScaledInstance(toWidth, toHight,
						java.awt.Image.SCALE_SMOOTH), 0, 0, null);
		r.writeHighQuality(thumbnailName,result,outputFolder);
	}
	/**生成缩略图*/
	public static String createThumbnail(String fileUrl, String outputFolder) throws IOException {
		ResizeImage r = new ResizeImage();
		String thumbnailName=fileUrl.substring(fileUrl.lastIndexOf("/")+1, fileUrl.lastIndexOf("."))+"t"+fileUrl.substring(fileUrl.lastIndexOf("."));
		File file=new File(fileUrl);
		BufferedImage image=javax.imageio.ImageIO.read(file);
		int toWidth=300;
		int toHight=(int) (image.getHeight()/(image.getWidth()/300.0));;
		BufferedImage result = new BufferedImage(toWidth, toHight,
				BufferedImage.TYPE_INT_RGB);
		result.getGraphics().drawImage(
				image.getScaledInstance(toWidth, toHight,
						java.awt.Image.SCALE_SMOOTH), 0, 0, null);
		r.writeHighQuality(thumbnailName,result,outputFolder);
		return thumbnailName;
	}

}







