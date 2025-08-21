package com.solicitud.pdf.domain.dtos;

import lombok.Data;

@Data
public class ArchivoRequest {

    private String base64;
    private String formato;
}
