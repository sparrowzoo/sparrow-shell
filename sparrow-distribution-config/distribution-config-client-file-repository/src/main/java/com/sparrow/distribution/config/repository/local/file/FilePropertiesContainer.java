package com.sparrow.distribution.config.repository.local.file;

import com.sparrow.distribution.config.repository.PropertiesContainer;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FilePropertiesContainer implements PropertiesContainer {
    private FileRepository fileRepository;

    public void setFileRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    private Map<String, Properties> container = new ConcurrentHashMap<>();

    @Override
    public void put(String fileName) {
        this.container.put(fileName, this.fileRepository.read(fileName));
    }

    @Override
    public Properties read(String fileName) {
        return this.container.get(fileName);
    }

    @Override
    public void remove(String fileName) {
        this.container.remove(fileName);
    }

    @Override
    public Set<String> getAllFileName() {
        return this.container.keySet();
    }
}
