package dao;

import java.sql.SQLException;
import java.util.List;

import model.DrawHistory;

public interface DrawHistoryDao {

	int insert(DrawHistory history) throws SQLException;
	
	List<DrawHistory> findByUserId(int userId, int limit) throws SQLException;
	
	List<DrawHistory> findAll(int limit) throws SQLException;
	
	boolean deleteById(int id) throws SQLException;
	
	boolean deleteByIdAndUserId(int id, int userId) throws SQLException;
}
