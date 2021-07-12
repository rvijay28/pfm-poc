package com.hsvj.pfm.service;

import com.hsvj.pfm.dto.UserSummary;
import com.hsvj.pfm.exception.EntityAlreadyExistsException;
import com.hsvj.pfm.exception.EntityNotFoundException;
import com.hsvj.pfm.model.User;
import com.hsvj.pfm.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Create User
     * @param user
     * @return
     */
    public UserSummary createOrUpdateUser(User user) {
        User createdOrUpdatedUser = null;
        if (null == user.getId()) {
            //create new user
            if (findByLogin(user.getLogin()) != null) {
                throw new EntityAlreadyExistsException("Login is taken!");
            }
            createdOrUpdatedUser = repository.save(user);
        } else {
            Optional<User> data = repository.findById(user.getId());
            if (!data.isPresent()) {
                throw new EntityNotFoundException("User not found with the id: " + user.getId());
            }
            User userToUpdate = data.get();
            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setLogin(user.getLogin());
            createdOrUpdatedUser = repository.save(userToUpdate);
        }
        return convertToUserSummary(Optional.ofNullable((createdOrUpdatedUser)));
    }
    /**
     * Delete user by user id
     * @param id
     */
    public void deleteUser(String id) {
        Optional<User> user = repository.findById(id);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("User not found with the id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Delete user by user object
     * @param user
     */
    public void deleteUser(User user) {
        repository.delete(user);
    }

    /**
     * Find user by login
     * @param login
     * @return
     */
    public UserSummary findByLogin(String login) {
       return convertToUserSummary(Optional.ofNullable(repository.findByLogin(login)));
    }



    /**
     * Get all users
     * @return
     */
    public List<UserSummary> getAllUsers() {
        return convertToUserSummary(repository.findAll());
    }

    /**
     * Get User by user id
     * @param id
     * @return
     */
    public UserSummary getUserById(String id) {
        Optional<User> user = repository.findById(id);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("User not found with the id: " + id);
        }
        return convertToUserSummary(user);
    }

    /**
     * Convert list of User -> UserSummary object, exclude password
     * @param users
     * @return list of userSummary
     */
    private List<UserSummary> convertToUserSummary(List<User> users){
        return users.stream()
                .map(user -> convertToUserSummary(Optional.ofNullable(user)))
                .collect(Collectors.toList());
    }

    /**
     * Conver to summary object, exclude password
     * @param user
     * @return userSummary
     */
    private UserSummary convertToUserSummary(Optional<User> user){
        UserSummary userSummary = user.map(u -> modelMapper.map(u, UserSummary.class)).orElse(null);
        return userSummary;
    }

}
