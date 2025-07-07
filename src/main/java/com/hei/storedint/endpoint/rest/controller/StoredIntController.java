package com.hei.storedint.endpoint.rest.controller;

import com.hei.hazavao.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoredIntController {

  private final FileService fileService;

  // Injection de dépendance du FileService
  public StoredIntController(FileService fileService) {
    this.fileService = fileService;
  }

  @GetMapping("/stored-int")
  public ResponseEntity<Integer> getStoredInt() {
    // a) Lire le nombre stocké dans le fichier stored-int.txt
    return fileService
        .getStoredInteger()
        .map(
            ResponseEntity::ok) // Si le nombre existe, le retourner en tant que réponse HTTP 200 OK
        .orElseGet(
            () -> {
              // b) Créer le fichier stored-int.txt sinon, et y écrire un nombre aléatoire
              int newRandomInt = fileService.createAndStoreRandomInteger();
              return ResponseEntity.ok(newRandomInt); // Retourner le nouveau nombre généré
            });
  }
}
