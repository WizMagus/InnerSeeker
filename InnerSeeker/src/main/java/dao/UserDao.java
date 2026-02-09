package dao;

import java.sql.SQLException;
import java.util.List;

import model.User;

public interface UserDao {

	// create
	int insert(User user) throws SQLException;

	// read
	User findByUsername(String username) throws SQLException;

	User findById(int id) throws SQLException;

	List<User> findAll() throws SQLException;

	// update
	boolean update(User user) throws SQLException;

	// delete
	boolean deleteById(int id) throws SQLException;

}
