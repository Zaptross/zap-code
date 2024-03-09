package api.api.entities;

import org.bson.types.ObjectId;

import api.database.entities.Job;
import api.database.enums.JobStatus;

public class JobRequest {
  public String userId;
  public String taskId;
  public String solution;

  public Job toJob() {
    var job = new Job();
    job.id = new ObjectId();
    job.userId = new ObjectId(userId);
    job.taskId = new ObjectId(taskId);
    job.status = JobStatus.PENDING;
    job.solution = solution;
    return job;
  }
}
