package api.api.entities;

import api.database.entities.User;

public class ApiUser {
  public String id;
  public String email;

  public static ApiUser fromUser(User user) {
    var u = new ApiUser();
    u.id = user.id.toString();
    u.email = user.email;
    return u;
  }
}
