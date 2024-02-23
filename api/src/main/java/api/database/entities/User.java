package api.database.entities;

import org.bson.types.ObjectId;

public class User {
  public ObjectId id;
  public String email;
  public AuthProvider authProvider;
}