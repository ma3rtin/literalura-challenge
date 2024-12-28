package servicio;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ConvertirDatosImpl implements ConvertirDatos{

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) throws Throwable {
        try {
            return objectMapper.readValue(json, clase);
        } catch (JsonMappingException e) {
            throw new Throwable("Error mapping JSON", e);
        }
    }
}
