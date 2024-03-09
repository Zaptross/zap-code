package api.database.entities;

import java.util.List;

import org.bson.types.ObjectId;

public class Logs {
  public ObjectId id;
  public ObjectId userId;
  public ObjectId taskId;
  public List<String> logs;
  public List<String> tests;
}
