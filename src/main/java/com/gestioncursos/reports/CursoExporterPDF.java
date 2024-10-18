package com.gestioncursos.reports;

import com.gestioncursos.entity.Curso;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CursoExporterPDF {

    private List<Curso> listaCursos;

    public CursoExporterPDF(List<Curso> listaCursos) {
        this.listaCursos = listaCursos;
    }


    /*
    * Para agregar una cabezera de la tabla
    * */
    private void writeTableHeader(PdfPTable pdfPTable) {
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setBackgroundColor(Color.BLUE);                // Color de fondo azul para la celda
        pdfPCell.setPadding(5);                                 // Espaciado interno de la celda

        Font font = FontFactory.getFont(FontFactory.HELVETICA); //Indicamos el tipo de fuente
        font.setColor(Color.white);                             //Indicamos el color de la fuente

        // Creamos celdas con texto para cada columna
        //Indicamos un pequeño parrafo en el primer parametro y en el segundo el tipo de fuente que creamos
        pdfPCell.setPhrase(new Phrase("ID", font));       // Texto "ID" en la celda con la fuente especificada
        pdfPTable.addCell(pdfPCell);                            // Añade la celda a la tabla

        pdfPCell.setPhrase(new Phrase("Titulo", font));   // Texto "Titulo"
        pdfPTable.addCell(pdfPCell);                            // Añade la celda a la tabla

        pdfPCell.setPhrase(new Phrase("Descripcion", font));// Texto "Descripcion"
        pdfPTable.addCell(pdfPCell);                            // Añade la celda a la tabla

        pdfPCell.setPhrase(new Phrase("Nivel", font));    // Texto "Nivel"
        pdfPTable.addCell(pdfPCell);                            // Añade la celda a la tabla

        pdfPCell.setPhrase(new Phrase("Publicado", font));// Texto "Publicado"
        pdfPTable.addCell(pdfPCell);                            // Añade la celda a la tabla
    }


    /*
    * Método para escribir los datos de la LISTA DE CURSOS en la tabla
    * */
    private void writeTableData(PdfPTable pdfPTable) {
        for(Curso curso:listaCursos) {
            pdfPTable.addCell(String.valueOf(curso.getId()));
            pdfPTable.addCell(curso.getTitulo());
            pdfPTable.addCell(curso.getDescripcion());
            pdfPTable.addCell(String.valueOf(curso.getNivel()));
            pdfPTable.addCell(String.valueOf(curso.isPublicado()));
        }
    }


    /*
    * Método para EXPORTAR EL ARCHIVO
    * */
    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());


        //Para escribir en el documento que estamos creando, tenemos que abrirlo
        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD); //Luego establecemos las fuentes
        font.setSize(18);                                            //Tamaño de la letra
        font.setColor(Color.BLUE);                                   //Color de letra

        Paragraph p = new Paragraph("Lista de Cursos", font);  //Escribimos un parrafo "Lista de Cursos" agregando la fuente creada
        p.setAlignment(Paragraph.ALIGN_CENTER);                      //El texto de arriba estara alineado al centro

        //Luego agregamos este parrafo al documento
        document.add(p);

        //Indicamos queremos 5 columnas para los valores del metodo "writeTableData" que tienen el id, titulo, etc
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f); //Indicamos el ancho
        table.setWidths(new float[]{1.3f, 3.5f, 3.5f, 2.0f, 1.5f});  //Anchos para las columnas, indicamos float ya que
                                                                     //calcularemos medidas para cada columna, de las 5
        table.setSpacingBefore(10);           //Es un espaciadoentre las columnas

        //Escribimos la cabecera, con los 5 valores que generamos con sus estilos
            writeTableHeader(table);
        writeTableData(table);

        //Al documento creado agregamos la tabla que creamos tambien
        document.add(table);

        //Al finalizar cerramos el documento
        document.close();
    }
}
