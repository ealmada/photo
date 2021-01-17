package com.photo.ingestion.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Photo implements Serializable {
    String id;
    String author;
    String camera;
    String cropped_picture;
    String full_picture;

}