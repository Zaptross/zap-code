package api.database.entities;

import org.bson.types.ObjectId;

public class Logs {
  public ObjectId Id;
  public ObjectId UserId;
  public ObjectId TaskId;
  public String[] Tests;
  public String[] Logs;
}
