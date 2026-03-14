package com.example.springboot.service;

public interface AiService {
    String getAnswer(String question);
    String queryDatabase(String questionType, String keyword);
}