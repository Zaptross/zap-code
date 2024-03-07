package api.tasks;

import java.util.Set;

import dagger.Component;

@Component(modules = { FizzBuzzTask.class, ReverseAString.class })
public interface Tasks {
  Set<Task> tasks();
}
