package api.providers.jobs;

import static com.mongodb.client.model.Filters.eq;

import javax.inject.Inject;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;

import api.database.entities.Job;
import api.database.enums.JobStatus;
import dagger.Module;

@Module
public class DatabaseJobProvider implements JobProvider {
  private final MongoCollection<Job> jobs;

  @Inject
  public DatabaseJobProvider(MongoCollection<Job> jobs) {
    this.jobs = jobs;
  }

  @Override
  public void SubmitJob(Job job) {
    jobs.insertOne(job);
  }

  @Override
  public void CancelJob(ObjectId jobId) {
    jobs.updateOne(
        new Document("_id", jobId),
        new Document("$set", new Document("status", JobStatus.CANCELLED)));
  }

  @Override
  public Job GetJob(ObjectId jobId) {
    var job = jobs.find(eq("_id", jobId)).first();

    if (job == null || job.id == null) {
      return null;
    }

    return job;
  }

}
