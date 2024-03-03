package api.database.entities;

import org.bson.types.ObjectId;
import api.database.JobStatus;

public class Job {
  public ObjectId id;
  public String taskId;
  public String userId;
  public JobStatus status;
  public String solution;
}
