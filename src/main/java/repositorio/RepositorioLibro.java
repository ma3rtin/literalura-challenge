package repositorio;

import modelo.Autor;
import modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepositorioLibro extends JpaRepository<Libro, Long> {
    Libro findByTitulo(String titulo);

    List<Libro> findByIdiomasContaining(String idioma);

    @Query("SELECT COUNT(l) FROM Libro l WHERE l.idiomas = :idiomas")
    int countByLanguage(@Param("idiomas") String idiomas);

    List<Libro> findTop10ByOrderByNumeroDescargasDesc();

    List<Libro> findByAutor(Autor autor);

    void obtenerLibroPorTitulo(String param);
}
