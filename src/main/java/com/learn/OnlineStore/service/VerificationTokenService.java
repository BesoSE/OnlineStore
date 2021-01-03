package com.learn.OnlineStore.service;

import com.learn.OnlineStore.model.User;
import com.learn.OnlineStore.model.VerificationToken;

;

public interface VerificationTokenService {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
    void save(User user, String token);
    void saveVT(VerificationToken verificationToken);

}
