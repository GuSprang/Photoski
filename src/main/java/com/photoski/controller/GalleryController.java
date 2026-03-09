/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.photoski.controller;

/**
 *
 * @author gusta
 */

import com.photoski.storage.StorageService;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/gallery/json")
public class GalleryController extends HttpServlet {

    private StorageService storageService;

    @Override
    public void init() throws ServletException {
        try {
            this.storageService = new StorageService();
        } catch (IOException e) {
            throw new ServletException("Não foi possível inicializar o StorageService", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<String> fileNames = storageService.loadAll();

        String json = new Gson().toJson(fileNames);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }
}