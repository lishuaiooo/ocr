package com.chanct.ocr;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CutImg {

	/**
	 * 
	 * @param srcImageFile		源图像地址
	 * @param descImageFile		切片目标文件夹
	 * @param startWidth		开始宽
	 * @param startHeight		开始高
	 * @param endWidth			结束宽
	 * @param endHeight			结束高
	 */
	public static void cutToFile(String srcImageFile, String descImageFile, float startWidth, float startHeight, float endWidth, float endHeight){
		// 输出为文件
		try {
			ImageIO.write(cutToBufferedImage(srcImageFile, endHeight, endHeight, endHeight, endHeight), "JPEG", new File(descImageFile + "pre_map.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * 
	 * @param srcImageFile		源图像地址
	 * @param startWidth		开始宽
	 * @param startHeight		开始高
	 * @param endWidth			结束宽
	 * @param endHeight			结束高
	 */
	public static BufferedImage cutToBufferedImage(String srcImageFile, float startWidth, float startHeight, float endWidth, float endHeight){
		try{
			long start = System.currentTimeMillis();
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			// 源图宽度
			int srcWidth = bi.getWidth(); 
			// 源图高度
			int srcHeight = bi.getHeight(); 

			Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
			Image img;
			CropImageFilter cropFilter;
			// 四个参数分别为图像起点坐标和宽高
			cropFilter = new CropImageFilter((int) (srcWidth*startWidth) ,(int) (srcHeight*startHeight)  ,(int) (srcWidth*endWidth)  ,(int) (srcHeight*endHeight));
			img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
			BufferedImage tag = new BufferedImage((int) (srcWidth*endWidth)  ,(int) (srcHeight*endHeight), BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(img, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			long end = System.currentTimeMillis();
			System.out.println(end -start);
			return tag;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
