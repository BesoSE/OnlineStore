package com.learn.OnlineStore.service;

import com.learn.OnlineStore.dto.UserDto;
import com.learn.OnlineStore.model.Role;
import com.learn.OnlineStore.model.User;

import com.learn.OnlineStore.model.VerificationToken;
import com.learn.OnlineStore.repository.RoleRepository;
import com.learn.OnlineStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private VerificationTokenService verificationTokenService;


   @Autowired
    private EmailService emailService;

    @Override
    public User save(UserDto userDto, HttpServletRequest request) {
        Optional<User> optional= Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
        if(optional.isPresent()){
            throw new RuntimeException("Email se koristi");
        }else{
            User user=new User();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            Role role;
            if(roleRepository.findByName("ROLE_USER")!=null){
                role=roleRepository.findByName("ROLE_USER");
            }else{
                role=new Role();
                role.setName("ROLE_USER");
            }
            user.setRoles(new HashSet<Role>(Arrays.asList(role)));
            user.setStatus(false);

        try{
                    String token=UUID.randomUUID().toString();
                    verificationTokenService.save(user,token);
                    String url=emailService.getSiteUrl(request);
                    emailService.sendHtmlEmail(user,url);


                }catch (Exception e){
                    e.printStackTrace();
                }





            return userRepository.save(user);
        }

    }


    @Override
    public User helpSave(User user){
        user.setStatus(true);
       return  userRepository.save(user);
    }


    @Override
    public void sendEmail(User user, HttpServletRequest request) {
        try{
            VerificationToken vf=verificationTokenService.findByUser(user);
            String token=UUID.randomUUID().toString();
            vf.setToken(token);
            verificationTokenService.saveVT(vf);
            String url=emailService.getSiteUrl(request);
            emailService.sendHtmlEmail(user,url);




        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user=userRepository.findByEmail(s);
        if(user.isStatus()) {
            if (user == null) {
                throw new RuntimeException("Pogresan email ili sifra");

            }

            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
        }else{
            throw new RuntimeException("Potvridte vasu prijavu na mailu");
        }
    }

    public Collection<? extends  GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
        return roles.stream().map(role->new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
    }

    @Override
    public User findById(Long id) {
        Optional<User> optional=userRepository.findById(id);
        User user;
        if(optional.isPresent()){
            user=optional.get();
        }else{
            throw new RuntimeException("There is no user with id: "+id);
        }
        return user;
    }
}
