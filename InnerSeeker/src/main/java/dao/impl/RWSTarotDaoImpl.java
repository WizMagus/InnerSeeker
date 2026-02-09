package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.DbConnection;
import dao.RWSTarotDao;
import model.RWSTarot;

public class RWSTarotDaoImpl implements RWSTarotDao {

	private RWSTarot mapRow(ResultSet rs) throws SQLException {
		RWSTarot t = new RWSTarot();
		t.setId(rs.getInt("id"));
		t.setCardId(rs.getString("card_id"));
		t.setCardNameZh(rs.getString("card_name_zh"));
		t.setCardNameEn(rs.getString("card_name_en"));
		t.setSuit(rs.getString("suit"));
		return t;
	}

	@Override
	public RWSTarot findById(int id) throws SQLException {
		String sql = "SELECT * FROM rws_tarot WHERE id = ?";
		try (Connection conn = DbConnection.getConnection(); 
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? mapRow(rs) : null;
			}
		}
	}

	@Override
	public List<RWSTarot> findAll() throws SQLException {
		String sql = "SELECT * FROM rws_tarot ORDER BY id";
		List<RWSTarot> list = new ArrayList<>();
		try (Connection conn = DbConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.add(mapRow(rs));
			}
		}
		return list;
	}

}
