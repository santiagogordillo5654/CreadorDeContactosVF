package coEduUniquindioPoo.Modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionContactos {
    private List<Contacto> contactos;

    public GestionContactos(){
        this.contactos = new ArrayList<Contacto>();
    }

    // Crear contacto
    public void agregarContacto(String nombre, String apellido, String telefono, String email, String urlFoto, LocalDate fecha)throws Exception {

        Contacto contacto = Contacto.builder()
                .nombre(nombre)
                .apellido(apellido)
                .telefono(telefono)
                .email(email)
                .fechaCumpleaños(fecha)
                .urlFotoPerfil(urlFoto)
                .build();

        if (!existeContacto(contacto)) {

            if (contacto.getNombre().isEmpty() || contacto.getTelefono().isEmpty()) {
                throw new IllegalArgumentException("Los campos de nombre y teléfono no pueden estar vacíos.");
            }
            if (!contacto.getTelefono().matches("\\d+")) {
                throw new IllegalArgumentException("El número de teléfono debe contener solo dígitos.");
            }
            if (contacto.getEmail().isEmpty()) {
                throw new IllegalArgumentException("El campo de correo electrónico no puede estar vacío.");
            }
            if (!contacto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new IllegalArgumentException("El correo electrónico no tiene un formato válido.");
            }
            if (contacto.getFechaCumpleaños() == null) {
                throw new IllegalArgumentException("El campo de cumpleaños no puede estar vacío.");
            }

            contactos.add(contacto);
        } else {
            throw new IllegalArgumentException("Contacto con el mismo teléfono ya existe.");
        }
    }

    // Leer todos los contactos
    public List<Contacto> obtenerContactos() {
        return contactos;
    }

    // Actualizar contacto
    public void actualizarContacto(Contacto contactoAntiguo, Contacto contactoNuevo) {
        if (!existeContacto(contactoNuevo) || contactoAntiguo.getTelefono().equals(contactoNuevo.getTelefono())) {
            contactos.remove(contactoAntiguo);
            contactos.add(contactoNuevo);
        } else {
            throw new IllegalArgumentException("Otro contacto con el mismo teléfono ya existe.");
        }
    }

    // Eliminar contacto
    public void eliminarContacto(Contacto contacto) {
        contactos.remove(contacto);
    }

    // Validar si el contacto ya existe por teléfono
    public boolean existeContacto(Contacto contacto) {
        return contactos.stream().anyMatch(c -> c.getTelefono().equals(contacto.getTelefono()));
    }

    // Filtrar contactos por nombre o teléfono
    public List<Contacto> filtrarContactos(String filtro, String tipo) {
        return contactos.stream()
                .filter(c -> tipo.equals("Nombre") ? c.getNombre().contains(filtro) : c.getTelefono().contains(filtro))
                .collect(Collectors.toList());
    }
}