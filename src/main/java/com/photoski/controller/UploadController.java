package com.photoski.controller;

/**
 *
 * @author gusta
 */

import com.photoski.storage.StorageService;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/upload")
@MultipartConfig
public class UploadController extends HttpServlet {

    private StorageService storageService;

    @Override
    public void init() throws ServletException {
        try {
            storageService = new StorageService();
        } catch (IOException e) {
            throw new ServletException("Erro ao inicializar StorageService", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            for (Part part : request.getParts()) {
                String fileName = Path.of(part.getSubmittedFileName()).getFileName().toString();
                if (!fileName.isEmpty()) {
                    try (InputStream input = part.getInputStream()) {
                        // Salva usando o StorageService (byte[])
                        storageService.saveFile(input.readAllBytes(), fileName);
                    }
                }
            }
            response.getWriter().write("Upload realizado com sucesso!");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erro ao enviar arquivo: " + e.getMessage());
        }
    }
}
