package com.gestioncursos.controller;

import com.gestioncursos.entity.Curso;
import com.gestioncursos.reports.CursoExporterExcel;
import com.gestioncursos.reports.CursoExporterPDF;
import com.gestioncursos.repository.CursoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping("/")
    public String home(){
        return "redirect:/cursos";
    }

    /*
    * Ruta para ver toda la lista de CURSOS
    * */
    @GetMapping("/cursos")
    public String listarCursos(Model model) {
        List<Curso> cursos = cursoRepository.findAll();
        model.addAttribute("cursos", cursos);
        return "cursos";
    }

    /*
    * Ruta para crear un CURSO
    * */
    @GetMapping("/cursos/nuevo")
    public String crearCurso(Model model){
        Curso curso = new Curso();
        curso.setPublicado(true);  //Por defecto será "true"

        model.addAttribute("nuevoCurso", curso);   //Enviamos el nuevo curso al html para rellenar los datos
        model.addAttribute("pageTitle", "Nuevo Curso");
        return "curso_form";
    }
    /*
    * Ruta para enviar los datos del formulario a la base de datos
    * */
    @PostMapping("/cursos/guardar")
    public String guardarCurso(@ModelAttribute Curso curso, RedirectAttributes redirectAttributes){
        try{
            cursoRepository.save(curso);
            redirectAttributes.addFlashAttribute("mensaje", "El curso a sido guardado con exito");
        } catch (Exception e) {
            redirectAttributes.addAttribute("mensaje", e.getMessage());
        }
        return "redirect:/cursos";
    }


    /*
    * Ruta para actualizar un curso
    * */
    @GetMapping("/cursos/{id}")
    public String editarCurso(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            Curso curso = cursoRepository.findById(id).get();  //Obtenemos el curso según el "id"

            model.addAttribute("pageTitle", "Editar Curso : "+id);
            model.addAttribute("nuevoCurso", curso); //Enviamos el curso encontrado para editarlo
            redirectAttributes.addFlashAttribute("mensaje", "El curos a sido editado con exito");

            return "curso_form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
        }
        return "redirect:/cursos";
    }

    /*
    * Ruta para eliminar un curso
    * */
    @GetMapping("/cursos/eliminar/{id}")
    public String eliminarCurso(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            cursoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Curso eliminado con éxito");
            return "redirect:/cursos";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("mensaje", exception.getMessage());
        }
        return "redirect:/cursos";
    }


    /*
    * Método para generar el reporte en PDF
    * */
    @GetMapping("/export/pdf")
    public void generarReportePDF(HttpServletResponse response) throws IOException {
        //El tipo de contenido es de PDF
        response.setContentType("application/pdf");
        //Formateador de fechas indicando el año,mes,hora,minuto y segundos
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        //Formateamos con la variable, la fecha con la hora actual
        String currentDateTime = dateFormat.format(new Date());


        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=cursos" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);


        List<Curso> cursos = cursoRepository.findAll();


        //Enviamos todos los cursos que tenemos como parametro para que lo agrege al archivo PDF
        CursoExporterPDF cursoExporterPDF = new CursoExporterPDF(cursos);
        cursoExporterPDF.export(response);
    }


    /*
    * Metodo para exportar el archivo EXCEL
    * */
    @GetMapping("/export/excel")
    public void generarReporteExcel(HttpServletResponse response) throws IOException {
        //El tipo de contenido es de PDF
        response.setContentType("application/octet-stream");
        //Formateador de fechas indicando el año,mes,hora,minuto y segundos
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        //Formateamos con la variable, la fecha con la hora actual
        String currentDateTime = dateFormat.format(new Date());


        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=cursos" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);


        List<Curso> cursos = cursoRepository.findAll();


        //Enviamos todos los cursos que tenemos como parametro para que lo agrege al archivo PDF
        CursoExporterExcel exporterExcel = new CursoExporterExcel(cursos);
        exporterExcel.export(response);
    }
}
