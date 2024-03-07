package api.tasks;

import org.bson.types.ObjectId;

import dagger.Module;

@Module
public interface Task {
  Task provideTask();

  ObjectId getId();

  String getName();

  String getDescription();

  String getTemplate();
}
