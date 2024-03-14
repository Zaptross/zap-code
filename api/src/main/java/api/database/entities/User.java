package api.database.entities;

import org.bson.types.ObjectId;

import api.database.enums.AuthProvider;

public class User {
  public ObjectId id;
  public String externalId;
  public AuthProvider authProvider;
  public String username;
  public String avatarUrl;
}