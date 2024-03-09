package com.zaptross.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class BuildCollectionFactoryTask extends DefaultTask {
  @TaskAction
  public void buildCollectionFactory() {
    FileUtils.ensureGeneratedDir();

    var fd = System.getProperty("user.dir") + "/api/src/main/java/api/database/entities/";

    var imports = new ArrayList<String>();
    var provides = new ArrayList<String>();

    var files = new File(fd).listFiles();
    for (int i = 0; i < files.length; i++) {
      var fn = files[i].getName().replace(".java", "");
      var pluralised = fn.endsWith("s") ? fn : fn + "s";
      imports.add(String.format("import api.database.entities.%s;", fn));
      provides.add(
          String.format("\n" +
              "  @Inject\n" +
              "  @Singleton\n" +
              "  @Provides\n" +
              "  public MongoCollection<%s> provide%sCollection(MongoDatabase database) {\n" +
              "    return database.getCollection(\"%s\", %s.class);\n" +
              "  }",
              fn,
              fn,
              pluralised.toLowerCase(),
              fn));
    }

    var generatedFactory = String.format("" +
        "/*\n" +
        " * This file is generated. Do not directly modify this file.\n" +
        " */\n" +
        "package api.generated;\n" +
        "\n" +
        "import javax.inject.Inject;\n" +
        "import javax.inject.Singleton;\n" +
        "\n" +
        "import com.mongodb.client.MongoCollection;\n" +
        "import com.mongodb.client.MongoDatabase;\n" +
        "\n" +
        "%s\n" +
        "import dagger.Module;\n" +
        "import dagger.Provides;\n" +
        "\n" +
        "@Module\n" +
        "public class CollectionFactory {\n" +
        "%s\n" +
        "}\n",
        String.join("\n", imports),
        String.join("\n", provides));

    var outputFilename = "CollectionFactory.java";

    try {
      var factoryFile = new BufferedWriter(
          new FileWriter(
              System.getProperty("user.dir") + "/api/src/main/java/api/generated/" + outputFilename));
      factoryFile.write(generatedFactory);
      factoryFile.close();
      System.out.println("Generated CollectionFactory.java from buildSrc");
    } catch (Exception e) {
      System.out.println("Failed to generate CollectionFactory.java");
      e.printStackTrace();
    }
  }
}
