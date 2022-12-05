package edu.geekhub.homework.hw2.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task("Some name", "Some description", 1);
    }

    @Test
    void can_get_task_name() {
        String expectedName = "Some name";

        assertEquals(expectedName, task.getName());
    }

    @Test
    void can_get_task_description() {
        String expectedDesc = "Some description";

        assertEquals(expectedDesc, task.getDescription());
    }

    @Test
    void can_get_task_priority() {
        int expectedPrior = 1;

        assertEquals(expectedPrior, task.getPriority());
    }

    @Test
    void can_compare_equal_tasks() {
        Task task2 = new Task("Some name", "Some description", 1);

        assertEquals(task2, task);
    }

    @Test
    void can_compare_not_equal_tasks() {
        Task task2 = new Task("Some name 2", "Some description 2", 2);

        assertNotEquals(task2, task);
    }

    @Test
    void can_get_hash_code() {
        int expectedHash = Objects.hash(task.getName(), task.getDescription(), task.getPriority());

        assertEquals(expectedHash, task.hashCode());
    }

    @Test
    void can_compare_equal_tasks_by_hash_code() {
        Task task2 = new Task("Some name", "Some description", 1);

        assertEquals(task2.hashCode(), task.hashCode());
    }

    @Test
    void can_compare_not_equal_tasks_by_hash_code() {
        Task task2 = new Task("Some name 2", "Some description 2", 2);

        assertNotEquals(task2.hashCode(), task.hashCode());
    }
}
