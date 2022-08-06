package com.project.arca.service;

import com.project.arca.repository.FoodRepository;
import com.project.arca.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    public FoodService(FoodRepository foodRepository){
        this.foodRepository = foodRepository;
    }

    public long total() { return foodRepository.count();}

    public Food getFoodByName(String name){
        return foodRepository.findByName(name);
    }

    public Food getFoodById(long id){
        return foodRepository.findById(id);
    }
    
}
