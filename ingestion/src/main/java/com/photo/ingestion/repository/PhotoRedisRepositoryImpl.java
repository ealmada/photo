package com.photo.ingestion.repository;

import com.google.common.collect.Sets;
import com.photo.ingestion.model.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Set;

@Repository
public class PhotoRedisRepositoryImpl implements PhotoRedisRepository {
    private static final String HASH_NAME = "organization";

    private RedisTemplate<String, Photo> redisTemplate;
    private HashOperations hashOperations;

    public PhotoRedisRepositoryImpl() {
        super();
    }

    @Autowired
    private PhotoRedisRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }


    @Override
    public void savePhoto(Photo photo) {
        hashOperations.put(HASH_NAME, photo.getId(), photo);
    }

    @Override
    public void updatePhoto(Photo photo) {
        hashOperations.put(HASH_NAME, photo.getId(), photo);
    }

    @Override
    public void deletePhoto(String photoId) {
        hashOperations.delete(HASH_NAME, photoId);
    }

    @Override
    public Photo findPhoto(String photoId) {
        return (Photo) hashOperations.get(HASH_NAME, photoId);
    }

    public Set<String> findPhotoByTerm(String pattern, int count){

        ScanOptions scanOptions;
        if (count > -1) {
            scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
        } else {
            scanOptions = ScanOptions.scanOptions().match(pattern).build();
        }
        ConvertingCursor<byte[], String> cursor = redisTemplate.executeWithStickyConnection((redisConnection) ->
                new ConvertingCursor<>(redisConnection.scan(scanOptions),
                        new StringRedisSerializer()::deserialize)
        );
        if (cursor != null) {
            Set<String> set = Sets.newHashSet();
            cursor.forEachRemaining(set::add);
            return set;
        }
        return Collections.emptySet();
    }
}
