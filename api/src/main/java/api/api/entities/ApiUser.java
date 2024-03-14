package api.api.entities;

import api.database.entities.User;

public class ApiUser {
  public String id;
  public String username;
  public String avatarUrl;

  public static ApiUser fromUser(User user) {
    var u = new ApiUser();
    u.id = user.id.toString();
    u.username = user.username;
    u.avatarUrl = user.avatarUrl;
    return u;
  }
}
