package com.solicitud.pdf.controllers;

import com.solicitud.pdf.domain.dtos.ArchivoRequest;
import com.solicitud.pdf.services.ArchivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArchivoController {

    private final ArchivoService archivoService;

    @PostMapping("/convertBase46")
    public ResponseEntity<String> convertBase64ToPdf(@RequestBody ArchivoRequest archivoRequest) {
        String response = archivoService.convertBase64ToPdf(archivoRequest);

        return ResponseEntity.ok(response);
    }
}
