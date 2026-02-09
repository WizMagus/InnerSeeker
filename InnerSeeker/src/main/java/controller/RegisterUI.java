package controller;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import exception.AuthException;
import exception.AuthException.AuthError;
import model.User;
import service.AuthService;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JPasswordField;

public class RegisterUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField userTF;
	private JTextField hintTF;
	private JTextField hintAnswerTF;
	private JButton btnRegister;
	private JButton btnBack;
	private final AuthService authService;
	private JLabel lblMsg;
	private JPasswordField passwordField;
	private JPasswordField passwordCheckField;
	private boolean openingLogin = false;



	/**
	 * Create the frame.
	 */
	public RegisterUI(AuthService authService) {
		this.authService = authService;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 359, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitle = new JLabel("註冊");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("標楷體", Font.PLAIN, 26));
		lblTitle.setBounds(136, 10, 52, 32);
		contentPane.add(lblTitle);

		JLabel lblNewLabel = new JLabel("帳號");
		lblNewLabel.setFont(new Font("標楷體", Font.PLAIN, 16));
		lblNewLabel.setBounds(76, 55, 32, 20);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("密碼");
		lblNewLabel_1.setFont(new Font("標楷體", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(76, 106, 32, 20);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("密碼確認");
		lblNewLabel_2.setFont(new Font("標楷體", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(76, 157, 64, 20);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("密碼提示");
		lblNewLabel_3.setFont(new Font("標楷體", Font.PLAIN, 16));
		lblNewLabel_3.setBounds(76, 208, 64, 20);
		contentPane.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("提示答案");
		lblNewLabel_4.setFont(new Font("標楷體", Font.PLAIN, 16));
		lblNewLabel_4.setBounds(76, 259, 64, 20);
		contentPane.add(lblNewLabel_4);

		userTF = new JTextField();
		userTF.setBounds(178, 52, 96, 21);
		contentPane.add(userTF);
		userTF.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(178, 103, 96, 20);
		contentPane.add(passwordField);

		passwordCheckField = new JPasswordField();
		passwordCheckField.setBounds(178, 154, 96, 21);
		contentPane.add(passwordCheckField);

		hintTF = new JTextField();
		hintTF.setBounds(178, 205, 96, 21);
		contentPane.add(hintTF);
		hintTF.setColumns(10);

		hintAnswerTF = new JTextField();
		hintAnswerTF.setBounds(178, 256, 96, 21);
		contentPane.add(hintAnswerTF);
		hintAnswerTF.setColumns(10);

		lblMsg = new JLabel("");
		lblMsg.setForeground(new Color(255, 0, 0));
		lblMsg.setFont(new Font("標楷體", Font.PLAIN, 16));
		lblMsg.setBounds(76, 289, 198, 20);
		contentPane.add(lblMsg);

		btnRegister = new JButton("確認註冊");
		btnRegister.setFont(new Font("標楷體", Font.PLAIN, 16));
		btnRegister.setBounds(42, 322, 106, 29);
		contentPane.add(btnRegister);

		btnBack = new JButton("返回首頁");
		btnBack.setFont(new Font("標楷體", Font.PLAIN, 16));
		btnBack.setBounds(192, 322, 106, 29);
		contentPane.add(btnBack);

		btnRegister.addActionListener(e -> onRegister());
		btnBack.addActionListener(e -> openLogin());

		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				if (!openingLogin) {
					new LoginUI().setVisible(true);
				}
			}
		});
	}

	private void onRegister() {
		lblMsg.setText("");

		if (authService == null) {
			JOptionPane.showMessageDialog(this, "AuthService 尚未注入，請從 LoginUI 進入。");
			dispose();
			return;
		}

		String username = userTF.getText() == null ? "" : userTF.getText().trim();
		String pwd1 = new String(passwordField.getPassword()).trim();
		String pwd2 = new String(passwordCheckField.getPassword()).trim();
		String hint = hintTF.getText() == null ? null : hintTF.getText().trim();
		String hintAnswer = hintAnswerTF.getText() == null ? null : hintAnswerTF.getText().trim();
		
		if (!pwd1.equals(pwd2)) {
			lblMsg.setText("密碼不一致");
			return;
		}
		
		try {
			User u = authService.register(username, pwd1, hint, hintAnswer);
			JOptionPane.showMessageDialog(this, "註冊成功，請重新登入");
		    openLogin();
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
	private void openLogin() {
	    openingLogin = true;
	    new LoginUI().setVisible(true);
	    dispose();
	}
	
	private String toMsg(AuthError err) {
		if (err == null) {
			return "Error";
		}
		switch (err) {
		case USERNAME_TAKEN:
		    return "帳號已被使用";
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
}
