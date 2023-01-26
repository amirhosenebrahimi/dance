package com.erymanthian.dance.controllers;

import com.erymanthian.dance.services.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class ResourceController {
    private final ResourceService service;
    @GetMapping(value = "/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource getResource(@PathVariable String id) {
        return service.getResource(id);
    }
}
