package ru.shcherbatykh.skiStore.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.classes.Role;
import ru.shcherbatykh.skiStore.models.City;
import ru.shcherbatykh.skiStore.models.User;
import ru.shcherbatykh.skiStore.repositories.UserRepository;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, @Lazy BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public User getUser(long idUser) {
        return userRepository.getUserById(idUser);
    }

    @Transactional
    public void addUser(User newUser) {
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
    }

    @Transactional
    public User findByUsername(String username){
        return userRepository.getUserByUsername(username).orElse(null);
    }

    @Transactional
    public User getUserByUserDetails(UserDetails userDetails){
        return findByUsername(userDetails.getUsername());
    }

    @Transactional
    public Role getRoleByUserDetails(UserDetails userDetails){
        if (userDetails!=null)
            return getUserByUserDetails(userDetails).getRole();
        else
            return Role.GUEST;
    }

    public void updateUserPersonalData(User newUser){
        User oldUser =  getUser(newUser.getId());

        if(!Objects.equals(oldUser.getName(), newUser.getName()))
            oldUser.setName(newUser.getName());

        if(!Objects.equals(oldUser.getLastname(), newUser.getLastname()))
            oldUser.setLastname(newUser.getLastname());

        if(!Objects.equals(oldUser.getPatronymic(), newUser.getPatronymic()))
            oldUser.setPatronymic(newUser.getPatronymic());

        if(!Objects.equals(oldUser.getUsername(), newUser.getUsername()))
            oldUser.setUsername(newUser.getUsername());

        if(!Objects.equals(oldUser.getPhoneNumber(), newUser.getPhoneNumber()))
            oldUser.setPhoneNumber(newUser.getPhoneNumber());

        if(!Objects.equals(oldUser.getCity(), newUser.getCity()))
            oldUser.setCity(newUser.getCity());

        if(!Objects.equals(oldUser.getStreetName(), newUser.getStreetName()))
            oldUser.setStreetName(newUser.getStreetName());

        if(!Objects.equals(oldUser.getHouseNumber(), newUser.getHouseNumber()))
            oldUser.setHouseNumber(newUser.getHouseNumber());

        if(!Objects.equals(oldUser.getFlatNumber(), newUser.getFlatNumber()))
            oldUser.setFlatNumber(newUser.getFlatNumber());

        userRepository.save(oldUser);
    }

    @Transactional
    public void updateName(long idUser, String newName) {
        User user = getUser(idUser);
        user.setName(newName);
        userRepository.save(user);
    }

    @Transactional
    public void updatePatronymic(long idUser, String newPatronymic) {
        User user = getUser(idUser);
        user.setPatronymic(newPatronymic);
        userRepository.save(user);
    }

    @Transactional
    public void updatePhoneNumber(long idUser, String newPhoneNumber) {
        User user = getUser(idUser);
        user.setPhoneNumber(newPhoneNumber);
        userRepository.save(user);
    }

    @Transactional
    public void updateCity(long idUser, City newCity) {
        User user = getUser(idUser);
        user.setCity(newCity);
        userRepository.save(user);
    }

    @Transactional
    public void updateStreetName(long idUser, String newStreetName) {
        User user = getUser(idUser);
        user.setStreetName(newStreetName);
        userRepository.save(user);
    }

    @Transactional
    public void updateHouseNumber(long idUser, String newHouseNumber) {
        User user = getUser(idUser);
        user.setHouseNumber(newHouseNumber);
        userRepository.save(user);
    }

    @Transactional
    public void updateFlatNumber(long idUser, int newFlatNumber) {
        User user = getUser(idUser);
        user.setFlatNumber(newFlatNumber);
        userRepository.save(user);
    }
}
