package com.hei.storedint.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class FileService {

  // IMPORTANT : Sur AWS Lambda, System.getProperty("java.io.tmpdir") pointera vers /tmp.
  // En local, il pointera vers le répertoire temporaire de votre OS.
  private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
  private static final Path FILE_PATH = Paths.get(TEMP_DIR, "stored-int.txt");

  /**
   * Tente de lire l'entier stocké dans le fichier stored-int.txt.
   *
   * @return Un Optional contenant l'entier si le fichier existe et est valide, sinon un Optional
   *     vide.
   */
  public Optional<Integer> getStoredInteger() {
    if (Files.exists(FILE_PATH)) {
      try {
        String content = Files.readString(FILE_PATH);
        // Utilise trim() pour s'assurer qu'il n'y a pas d'espaces blancs indésirables
        return Optional.of(Integer.parseInt(content.trim()));
      } catch (IOException e) {
        System.err.println("Erreur de lecture du fichier " + FILE_PATH + ": " + e.getMessage());
        // Si une erreur I/O se produit, agissons comme si le fichier n'existait pas
        return Optional.empty();
      } catch (NumberFormatException e) {
        System.err.println(
            "Contenu du fichier non valide (pas un entier): " + FILE_PATH + ". " + e.getMessage());
        // Si le contenu n'est pas un entier, agissons comme si le fichier était corrompu
        return Optional.empty();
      }
    }
    return Optional.empty();
  }

  /**
   * Génère un nombre aléatoire, l'écrit dans stored-int.txt et le retourne.
   *
   * @return Le nombre aléatoire généré et stocké.
   * @throws RuntimeException si l'écriture du fichier échoue.
   */
  public int createAndStoreRandomInteger() {
    Random random = new Random();
    int randomNumber = random.nextInt(1000); // Nombre aléatoire entre 0 et 999

    try {
      Files.writeString(FILE_PATH, String.valueOf(randomNumber));
      System.out.println(
          "Nouveau nombre aléatoire généré et stocké dans " + FILE_PATH + ": " + randomNumber);
      return randomNumber;
    } catch (IOException e) {
      System.err.println(
          "Erreur lors de l'écriture du fichier " + FILE_PATH + ": " + e.getMessage());
      // Relancer une RuntimeException car l'écriture est essentielle à cette logique
      throw new RuntimeException("Impossible d'écrire le nombre dans le fichier", e);
    }
  }
}
