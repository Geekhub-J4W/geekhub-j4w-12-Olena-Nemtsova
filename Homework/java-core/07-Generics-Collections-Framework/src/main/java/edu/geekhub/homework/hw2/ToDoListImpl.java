package edu.geekhub.homework.hw2;

import edu.geekhub.homework.hw2.entity.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ToDoListImpl<E extends Task> implements ToDoList<E> {
    private final List<E> tasksStorage = new ArrayList<>();

    @Override
    public E getTopPriorityTask() {
        List<E> priorityTasks = getSortedPriorityTasks();

        return priorityTasks.get(priorityTasks.size() - 1);
    }

    @Override
    public E getTaskByIndex(int index) {
        validateIndex(index);

        return tasksStorage.get(index);
    }

    @Override
    public List<E> getAllTasks() {
        return tasksStorage;
    }

    @Override
    public List<E> getSortedPriorityTasks() {
        List<E> priorityTasks = new ArrayList<>(tasksStorage);

        priorityTasks.sort(Comparator.comparingInt(Task::getPriority));

        return priorityTasks;
    }

    @Override
    public List<E> getSortedByAlphabetTasks() {
        List<E> byAlphabetTasks = new ArrayList<>(tasksStorage);

        byAlphabetTasks.sort((o1, o2) -> {
            int isEqual = o1.getName().compareTo(o2.getName());

            if (isEqual == 0) {
                return o1.getDescription().compareTo(o2.getDescription());
            } else {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return byAlphabetTasks;
    }

    @Override
    public boolean addTaskToTheEnd(E task) {
        if (task != null) {
            tasksStorage.add(task);
            return true;
        }

        return false;
    }

    @Override
    public boolean addTaskToTheStart(E task) {
        if (task != null) {
            tasksStorage.add(0, task);
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteTaskByIndex(int index) {
        try {
            tasksStorage.remove(index);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteTask(E task) {
        int index = tasksStorage.indexOf(task);

        try {
            tasksStorage.remove(index);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        return true;
    }

    private void validateIndex(int index) {
        if (index < 0 || index > tasksStorage.size() - 1) {
            throw new IllegalArgumentException("Index is out of range");
        }
    }
}
