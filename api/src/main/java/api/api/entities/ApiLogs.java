package api.api.entities;

import java.util.List;

import api.database.entities.Logs;

public class ApiLogs {
  public String id;
  public String userId;
  public String taskId;
  public List<String> tests;
  public List<String> logs;

  public static ApiLogs fromLogs(Logs logs) {
    var l = new ApiLogs();
    l.id = logs.id.toString();
    l.userId = logs.userId.toString();
    l.taskId = logs.taskId.toString();
    l.tests = logs.tests;
    l.logs = logs.logs;
    return l;
  }
}
