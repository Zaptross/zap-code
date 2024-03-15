package api.providers.jobs;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import javax.inject.Inject;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

import com.mongodb.client.MongoCollection;

import api.database.entities.Job;
import api.database.enums.JobStatus;
import api.providers.logger.LoggerProvider;
import dagger.Module;

@Module
public class DatabaseJobProvider implements JobProvider {
  private final MongoCollection<Job> jobs;
  private final Logger logger;

  @Inject
  public DatabaseJobProvider(MongoCollection<Job> jobs, LoggerProvider loggerProvider) {
    this.jobs = jobs;
    this.logger = loggerProvider.getLogger(getClass());
  }

  @Override
  public void SubmitJob(Job job) {
    try {
      jobs.insertOne(job);
    } catch (Exception e) {
      logger.error("Failed to create job.", e);
    }
  }

  @Override
  public void CancelJob(ObjectId jobId) {
    try {
      jobs.updateOne(
          new Document("_id", jobId),
          new Document("$set", new Document("status", JobStatus.CANCELLED)));
    } catch (Exception e) {
      logger.error("Failed to cancel job.", e);
    }
  }

  @Override
  public Job GetJob(ObjectId jobId, ObjectId userId) {
    try {
      var job = jobs.find(getJobFilter(userId, jobId)).first();

      if (job == null || job.id == null) {
        return null;
      }

      return job;
    } catch (Exception e) {
      logger.error("Failed to retrieve job by id.", e);
      return null;
    }
  }

  private Bson getJobFilter(ObjectId userId, ObjectId jobId) {
    return and(eq("_id", jobId), eq("userId", userId));
  }
}
