package api.env;

import java.lang.reflect.Modifier;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig<T> {
  private T config;
  private String key;
  private Class<T> cls;

  private Dotenv env;

  public EnvConfig(Class<T> cls, T config) {
    this.config = config;
    this.cls = cls;
  }

  public EnvConfig(Class<T> cls, T config, String key) {
    this.config = config;
    this.key = key;
    this.cls = cls;
  }

  public T Build() {
    if (hasPublicSetters()) {
      return BuildWithSetters();
    } else if (hasPublicFields()) {
      return BuildWithPublicFields();
    } else {
      throw new RuntimeException("No public setters or fields found");
    }
  }

  public EnvConfig<T> FromEnv(Dotenv env) {
    this.env = env;
    return this;
  }

  private String getEnvValue(String key) {
    if (env != null && env.get(key) != null) {
      return env.get(key);
    }
    return System.getenv(key);
  }

  private boolean hasPublicSetters() {
    var methods = cls.getDeclaredMethods();
    for (var method : methods) {
      var mods = method.getModifiers();
      var name = method.getName();
      if (Modifier.isPrivate(mods) || !name.startsWith("set")) {
        continue;
      }
      return true;
    }
    return false;
  }

  public T BuildWithSetters() {
    var methods = cls.getDeclaredMethods();

    for (var method : methods) {
      var mods = method.getModifiers();
      var name = method.getName();
      if (Modifier.isPrivate(mods) || !name.startsWith("set")) {
        continue;
      }

      // eg: "setPort" -> "port"
      var fieldName = name.substring(3).toLowerCase();

      // Where key = "API"
      // eg: "API" + "_" + "PORT"
      // Where key = null
      // eg: "PORT"
      var envKey = key != null
          ? key.toUpperCase() + "_" + fieldName.toUpperCase()
          : fieldName.toUpperCase();

      var envValue = this.getEnvValue(envKey);
      if (envValue == null) {
        // fallback to lowercase
        // eg: "api_port"
        envValue = this.getEnvValue(fieldName.toLowerCase());
      }

      if (envValue == null) {
        // if no environment variable is found, continue
        continue;
      }

      try {
        // eg: "setPort"
        // cls.getMethod("setPort", int.class)
        var paramType = method.getParameterTypes()[0];
        var paramValue = paramType.cast(envValue);
        cls.getMethod(name, paramType).invoke(config, paramValue);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return this.config;
  }

  private boolean hasPublicFields() {
    var fields = cls.getDeclaredFields();
    for (var field : fields) {
      var mods = field.getModifiers();
      if (Modifier.isPublic(mods)) {
        return true;
      }
    }
    return false;
  }

  public T BuildWithPublicFields() {
    var fields = cls.getDeclaredFields();

    for (var field : fields) {
      var mods = field.getModifiers();
      var name = field.getName();
      if (Modifier.isPrivate(mods)) {
        continue;
      }

      // eg: "Port" -> "port"
      var fieldName = name.toLowerCase();

      // Where key = "API"
      // eg: "API" + "_" + "PORT"
      // Where key = null
      // eg: "PORT"
      var envKey = key != null
          ? key.toUpperCase() + "_" + fieldName.toUpperCase()
          : fieldName.toUpperCase();

      var envValue = this.getEnvValue(envKey);
      if (envValue == null) {
        // fallback to lowercase
        // eg: "api_port"
        envValue = this.getEnvValue(fieldName.toLowerCase());
      }

      if (envValue == null) {
        // if no environment variable is found, continue
        continue;
      }

      try {
        var paramType = field.getType();
        var paramValue = paramType.cast(envValue);
        field.set(config, paramValue);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return this.config;
  }
}
