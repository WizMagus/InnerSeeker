package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import config.DbConnection;
import dao.UserDao;
import model.Role;
import model.User;

public class UserDaoImpl implements UserDao {

	@Override
	public int insert(User user) throws SQLException {
		String sql = "INSERT INTO users (username, password, hint, hint_answer) VALUES (?, ?, ?, ?)";
		try (Connection conn = DbConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getHint());
			ps.setString(4, user.getHintAnswer());

			int affected = ps.executeUpdate();
			if (affected != 1) {
				throw new SQLException("insert user fail: affected rows = " + affected);
			}
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					int id = keys.getInt(1);
					user.setId(id);
					return id;
				}
			}
		}
		throw new SQLException("insert users fail: failed to get generated key.");
	}

	@Override
	public User findByUsername(String username) throws SQLException {
		String sql = "SELECT * FROM users WHERE username = ?";
		try (Connection conn = DbConnection.getConnection(); 
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapRow(rs);
				}
			}
		}
		return null;
	}

	@Override
	public User findById(int id) throws SQLException {
		String sql = "SELECT * FROM users WHERE id = ?";
		try (Connection conn = DbConnection.getConnection(); 
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapRow(rs);
				}
			}
		}
		return null;
	}

	@Override
	public List<User> findAll() throws SQLException {
		String sql = "SELECT * FROM users ORDER BY id ASC";

		List<User> list = new ArrayList<>();

		try (Connection conn = DbConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.add(mapRow(rs));
			}
		}

		return list;
	}

	@Override
	public boolean update(User user) throws SQLException {
		String sql = "UPDATE users SET username = ?, password = ?, hint = ?, hint_answer = ? WHERE id = ?";
		try (Connection conn = DbConnection.getConnection(); 
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getHint());
			ps.setString(4, user.getHintAnswer());
			ps.setInt(5, user.getId());

			return ps.executeUpdate() == 1;
		}

	}

	@Override
	public boolean deleteById(int id) throws SQLException {
		String sql = "DELETE FROM users WHERE id = ?";
		try (Connection conn = DbConnection.getConnection(); 
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, id);
			return ps.executeUpdate() == 1;
		}

	}

	private User mapRow(ResultSet rs) throws SQLException {
		User u = new User();
		u.setId(rs.getInt("id"));
		u.setUsername(rs.getString("username"));
		u.setPassword(rs.getString("password"));
		u.setHint(rs.getString("hint"));
		u.setHintAnswer(rs.getString("hint_answer"));
		String roleStr = rs.getString("role");
		Role role = Role.USER;
		if (roleStr != null && !roleStr.isBlank()) {
			try {
				role = Role.valueOf(roleStr.trim().toUpperCase());
			} catch (IllegalArgumentException ex) {
				role = Role.USER;
			}
		}
		u.setRole(role);
		u.setCreatedAt(rs.getTimestamp("created_at"));
		return u;
	}

}
