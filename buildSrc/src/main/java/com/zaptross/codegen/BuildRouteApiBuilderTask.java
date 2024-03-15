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

    var filePaths = collectAllHandlerFilepaths(fd);
    var classPathGroups = buildRouteGroupsFromFilepaths(filePaths);

    var apiBuilderClasses = new ArrayList<String>();

    System.out.println(
        String.format("Generating ApiBuilders for %d route groups...", classPathGroups.size()));
    for (var cpg : classPathGroups) {
      var len = cpg.get(0).toArray().length;
      var fileName = buildFileName(cpg.get(0));
      var className = buildClassName(cpg.get(0));
      var fileContent = buildFileTemplate(
          className,
          buildImports(cpg),
          buildRoute(cpg.get(0)),
          buildClassFields(cpg),
          buildConstructorFields(cpg),
          buildFieldAssignments(cpg),
          buildApplies(cpg));

      apiBuilderClasses.add(className);

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
        System.out.println(String.format("Failed to generate %s from buildSrc",
            fileName));
        e.printStackTrace();
      }
    }

    var applicatorFileName = "ApiBuildersApplicator";
    System.out.println(String.format("Generating %s...", applicatorFileName));
    try {
      var newFile = new BufferedWriter(
          new FileWriter(
              System.getProperty("user.dir") + "/api/src/main/java/api/generated/" +
                  applicatorFileName + ".java"));
      newFile.write(buildApplicatorTemplate(apiBuilderClasses));
      newFile.close();
      System.out.println(String.format("%s", applicatorFileName));
    } catch (Exception e) {
      System.out.println(String.format("Failed to generate %s from buildSrc",
          applicatorFileName));
      e.printStackTrace();
    }
  }

  private ArrayList<String> collectAllHandlerFilepaths(String handlerDirectory) {
    var filePaths = new ArrayList<String>();
    var directories = new ArrayList<File>();
    directories.add(new File(handlerDirectory));

    while (directories.size() > 0) {
      var files = directories.remove(0).listFiles();

      if (files.length == 0) {
        continue;
      }

      for (var file : files) {
        if (file.isDirectory()) {
          directories.add(file);
        } else {
          filePaths.add(file.getAbsolutePath().replace(handlerDirectory, "").replace(".java", ""));
        }
      }
    }

    filePaths.sort((a, b) -> a.compareTo(b));

    return filePaths;
  }

  private ArrayList<ArrayList<ArrayList<String>>> buildRouteGroupsFromFilepaths(ArrayList<String> filePaths) {
    var groups = new ArrayList<ArrayList<ArrayList<String>>>();
    for (var fp : filePaths) {
      var split = splitFilePath(fp);
      if (groups.size() == 0) {
        var classPath = new ArrayList<ArrayList<String>>();
        classPath.add(split);
        groups.add(classPath);
        continue;
      }

      // Handle root paths
      if (split.size() == 1) {
        for (var cpg : groups) {
          if (cpg.get(0).size() == 1) {
            cpg.add(split);
          }
          break;
        }
      } else {
        var added = false;

        // Check if the path is a method in an existing group
        for (var cpg : groups) {
          var gp = String.join("/", cpg.get(0).subList(0, cpg.get(0).size() - 1));
          var sp = String.join("/", split.subList(0, split.size() - 1));
          if (split.size() == cpg.get(0).size() && gp.equals(sp)) {
            cpg.add(split);
            added = true;
          }
        }

        // If no group was found, create a new one
        if (!added) {
          var classPath = new ArrayList<ArrayList<String>>();
          classPath.add(split);
          groups.add(classPath);
        }
      }
    }

    return groups;
  }

  private ArrayList<String> splitFilePath(String filePath) {
    var al = new ArrayList<String>();
    for (var segment : filePath.split("/")) {
      al.add(segment);
    }
    return al;
  }

  private String buildClassName(ArrayList<String> classPath) {
    var out = "";
    if (classPath.size() == 1) {
      return "Root";
    }
    for (var path : classPath.subList(0, classPath.size() - 1)) {
      if (path.startsWith("_")) {
        out += capitaliseFirstLetter(path.replace("_", ""));
      } else {
        out += capitaliseFirstLetter(path);
      }
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
    var routeSegments = new ArrayList<String>();
    for (var segment : classPath.subList(0, classPath.size() - 1)) {
      if (segment.startsWith("_")) {
        routeSegments.add(
            segment.replace("_", "{") + "}");
      } else {
        routeSegments.add(segment);
      }
    }
    return "/" + String.join("/", routeSegments).toLowerCase();
  }

  private String buildApplies(ArrayList<ArrayList<String>> classPaths) {
    var applies = new ArrayList<String>();
    for (var classPath : classPaths) {
      var fieldName = getFieldNameFromClassName(classPath.get(classPath.size() - 1));
      applies.add(
          String.format("    Stream.of(%s.before()).forEach(handler -> router.before(basePath, handler));", fieldName));
      applies.add(String.format("    router.%s(basePath, %s);", fieldName, fieldName));
      applies.add(
          String.format("    Stream.of(%s.after()).forEach(handler -> router.after(basePath, handler));", fieldName));
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
      methods.add(String.format("%s\t%s", getFieldNameFromClassName(className).toUpperCase(), route));
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
      fields.add(String.format("  private final %s %s;", className, getFieldNameFromClassName(className)));
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

  private String buildFieldName(String className) {
    return className.substring(0, 1).toLowerCase() + className.substring(1);
  }

  private String buildApplicatorTemplate(ArrayList<String> classNames) {
    var injects = String.join(",\n",
        classNames.stream().map(cn -> String.format("      %sApiBuilder %sApiBuilder", cn, buildFieldName(cn)))
            .toList());
    var applies = String.join(",\n",
        classNames.stream().map(cn -> String.format("        %sApiBuilder", buildFieldName(cn))).toList());
    return String.format(
        "package api.generated;\n" +
            "\n" +
            "import java.util.Set;\n" +
            "\n" +
            "import javax.inject.Inject;\n" +
            "\n" +
            "import io.javalin.Javalin;\n" +
            "\n" +
            "import api.api.ApiBuilderInterface;\n" +
            "import dagger.Module;\n" +
            "\n" +
            "@Module\n" +
            "public class ApiBuildersApplicator {\n" +
            "  private final Set<ApiBuilderInterface> apiBuilders;\n" +
            "\n" +
            "  @Inject\n" +
            "  public ApiBuildersApplicator(\n" +
            "%s) {\n" +
            "    this.apiBuilders = Set.of(\n" +
            "%s);\n" +
            "  }\n" +
            "\n" +
            "  public void applyAll(Javalin router) {\n" +
            "    apiBuilders.forEach(builder -> builder.apply(router));\n" +
            "  }\n" +
            "}\n",
        injects,
        applies);
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
        "import java.util.stream.Stream;\n" +
        "\n" +
        "import javax.inject.Inject;\n" +
        "\n" +
        "import io.javalin.Javalin;\n" +
        "\n" +
        "import api.api.ApiBuilderInterface;\n" +
        "%s\n" + // imports eg: "import api.handlers.$nameLower.GetRequestHandler;"
        "import dagger.Module;\n" +
        "import dagger.Provides;\n" +
        "import dagger.multibindings.IntoSet;\n" +
        "\n" +
        "@Module\n" +
        "public class %sApiBuilder implements ApiBuilderInterface {\n" +
        "  private final String basePath = \"%s\";\n" +
        "%s\n" +
        "\n" +
        "  @Inject\n" +
        "  public %sApiBuilder(%s) {\n" +
        "%s\n" +
        "  }\n" +
        "\n" +
        "  @Override\n" +
        "  public void apply(Javalin router) {\n" +
        "%s\n" +
        "  }\n" +
        "\n" +
        "  @Provides\n" +
        "  @IntoSet\n" +
        "  public static ApiBuilderInterface provideApiBuilder(%sApiBuilder builder) {\n" +
        "    return builder;\n" +
        "  }\n" +
        "}\n",
        imports,
        className,
        route,
        classFields,
        className,
        constructorFields,
        fieldAssignments,
        applies,
        className);
  }
}
