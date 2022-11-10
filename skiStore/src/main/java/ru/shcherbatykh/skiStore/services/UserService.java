package ru.shcherbatykh.skiStore.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.classes.Role;
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

    // This method DOES NOT change the username (email) if a new value arrives.
    // This is done due to the nuances of working with SpringSecurity
    public User updateUserPersonalData(User newUser){
        User oldUser =  getUser(newUser.getId());

        if(!Objects.equals(oldUser.getName(), newUser.getName()))
            oldUser.setName(newUser.getName());

        if(!Objects.equals(oldUser.getLastname(), newUser.getLastname()))
            oldUser.setLastname(newUser.getLastname());

        if(!Objects.equals(oldUser.getPatronymic(), newUser.getPatronymic()))
            oldUser.setPatronymic(newUser.getPatronymic());

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

        return oldUser;
    }
}
