package com.solicitud.pdf.services;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class SolicitudPDFServiceImpl implements SolicitudPDFService {

    @Override
    public String generatePdfWithHeader() {
        // 🔹 1. Crear un stream en memoria en lugar de un archivo
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // 🔹 2. Ajustar márgenes para el header
        document.setMargins(70, 36, 36, 36);

        // 🔹 3. Agregar manejador de eventos para la cabecera
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, new HeaderEventHandler());

        // 🔹 4. Agregar contenido de prueba
        for (int i = 0; i < 10; i++) {
            document.add(new Paragraph("Contenido del documento en la página " + (i + 1)));
            document.add(new Paragraph("\nEste es un párrafo de prueba para demostrar cómo se ven varias páginas con cabecera.\n\n"));
            document.add(new Paragraph("Este es otro párrafo de prueba para llenar más espacio y forzar una nueva página."));
            document.add(new Paragraph("\n\n--------------------------------\n\n"));
        }

        // 🔹 5. Cerrar el documento
        document.close();

        // 🔹 6. Convertir el PDF en memoria a un array de bytes
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        // 🔹 7. Convertir el array de bytes a Base64
        String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

        // 🔹 8. Mostrar el Base64 en consola (solo como demostración)
        System.out.println("✅ PDF generado. Verifica el header.");

        return base64Pdf;
    }

    @Override
    public String generatePdfWithHeaderAndFooter() {
        // 🔹 1. Crear un stream en memoria para el PDF
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // 🔹 2. Ajustar márgenes para no sobrescribir el header y footer
        document.setMargins(70, 36, 50, 36); // Márgenes: superior, derecho, inferior, izquierdo

        // 🔹 3. Agregar header y footer a cada página
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, new HeaderEventHandler());
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new FooterEventHandler());

        // 🔹 4. Agregar contenido de prueba en varias páginas
        for (int i = 0; i < 10; i++) {
            document.add(new Paragraph("Contenido del documento en la página " + (i + 1)));
            document.add(new Paragraph("\nEste es un párrafo de prueba para verificar varias páginas con header y footer.\n\n"));
            document.add(new Paragraph("\n\n--------------------------------\n\n"));
        }

        // 🔹 5. Cerrar el documento
        document.close();

        // 🔹 6. Convertir el PDF en Base64
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

        System.out.println("✅ PDF generado. Verifica el header y footer.");

        return base64Pdf;
    }

// 🔹 Clase para manejar el FOOTER (pie de página azul con texto blanco)
private class FooterEventHandler implements IEventHandler {
    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

        float x = 36;
        float y = 20; // 🔹 Posición del pie de página (cerca del borde inferior)
        float width = page.getPageSize().getWidth() - 72;
        float height = 40;

        // 🔹 Dibujar rectángulo azul para el pie de página
        canvas.saveState()
                .setFillColor(ColorConstants.BLUE)
                .rectangle(x, y, width, height)
                .fill()
                .restoreState();

        // 🔹 Agregar texto blanco encima del rectángulo azul
        Rectangle rect = new Rectangle(x, y, width, height);
        Canvas footerCanvas = new Canvas(canvas, pdfDoc, rect);
        footerCanvas.add(new Paragraph("Pie de Página - Página " + pdfDoc.getPageNumber(page))
                .setFontSize(12)
                .setBold()
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
    }
}

// 🔹 Clase para manejar el HEADER (cabecera roja con texto blanco)
private class HeaderEventHandler implements IEventHandler {
    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

        // 🔹 Ajustamos el rectángulo para que la cabecera tenga suficiente espacio
        float x = 36; // Margen izquierdo
        float y = page.getPageSize().getTop() - 50; // Posición más baja para evitar solapamiento
        float width = page.getPageSize().getWidth() - 72; // Ancho total con márgenes
        float height = 40; // Altura del header

        canvas.saveState()
                .setFillColor(ColorConstants.RED)
                .rectangle(x, y, width, height)
                .fill()
                .restoreState();

        Rectangle rect = new Rectangle(x, y, width, height);
        Canvas headerCanvas = new Canvas(canvas, pdfDoc, rect);

        // Agregar texto dentro del rectangulo rojo
        headerCanvas.add(new Paragraph("Mi Cabecera - Documento PDF")
                .setFontSize(12)
                .setBold()
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
    }
}
}
