package com.project.arca.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.arca.model.Food;
import com.project.arca.model.LoginInput;
import com.project.arca.model.RegisterInput;
import com.project.arca.model.User;
import com.project.arca.repository.FoodRepository;
import com.project.arca.repository.LoginInputRepository;
import com.project.arca.repository.UserRepository;
import com.project.arca.service.FoodService;
import com.project.arca.service.LoginInputService;
import com.project.arca.service.UserService;
import org.springframework.beans.factory.ObjectFactory;

@Controller
public class ArcaController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodService foodService;

    @Autowired
    private LoginInputRepository loginInputRepository;

    @Autowired
    private LoginInputService loginInputService;

    @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    @ModelAttribute("LoginInput")
    public LoginInput getGreetingObject() {
        return new LoginInput();
    }

    @ModelAttribute("RegisterInput")
    public RegisterInput getRegisterInput() {
        return new RegisterInput();
    }

    @ModelAttribute("inputFood")
    public Food getGreetingObjectFood() {
        return new Food();
    }
    
    @GetMapping("/index")
    public String getAskIdentifier(Model model) {
      return "index";
    }

    @PostMapping("/index")
    public String greetingSubmit(@ModelAttribute LoginInput inputLogin, Model model) throws SQLException, ClassNotFoundException {
        HttpSession session = httpSessionFactory.getObject();
        String email = inputLogin.getEmail();
        String password = inputLogin.getPassword();

        if (email == "" || password == "") {
        model.addAttribute("error", "Todos os campos devem ser preenchidos");
        return "index";
        }

        User user = userService.getUserByEmail(email);
        if(user != null && (user.getPassword().equals(password))) {
            session.setAttribute("email", email);
            session.setAttribute("name", user.getName());
            model.addAttribute("name", user.getName());
            model.addAttribute("email", email);
            LoginInput loginInput = new LoginInput(email, password);
            loginInputRepository.saveAndFlush(loginInput);
            return "redirect:/home";
        }
    model.addAttribute("error", "Email ou Password Incorretoss");
    return "index";
  }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String postRegisterForm(@ModelAttribute RegisterInput RegisterInput, Model model) {

        String name = RegisterInput.getName();
        String email = RegisterInput.getEmail();
        String password = RegisterInput.getPassword();

        User user = new User(name, email, password);

        if (email == "" || name == "" || password == "") {
                model.addAttribute("error", "All fields must be filled!");
                return "register";
            }else{
                userRepository.save(user);
                return "redirect:/index";
        }
    }

    @GetMapping("/logout")
    public String logout(Model model) throws SQLException, ClassNotFoundException {
        List<LoginInput> login = new ArrayList<LoginInput>();
        login = loginInputRepository.findAll();
        String email = login.get(0).getEmail();
        String myDriver = "com.mysql.jdbc.Driver";
        String myUrl = "jdbc:mysql://localhost:3306/arca";
        Class.forName(myDriver);
        Connection conn = DriverManager.getConnection(myUrl, "root", "pedrito10");
        
        Statement st = conn.createStatement();
        st.executeUpdate(" DELETE FROM login_info WHERE email='"+ email + "'");

        conn.close();

        return "redirect:/index";
    }

    @GetMapping("/home")
    public String getDashboard( Model model) {
      List<Food> allFood = new ArrayList<Food>();
      int countCarne = 0;
      int countPeixe = 0;
      int countVegetal = 0;
      int countPizza = 0;
      int countGelado = 0;
      allFood = foodRepository.findAll();
      model.addAttribute("FoodList", allFood);
      for(int i = 0; i < allFood.size(); i++){
        System.out.println(allFood.get(i).getCategory().toString());
        if (allFood.get(i).getCategory().toString().equals("Carne")){
            countCarne = countCarne + allFood.get(i).getQuantity();
            model.addAttribute("NCarne", countCarne);
        }else if(allFood.get(i).getCategory().toString().equals("Peixe")){
            countPeixe = countPeixe + allFood.get(i).getQuantity();
            model.addAttribute("NPeixe", countPeixe);
        }else if(allFood.get(i).getCategory().toString().equals("Legume")){
            countVegetal = countVegetal + allFood.get(i).getQuantity();
            model.addAttribute("NVegetal",countVegetal);
        }else if(allFood.get(i).getCategory().toString().equals("Pizza")){
            countPizza = countPizza + allFood.get(i).getQuantity();
            model.addAttribute("NPizza", countPizza);
        }else if(allFood.get(i).getCategory().toString().equals("Sobremesa")){
            System.out.println("CHEGUEI");
            countGelado = countGelado + allFood.get(i).getQuantity();
            if (countGelado == 0){
                countGelado = 0;
            }
            model.addAttribute("NSobremesa", countGelado);
        }
      }
      List<LoginInput> login = new ArrayList<LoginInput>();
      login = loginInputRepository.findAll();
      User user = userService.getUserByEmail(login.get(0).getEmail());
      model.addAttribute("user", user);
      return "home"; 
    }

    @GetMapping("/addFoodCarne")
    public String addFoodCarne(Model model){
        return "addFoodCarne";
    }

    @PostMapping("/addFoodCarne")
    public String addFoodPostCarne(@ModelAttribute Food inputFood, Model model) {

        String name = inputFood.getName();
        Integer quantity = inputFood.getQuantity();

        List<LoginInput> login = new ArrayList<LoginInput>();
        login = loginInputRepository.findAll();
        User user = userService.getUserByEmail(login.get(0).getEmail());
        model.addAttribute("user", user);

        Food f = new Food("Carne", name, quantity, user.getId());


        if ( name == "" || quantity == null) {
            model.addAttribute("error", "Todos os campos devem ser preenchidos");
            return "addFoodCarne";
        }
        else{
            foodRepository.save(f);
            return "redirect:/carne";
        }
    }

    @GetMapping("/addFoodPeixe")
    public String addFoodPeixe(Model model){
        return "addFoodPeixe";
    }

    @PostMapping("/addFoodPeixe")
    public String addFoodPostPeixe(@ModelAttribute Food inputFood, Model model) {

        String name = inputFood.getName();
        Integer quantity = inputFood.getQuantity();

        List<LoginInput> login = new ArrayList<LoginInput>();
        login = loginInputRepository.findAll();
        User user = userService.getUserByEmail(login.get(0).getEmail());
        model.addAttribute("user", user);

        Food f = new Food("Peixe", name, quantity, user.getId());


        if (name == ""  || quantity == null) {
            model.addAttribute("error", "All fields must be filled!");
            return "addFoodPeixe";
        }
        else{
            foodRepository.save(f);
            return "redirect:/peixe";
        }
    }

    @GetMapping("/addFoodLegume")
    public String addFoodLegume(Model model){
        return "addFoodLegume";
    }

    @PostMapping("/addFoodLegume")
    public String addFoodPostLegume(@ModelAttribute Food inputFood, Model model) {

        String name = inputFood.getName();
        Integer quantity = inputFood.getQuantity();

        List<LoginInput> login = new ArrayList<LoginInput>();
        login = loginInputRepository.findAll();
        User user = userService.getUserByEmail(login.get(0).getEmail());
        model.addAttribute("user", user);

        Food f = new Food("Legume", name, quantity, user.getId());


        if ( name == "" || quantity == null) {
            model.addAttribute("error", "Todos os campos devem estar preenchidos");
            return "addFoodLegume";
        }
        else{
            foodRepository.save(f);
            return "redirect:/legumes";
        }
    }

    @GetMapping("/addFoodPizza")
    public String addFoodPizza(Model model){
        return "addFoodPizza";
    }

    @PostMapping("/addFoodPizza")
    public String addFoodPostPizza(@ModelAttribute Food inputFood, Model model) {

        String name = inputFood.getName();
        Integer quantity = inputFood.getQuantity();

        List<LoginInput> login = new ArrayList<LoginInput>();
        login = loginInputRepository.findAll();
        User user = userService.getUserByEmail(login.get(0).getEmail());
        model.addAttribute("user", user);

        Food f = new Food("Pizza", name, quantity, user.getId());


        if (name == "" || quantity == null) {
            model.addAttribute("error", "Todos os campos devem estar preenchidos");
            return "addFoodPizza";
        }
        else{
            foodRepository.save(f);
            return "redirect:/pizza";
        }
    }

    @GetMapping("/addFoodSobremesa")
    public String addFoodSobremesa(Model model){
        return "addFoodSobremesa";
    }

    @PostMapping("/addFoodSobremesa")
    public String addFoodPostSobremesa(@ModelAttribute Food inputFood, Model model) {

        String name = inputFood.getName();
        Integer quantity = inputFood.getQuantity();

        List<LoginInput> login = new ArrayList<LoginInput>();
        login = loginInputRepository.findAll();
        User user = userService.getUserByEmail(login.get(0).getEmail());
        model.addAttribute("user", user);

        Food f = new Food("Sobremesa", name, quantity, user.getId());


        if (name == "" || quantity == null) {
            model.addAttribute("error", "Todos os campos devem estar preenchidos");
            return "addFoodSobremesa";
        }
        else{
            foodRepository.save(f);
            return "redirect:/sobremesa";
        }
    }

    @PostMapping("/deleteCarne/{id}")
    public String deleteCarne(@PathVariable long id) throws SQLException, ClassNotFoundException{
        foodRepository.deleteById(id);
        return "redirect:/carne";
    }

    @PostMapping("/deletePeixe/{id}")
    public String deletePeixe(@PathVariable long id) throws SQLException, ClassNotFoundException{
        foodRepository.deleteById(id);
        return "redirect:/peixe";
    }

    @PostMapping("/deleteLegume/{id}")
    public String deleteLegume(@PathVariable long id) throws SQLException, ClassNotFoundException{
        foodRepository.deleteById(id);
        return "redirect:/legumes";
    }

    @PostMapping("/deletePizza/{id}")
    public String deletePizza(@PathVariable long id) throws SQLException, ClassNotFoundException{
        foodRepository.deleteById(id);
        return "redirect:/pizza";
    }

    @PostMapping("/deleteSobremesa/{id}")
    public String deleteSobremesa(@PathVariable long id) throws SQLException, ClassNotFoundException{
        foodRepository.deleteById(id);
        return "redirect:/sobremesa";
    }

    @GetMapping("/carne")
    public String getCarne( Model model) {
        List<LoginInput> login = new ArrayList<LoginInput>();
        login = loginInputRepository.findAll();
        User user = userService.getUserByEmail(login.get(0).getEmail());
        model.addAttribute("user", user);

        List<Food> allFood = new ArrayList<Food>();
        List<Food> foodPerUser = new ArrayList<Food>();

        allFood = foodRepository.findAll();
        for (Food food : allFood){
                if (food.getUser_id()==user.getId()){
                    if (food.getCategory().toString().equals("Carne")){
                            foodPerUser.add(food);
                        }
                }

            
        }
        model.addAttribute("Carnes", foodPerUser);
        return "carne";
    }

    @GetMapping("/peixe")
    public String getPeixe( Model model) {
        List<LoginInput> login = new ArrayList<LoginInput>();
        login = loginInputRepository.findAll();
        User user = userService.getUserByEmail(login.get(0).getEmail());
        model.addAttribute("user", user);

        List<Food> allFood = new ArrayList<Food>();
        List<Food> foodPerUser = new ArrayList<Food>();

        allFood = foodRepository.findAll();
        for (Food food : allFood){
                if (food.getUser_id()==user.getId()){
                    if (food.getCategory().toString().equals("Peixe")){
                            foodPerUser.add(food);
                        }
                }

            
        }
        model.addAttribute("Peixes", foodPerUser);
        return "peixe";
    }

    @GetMapping("/legumes")
    public String getLegumes( Model model) {
        List<LoginInput> login = new ArrayList<LoginInput>();
        login = loginInputRepository.findAll();
        User user = userService.getUserByEmail(login.get(0).getEmail());
        model.addAttribute("user", user);

        List<Food> allFood = new ArrayList<Food>();
        List<Food> foodPerUser = new ArrayList<Food>();

        allFood = foodRepository.findAll();
        for (Food food : allFood){
                if (food.getUser_id()==user.getId()){
                    if (food.getCategory().toString().equals("Legume")){
                            foodPerUser.add(food);
                        }
                }

            
        }
        model.addAttribute("Legumes", foodPerUser);
        return "legumes";
    }

    @GetMapping("/pizza")
    public String getPizza( Model model) {
        List<LoginInput> login = new ArrayList<LoginInput>();
        login = loginInputRepository.findAll();
        User user = userService.getUserByEmail(login.get(0).getEmail());
        model.addAttribute("user", user);

        List<Food> allFood = new ArrayList<Food>();
        List<Food> foodPerUser = new ArrayList<Food>();

        allFood = foodRepository.findAll();
        for (Food food : allFood){
                if (food.getUser_id()==user.getId()){
                    if (food.getCategory().toString().equals("Pizza")){
                            foodPerUser.add(food);
                        }
                }

            
        }
        model.addAttribute("Pizzas", foodPerUser);
        return "pizza";
    }

    @GetMapping("/sobremesa")
    public String getSobremesa( Model model) {
        List<LoginInput> login = new ArrayList<LoginInput>();
        login = loginInputRepository.findAll();
        User user = userService.getUserByEmail(login.get(0).getEmail());
        model.addAttribute("user", user);

        List<Food> allFood = new ArrayList<Food>();
        List<Food> foodPerUser = new ArrayList<Food>();

        allFood = foodRepository.findAll();
        for (Food food : allFood){
                if (food.getUser_id()==user.getId()){
                    if (food.getCategory().toString().equals("Sobremesa")){
                            foodPerUser.add(food);
                        }
                }

            
        }
        model.addAttribute("Sobremesas", foodPerUser);
        return "sobremesa";
    }
}
