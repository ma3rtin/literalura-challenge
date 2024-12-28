package modelo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AutorDTO(@JsonAlias("name") String nombre,
                       @JsonAlias("birth_year") String anio_nacimiento,
                       @JsonAlias("death_year") String anio_defuncion) {

}
