package dao;

import java.sql.SQLException;
import java.util.List;

import model.RWSTarot;

public interface RWSTarotDao {

	RWSTarot findById(int id) throws SQLException;

	List<RWSTarot> findAll() throws SQLException;

}
