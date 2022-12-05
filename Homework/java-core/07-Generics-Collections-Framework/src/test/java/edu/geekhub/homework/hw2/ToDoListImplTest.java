package edu.geekhub.homework.hw2;

import edu.geekhub.homework.hw2.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ToDoListImplTest {
    private ToDoListImpl<Task> toDoList;

    @BeforeEach
    void setUp() {
        toDoList = new ToDoListImpl<>();
    }

    @Test
    void can_not_add_null_to_start() {
        boolean isAdded = toDoList.addTaskToTheStart(null);

        assertFalse(isAdded);
    }

    @Test
    void can_not_add_null_to_end() {
        boolean isAdded = toDoList.addTaskToTheEnd(null);

        assertFalse(isAdded);
    }

    @Test
    void can_add_task_to_the_end() {
        Task startTask = new Task("Name1", "Description1", 1);
        Task endTask = new Task("Name2", "Description2", 2);

        toDoList.addTaskToTheEnd(startTask);
        toDoList.addTaskToTheEnd(endTask);

        assertEquals(endTask, toDoList.getTaskByIndex(1));
    }

    @Test
    void can_add_task_to_the_start() {
        Task startTask = new Task("Name 1", "Description 1", 1);
        Task endTask = new Task("Name 2", "Description 2", 2);

        toDoList.addTaskToTheStart(endTask);
        toDoList.addTaskToTheStart(startTask);


        assertEquals(startTask, toDoList.getTaskByIndex(0));
    }

    @Test
    void can_not_get_task_by_index_less_than_0() {
        Task task = new Task("", "", 1);
        toDoList.addTaskToTheEnd(task);

        assertThrows(
            IllegalArgumentException.class,
            () -> toDoList.getTaskByIndex(-10)
        );
    }

    @Test
    void can_not_get_task_by_index_more_than_list_length() {
        Task task = new Task("", "", 1);
        toDoList.addTaskToTheEnd(task);

        assertThrows(
            IllegalArgumentException.class,
            () -> toDoList.getTaskByIndex(1)
        );
    }

    @Test
    void can_get_sorted_priority_tasks() {
        Task task1 = new Task("", "", 10);
        Task task2 = new Task("", "", 5);
        toDoList.addTaskToTheEnd(task1);
        toDoList.addTaskToTheEnd(task2);

        List<Task> expectedList = new ArrayList<>();
        expectedList.add(task2);
        expectedList.add(task1);

        assertEquals(expectedList, toDoList.getSortedPriorityTasks());
    }

    @Test
    void can_get_sorted_by_alphabet() {
        Task task1 = new Task("Name 2", "", 1);
        Task task2 = new Task("Name 1", "", 1);
        toDoList.addTaskToTheEnd(task1);
        toDoList.addTaskToTheEnd(task2);

        List<Task> expectedList = new ArrayList<>();
        expectedList.add(task2);
        expectedList.add(task1);

        assertEquals(expectedList, toDoList.getSortedByAlphabetTasks());
    }

    @Test
    void can_set_sorted_by_alphabet_with_equal_name() {
        Task task1 = new Task("Name 1", "Desc 2", 1);
        Task task2 = new Task("Name 1", "Desc 1", 1);
        toDoList.addTaskToTheEnd(task1);
        toDoList.addTaskToTheEnd(task2);

        List<Task> expectedList = new ArrayList<>();
        expectedList.add(task2);
        expectedList.add(task1);

        assertEquals(expectedList, toDoList.getSortedByAlphabetTasks());
    }

    @Test
    void can_get_top_priority_task() {
        Task expectedTask = new Task("Some name 1", "Some description 1", 10);

        toDoList.addTaskToTheEnd(new Task("", "", 1));
        toDoList.addTaskToTheEnd(expectedTask);
        toDoList.addTaskToTheEnd(new Task("", "", 8));


        assertEquals(expectedTask, toDoList.getTopPriorityTask());
    }

    @Test
    void can_get_all_tasks() {
        Task task1 = new Task("", "", 1);
        Task task2 = new Task("", "", 2);
        toDoList.addTaskToTheEnd(task1);
        toDoList.addTaskToTheEnd(task2);

        List<Task> expectedList = new ArrayList<>();
        expectedList.add(task1);
        expectedList.add(task2);

        assertEquals(expectedList, toDoList.getAllTasks());
    }

    @Test
    void can_delete_task() {
        Task task1 = new Task("", "", 1);
        Task task2 = new Task("", "", 2);
        toDoList.addTaskToTheEnd(task1);
        toDoList.addTaskToTheEnd(task2);
        toDoList.deleteTask(task1);

        List<Task> expectedList = new ArrayList<>();
        expectedList.add(task2);

        assertEquals(expectedList, toDoList.getAllTasks());
    }

    @Test
    void can_not_delete_task_that_is_not_in_list() {
        Task task1 = new Task("", "", 1);
        Task task2 = new Task("", "", 2);
        toDoList.addTaskToTheEnd(task1);

        boolean isDeleted = toDoList.deleteTask(task2);

        List<Task> expectedList = new ArrayList<>();
        expectedList.add(task1);

        assertEquals(expectedList, toDoList.getAllTasks());
        assertFalse(isDeleted);
    }

    @Test
    void can_delete_task_by_index() {
        Task task1 = new Task("", "", 1);
        Task task2 = new Task("", "", 2);
        toDoList.addTaskToTheEnd(task1);
        toDoList.addTaskToTheEnd(task2);
        toDoList.deleteTaskByIndex(0);

        List<Task> expectedList = new ArrayList<>();
        expectedList.add(task2);

        assertEquals(expectedList, toDoList.getAllTasks());
    }

    @Test
    void can_not_delete_task_by_index_less_than_0() {
        Task task1 = new Task("", "", 1);
        toDoList.addTaskToTheEnd(task1);
        boolean isDeleted = toDoList.deleteTaskByIndex(-10);

        List<Task> expectedList = new ArrayList<>();
        expectedList.add(task1);

        assertEquals(expectedList, toDoList.getAllTasks());
        assertFalse(isDeleted);
    }

    @Test
    void can_not_delete_task_by_index_more_than_list_length() {
        Task task1 = new Task("", "", 1);
        toDoList.addTaskToTheEnd(task1);
        boolean isDeleted = toDoList.deleteTaskByIndex(-10);

        List<Task> expectedList = new ArrayList<>();
        expectedList.add(task1);

        assertEquals(expectedList, toDoList.getAllTasks());
        assertFalse(isDeleted);
    }
}
