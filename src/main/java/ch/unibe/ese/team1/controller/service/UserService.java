package ch.unibe.ese.team1.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.UserDao;

/**
 * Handles all database actions concerning users.
 */
@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;

	/** Gets the user with the given username. */
	@Transactional
	public User findUserByUsername(String username) {
		return userDao.findByUsername(username);
	}
	
	/** Gets the user with the given id. */
	@Transactional
	public User findUserById(long id) {
		return userDao.findUserById(id);
	}

	/** Set the user premium. */
	@Transactional
	public void setUserToPremium(User user, boolean toPremium) {
		user.setPremium(toPremium);
		userDao.save(user);
	}

	public void dislike(User ratingPerson) {
		ratingPerson.setDislikes(ratingPerson.getDislikes() + 1);
		userDao.save(ratingPerson);
	}

	public void like(User ratingPerson) {
		ratingPerson.setLikes(ratingPerson.getLikes() + 1);
		userDao.save(ratingPerson);
	}
}
