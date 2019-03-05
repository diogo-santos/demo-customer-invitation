package com.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.InputStream;
import java.util.List;

public interface FileService {
    InputStream getFile(String url);
    List<?> getListFromFile(String url, TypeReference<?> typeReference);
}