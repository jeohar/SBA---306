package sba.sms.services;

import Exceptions.StudentAlreadyExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sba.sms.dao.CourseI;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.Collections;
import java.util.List;


/**
 * StudentService is a concrete class. This class implements the
 * StudentI interface, overrides all abstract service methods and
 * provides implementation for each method. Lombok @Log used to
 * generate a logger file.
 */

public class StudentService implements StudentI {


    @Override
    public List<Student> getAllStudents() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            return session.createQuery("from Student", Student.class).list();
        }
    }

@Override
public void createStudent(Student student) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        transaction = session.beginTransaction();


        Student existingStudent = session.get(Student.class, student.getEmail());

        // Check if the student already exists
        if (existingStudent != null) {
            throw new StudentAlreadyExistsException("Student with email " + student.getEmail() + " already exists");
        } else {

            session.persist(student);
        }

        transaction.commit();
    } catch (HibernateException e) {
        if (transaction != null) {
            transaction.rollback();
        }
        e.printStackTrace();
    } catch (StudentAlreadyExistsException e) {
        throw new RuntimeException(e);
    }
}


    @Override
    public Student getStudentByEmail(String email) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            return session.get(Student.class, email);
        }

    }


    @Override
    public boolean validateStudent(String email, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Retrieve the student from the database based on the provided email
            Student student = session.createQuery("FROM Student WHERE email = :email", Student.class)
                    .setParameter("email", email)
                    .uniqueResult();

            // Check if the student exists and the password matches
            return student != null && student.getPassword().equals(password);
        } catch (Exception e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            return false; // Return false indicating validation failure
        }
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Retrieve the student by email
            Student student = session.createQuery("FROM Student WHERE email = :email", Student.class)
                    .setParameter("email", email)
                    .uniqueResult();

            // Retrieve the course by courseId
            Course course = session.get(Course.class, courseId);

            if (student != null && course != null) {
                // Associate the course with the student
                student.getCourses().add(course);
                course.getStudents().add(student);

                // Update the associations in the database
                session.persist(student);
                session.persist(course);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> getStudentCourses(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Query to retrieve courses associated with the student's email
            return session.createQuery(
                            "SELECT c FROM Course c JOIN c.students s WHERE s.email = :email", Course.class)
                    .setParameter("email", email)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list if an exception occurs
        }
    }
}
