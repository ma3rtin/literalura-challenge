package modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String nombreAutor;
    private String idiomas;
    private  int numeroDescargas;
    @ManyToOne
    private Autor autor;

    public Libro() {
    }

    public Libro(LibroDTO datosLibro, Autor autor) {
        this.titulo = datosLibro.titulo();
        this.nombreAutor = autor.getNombre();
        this.autor = autor;
        this.idiomas = datosLibro.lenguajes().get(0);
        this.numeroDescargas = datosLibro.descargas();
    }

    @Override
    public String toString() {
        return "******************************************************************" + "\n" +
                "   Titulo: " + titulo  +  "\n" +
                "   Nombre autor: " + nombreAutor  + "\n" +
                "   Idioma:  " + idiomas + "\n" +
                "   Numero descargas:  " + numeroDescargas + "\n" +
                "******************************************************************";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public int getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(int numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}
