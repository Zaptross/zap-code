package api.api.entities;

import api.tasks.Task;

public class ApiTask {
  public String id;
  public String name;
  public String description;
  public String template;

  public ApiTask fromTask(Task task) {
    var t = new ApiTask();
    t.id = task.getId().toString();
    t.name = task.getName();
    t.description = task.getDescription();
    t.template = task.getTemplate();
    return t;
  }
}
