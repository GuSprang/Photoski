/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.photoski.storage;

/**
 *
 * @author gusta
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço para gerenciar arquivos enviados.
 */
public class StorageService {

    private final Path uploadDir;

    public StorageService() throws IOException {
        // Define a pasta uploads dentro do webapp
        this.uploadDir = Paths.get(System.getProperty("user.dir"), "web", "uploads");
        // Cria a pasta se não existir
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    /**
     * Retorna a lista de arquivos existentes
     */
    public List<String> loadAll() {
        List<String> fileNames = new ArrayList<>();
        File[] files = uploadDir.toFile().listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isFile()) {
                    fileNames.add(f.getName());
                }
            }
        }
        return fileNames;
    }

    /**
     * Salva um arquivo na pasta uploads a partir de um array de bytes
     */
    public void saveFile(byte[] bytes, String fileName) throws IOException {
        Path target = uploadDir.resolve(fileName);
        Files.write(target, bytes);
    }

    /**
     * Retorna o caminho completo da pasta uploads
     */
    public Path getUploadDir() {
        return uploadDir;
    }
}