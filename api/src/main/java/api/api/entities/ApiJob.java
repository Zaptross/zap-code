package api.api.entities;

import api.database.entities.Job;

public class ApiJob {
  public String id;
  public String userId;
  public String taskId;
  public String status;
  public String solution;

  public static ApiJob fromJob(Job job) {
    var j = new ApiJob();
    j.id = job.id.toString();
    j.userId = job.userId.toString();
    j.taskId = job.taskId.toString();
    j.status = job.status.toString();
    j.solution = job.solution;
    return j;
  }
}
