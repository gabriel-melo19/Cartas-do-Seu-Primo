package com.cardgame.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class CardNode extends StackPane {

    private static final double LARGURA_CARD = 160;
    private static final double ALTURA_CARD = 230;
    private static final double LARGURA_IMAGEM = 132;
    private static final double ALTURA_IMAGEM = 126;

    private final StackPane containerImagem;
    private final ImageView imagemView;
    private final Label labelPlaceholder;
    private final Label labelNome;
    private final Label labelAtributos;
    private final Label labelSelo;
    private final StackPane faixaNome;

    public CardNode(String nome, Image imagem) {
        getStyleClass().add("card-node");

        setPrefWidth(LARGURA_CARD);
        setPrefHeight(ALTURA_CARD);
        setMinWidth(LARGURA_CARD);
        setMinHeight(ALTURA_CARD);
        setMaxWidth(LARGURA_CARD);
        setMaxHeight(ALTURA_CARD);

        VBox container = new VBox(8);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(10, 10, 10, 10));
        container.getStyleClass().add("card-node-container");

        containerImagem = new StackPane();
        containerImagem.setPrefSize(LARGURA_IMAGEM, ALTURA_IMAGEM);
        containerImagem.setMinSize(LARGURA_IMAGEM, ALTURA_IMAGEM);
        containerImagem.setMaxSize(LARGURA_IMAGEM, ALTURA_IMAGEM);
        containerImagem.getStyleClass().add("card-node-image-wrapper");

        imagemView = new ImageView();
        imagemView.setFitWidth(LARGURA_IMAGEM);
        imagemView.setFitHeight(ALTURA_IMAGEM);
        imagemView.setPreserveRatio(false);
        imagemView.setSmooth(true);
        imagemView.getStyleClass().add("card-node-image");

        Rectangle clip = new Rectangle(LARGURA_IMAGEM, ALTURA_IMAGEM);
        clip.setArcWidth(14);
        clip.setArcHeight(14);
        imagemView.setClip(clip);

        labelPlaceholder = new Label("SEM\nIMAGEM");
        labelPlaceholder.setAlignment(Pos.CENTER);
        labelPlaceholder.setWrapText(true);
        labelPlaceholder.getStyleClass().add("card-node-placeholder");

        containerImagem.getChildren().addAll(labelPlaceholder, imagemView);

        labelAtributos = new Label("ATK: --        HP: --");
        labelAtributos.setAlignment(Pos.CENTER);
        labelAtributos.setMaxWidth(Double.MAX_VALUE);
        labelAtributos.getStyleClass().add("card-node-attributes");

        labelSelo = new Label("◉");
        labelSelo.getStyleClass().add("card-node-seal");

        labelNome = new Label(nome != null && !nome.isBlank() ? nome : "Carta");
        labelNome.setWrapText(true);
        labelNome.setAlignment(Pos.CENTER);
        labelNome.setMaxWidth(Double.MAX_VALUE);
        labelNome.getStyleClass().add("card-node-name");

        faixaNome = new StackPane(labelNome);
        faixaNome.setAlignment(Pos.CENTER);
        faixaNome.getStyleClass().add("card-node-name-bar");

        Region espacador = new Region();
        VBox.setVgrow(espacador, Priority.ALWAYS);

        container.getChildren().addAll(
                containerImagem,
                labelSelo,
                labelAtributos,
                espacador,
                faixaNome
        );

        getChildren().add(container);

        setImagem(imagem);
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            labelNome.setText("Carta");
            return;
        }

        labelNome.setText(nome);
    }

    public void setImagem(Image imagem) {
        imagemView.setImage(imagem);

        boolean temImagem = imagem != null;
        imagemView.setVisible(temImagem);
        imagemView.setManaged(temImagem);

        labelPlaceholder.setVisible(!temImagem);
        labelPlaceholder.setManaged(!temImagem);
    }

    public void setSelo(String texto) {
        if (texto == null || texto.isBlank()) {
            labelSelo.setText("◉");
            return;
        }

        labelSelo.setText(texto);
    }

    public void setAtributos(String texto) {
        if (texto == null || texto.isBlank()) {
            labelAtributos.setText("ATK: --        HP: --");
            return;
        }

        labelAtributos.setText(texto);
    }
}