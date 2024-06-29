package com.ruvianfabrin;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o caminho do diretório onde estão as imagens: ");
        String diretorio = scanner.nextLine();
        System.out.print("De quantas em quantas imagens você deseja unir? ");
        int quantidade = scanner.nextInt();

        File dir = new File(diretorio);
        File[] imagens = dir.listFiles((d, name) -> name.endsWith(".jpg") || name.endsWith(".png"));
        Arrays.sort(imagens, Comparator.comparing(File::getName));

        if (imagens != null && imagens.length >= quantidade) {
            Path pastaUnidas = Paths.get(diretorio + "/ImagensUnidas");
            try {
                Files.createDirectories(pastaUnidas);

                for (int i = 0; i < imagens.length; i += quantidade) {
                    int totalHeight = 0;
                    int maxWidth = 0;
                    for (int j = i; j < i + quantidade && j < imagens.length; j++) {
                        BufferedImage image = ImageIO.read(imagens[j]);
                        totalHeight += image.getHeight();
                        maxWidth = Math.max(maxWidth, image.getWidth());
                    }

                    BufferedImage combinedImage = new BufferedImage(
                            maxWidth, // Largura da imagem inicial
                            totalHeight, // Soma das alturas das imagens
                            BufferedImage.TYPE_INT_RGB);

                    Graphics g = combinedImage.getGraphics();
                    int currentHeight = 0;
                    for (int j = i; j < i + quantidade && j < imagens.length; j++) {
                        BufferedImage image = ImageIO.read(imagens[j]);
                        g.drawImage(image, 0, currentHeight, null);
                        currentHeight += image.getHeight();
                    }

                    g.dispose();
                    File saidaImagem = pastaUnidas.resolve(String.format("ImagemCombinada_%02d.jpg", (i / quantidade) + 1)).toFile();
                    ImageIO.write(combinedImage, "jpg", saidaImagem);
                    System.out.println("Imagem " + saidaImagem.getName() + " criada.");
                }

                System.out.println("Todas as imagens foram unidas e salvas com sucesso!");
            } catch (IOException e) {
                System.out.println("Ocorreu um erro ao unir as imagens: " + e.getMessage());
            }
        } else {
            System.out.println("Não há imagens suficientes no diretório fornecido ou o diretório é inválido.");
        }

        scanner.close();
    }
}
