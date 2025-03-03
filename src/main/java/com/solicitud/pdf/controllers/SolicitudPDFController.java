package com.solicitud.pdf.controllers;

import com.solicitud.pdf.services.SolicitudPDFService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SolicitudPDFController {

    private final SolicitudPDFService solicitudPDFService;

    @PostMapping("/pdf/header")
    public ResponseEntity<String> generatePdfWithHeader() {
        String response = solicitudPDFService.generatePdfWithHeader();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/pdf")
    public ResponseEntity<String> generatePdfWithHeaderAndFooter() {
        String response = solicitudPDFService.generatePdfWithHeaderAndFooter();

        return ResponseEntity.ok(response);
    }
}
