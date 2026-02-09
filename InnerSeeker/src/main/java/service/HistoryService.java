package service;

import java.sql.SQLException;
import java.util.List;

import model.DrawHistory;
import model.Role;

public interface HistoryService {

	List<DrawHistory> list(Integer userId, Role role, int limit) throws SQLException;
	
	boolean delete(Integer userId, Role role, int historyId) throws SQLException;
	
	String buildPrompt(DrawHistory h) throws SQLException;
}
