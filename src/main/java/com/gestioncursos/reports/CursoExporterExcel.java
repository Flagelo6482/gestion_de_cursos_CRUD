package com.gestioncursos.reports;
import com.gestioncursos.entity.Curso;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.util.List;


public class CursoExporterExcel {

    //Para crear el "libro" en el excel
    private XSSFWorkbook workbook;
    //Para crear las hojas
    private XSSFSheet sheet;
    //Listado de la base de datos
    private List<Curso> cursos;

    public CursoExporterExcel(List<Curso> cursos) {
        this.cursos = cursos;
        workbook = new XSSFWorkbook();  //Inicializamos un libro
    }

    //Escribimos la cabecera sobre el libro
    private void writeHeaderLine() {
        //Creamos la hoja llamada "Cursos" en el libro
        sheet = workbook.createSheet("Cursos");
        //Creamos una fila a partir de la hoja
        Row row = sheet.createRow(0);

        //Creamos un estilo de celda
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true); //Indicamos que este en negrita
        font.setFontHeight(16); //Le indicamos la altura
        style.setFont(font);


        //Llamamos al método "createCell" para llenar las celdas con los datos
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Título", style);
        createCell(row, 2, "Descripción", style);
        createCell(row, 3, "Nivel", style);
        createCell(row, 4, "Estado de publicación", style);
    }


    /*
    * Método para crear las celdas de DATOS
    * */
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if(value instanceof Integer) {
            cell.setCellValue((Integer) value);
        }
        else if(value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }
        else {
            cell.setCellValue((String) value);
        }

        cell.setCellStyle(style);
    }


    /*
    * Método para escribir las líneas/datos
    * */
    private void writeDataLines() {
        int rowCount = 1;

        //Agregamos los estilos para la celda junto con la fuente
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();

        font.setFontHeight(14);
        style.setFont(font);

        for(Curso curso:cursos) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, curso.getId(), style);
            createCell(row, columnCount++, curso.getTitulo(), style);
            createCell(row, columnCount++, curso.getDescripcion(), style);
            createCell(row, columnCount++, curso.getNivel(), style);
            createCell(row, columnCount++, curso.isPublicado(), style);
        }
    }

    /*
    * Método para exportar toda la data que generamos
    * */
    public void export(HttpServletResponse response) throws IOException {
        //1.Primero escribimos la cabecera
        writeHeaderLine();
        //2.Escribimos los datos que tenemos en la tabla
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close(); //Para cerrar el archivo

        outputStream.close(); //El objeto "outputStream" sirve para realizar alguna escritura
    }










}

