package by.bntu.fitr.springtry.service;


import by.bntu.fitr.springtry.entity.Lot;
import by.bntu.fitr.springtry.entity.User;

/**
 * The interface provides action for admin service
 */
public interface AdminService {

    /**
     * Method to ban user
     *
     * @param userId
     * @throws ServiceException
     */
    User ban(long userId) throws ServiceException;

    /**
     * Method to unban user
     *
     * @param userId
     * @throws ServiceException
     */
    User unban(long userId) throws ServiceException;

    /**
     * Method to submit that lot has been submitted
     *
     * @param lot
     * @throws ServiceException
     */
    Lot submitWinner(Lot lot) throws ServiceException;
}
