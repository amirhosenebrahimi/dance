package com.erymanthian.dance.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ResourceService {
    private final Path path;

    public Resource getResource(String id) {
        return new FileSystemResource(path.resolve(id));
    }
}
