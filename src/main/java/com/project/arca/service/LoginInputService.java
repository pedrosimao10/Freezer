package com.project.arca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.arca.model.*;
import com.project.arca.repository.*;

@Service
public class LoginInputService {

    @Autowired
    private LoginInputRepository loginInputRepository;

    @Autowired
    public LoginInputService(LoginInputRepository loginInputRepository){
        this.loginInputRepository = loginInputRepository;
    }

    public long total() { return loginInputRepository.count();}

    public LoginInput getUserByEmail(String email){
        return loginInputRepository.findByEmail(email);
    }
}
