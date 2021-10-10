package com.criativoweb.libraryapi.api.service;

import org.springframework.stereotype.Service;

import java.util.List;

public interface EmailService {
    public void sendMails(List<String> emailsList, String message);
}
