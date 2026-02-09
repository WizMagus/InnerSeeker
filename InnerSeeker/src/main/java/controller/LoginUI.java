package controller;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dao.impl.DrawHistoryDaoImpl;
import dao.impl.RWSTarotDaoImpl;
import dao.impl.UserDaoImpl;
import exception.AuthException;
import exception.AuthException.AuthError;
import model.Role;
import model.User;
import service.AuthService;
import service.impl.AuthServiceImpl;
import service.impl.HistoryServiceImpl;
import service.impl.TarotServiceImpl;
import service.TarotService;
import service.HistoryService;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.sql.SQLException;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Color;

public class LoginUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField userTF;
	private JPasswordField passwordTF;
	private JLabel lblMsg;
	
	private final DrawHistoryDaoImpl historyDao = new DrawHistoryDaoImpl();
	private final RWSTarotDaoImpl tarotDao = new RWSTarotDaoImpl();

	private final AuthService authService = new AuthServiceImpl(new UserDaoImpl());
	private final TarotService tarotService = new TarotServiceImpl(tarotDao, historyDao);
	private final HistoryService historyService = new HistoryServiceImpl(historyDao, tarotDao);
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginUI frame = new LoginUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 428, 206);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel usernameLabel = new JLabel("帳號");
		usernameLabel.setFont(new Font("標楷體", Font.PLAIN, 16));
		usernameLabel.setBounds(44, 24, 32, 20);
		contentPane.add(usernameLabel);

		JLabel passwordLabel = new JLabel("密碼");
		passwordLabel.setFont(new Font("標楷體", Font.PLAIN, 16));
		passwordLabel.setBounds(44, 68, 32, 20);
		contentPane.add(passwordLabel);

		userTF = new JTextField();
		userTF.setBounds(106, 24, 96, 21);
		contentPane.add(userTF);
		userTF.setColumns(10);

		passwordTF = new JPasswordField();
		passwordTF.setBounds(106, 68, 96, 21);
		contentPane.add(passwordTF);

		lblMsg = new JLabel("");
		lblMsg.setForeground(new Color(255, 0, 0));
		lblMsg.setFont(new Font("標楷體", Font.PLAIN, 14));
		lblMsg.setBounds(44, 98, 158, 21);
		contentPane.add(lblMsg);

		JButton btnLogin = new JButton("登入");
		btnLogin.setFont(new Font("標楷體", Font.PLAIN, 16));
		btnLogin.setBounds(66, 129, 115, 30);
		contentPane.add(btnLogin);

		JButton btnGuestLogin = new JButton("訪客登入");
		btnGuestLogin.setFont(new Font("標楷體", Font.PLAIN, 16));
		btnGuestLogin.setBounds(289, 10, 115, 30);
		contentPane.add(btnGuestLogin);

		JButton btnForgotPwd = new JButton("忘記密碼");
		btnForgotPwd.setFont(new Font("標楷體", Font.PLAIN, 16));
		btnForgotPwd.setBounds(289, 90, 115, 30);
		contentPane.add(btnForgotPwd);

		JButton btnClose = new JButton("離開");
		btnClose.setFont(new Font("標楷體", Font.PLAIN, 16));
		btnClose.setBounds(289, 129, 115, 30);
		contentPane.add(btnClose);

		JButton btnRegister = new JButton("註冊新帳號");
		btnRegister.setFont(new Font("標楷體", Font.PLAIN, 16));
		btnRegister.setBounds(289, 50, 115, 30);
		contentPane.add(btnRegister);

		/**
		 * events，將view層與controller層分離
		 */

		btnLogin.addActionListener(e -> onLogin());
		btnGuestLogin.addActionListener(e -> onGuestLogin());
		btnForgotPwd.addActionListener(e -> onForgotPwd());
		btnRegister.addActionListener(e -> onRegister());
		btnClose.addActionListener(e -> onClose());

	}
	
	/**
	 * controller
	 */
	
	private void onLogin() {
		lblMsg.setText("");
		
		String username = userTF.getText() == null ? "" : userTF.getText().trim();
		String password = new String(passwordTF.getPassword()).trim();
		
		try {
			User user = authService.login(username, password);
			openNext(user.getId(), user.getRole());
			dispose();
		} catch (AuthException ex) {
			lblMsg.setText(toMsg(ex.getError()));
		} catch (SQLException ex) {
			lblMsg.setText("Database Error");
			ex.printStackTrace();
		} catch (RuntimeException ex) {
			lblMsg.setText("Error");
			ex.printStackTrace();
		}
	}
	
	private void onGuestLogin() {
		lblMsg.setText("");
		openNext(null, Role.USER);
		dispose();
	}
	
	private void onRegister() {
		lblMsg.setText("");
		RegisterUI next = new RegisterUI(authService);
		next.setVisible(true);
		dispose();
	}
	
	private void onForgotPwd() {
		JOptionPane.showMessageDialog(this, "尚未實作");
	}
	
	private void onClose() {
		dispose();
		System.exit(0);
	}
	
	private String toMsg(AuthError err) {
		if (err == null) {
			return "Error";
		}
		switch (err) {
		case USERNAME_REQUIRED:
			return "請輸入帳號";
		case PASSWORD_REQUIRED:
			return "請輸入密碼";
		case INVALID_CREDENTIALS:
			return "帳號或密碼錯誤";
		default:
			return "Error";
		}
	}
	
	private void openNext(Integer userId, Role role) {
		MenuUI next = new MenuUI(tarotService, historyService, userId, role);
	    next.setVisible(true);
	}
	
	
}
