package servicio;

import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

public interface ConvertirDatos {
    <T> T obtenerDatos(String json, Class<T> clase) throws Throwable;
}
