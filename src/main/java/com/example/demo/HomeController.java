package com.example.demo;

import java.util.Map;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
public class HomeController {
    @Autowired
    DogRepository dogRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String listDogs(Model model){
        model.addAttribute("dogs", dogRepository.findAll());
        return "list";
    }
    @GetMapping("/add")
    public String newDog(Model model){
        model.addAttribute("dog",new Dog());
        return "form";
    }
    @PostMapping("/add")
    public String processDog(@ModelAttribute Dog dog, @RequestParam("file")MultipartFile file){
        if(file.isEmpty()){
            return "redirect:/add";
        }
        try {
            Map uploadResult = cloudc.upload(file.getBytes(),ObjectUtils.asMap("resourcetype","auto"));
            dog.setImage(uploadResult.get("url").toString());
            dogRepository.save(dog);
        } catch (IOException e){
            e.printStackTrace();
            return "redirect:/add";
        }
        return "redirect:/";
    }
}
