package com.sparrow.support.file;

public class FileNameProperty {
    private String name;
    private String extension;
    private String extensionWithoutDot;
    private String directory;
    private String fullFileName;
    private Boolean image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getFullFileName() {
        return fullFileName;
    }

    public void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
    }

    public Boolean isImage() {
        return image;
    }

    public void setImage(Boolean image) {
        this.image = image;
    }

    public String getExtensionWithoutDot() {
        if (extensionWithoutDot == null) {
            this.extensionWithoutDot = this.extension.substring(1);
        }
        return extensionWithoutDot;
    }

    @Override
    public String toString() {
        return "FileNameProperty{" +
                "name='" + name + '\'' +
                ", extension='" + extension + '\'' +
                ", extensionWithoutDot='" + extensionWithoutDot + '\'' +
                ", directory='" + directory + '\'' +
                ", fullFileName='" + fullFileName + '\'' +
                ", image=" + image +
                '}';
    }
}
