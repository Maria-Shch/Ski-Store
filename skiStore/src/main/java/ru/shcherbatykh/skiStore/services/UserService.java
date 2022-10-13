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

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, @Lazy BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public List<User> getUsers() {
        return userRepository.findAll();
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
        return userRepository.findByUsername(username).orElse(null);
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
    public void updateHouseNumber(long idUser, int newHouseNumber) {
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
