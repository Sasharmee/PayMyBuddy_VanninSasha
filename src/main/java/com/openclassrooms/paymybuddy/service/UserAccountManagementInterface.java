package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.User;
import jakarta.transaction.Transactional;

public interface UserAccountManagementInterface {


    User registerUser(String username, String email, String password);


    User updateUserInformations(String username, String email, String password);
}
