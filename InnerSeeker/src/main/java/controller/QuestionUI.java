package controller;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Role;
import service.TarotService;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JButton;

public class QuestionUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JTextArea textArea;

	private final TarotService tarotService;
	private final Integer userId;
	private final Role role;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuestionUI frame = new QuestionUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public QuestionUI() {
		this(null, null, Role.USER);
		
	}

	public QuestionUI(TarotService tarotService, Integer userId, Role role) {
		this.tarotService = tarotService;
		this.userId = userId;
		this.role = role;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitle = new JLabel("請輸入想占卜的問題：");
		lblTitle.setFont(new Font("標楷體", Font.BOLD, 24));
		lblTitle.setBounds(10, 10, 248, 30);
		contentPane.add(lblTitle);

		textArea = new JTextArea();
		textArea.setFont(new Font("標楷體", Font.PLAIN, 14));
		textArea.setBounds(10, 50, 414, 165);
		contentPane.add(textArea);

		JButton btnConfirm = new JButton("確認，開始占卜");
		btnConfirm.setFont(new Font("標楷體", Font.PLAIN, 16));
		btnConfirm.setBounds(41, 225, 168, 29);
		contentPane.add(btnConfirm);

		JButton btnReset = new JButton("重新填寫");
		btnReset.setFont(new Font("標楷體", Font.PLAIN, 16));
		btnReset.setBounds(273, 225, 117, 29);
		contentPane.add(btnReset);

		btnConfirm.addActionListener(e -> onConfirm());
		btnReset.addActionListener(e -> textArea.setText(""));

	}

	private void onConfirm() {
		String q = textArea.getText() == null ? "" : textArea.getText().trim();
		if (q.isEmpty()) {
			JOptionPane.showMessageDialog(this, "問題為必填。");
			return;
		}
		if (tarotService == null) {
			JOptionPane.showConfirmDialog(this, "登入狀態異常，請重新開啟程式", "錯誤", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			return;
		}
		new DrawUI(tarotService,userId, role, q).setVisible(true);
		dispose();
	}
}
