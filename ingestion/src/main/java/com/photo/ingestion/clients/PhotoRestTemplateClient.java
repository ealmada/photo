package com.photo.ingestion.clients;

import com.photo.ingestion.model.Photo;
import com.photo.ingestion.repository.PhotoRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import java.util.List;
import java.util.Set;

@Component
public class PhotoRestTemplateClient {

    @Autowired
    OAuth2RestTemplate restTemplate;

    @Autowired
    PhotoRedisRepository photoRedisRepo;

    private static final Logger logger = LoggerFactory.getLogger(PhotoRestTemplateClient.class);

    private Photo checkRedisCache(String photoId) {
        try {
            return photoRedisRepo.findPhoto(photoId);
        }
        catch (Exception ex){
            logger.error("Error encountered while trying to retrieve photo {} check Redis Cache.  Exception {}", photoId, ex);
            return null;
        }
    }

    private void cachePhotoObject(Photo photo) {
        try {
            photoRedisRepo.savePhoto(photo);
        }catch (Exception ex){
            logger.error("Unable to cache photo {} in Redis. Exception {}", photo.getId(), ex);
        }
    }

    public Photo getPhoto(String photoId){
        //logger.debug("In Photo Service.getPhoto: {}", UserContext.getCorrelationId());

        Photo photo = checkRedisCache(photoId);

        if (photo!=null){
            logger.debug("I have successfully retrieved a photo {} from the redis cache: {}", photoId, photo);
            return photo;
        }

        logger.debug("Unable to locate photo from the redis cache: {}.", photoId);

        ResponseEntity<Photo> restExchange =
                restTemplate.exchange(
                        "http://interview.agileengine.com/images/{photoId}",
                        HttpMethod.GET,
                        null, Photo.class, photoId);

        /*Save the record from cache*/
        photo = restExchange.getBody();

        if (photo!=null) {
            cachePhotoObject(photo);
        }

        return photo;
    }


    public Set<String> getPhotoByTerm(String terms, int count) {
        return photoRedisRepo.findPhotoByTerm(terms, count);
    }
}
