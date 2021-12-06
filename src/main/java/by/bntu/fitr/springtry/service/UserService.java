package by.bntu.fitr.springtry.service;

import by.bntu.fitr.springtry.entity.Lot;
import by.bntu.fitr.springtry.entity.User;
import by.bntu.fitr.springtry.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * The interface provides action for user
 */
@Service
public interface UserService {
    /**
     * Service to sign in user
     *
     * @param login
     * @param password
     * @return
     * @throws ServiceException
     */
    User login(String login, String password) throws ServiceException;

    /**
     * Service to sign up user
     *
     * @param name
     * @param mail
     * @param login
     * @param password
     * @param role
     * @throws ServiceException
     * @return
     */
    User register(String name, String mail, String login, String password, UserRole role) throws ServiceException;

    /**
     * Service for buyer to make bid
     *
     * @param user
     * @param bid
     * @param lot
     * @throws ServiceException
     * @return
     */
    Lot makeBid(User user, String bid, Lot lot) throws ServiceException;

    /**
     * Service to find user by id
     *
     * @param id
     * @return found user
     * @throws ServiceException
     */
    User findUserById(long id) throws ServiceException;

    /**
     * Service to change user data (avatar, name, mail)
     *
     * @param userId
     * @param avatar
     * @param name
     * @param mail
     * @throws ServiceException
     * @return
     */
    User changeUserData(long userId, String avatar, String name, String mail) throws ServiceException;

    /**
     * Service to change user password
     *
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @throws ServiceException
     * @return
     */
    User changeUserPassword(long userId, String oldPassword, String newPassword) throws ServiceException;

    /**
     * Service to add some money to user account
     *
     * @param userId
     * @param payment
     * @throws ServiceException
     * @return
     */
    User addBalance(long userId, String payment) throws ServiceException;

    /**
     * Service to find list of users that has exact string in their name
     *
     * @param name
     * @param pageNumber number of page for paging
     * @param amountPerPage amount of users per one page
     * @return list of users or empty list if no users found
     * @throws ServiceException
     */
    Page<User> findUserByName(String name, int pageNumber, int amountPerPage) throws ServiceException;


    /**
     * Service to find list of all users
     *
     * @param pageNumber number of page for paging
     * @param amountPerPage amount of users per one page
     * @return list of users or empty list if no users found
     * @throws ServiceException
     */
    Page<User> findAll(int pageNumber, int amountPerPage) throws ServiceException;

}
