package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import config.DbConnection;
import dao.DrawHistoryDao;
import model.DrawHistory;

public class DrawHistoryDaoImpl implements DrawHistoryDao {

	private DrawHistory mapRow(ResultSet rs) throws SQLException {
		DrawHistory h = new DrawHistory();
		h.setId(rs.getInt("id"));
		int uid = rs.getInt("user_id");
		h.setUserId(rs.wasNull() ? null : uid);
		h.setCardPk(rs.getInt("card_pk"));
		h.setQuestion(rs.getString("question"));
		h.setReversed(rs.getBoolean("reversed"));
		h.setCreatedAt(rs.getTimestamp("created_at"));
		return h;
	}

	@Override
	public int insert(DrawHistory h) throws SQLException {
		String sql = "INSERT INTO draw_history (user_id, card_pk, question, reversed) VALUES (?, ?, ?, ?)";
		try (Connection conn = DbConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			if (h.getUserId() == null) {
				ps.setNull(1, Types.INTEGER);
			} else {
				ps.setInt(1, h.getUserId());
			}
			ps.setInt(2, h.getCardPk());
			ps.setString(3, h.getQuestion());
			ps.setBoolean(4, h.isReversed());

			ps.executeUpdate();

			try (ResultSet keys = ps.getGeneratedKeys()) {
				return keys.next() ? keys.getInt(1) : 0;
			}
		}
	}

	@Override
	public List<DrawHistory> findByUserId(int userId, int limit) throws SQLException {
		String sql = "SELECT * FROM draw_history WHERE user_id = ? ORDER BY created_at DESC LIMIT ?";
		List<DrawHistory> list = new ArrayList<>();
		try (Connection conn = DbConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ps.setInt(2, limit);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(mapRow(rs));
				}
			}
		}
		return list;
	}

	@Override
	public List<DrawHistory> findAll(int limit) throws SQLException {
		String sql = "SELECT * FROM draw_history ORDER BY created_at DESC LIMIT ?";
		List<DrawHistory> list = new ArrayList<>();
		try (Connection conn = DbConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setInt(1, limit);
			
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(mapRow(rs));
				}
			}
		}
		return list;
	}

	@Override
	public boolean deleteById(int id) throws SQLException {
		String sql = "DELETE FROM draw_history where id = ?";
		try (Connection conn = DbConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() == 1;
		}
		
	}

	@Override
	public boolean deleteByIdAndUserId(int id, int userId) throws SQLException {
		String sql = "DELETE FROM draw_history WHERE id = ? AND user_id = ?";
		try (Connection conn = DbConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.setInt(2, userId);
			return ps.executeUpdate() == 1;
		}
	}

}
