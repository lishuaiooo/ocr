package com.chanct.ocr;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.filechooser.FileSystemView;

import com.chanct.hotkey.HotkeyListenerUtil;

public class RectD extends JFrame{

	private static final long serialVersionUID = 1L;
	
	int orgx,orgy,endx,endy,x,y,width,height;
	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	BufferedImage image;
	BufferedImage tempImage;
	BufferedImage saveImage;
	Graphics g;

	@Override
	public void paint(Graphics g) {
		//缩放因子和偏移量
		RescaleOp ro = new RescaleOp(0.8f, 0, null);
		tempImage = ro.filter(image, null);
		g.drawImage(tempImage, 0, 0,this);
	}

	public void snapshot(){

		try {
			Robot robot = new Robot();
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			image = robot.createScreenCapture(new Rectangle(0,0,d.width,d.height));
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public RectD(){
		snapshot();
		//全屏运行
		setUndecorated(true);
		setVisible(true);
		//最大化窗口
		setSize(d);
		setDefaultCloseOperation(EXIT_ON_CLOSE);


		this.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				orgx=e.getX();
				orgy=e.getY();
			}
		});
		//鼠标运动监听器
		this.addMouseMotionListener(new MouseMotionAdapter() {
			//鼠标拖拽事件
			public void mouseDragged(MouseEvent e) {
				endx = e.getX();
				endy = e.getY();
				g = getGraphics();
				g.drawImage(tempImage, 0, 0, RectD.this);
				x = Math.min(orgx, endx);
				y = Math.min(orgy, endy);
				//加上1，防止width,height为0
				width=Math.abs(endx-orgx)+1;
				height=Math.abs(endy-orgy)+1;
				g.setColor(Color.BLUE);
				g.drawRect(x-1, y-1, width+1, height+1);
				//减1，加1都是为了防止图片将矩形框覆盖掉
				saveImage = image.getSubimage(x, y, width, height);
				g.drawImage(saveImage, x, y,RectD.this);
			}
		});

		this.addKeyListener(new KeyAdapter() {
			@Override
			//按键释放
			public void keyReleased(KeyEvent e){
				//按 回车保存退出
				if(e.getKeyCode()==10){
//					saveToFile();
					System.out.println(x + "  " + y + "  " + width + "  " + height);
					System.exit(0);
				}
				//按 ESC 不保存 退出
				if(e.getKeyCode()==27){
					System.exit(0);
				}
			}
		});
	}
	
	
	public void saveToFile(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddHHmmss");
		String name = sdf.format(new Date());
		File path = FileSystemView.getFileSystemView().getHomeDirectory();
		String format = "jpg";
		File f = new File(path+File.separator+name+"."+format);
		try {
			ImageIO.write(saveImage, format, f);
		} catch (IOException e) {
			e.printStackTrace();	
		}
	}

	public int[] getSelectCoordinate() {
		return new int[] {x, y, width, height};
	}
	public static void main(String[] args) {
		
		HotkeyListenerUtil.add("alt+p", ()->{
			RectD rd = new RectD();
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			gd.setFullScreenWindow(rd);
		});
		
	}
}
