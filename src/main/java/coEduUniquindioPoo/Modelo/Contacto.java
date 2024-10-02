package coEduUniquindioPoo.Modelo;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contacto {
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private LocalDate fechaCumpleaños;
    private String urlFotoPerfil;
}