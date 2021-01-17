package com.photo.ingestion.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.photo.ingestion.clients.PhotoRestTemplateClient;
import com.photo.ingestion.config.ServiceConfig;
import com.photo.ingestion.model.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PhotoService {

    @Autowired
    ServiceConfig config;

    @Autowired
    PhotoRestTemplateClient photoRestTemplateClient;

    @HystrixCommand
    public Photo getPhoto(String photoId) {
        return photoRestTemplateClient.getPhoto(photoId);
    }

    public Set<String> getPhotoByTerm(String searchTerm, int count) {
        //need to create an index on every term
        return photoRestTemplateClient.getPhotoByTerm(searchTerm, count);
    }


}
