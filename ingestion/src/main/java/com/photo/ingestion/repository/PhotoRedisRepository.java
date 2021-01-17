package com.photo.ingestion.repository;

import com.photo.ingestion.model.Photo;

import java.util.Set;

public interface PhotoRedisRepository {
    void savePhoto(Photo photo);
    void updatePhoto(Photo photo);
    void deletePhoto(String photoId);
    Photo findPhoto(String photoId);

    Set<String> findPhotoByTerm(String terms, int count);
}
