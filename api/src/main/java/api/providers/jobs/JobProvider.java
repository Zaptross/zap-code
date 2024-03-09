package api.providers.jobs;

import org.bson.types.ObjectId;

import api.database.entities.Job;

public interface JobProvider {
  public void SubmitJob(Job job);

  public void CancelJob(ObjectId jobId);

  public Job GetJob(ObjectId jobId);
}
