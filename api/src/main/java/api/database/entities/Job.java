package api.database.entities;

import org.bson.types.ObjectId;
import api.database.JobStatus;

public class Job {
  public ObjectId id;
  public ObjectId taskId;
  public ObjectId userId;
  public JobStatus status;
  public String solution;
}
