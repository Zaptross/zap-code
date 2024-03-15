package api.utils;

import org.bson.types.ObjectId;

public class Utils {
  public static boolean IsValidOID(String oid) {
    if (oid == null || !ObjectId.isValid(oid)) {
      return false;
    }
    return true;
  }
}
