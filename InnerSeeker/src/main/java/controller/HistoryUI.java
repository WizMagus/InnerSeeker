package controller;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import model.DrawHistory;
import model.Role;
import service.HistoryService;

import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Font;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JTextArea;

public class HistoryUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTextArea promptTA;
	private final HistoryService historyService;
	private final Integer userId;
	private final Role role;
	private final MenuUI prev;

	private List<DrawHistory> rows;

	/**
	 * Create the frame.
	 */
	public HistoryUI(HistoryService historyService, Integer userId, Role role, MenuUI prev) {
		
		this.historyService = historyService;
	    this.userId = userId;
	    this.role = role;
	    this.prev = prev;

	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		table = new JTable();
		table.setBounds(10, 10, 414, 240);
		contentPane.add(table);
		
		JButton btnRead = new JButton("檢視");
		btnRead.setFont(new Font("標楷體", Font.PLAIN, 16));
		btnRead.setBounds(52, 528, 75, 29);
		contentPane.add(btnRead);
		
		JButton btnDelete = new JButton("刪除");
		btnDelete.setFont(new Font("標楷體", Font.PLAIN, 16));
		btnDelete.setBounds(179, 528, 75, 29);
		contentPane.add(btnDelete);
		
		JButton btnReturn = new JButton("返回");
		btnReturn.setFont(new Font("標楷體", Font.PLAIN, 16));
		btnReturn.setBounds(306, 528, 75, 29);
		contentPane.add(btnReturn);
		
		promptTA = new JTextArea();
		promptTA.setEditable(false);
		promptTA.setBounds(10, 260, 414, 240);
		contentPane.add(promptTA);

		btnRead.addActionListener(e -> showSelectedPrompt());
	    btnDelete.addActionListener(e -> deleteSelected());
	    btnReturn.addActionListener(e -> {
	        if (prev != null) prev.setVisible(true);
	        dispose();
	    });

	    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	        @Override
	        public void valueChanged(ListSelectionEvent e) {
	            if (!e.getValueIsAdjusting()) showSelectedPrompt();
	        }
	    });

	    loadTable();
	}
	private void loadTable() {
	    try {
	        rows = historyService.list(userId, role, 200);

	        DefaultTableModel m = new DefaultTableModel(new Object[] { "ID", "時間" }, 0) {
	            @Override public boolean isCellEditable(int r, int c) { return false; }
	        };

	        for (DrawHistory h : rows) {
	            m.addRow(new Object[] { h.getId(), h.getCreatedAt() }); // Timestamp 直接顯示即可
	        }

	        table.setModel(m);

	        // 隱藏 ID 欄
	        table.getColumnModel().getColumn(0).setMinWidth(0);
	        table.getColumnModel().getColumn(0).setMaxWidth(0);
	        table.getColumnModel().getColumn(0).setWidth(0);

	    } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(this, "讀取歷史失敗：" + ex.getMessage());
	    }
	}

	private Integer selectedId() {
	    int r = table.getSelectedRow();
	    if (r < 0) return null;
	    Object v = table.getValueAt(r, 0);
	    try { return Integer.parseInt(String.valueOf(v)); }
	    catch (Exception e) { return null; }
	}

	private DrawHistory findById(int id) {
	    if (rows == null) return null;
	    for (DrawHistory h : rows) if (h.getId() == id) return h;
	    return null;
	}

	private void showSelectedPrompt() {
	    Integer id = selectedId();
	    if (id == null) return;

	    DrawHistory h = findById(id);
	    if (h == null) return;

	    try {
	        String prompt = historyService.buildPrompt(h);
	        promptTA.setText(prompt);
	        promptTA.setCaretPosition(0);
	    } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(this, "生成 prompt 失敗：" + ex.getMessage());
	    }
	}

	private void deleteSelected() {
	    Integer id = selectedId();
	    if (id == null) {
	        JOptionPane.showMessageDialog(this, "請先選取一筆紀錄");
	        return;
	    }

	    int ok = JOptionPane.showConfirmDialog(this, "確定刪除？", "確認", JOptionPane.YES_NO_OPTION);
	    if (ok != JOptionPane.YES_OPTION) return;

	    try {
	        boolean deleted = historyService.delete(userId, role, id);
	        if (!deleted) {
	            JOptionPane.showMessageDialog(this, "刪除失敗（可能無權限或資料不存在）");
	        }
	        promptTA.setText("");
	        loadTable();
	    } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(this, "刪除失敗：" + ex.getMessage());
	    }
	}
}
