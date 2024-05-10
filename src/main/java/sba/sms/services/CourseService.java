package sba.sms.services;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.utils.HibernateUtil;
import java.util.List;

/**
 * CourseService is a concrete class. This class implements the
 * CourseI interface, overrides all abstract service methods and
 * provides implementation for each method.
 */
public class CourseService implements CourseI {



    @Override
    public void createCourse(Course course) {
        Transaction transation = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
                transation = session.beginTransaction();
                Course existcourse = session.get(Course.class, course.getId());
                if (existcourse == null) {

                    session.persist(course);
                }else{
                    existcourse.setId(course.getId());
                    existcourse.setName(course.getName());
                    existcourse.setInstructor(course.getInstructor());
                    session.saveOrUpdate(existcourse);
                }
            transation.commit();
        } catch (Exception e) {
            if (transation != null && transation.isActive()) {
                transation.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Course getCourseById(int courseId) {

        try
                (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.get(Course.class, courseId);
            }
        }



    @Override
    public List<Course> getAllCourses() {
        try
            (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.createQuery("from Course", Course.class).list();
            }

    }

    }
