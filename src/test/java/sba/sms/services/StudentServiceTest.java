package sba.sms.services;

import org.junit.jupiter.api.*;
import sba.sms.models.Student;
import sba.sms.services.StudentService;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceTest {

    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        studentService = new StudentService();
    }

    @Test
    public void testCreateStudent() {

        Student student = new Student("johar@gmail.com", "Hasib Johar", "password");


        assertDoesNotThrow(() -> studentService.createStudent(student));


        Student createdStudent = studentService.getStudentByEmail("johar@gmail.com");


        assertNotNull(createdStudent);
        assertEquals("johar@gmail.com", createdStudent.getEmail());
        assertEquals("Hasib Johar", createdStudent.getName());
        assertEquals("password", createdStudent.getPassword());
    }

    @AfterEach
    public void tearDown() {

    }
}
