package controller;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import model.Role;
import model.RWSTarot;
import service.TarotService;
import service.TarotService.DrawResult;
import service.session.DeckSession;

public class DrawUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private final TarotService tarotService;
	private final Integer userId;
	private final String question;

	
	private JLabel lblCard;
	private JTextArea textArea;
	private JButton btnShuffle;
	private JButton btnDraw;
	private JButton btnClose;

	
	private DeckSession session;
	private int drawPos = 0;
	private boolean shuffling = false;
	private boolean drawn = false;

	
	private BufferedImage backImg;
	private final Map<Integer, BufferedImage> frontCache = new HashMap<>();

	
	private int baseX, baseY;
	private final Random random = new Random();


	public DrawUI() {
		this(null, null, Role.USER, "");
	}

	public DrawUI(TarotService tarotService, Integer userId, Role role, String question) {
		this.tarotService = tarotService;
		this.userId = userId;
		this.question = question;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(10, 10, 505, 541);
		contentPane.add(panel);
		panel.setLayout(null);

		lblCard = new JLabel("");
		lblCard.setBounds(132, 91, 210, 360);
		panel.add(lblCard);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(525, 10, 249, 287);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("複製以下內容給AI：");
		lblNewLabel_1.setBounds(16, 6, 216, 30);
		lblNewLabel_1.setFont(new Font("標楷體", Font.PLAIN, 24));
		panel_1.add(lblNewLabel_1);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("標楷體", Font.PLAIN, 16));
		textArea.setBounds(16, 36, 223, 241);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		panel_1.add(textArea);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBounds(525, 307, 249, 244);
		contentPane.add(panel_2);
		panel_2.setLayout(null);

		btnShuffle = new JButton("洗牌");
		btnShuffle.setFont(new Font("標楷體", Font.PLAIN, 22));
		btnShuffle.setBounds(80, 31, 81, 39);
		panel_2.add(btnShuffle);

		btnDraw = new JButton("抽牌");
		btnDraw.setFont(new Font("標楷體", Font.PLAIN, 22));
		btnDraw.setBounds(80, 101, 81, 39);
		panel_2.add(btnDraw);

		btnClose = new JButton("離開");
		btnClose.setFont(new Font("標楷體", Font.PLAIN, 22));
		btnClose.setBounds(80, 171, 81, 39);
		panel_2.add(btnClose);

		// 洗牌動畫要用：記住牌的位置
		baseX = lblCard.getX();
		baseY = lblCard.getY();

		// 初始化
		initOrExit();

		/**
		 * events，參考 LoginUI/QuestionUI：view 建完後再集中掛事件
		 */
		btnShuffle.addActionListener(e -> onShuffle());
		btnDraw.addActionListener(e -> onDraw());
		btnClose.addActionListener(e -> onClose());
	}

	/**
	 * controller
	 */

	private void onShuffle() {
		if (shuffling) return;

		
		resetToBack();

		runShuffleAnimationThenCreateSession();
	}

	private void onDraw() {
		if (shuffling) return;

		if (session == null) {
			JOptionPane.showMessageDialog(this, "尚未建立牌組，請先洗牌。");
			return;
		}
		if (drawn) {
			JOptionPane.showMessageDialog(this, "本次已抽牌，請先洗牌再抽。");
			return;
		}
		if (drawPos >= session.size()) {
			JOptionPane.showMessageDialog(this, "牌堆已空，請先洗牌。");
			return;
		}

		try {
			DrawResult r = tarotService.pickCards(session, drawPos, userId, question);
			drawPos++;
			drawn = true;

			RWSTarot card = r.getCard();
			BufferedImage front = loadFrontById(card.getId()); // /images/tarot/{id}.png
			if (front == null) {
				JOptionPane.showMessageDialog(this, "找不到牌面圖片：/resources/images/tarot/" + card.getId() + ".png");
				return;
			}

			BufferedImage showImg = r.isReversed() ? rotate180(front) : front;
			setLabelIcon(lblCard, showImg);

			textArea.setText(r.getPrompt());

			btnDraw.setEnabled(false); // 抽一次就鎖，靠洗牌重置
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
		} catch (IllegalStateException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "抽牌失敗：" + ex.getMessage());
		}
	}

	private void onClose() {
		dispose();
		System.exit(0);
	}

	/**
	 * init / helpers
	 */

	private void initOrExit() {
		if (tarotService == null) {
			JOptionPane.showMessageDialog(this, "TarotService 尚未注入，請從 QuestionUI 正確進入。");
			System.exit(0);
			return;
		}

		// 牌背：/resources/images/tarot/back.png
		backImg = loadFromResource("/resources/images/tarot/back.png");
		resetToBack();

		// 進入先洗一次（不做動畫）
		try {
			session = tarotService.createSession(true);
			drawPos = 0;
			drawn = false;
			btnDraw.setEnabled(true);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "建立牌組失敗：" + ex.getMessage());
			System.exit(0);
		}
	}

	
	private void resetToBack() {
		lblCard.setLocation(baseX, baseY);
		textArea.setText("");
		drawn = false;

		if (backImg != null) {
			setLabelIcon(lblCard, backImg);
		} else {
			lblCard.setIcon(null);
			lblCard.setText("BACK");
			lblCard.setHorizontalAlignment(JLabel.CENTER);
		}

		btnDraw.setEnabled(true);
	}

	private void runShuffleAnimationThenCreateSession() {
		shuffling = true;
		btnShuffle.setEnabled(false);
		btnDraw.setEnabled(false);

		// 抖動次數與幅度
		final int ticks = 18;
		final int maxDx = 10;
		final int maxDy = 6;

		final int[] i = { 0 };
		Timer t = new Timer(35, ev -> {
			i[0]++;
			if (i[0] >= ticks) {
				((Timer) ev.getSource()).stop();
				lblCard.setLocation(baseX, baseY);

				try {
					session = tarotService.createSession(true);
					drawPos = 0;
					drawn = false;
					textArea.setText("");

					// 洗完維持牌背
					if (backImg != null) setLabelIcon(lblCard, backImg);
					else lblCard.setText("BACK");

					btnDraw.setEnabled(true);
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(this, "洗牌失敗：" + ex.getMessage());
				}

				shuffling = false;
				btnShuffle.setEnabled(true);
				return;
			}

			int dx = random.nextInt(maxDx * 2 + 1) - maxDx;
			int dy = random.nextInt(maxDy * 2 + 1) - maxDy;
			lblCard.setLocation(baseX + dx, baseY + dy);
		});
		t.start();
	}

	private BufferedImage loadFrontById(int id) {
		BufferedImage cached = frontCache.get(id);
		if (cached != null) return cached;

		
		String path = "/resources/images/tarot/" + id + ".png";
		BufferedImage img = loadFromResource(path);
		if (img != null) frontCache.put(id, img);
		return img;
	}

	private static BufferedImage loadFromResource(String path) {
		try (InputStream in = DrawUI.class.getResourceAsStream(path)) {
			if (in == null) return null;
			return ImageIO.read(in);
		} catch (IOException e) {
			return null;
		}
	}

	private void setLabelIcon(JLabel label, BufferedImage img) {
		Image scaled = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
		label.setText("");
		label.setIcon(new ImageIcon(scaled));
	}

	private static BufferedImage rotate180(BufferedImage src) {
		int w = src.getWidth();
		int h = src.getHeight();
		BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = dst.createGraphics();
		try {
			AffineTransform at = new AffineTransform();
			at.translate(w, h);
			at.rotate(Math.PI);
			g2.drawImage(src, at, null);
		} finally {
			g2.dispose();
		}
		return dst;
	}
}
