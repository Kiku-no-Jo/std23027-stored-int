package com.hei.storedint.file.hash;

import com.hei.storedint.PojaGenerated;

@PojaGenerated
public record FileHash(FileHashAlgorithm algorithm, String value) {}
