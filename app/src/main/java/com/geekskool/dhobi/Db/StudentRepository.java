package com.geekskool.dhobi.Db;

import com.geekskool.dhobi.Helpers.DbConstants;
import com.geekskool.dhobi.Models.Course;
import com.geekskool.dhobi.Models.Expense;
import com.geekskool.dhobi.Models.Student;

import io.realm.Realm;
import io.realm.Realm.Transaction.OnSuccess;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by manisharana on 1/3/17.
 */

public class StudentRepository extends Repository{

    public void addStudent(String courseId, final Student student, final OnSuccess callback) {
        final Course course = getCourse(courseId);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Student studRow = realm.copyToRealmOrUpdate(student);
                course.getStudents().add(studRow);
                if (callback != null)
                    callback.onSuccess();
            }
        });
    }

    private Course getCourse(String courseId) {
        return new CourseRepository().get(courseId);
    }

    public Student getStudent(String studentId) {
        return realm.where(Student.class).equalTo(DbConstants.studentID, studentId).findFirst();
    }

    public RealmResults<Student> getAllStudents(String courseId) {
        return getCourse(courseId).getStudents().sort(DbConstants.studentName, Sort.ASCENDING);
    }

    private RealmList<Expense> getAllExpenses(String studentId) {
        return getStudent(studentId).getExpenses();
    }

    public RealmResults<Expense> getAllExpensesSorted(String studentId) {
        return getAllExpenses(studentId).sort(DbConstants.expenseDate);
    }

    public Number getDeposits(String studentId) {
        return getAllExpenses(studentId).where().equalTo("name","Deposit").sum("amount");
    }
}
