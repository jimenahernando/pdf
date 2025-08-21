package com.solicitud.pdf.services;

import com.solicitud.pdf.domain.dtos.ArchivoRequest;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;

@Service
public class ArchivoServiceImpl implements ArchivoService {
    @Override
    public String convertBase64ToPdf(ArchivoRequest archivoRequest) {
        try {
            byte[] bytes = Base64.getDecoder().decode(archivoRequest.getBase64());
            String extension = archivoRequest.getFormato().toLowerCase();
            String nombreArchivo = "archivo_" + UUID.randomUUID() + "." + extension;

            Path rutaDestino = Path.of("target", nombreArchivo);

            try (FileOutputStream fos = new FileOutputStream(rutaDestino.toFile())) {
                fos.write(bytes);
            }

            return rutaDestino.toAbsolutePath().toString();

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Base64 invalido");
        } catch (Exception e) {
            throw new RuntimeException("Errro al guardar el archivo: " + e.getMessage());
        }
    }
}
