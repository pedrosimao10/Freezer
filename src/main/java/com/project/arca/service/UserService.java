package com.project.arca.service;

import com.project.arca.repository.*;

import com.project.arca.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public long total() { return userRepository.count();}

    public User getUserByName(String name){
        return userRepository.findByName(name);
    }

    public User getUserById(Integer id){
        return userRepository.findById(id);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public boolean accountExists(String email) {
        return userRepository.existsByEmail(email);
    }

}
