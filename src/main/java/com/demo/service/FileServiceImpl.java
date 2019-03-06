package com.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public InputStream getFile(String url) {
        logger.debug("In getFile with '{}'", url);
        InputStream inputStream = null;
        try {
            URL urlObject = new URL(url);
            URLConnection urlConnection = urlObject.openConnection();
            inputStream = urlConnection.getInputStream();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.debug("Out getFile with '{}'", inputStream);
        return Optional
                .ofNullable(inputStream)
                .orElseThrow(()-> new NoSuchElementException("Error accessing file list"));
    }

    @Override
    public List<?> getListFromFile(String url, TypeReference<?> typeReference) {
        logger.debug("In getListFromFile with '{}' '{}'", url, typeReference);
        Optional.ofNullable(typeReference)
                .orElseThrow(()-> new NoSuchElementException("Type reference not provided"));

        InputStream inputStream = this.getFile(url);
        List<?> list = null;
        ObjectMapper mapper = new ObjectMapper();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            list = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    list.add(mapper.readValue(line, typeReference));
                } catch (Exception e) {
                    logger.error("Error parse line '{}'", line);
                    logger.error(e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.debug("Out getListFromFile with '{}'", list);
        return Optional
                .ofNullable(list)
                .orElseThrow(()-> new NoSuchElementException("Error reading file list"));
    }
}