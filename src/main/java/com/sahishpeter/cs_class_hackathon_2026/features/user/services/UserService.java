package com.sahishpeter.cs_class_hackathon_2026.features.user.services;

import java.util.HashMap;
import java.util.Map;

import com.google.cloud.firestore.Firestore;
import com.sahishpeter.cs_class_hackathon_2026.lib.Firebase;

public class UserService {

    private final Firestore firestore;

    public UserService() {
        this.firestore = Firebase.firestore();
    }

    public void upsertUser(String userId, String firstName, String lastName) {
        
        if(userId == null) return;

        Map<String, Object> payload = new HashMap<>();
        payload.put("firstName", firstName);
        payload.put("lastName", lastName);

        firestore.collection("users").document(userId).set(payload);

    }
    
}
