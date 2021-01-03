package com.learn.OnlineStore.service;


import com.learn.OnlineStore.model.User;
import com.learn.OnlineStore.model.VerificationToken;
import com.learn.OnlineStore.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public VerificationToken findByUser(User user) {
        return verificationTokenRepository.findByUser(user);
    }

    @Override
    public void save(User user, String token) {
        VerificationToken v=new VerificationToken();
        v.setToken(token);
       v.setUser(user);
        v.setExpiryDate(caculateExpiryDate(24*60));
        verificationTokenRepository.save(v);
    }


    private Timestamp caculateExpiryDate(int expiryTimeInMinutes){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.MINUTE,expiryTimeInMinutes);
        return new Timestamp(cal.getTime().getTime());
    }

    @Override
    public void saveVT(VerificationToken verificationToken) {

        verificationToken.setExpiryDate(caculateExpiryDate(24*60));
        verificationTokenRepository.save(verificationToken);
    }
}
