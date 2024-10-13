package com.gestioncursos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "cursos")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //Caracteres maximos 125, no tiene que ser false/vacio
    @Column(length = 125, nullable = false)
    private String titulo;

    @Column(length = 256)
    private String descripcion;

    @Column(nullable = false)
    private int nivel;

    //Asignamos el nombre de la columna de este dato como "estado_publicacion"
    @Column(name = "estado_publicacion")
    private boolean publicado;

    // Getter para 'publicado'
    public boolean isPublicado() {
        return publicado;
    }

    // Setter para 'publicado'
    public void setPublicado(boolean publicado) {
        this.publicado = publicado;
    }
}
