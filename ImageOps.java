package painting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class ImageOps extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private int flagWidth = 900;
	private int flagHeight = 600;
	private String command;
	private int checkWidth;
	private int checkHeight;
	private File imgFile;
	private BufferedImage bi;
	private BufferedImage bi2;
	private BufferedImage bi3;
	private boolean filtered = false;
	private boolean changed = false;

	public ImageOps() {

	}

	public JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenuItem menuItem;
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		String[] commands = { "Open", "Save", "Save As..", "Clear Screen" };
		int[] keyEvents = { KeyEvent.VK_O, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_C };
		for (int i = 0; i < commands.length; i++) {
			menuItem = new JMenuItem(commands[i]);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(keyEvents[i], ActionEvent.ALT_MASK));
			menuItem.addActionListener(this);
			fileMenu.add(menuItem);
		}
		menuBar.add(fileMenu);

		JMenu flagsMenu = new JMenu("Flags");
		flagsMenu.setMnemonic(KeyEvent.VK_L);
		String[] flagCommands = { "Draw All", "horizontalStripes", "verticalStripes", "diagonal flag", "checkerBoard",
				"cross", "inset", "quad", "japaneseFlag" };
		int[] flagKeyEvents = { KeyEvent.VK_D, KeyEvent.VK_H, KeyEvent.VK_V, KeyEvent.VK_G, KeyEvent.VK_C,
				KeyEvent.VK_R, KeyEvent.VK_I, KeyEvent.VK_Q, KeyEvent.VK_J };
		for (int i = 0; i < flagCommands.length; i++) {
			menuItem = new JMenuItem(flagCommands[i]);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(flagKeyEvents[i], ActionEvent.CTRL_MASK));
			menuItem.addActionListener(this);
			flagsMenu.add(menuItem);
		}
		menuBar.add(flagsMenu);

		JMenu transformationsMenu = new JMenu("Transformations");
		transformationsMenu.setMnemonic(KeyEvent.VK_T);
		String[] drawCommands = {"redFilter", "negative", "sepia_tone", "convert_to_gray_scale", "flip_vertical",
				"flip_horizontal", "rotate_right_90", "Revert"};
		int[] drawKeyEvents = { KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5,
				KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8};
		for (int i = 0; i < drawCommands.length; i++) {
			menuItem = new JMenuItem(drawCommands[i]);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(drawKeyEvents[i], ActionEvent.CTRL_MASK));
			menuItem.addActionListener(this);
			transformationsMenu.add(menuItem);
		}
		menuBar.add(transformationsMenu);

		return menuBar;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int x = ((getWidth() / 2) - (flagWidth / 2));
		int y = ((getHeight() / 2) - (flagHeight / 2));
		g2.setColor(Color.black);
		g2.fillRect(0, 0, getWidth(), getHeight());
		if (command != null) {
			switch (command) {
			case "drawAll":
				drawAll(g2);
				break;
			case "horizontalStripes":
				horizontalStripes(g2, x, y);
				break;
			case "verticalStripes":
				verticalStripes(g2, x, y);
				break;
			case "diagonal flag":
				diagonalFlag(g2, x, y);
				break;
			case "checkerBoard":
				checkerBoard(g2, x, y);
				break;
			case "cross":
				cross(g2, x, y);
				break;
			case "inset":
				inset(g2, x, y);
				break;
			case "quad":
				quad(g2, x, y);
				break;
			case "japaneseFlag":
				japaneseFlag(g2, x, y);
				break;
			case "open":
				drawImg(g2);
				break;
			case "Revert":
				Revert(g2);
				break;
			case "transformation":
				if (filtered == true && changed == true) {
					drawImg3(g2);
				}else {
					drawImg2(g2);
				}
				break;
			}
		}
	}

	public void drawAll(Graphics2D g2) {
		int x = ((getWidth() - 1000) / 2);
		int y = ((getHeight() - 700) / 2);
		horizontalStripes(g2, x, y);
		verticalStripes(g2, x + 350, y);
		diagonalFlag(g2, x + 700, y);
		checkerBoard(g2, x, y + 250);
		cross(g2, x + 350, y + 250);
		inset(g2, x + 700, y + 250);
		quad(g2, x, y + 500);
		japaneseFlag(g2, x + 350, y + 500);
	}

	public void horizontalStripes(Graphics2D g2, int x, int y) {
		g2.setColor(Color.red);
		g2.fillRect(x, y, flagWidth, flagHeight / 3);
		g2.setColor(Color.green);
		g2.fillRect(x, y + (flagHeight / 3), flagWidth, flagHeight / 3);
		g2.setColor(Color.blue);
		g2.fillRect(x, y + 2 * (flagHeight / 3), flagWidth, flagHeight / 3);
	}

	public void verticalStripes(Graphics2D g2, int x, int y) {
		g2.setColor(Color.red);
		g2.fillRect(x, y, flagWidth / 3, flagHeight);
		g2.setColor(Color.green);
		g2.fillRect(x + (flagWidth / 3), y, flagWidth / 3, flagHeight);
		g2.setColor(Color.blue);
		g2.fillRect(x + 2 * (flagWidth / 3), y, flagWidth / 3, flagHeight);
	}

	public void diagonalFlag(Graphics2D g2, int x, int y) {
		g2.setColor(Color.red);
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];
		xPoints[0] = x;
		xPoints[1] = x;
		xPoints[2] = flagWidth + x;
		yPoints[0] = y;
		yPoints[1] = flagHeight + y;
		yPoints[2] = flagHeight + y;
		g2.setColor(Color.red);
		g2.fillPolygon(xPoints, yPoints, 3);
		g2.setColor(Color.green);
		xPoints[0] = x;
		xPoints[1] = flagWidth + x;
		xPoints[2] = flagWidth + x;
		yPoints[0] = y;
		yPoints[1] = y;
		yPoints[2] = flagHeight + y;
		g2.fillPolygon(xPoints, yPoints, 3);
	}

	public void checkerBoard(Graphics2D g2, int x, int y) {
		int count = 1;
		for (int j = 0; j < 8; j++) {
			count += 1;
			if (count % 2 == 0) {
				g2.setColor(Color.red);
				for (int i = 0; i < 10; i += 2) {
					g2.fillRect(x + (i * checkWidth), y + (j * checkHeight), checkWidth, checkHeight);
				}
				g2.setColor(Color.green);
				for (int i = 1; i < 11; i += 2) {
					g2.fillRect(x + (i * checkWidth), y + (j * checkHeight), checkWidth, checkHeight);
				}
			} else if (j == 7) {
				g2.setColor(Color.green);
				for (int i = 0; i < 10; i += 2) {
					g2.fillRect(x + (i * checkWidth), y + (j * checkHeight), checkWidth, checkHeight / 2);
				}
				g2.setColor(Color.red);
				for (int i = 1; i < 11; i += 2) {
					g2.fillRect(x + (i * checkWidth), y + (j * checkHeight), checkWidth, checkHeight / 2);
				}
			} else {
				g2.setColor(Color.green);
				for (int i = 0; i < 10; i += 2) {
					g2.fillRect(x + (i * checkWidth), y + (j * checkHeight), checkWidth, checkHeight);
				}
				g2.setColor(Color.red);
				for (int i = 1; i < 11; i += 2) {
					g2.fillRect(x + (i * checkWidth), y + (j * checkHeight), checkWidth, checkHeight);
				}
			}
		}
	}

	public void cross(Graphics2D g2, int x, int y) {
		g2.setColor(Color.green);
		g2.fillRect(x, y, flagWidth, flagHeight);
		g2.setColor(Color.red);
		g2.fillRect(x, y + (flagHeight / 3), flagWidth, flagHeight / 3);
		g2.fillRect(x + (flagWidth / 3), y, flagWidth / 3, flagHeight);
	}

	public void inset(Graphics2D g2, int x, int y) {
		g2.setColor(Color.green);
		g2.fillRect(x, y, flagWidth, flagHeight);
		g2.setColor(Color.red);
		g2.fillRect(x + (flagWidth / 3), y + (flagHeight / 4), flagWidth / 3, flagWidth / 3);
	}

	public void quad(Graphics2D g2, int x, int y) {
		g2.setColor(Color.red);
		g2.fillRect(x, y, flagWidth / 2, flagHeight / 2);
		g2.fillRect(x + (flagWidth / 2), y + (flagHeight / 2), flagWidth / 2, flagHeight / 2);
		g2.setColor(Color.green);
		g2.fillRect(x + (flagWidth / 2), y, flagWidth / 2, flagHeight / 2);
		g2.fillRect(x, y + (flagHeight / 2), flagWidth / 2, flagHeight / 2);
	}

	public void japaneseFlag(Graphics2D g2, int x, int y) {
		g2.setColor(Color.white);
		g2.fillRect(x, y, flagWidth, flagHeight);
		g2.setColor(Color.red);
		Ellipse2D circle = new Ellipse2D.Double(x + (flagWidth * .3), y + (flagHeight * .2), flagHeight * .6,
				flagHeight * .6);
		g2.fill(circle);
	}

	public void openImg() {
		changed = false;
		filtered = false;
		JFileChooser jfc = new JFileChooser(".");
		int option = jfc.showOpenDialog(null);

		try {
			if (option == JFileChooser.APPROVE_OPTION) {
				imgFile = jfc.getSelectedFile();
				bi = ImageIO.read(imgFile);
				bi2 = bi;
				bi3 = null;
			} else {
					
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void drawImg(Graphics2D g2) {
		if (bi != null) {
			g2.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), this);
		}
	}

	public void drawImg2(Graphics2D g2) {
		if (bi2 != null) {
			g2.drawImage(bi2, 0, 0, bi2.getWidth(), bi2.getHeight(), this);
		}
	}
	
	public void drawImg3(Graphics2D g2) {
		if (bi2 != null) {
			g2.drawImage(bi3, 0, 0, bi3.getWidth(), bi3.getHeight(), this);
		}
	}
	
	public void Revert(Graphics2D g2) {
		try {
			bi = ImageIO.read(imgFile);
			bi2 = bi;
			bi3 = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (bi != null) {
			g2.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), this);
		}
	}

	

	public void redFilter(BufferedImage biX) {
		filtered = true;
		changed = false;
		try {
			bi = ImageIO.read(imgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bi2 = bi;
		for (int x = 0; x < bi.getWidth(); x++) {
			for (int y = 0; y < bi.getHeight(); y++) {
				int p = bi.getRGB(x, y);
				int alpha = p >> 24 & 0xFF;
				int red = p >> 16 & 0xFF;
				int green = p >> 8 & 0xFF;
				green = 0;
				int blue = p & 0xFF;
				blue = 0;
				p = alpha << 24 | red << 16 | green << 8 | blue;
				bi2.setRGB(x, y, p);
			}
		}
		repaint();
	}

	public void negative(BufferedImage biX) {
		filtered = true;
		changed = false;
		try {
			bi = ImageIO.read(imgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bi2 = bi;
		for (int x = 0; x < bi.getWidth(); x++) {
			for (int y = 0; y < bi.getHeight(); y++) {
				int p = bi.getRGB(x, y);
				int alpha = p >> 24 & 0xFF;
				int red = p >> 16 & 0xFF;
				red = 255 - red;
				int green = p >> 8 & 0xFF;
				green = 255 - green;
				int blue = p & 0xFF;
				blue = 255 - blue;
				p = alpha << 24 | red << 16 | green << 8 | blue;
				bi2.setRGB(x, y, p);
			}
		}
		repaint();
	}

	public void sepia(BufferedImage biX) {
		filtered = true;
		changed = false;
		try {
			bi = ImageIO.read(imgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bi2 = bi;
		for (int x = 0; x < bi.getWidth(); x++) {
			for (int y = 0; y < bi.getHeight(); y++) {
				Color c = new Color(bi.getRGB(x, y));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				int red2 = (int) (red * 0.393 + green * 0.769 + blue * 0.189);
				int green2 = (int) (red * 0.349 + green * 0.686 + blue * 0.168);
				int blue2 = (int) (red * 0.272 + green * 0.534 + blue * 0.131);
				if (red2 > 255) {
					red2 = 255;
				}
				if (green2 > 255) {
					green2 = 255;
				}
				if (blue2 > 255) {
					blue2 = 255;
				}
				Color c2 = new Color(red2, green2, blue2);
				bi2.setRGB(x, y, c2.getRGB());
			}
		}
		repaint();
	}

	public void grayScale(BufferedImage biX) {
		filtered = true;
		changed = false;
		try {
			bi = ImageIO.read(imgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bi2 = bi;
		for (int x = 0; x < bi.getWidth(); x++) {
			for (int y = 0; y < bi.getHeight(); y++) {
				Color c = new Color(bi.getRGB(x, y));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				int lum = (int) (.299 * red + .5878 * green + .114 * blue);
				if (lum > 255) {
					lum = 255;
				}
				Color c2 = new Color(lum, lum, lum);
				bi2.setRGB(x, y, c2.getRGB());
			}
		}
		repaint();
	}

	public void flipVert(BufferedImage biX) {
		changed = true;
		try {
			bi = ImageIO.read(imgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bi3 = bi2;
		int[] pix = new int[bi2.getHeight()];
		for (int x = 0; x < bi2.getWidth(); x++) {
			for (int y = 0; y < bi2.getHeight(); y++) {
				int p = bi2.getRGB(x, y);
				int alpha = p >> 24 & 0xFF;
				int red = p >> 16 & 0xFF;
				int green = p >> 8 & 0xFF;
				int blue = p & 0xFF;
				p = alpha << 24 | red << 16 | green << 8 | blue;
				pix[y] = p;
			}
			for (int i = 0; i < pix.length; i++) {
				bi3.setRGB(x, bi2.getHeight() - i - 1, pix[i]);
			}
		}
		repaint();
	}

	public void flipHoriz(BufferedImage biX) {
		changed = true;
		try {
			bi = ImageIO.read(imgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bi3 = bi2;
		int[] pix = new int[bi2.getWidth()];
		for (int y = 0; y < bi2.getHeight(); y++) {
			for (int x = 0; x < bi2.getWidth(); x++) {
				int p = bi2.getRGB(x, y);
				int alpha = p >> 24 & 0xFF;
				int red = p >> 16 & 0xFF;
				int green = p >> 8 & 0xFF;
				int blue = p & 0xFF;
				p = alpha << 24 | red << 16 | green << 8 | blue;
				pix[x] = p;
			}
			for (int i = 0; i < pix.length; i++) {
				bi3.setRGB(bi2.getWidth() - i - 1, y, pix[i]);
			}
		}
		repaint();
	}

	public void rotate90(BufferedImage biX) {
		changed = true;
		if (bi != null) {
			int w = bi.getWidth();
			int h = bi.getHeight();
			int type = bi.getType();
			int p;
			bi2 = new BufferedImage(h, w, type);
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y ++) {
					p = bi.getRGB(x, y);
					bi2.setRGB(h-y-1, x, p);
				}
			}
		}
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		command = e.getActionCommand();

		flagWidth = 900;
		flagHeight = 600;

		switch (command) {
		case "Open":
			openImg();
			command = "open";
			repaint();
			break;
		case "Save As..":
			String name = JOptionPane.showInputDialog("Enter File Name");
			File file = new File(name);
			try {
				ImageIO.write(bi2, "jpg", file);
			} catch (Exception ioe) {
				ioe.printStackTrace();
			}
			break;
		case "Save":
			try {
				ImageIO.write(bi2, "jpg", imgFile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case "Clear Screen":
			repaint();
			break;
		case "Draw All":
			flagWidth = 300;
			flagHeight = 200;
			checkWidth = 30;
			checkHeight = 27;
			command = "drawAll";
			repaint();
			break;
		case "horizontalStripes":
			command = "horizontalStripes";
			repaint();
			break;
		case "verticalStripes":
			command = "verticalStripes";
			repaint();
			break;
		case "diagonal flag":
			command = "diagonal flag";
			repaint();
			break;
		case "checkerBoard":
			checkWidth = 90;
			checkHeight = 80;
			command = "checkerBoard";
			repaint();
			break;
		case "cross":
			command = "cross";
			repaint();
			break;
		case "inset":
			command = "inset";
			repaint();
			break;
		case "quad":
			command = "quad";
			repaint();
			break;
		case "japaneseFlag":
			command = "japaneseFlag";
			repaint();
			break;
		case "redFilter":
			command = "transformation";
			redFilter(bi);
			break;
		case "negative":
			command = "transformation";
			negative(bi);
			break;
		case "sepia_tone":
			command = "transformation";
			sepia(bi);
			break;
		case "convert_to_gray_scale":
			command = "transformation";
			grayScale(bi);
			break;
		case "flip_vertical":
			command = "transformation";
			flipVert(bi);
			break;
		case "flip_horizontal":
			command = "transformation";
			flipHoriz(bi);
			break;
		case "rotate_right_90":
			command = "transformation";
			rotate90(bi);
			break;
		case "Revert":
			command = "Revert";
			repaint();
			break;
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Image Ops");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.getContentPane().setBackground(Color.black);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		ImageOps image = new ImageOps();

		frame.add(image);
		image.setBackground(Color.black);

		JMenuBar menuBar = image.createMenuBar();
		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
	}
}
