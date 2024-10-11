package org.jaxtech.jaxtech.modelo;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageTableCell extends TableCell<Producto, Image> {
    private final ImageView imageView;

    public ImageTableCell() {
        this.imageView = new ImageView();
        this.imageView.setFitHeight(50); // Ajusta el tamaño de la imagen según sea necesario
        this.imageView.setFitWidth(50);
        setGraphic(imageView);
    }

    @Override
    protected void updateItem(Image image, boolean empty) {
        super.updateItem(image, empty);
        if (empty || image == null) {
            imageView.setImage(null);
        } else {
            imageView.setImage(image);
        }
    }
}