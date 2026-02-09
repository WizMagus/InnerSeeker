package controller;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Role;
import service.HistoryService;
import service.TarotService;

import javax.swing.JButton;
import java.awt.Font;

public class MenuUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

    private final TarotService tarotService;
    private final HistoryService historyService;
    private final Integer userId;
    private final Role role;

	/**
	 * Create the frame.
	 */
	public MenuUI(TarotService tarotService, HistoryService historyService, Integer userId, Role role) {
		
		this.tarotService = tarotService;
        this.historyService = historyService;
        this.userId = userId;
        this.role = role;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnHistory = new JButton("歷史紀錄");
		btnHistory.setFont(new Font("標楷體", Font.PLAIN, 24));
		btnHistory.setBounds(143, 161, 148, 39);
		contentPane.add(btnHistory);
		
		JButton btnNext = new JButton("開始占卜");
		btnNext.setFont(new Font("標楷體", Font.PLAIN, 24));
		btnNext.setBounds(143, 61, 148, 39);
		contentPane.add(btnNext);
		
		btnNext.addActionListener(e -> {
            new QuestionUI(tarotService, userId, role).setVisible(true);
            dispose();
        });

        btnHistory.addActionListener(e -> {
            new HistoryUI(historyService, userId, role, this).setVisible(true);
            setVisible(false);
        });

	}
}
