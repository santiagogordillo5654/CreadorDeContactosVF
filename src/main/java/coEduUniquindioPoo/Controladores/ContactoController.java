package coEduUniquindioPoo.Controladores;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import coEduUniquindioPoo.Modelo.Contacto;
import coEduUniquindioPoo.Modelo.GestionContactos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;

public class ContactoController {

    @FXML
    private TableView<Contacto> tablaContactos;
    @FXML
    private TableColumn<Contacto, String> columnaNombre;
    @FXML
    private TableColumn<Contacto, String> columnaApellido;
    @FXML
    private TableColumn<Contacto, String> columnaTelefono;
    @FXML
    private TableColumn<Contacto, String> columnaEmail;
    @FXML
    private TableColumn<Contacto, LocalDate> columnaFechaCumpleaños;
    @FXML
    private TableColumn<Contacto, ImageView> columnaFoto;

    @FXML
    private TextField nombreInput, apellidoInput, telefonoInput, emailInput, urlFotoInput;
    @FXML
    private DatePicker cumpleInput;
    @FXML
    private ComboBox<String> filtroComboBox;
    @FXML
    private TextField filtroInput;

    private GestionContactos gestionContactos = new GestionContactos();
    private ObservableList<Contacto> contactoObservableList;

    public void initialize() {
        filtroComboBox.setItems(FXCollections.observableArrayList("Nombre", "Teléfono"));

        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        columnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        columnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnaFechaCumpleaños.setCellValueFactory(new PropertyValueFactory<>("fechaCumpleaños"));
        columnaFoto.setCellValueFactory(contacto -> {
            String urlFoto = contacto.getValue().getUrlFotoPerfil();
            ImageView imageView = new ImageView(new Image(urlFoto));
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            return new javafx.beans.property.SimpleObjectProperty<>(imageView);
        });

        contactoObservableList = FXCollections.observableArrayList();
        inicializarTabla();

        tablaContactos.getSelectionModel().getSelectedCells().addListener((ListChangeListener<? super TablePosition>) listener -> {
            Contacto contacto = tablaContactos.getSelectionModel().getSelectedItem();
            if (contacto != null) {
                nombreInput.setText(contacto.getNombre());
                apellidoInput.setText(contacto.getApellido());
                telefonoInput.setText(contacto.getTelefono());
                emailInput.setText(contacto.getEmail());
                cumpleInput.setValue(contacto.getFechaCumpleaños());
                urlFotoInput.setText(contacto.getUrlFotoPerfil());
            }
        });
    }

    private void inicializarTabla() {
        contactoObservableList.setAll(gestionContactos.obtenerContactos());
        tablaContactos.setItems(contactoObservableList);
    }

    public void agregarContacto() {
        try {
            gestionContactos.agregarContacto(
                    nombreInput.getText(),
                    apellidoInput.getText(),
                    telefonoInput.getText(),
                    emailInput.getText(),
                    urlFotoInput.getText(),
                    cumpleInput.getValue()
            );
            limpiarCampos();
            cargarLista();
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    public void actualizarContacto() {
        Contacto contactoSeleccionado = tablaContactos.getSelectionModel().getSelectedItem();
        if (contactoSeleccionado != null) {
            try {
                Contacto contactoActualizado = Contacto.builder()
                        .nombre(nombreInput.getText())
                        .apellido(apellidoInput.getText())
                        .telefono(telefonoInput.getText())
                        .email(emailInput.getText())
                        .fechaCumpleaños(cumpleInput.getValue())
                        .urlFotoPerfil(urlFotoInput.getText())
                        .build();

                gestionContactos.actualizarContacto(contactoSeleccionado, contactoActualizado);
                limpiarCampos();
                cargarLista();
            } catch (Exception e) {
                mostrarError(e.getMessage());
            }
        }
    }

    public void eliminarContacto() {
        Contacto contactoSeleccionado = tablaContactos.getSelectionModel().getSelectedItem();
        if (contactoSeleccionado != null) {
            gestionContactos.eliminarContacto(contactoSeleccionado);
            cargarLista();
        }
    }

    public void buscarContacto() {
        String filtro = filtroInput.getText();
        String tipoFiltro = filtroComboBox.getSelectionModel().getSelectedItem();
        tablaContactos.setItems(FXCollections.observableArrayList(
                gestionContactos.filtrarContactos(filtro, tipoFiltro)
        ));
    }

    private void limpiarCampos() {
        nombreInput.clear();
        apellidoInput.clear();
        telefonoInput.clear();
        emailInput.clear();
        cumpleInput.setValue(null);
        urlFotoInput.clear();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cargarLista() {
        contactoObservableList.setAll(gestionContactos.obtenerContactos());
    }
}
