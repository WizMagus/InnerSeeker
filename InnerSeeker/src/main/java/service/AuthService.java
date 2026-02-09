package service;

import java.sql.SQLException;

import model.User;

public interface AuthService {
	User register(String username, String password, String hint, String hintAnser) throws SQLException;
	User login(String username, String password) throws SQLException;

}
