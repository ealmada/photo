package com.photo.ingestion.controllers;

import com.photo.ingestion.services.PhotoService;
import com.photo.ingestion.config.ServiceConfig;
import com.photo.ingestion.model.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping
public class PhotoServiceController {
    @Autowired
    private PhotoService photoService;

    @Autowired
    private ServiceConfig serviceConfig;

    private static final Logger logger = LoggerFactory.getLogger(PhotoServiceController.class);

    @RequestMapping(value="/search/{searchTerm}",method = RequestMethod.GET)
    public Set<String> getPhotosBySearchTerm(@PathVariable("searchTerm") String searchTerm, int n) {
        logger.debug("Entered to PhotoService-controller: {} ");
        return photoService.getPhotoByTerm(searchTerm, n);
    }

}