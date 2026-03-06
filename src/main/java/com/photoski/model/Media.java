/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.photoski.model;

/**
 *
 * @author gusta
 */

public class Media {
        //atributos
    private String id;
    private String fileName;
    private String filePath;
    private String type;
    private long size;

    public Media() {
    }
        //Construtor
    public Media(String id, String fileName, String filePath, String type, long size) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.type = type;
        this.size = size;
    }
        //Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
