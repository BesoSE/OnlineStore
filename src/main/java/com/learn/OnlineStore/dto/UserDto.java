package com.learn.OnlineStore.dto;

import com.sun.istack.NotNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserDto {



    @NotNull
    @NotEmpty(message = "morate unijeti ime")
    private String firstName;
    @NotNull
    @NotEmpty(message = "morate unijeti prezime")
    private String lastName;
    @NotNull
    @NotEmpty(message = "morate unijeti email")
    @Email
    private String email;
    @NotNull
    @NotEmpty(message = "morate unijeti  sifru")
    @Size(min = 5, max = 20, message = "Sifra mora biti duza od 5 i kraca od 20 karaktera")
    private String password;
    @NotEmpty(message = "morate potvrditi sifru")
    private String repeatPwd;







    public UserDto() {
    }




    public boolean checkPwd(){
        if(this.password.equals(this.repeatPwd)){
            return true;
        }else{
            return false;
        }
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPwd() {
        return repeatPwd;
    }

    public void setRepeatPwd(String repeatPwd) {
        this.repeatPwd = repeatPwd;
    }


}
