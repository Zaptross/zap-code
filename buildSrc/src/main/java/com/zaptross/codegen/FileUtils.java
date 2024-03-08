package com.zaptross.codegen;

import java.io.File;

public class FileUtils {
  public static void ensureGeneratedDir() {
    var generatedDir = System.getProperty("user.dir") + "/api/src/main/java/api/generated/";
    var generatedDirFile = new File(generatedDir);
    if (!generatedDirFile.exists()) {
      generatedDirFile.mkdirs();
    }
  }
}
