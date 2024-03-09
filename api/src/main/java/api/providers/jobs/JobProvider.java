package api.providers.jobs;

import org.bson.types.ObjectId;

import api.database.entities.Job;
import api.database.enums.JobStatus;

public interface JobProvider {
  public void SubmitJob(Job job);

  public void CancelJob(ObjectId jobId);

  public JobStatus GetJobStatus(ObjectId jobId);
}
