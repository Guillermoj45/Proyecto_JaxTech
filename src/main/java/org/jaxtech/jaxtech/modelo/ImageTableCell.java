package org.jaxtech.jaxtech.modelo;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Clase ImageTableCell que extiende TableCell para mostrar imágenes en una celda de una TableView.
 */
public class ImageTableCell extends TableCell<Producto, Image> {
    private final ImageView imageView;

    /**
     * Constructor de la clase ImageTableCell.
     * Inicializa el ImageView y ajusta su tamaño.
     */
    public ImageTableCell() {
        this.imageView = new ImageView();
        this.imageView.setFitHeight(50); // Ajusta el tamaño de la imagen según sea necesario
        this.imageView.setFitWidth(50);
        setGraphic(imageView); // Establece el ImageView como el gráfico de la celda
    }

    /**
     * Método updateItem que se llama cuando se actualiza el contenido de la celda.
     * @param image La imagen a mostrar en la celda.
     * @param empty Indica si la celda está vacía.
     */
    @Override
    protected void updateItem(Image image, boolean empty) {
        super.updateItem(image, empty);
        if (empty || image == null) {
            imageView.setImage(null); // Si la celda está vacía o la imagen es nula, no muestra ninguna imagen
        } else {
            imageView.setImage(image); // Si no, muestra la imagen proporcionada
        }
    }
}