package service.impl;

import java.sql.SQLException;

import dao.UserDao;
import exception.AuthException;
import exception.AuthException.AuthError;
import model.User;
import service.AuthService;

public class AuthServiceImpl implements AuthService {

	private final UserDao userDao;

	public AuthServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	private static String normalize(String s) {
		return s == null ? null : s.trim();
	}

	private static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	@Override
	public User register(String username, String password, String hint, String hintAnswer) throws SQLException {
		String u = normalize(username);
		String p = normalize(password);
		
		if (isBlank(u)) {
			throw new AuthException(AuthError.USERNAME_REQUIRED);
		}
		if (isBlank(p)) {
			throw new AuthException(AuthError.PASSWORD_REQUIRED);
		}
		if (userDao.findByUsername(u) != null) {
			throw new AuthException(AuthError.USERNAME_TAKEN);
		}
		
		User newUser = new User(u, p, hint, hintAnswer);
		int newId = userDao.insert(newUser);
		return userDao.findById(newId);
	}

	@Override
	public User login(String username, String password) throws SQLException {
		String u = normalize(username);
		String p = normalize(password);
		
		if (isBlank(u)) {
			throw new AuthException(AuthError.USERNAME_REQUIRED);
		}
		if (isBlank(p)) {
			throw new AuthException(AuthError.PASSWORD_REQUIRED);
		}	
		
		User dbUser = userDao.findByUsername(u);
		if (dbUser == null) {
			throw new AuthException(AuthError.INVALID_CREDENTIALS);
		}
		if (!p.equals(dbUser.getPassword())) {
			throw new AuthException(AuthError.INVALID_CREDENTIALS);
		}
		return dbUser;
	}

}
