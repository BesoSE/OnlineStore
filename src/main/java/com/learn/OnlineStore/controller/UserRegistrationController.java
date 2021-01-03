package com.learn.OnlineStore.controller;

import com.learn.OnlineStore.dto.UserDto;

import com.learn.OnlineStore.model.User;
import com.learn.OnlineStore.model.VerificationToken;
import com.learn.OnlineStore.service.UserService;
import com.learn.OnlineStore.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;

@Controller
public class UserRegistrationController {

    @Autowired
    private UserService userService;

   @Autowired
    private VerificationTokenService verificationTokenService;


    @GetMapping("/registration")
    public String FormRegistraion(Model model, UserDto userDto){
        model.addAttribute("user",userDto);
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@Valid UserDto userDto, BindingResult bindingResult, Model model, HttpServletRequest request,
                               RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            model.addAttribute("user",userDto);
            model.addAttribute("bindingResult",bindingResult);
            return "registration";
        }else if(userDto.checkPwd()==false){
            model.addAttribute("user",userDto);
            model.addAttribute("message","Molimo vas unseite istu sifru u oba slucaja");
            return "registration";
        } else{
            try{

                userService.save(userDto,request);
                redirectAttributes.addFlashAttribute("message","Poslan vam je mail za potvrdu vase email adrese.");
                return "redirect:/login";
            }catch(RuntimeException r){
                model.addAttribute("message", r.getMessage());
                model.addAttribute("user",userDto);
                return "registration";
            }
        }
    }

    @GetMapping("/activation")
    public String activation(@RequestParam("token") String token, Model model, HttpServletRequest request){
        VerificationToken verificationToken=verificationTokenService.findByToken(token);
        if(verificationToken==null){
            model.addAttribute("message","Vas kod nije vazeci");
        }else{
            User user=verificationToken.getUser();

            if(!user.isStatus()){
                Timestamp currentTimestamp=new Timestamp(System.currentTimeMillis());
                if(verificationToken.getExpiryDate().before(currentTimestamp)){
                    model.addAttribute("message","Vas kod je istekao, poslali smo vam novi verifikacijiski kod na email");
                    userService.sendEmail(user,request);
                }else{

                    userService.helpSave(user);
                    model.addAttribute("message","Vas akaunt je uspjesno aktiviran");

                }
            }else{
                model.addAttribute("message","Vas akaunt je vec aktiviran");
            }
        }
        return "activation";

    }

}
