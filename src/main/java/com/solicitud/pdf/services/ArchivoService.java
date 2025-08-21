package com.solicitud.pdf.services;

import com.solicitud.pdf.domain.dtos.ArchivoRequest;

public interface ArchivoService {

    String convertBase64ToPdf(ArchivoRequest archivoRequest);
}
