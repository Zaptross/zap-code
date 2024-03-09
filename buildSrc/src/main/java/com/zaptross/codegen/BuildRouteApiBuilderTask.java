package com.zaptross.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class BuildRouteApiBuilderTask extends DefaultTask {
  @TaskAction
  public void buildCollectionFactory() {
    FileUtils.ensureGeneratedDir();

    var fd = System.getProperty("user.dir") + "/api/src/main/java/api/handlers/";
    var files = new File(fd).listFiles();

    if (files.length == 0) {
      System.out.println("No files found in " + fd);
      return;
    }

    ArrayList<ArrayList<ArrayList<String>>> classPathGroups = new ArrayList<ArrayList<ArrayList<String>>>();
    for (int i = 0; i < files.length; i++) {
      if (files[i].isDirectory()) {
        var subFiles = files[i].listFiles();
        var classPath = new ArrayList<ArrayList<String>>();
        for (int j = 0; j < subFiles.length; j++) {
          var segments = new ArrayList<String>();
          if (subFiles[j].getName().endsWith("RequestHandler.java")) {
            segments.add(files[i].getName());
            segments.add(subFiles[j].getName().replace(".java", ""));
            classPath.add(segments);
          }
        }
        classPathGroups.add(classPath);
      }
    }

    System.out.println("Generating ApiBuilders for " + classPathGroups.size() + " route groups...");
    for (var cpg : classPathGroups) {
      var len = cpg.get(0).toArray().length;
      var fileName = buildFileName(cpg.get(0));
      var fileContent = buildFileTemplate(
          buildClassName(cpg.get(0)),
          buildImports(cpg),
          buildRoute(cpg.get(0)),
          buildClassFields(cpg),
          buildConstructorFields(cpg),
          buildFieldAssignments(cpg),
          buildApplies(cpg));

      try {
        var newFile = new BufferedWriter(
            new FileWriter(
                System.getProperty("user.dir") + "/api/src/main/java/api/generated/" +
                    fileName));
        newFile.write(fileContent);
        newFile.close();
        System.out.println(String.format("%s", fileName));
        System.out.println(getMethodsHandled(cpg));
      } catch (Exception e) {
        System.out.println(String.format("Failed to generate %s from buildSrc", fileName));
        e.printStackTrace();
      }
    }
  }

  private String buildClassName(ArrayList<String> classPath) {
    var out = "";
    for (var path : classPath.subList(0, classPath.size() - 1)) {
      out += capitaliseFirstLetter(path);
    }
    return out;
  }

  private String buildFileName(ArrayList<String> classPath) {
    return buildClassName(classPath) + "ApiBuilder.java";
  }

  private String capitaliseFirstLetter(String string) {
    return string.substring(0, 1).toUpperCase() + string.substring(1);
  }

  private String buildRoute(ArrayList<String> classPath) {
    return "" + String.join("/", classPath.subList(0, classPath.size() - 1)).toLowerCase();
  }

  private String buildApplies(ArrayList<ArrayList<String>> classPaths) {
    var applies = new ArrayList<String>();
    for (var classPath : classPaths) {
      var fieldName = getFieldNameFromClassName(classPath.get(classPath.size() - 1));
      applies.add(String.format("    router.%s(basePath, %s);", fieldName, fieldName));
    }
    return String.join("\n", applies);
  }

  private String buildFieldAssignments(ArrayList<ArrayList<String>> classPaths) {
    var assignments = new ArrayList<String>();
    for (var classPath : classPaths) {
      var fieldName = getFieldNameFromClassName(classPath.get(classPath.size() - 1));
      assignments.add(String.format("    this.%s = %s;", fieldName, fieldName));
    }
    return String.join("\n", assignments);
  }

  private String getMethodsHandled(ArrayList<ArrayList<String>> classPaths) {
    var methods = new ArrayList<String>();
    var route = buildRoute(classPaths.get(0));
    for (var classPath : classPaths) {
      var className = getFieldNameFromClassName(classPath.get(classPath.size() - 1));
      methods.add(String.format("%s\t/%s", getFieldNameFromClassName(className).toUpperCase(), route));
    }
    return "  -> " + String.join("\n  -> ", methods);
  }

  private String buildConstructorFields(ArrayList<ArrayList<String>> classPaths) {
    var fields = new ArrayList<String>();
    for (var classPath : classPaths) {
      var className = classPath.get(classPath.size() - 1);
      fields.add(String.format("%s %s", className, getFieldNameFromClassName(className)));
    }
    return String.join(", ", fields);
  }

  private String buildClassFields(ArrayList<ArrayList<String>> classPaths) {
    var fields = new ArrayList<String>();
    for (var classPath : classPaths) {
      var className = classPath.get(classPath.size() - 1);
      fields.add(String.format("    private final %s %s;", className, getFieldNameFromClassName(className)));
    }
    return String.join("\n", fields);
  }

  private String getFieldNameFromClassName(String className) {
    return className.replace("RequestHandler", "").toLowerCase();
  }

  private String buildImports(ArrayList<ArrayList<String>> classPaths) {
    var imports = new ArrayList<String>();
    for (var classPath : classPaths) {
      var segments = "";
      for (var path : classPath) {
        if (classPath.get(classPath.size() - 1) == path) {
          segments += path;
        } else {
          segments += path.toLowerCase() + ".";
        }
      }
      imports.add(String.format("import api.handlers.%s;", segments));
    }
    return String.join("\n", imports);
  }

  private String buildFileTemplate(String className, String imports, String route, String classFields,
      String constructorFields, String fieldAssignments, String applies) {
    var nameLower = className.toLowerCase();
    return String.format("" +
        "/*\n" +
        " * This file is generated. Do not directly modify this file.\n" +
        " */\n" +
        "package api.generated;\n" +
        "\n" +
        "import javax.inject.Inject;\n" +
        "\n" +
        "import dagger.Module;\n" +
        "import io.javalin.Javalin;\n" +
        "\n" +
        "%s\n" + // imports eg: "import api.handlers.$nameLower.GetRequestHandler;"
        "\n" +
        "@Module\n" +
        "public class %sApiBuilder {\n" +
        "  private final Javalin router;\n" +
        "  private final String basePath = \"/%s\";\n" +
        "%s\n" +
        "\n" +
        "  @Inject\n" +
        "  public %sApiBuilder(Javalin router, %s) {\n" +
        "    this.router = router;\n" +
        "%s\n" +
        "  }\n" +
        "\n" +
        "  public void apply() {\n" +
        "%s\n" +
        "  }\n" +
        "}\n",
        imports,
        className,
        route,
        classFields,
        className,
        constructorFields,
        fieldAssignments,
        applies);
  }
}
