package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.User;

import java.util.List;

public interface UserRelationInterface {

    List<User> getFriends (String userEmail);

    public void addFriend(String userEmail, String friendEmail);
}
