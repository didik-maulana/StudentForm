package examples;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object (DAO) class for managing student data.
 *
 * @author didikmaulanaardiansyah
 */
public class StudentDAO {
    private Connection dbConnection;

     /**
     * Constructs a new StudentDAO with the specified database connection.
     *
     * @param dbConnection the database connection to use
     */
    public StudentDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }
    
    /**
     * Retrieves all students from the database.
     *
     * @return a ResultSet containing all students
     */
    public ResultSet getStudents() {
        try {
            Statement statement = dbConnection.createStatement();
            return statement.executeQuery("SELECT * FROM students");
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return null;
    }
    
    /**
     * Retrieves a student with the specified NIM from the database.
     *
     * @param nim the NIM of the student to retrieve
     * @return a ResultSet containing the student data
     */
    public ResultSet getStudent(String nim) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT * FROM students WHERE nim = ?");
            preparedStatement.setString(1, nim);
            return preparedStatement.executeQuery();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts a new student into the database.
     *
     * @param nim the NIM of the student
     * @param name the name of the student
     * @param score_tugas the task score of the student
     * @param score_quiz the quiz score of the student
     * @param score_uts the UTS score of the student
     * @param score_uas the UAS score of the student
     * @param average the average score of the student
     * @param grade the grade of the student
     * @param description the description of the student
     */
    public void insertStudent(String nim, String name, double score_tugas, double score_quiz, double score_uts, double score_uas, double average, String grade, String description) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("INSERT INTO students (nim, name, score_tugas, score_quiz, score_uts, score_uas, average, grade, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, nim);
            preparedStatement.setString(2, name);
            preparedStatement.setDouble(3, score_tugas);
            preparedStatement.setDouble(4, score_quiz);
            preparedStatement.setDouble(5, score_uts);
            preparedStatement.setDouble(6, score_uas);
            preparedStatement.setDouble(7, average);
            preparedStatement.setString(8, grade);
            preparedStatement.setString(9, description);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    /**
     * Updates an existing student in the database.
     *
     * @param nim the NIM of the student
     * @param name the name of the student
     * @param score_tugas the task score of the student
     * @param score_quiz the quiz score of the student
     * @param score_uts the UTS score of the student
     * @param score_uas the UAS score of the student
     * @param average the average score of the student
     * @param grade the grade of the student
     * @param description the description of the student
     */
    public void updateStudent(String nim, String name, double score_tugas, double score_quiz, double score_uts, double score_uas, double average, String grade, String description) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("UPDATE students SET name = ?, score_tugas = ?, score_quiz = ?, score_uts = ?, score_uas = ?, average = ?, grade = ?, description = ? WHERE nim = ?");
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, score_tugas);
            preparedStatement.setDouble(3, score_quiz);
            preparedStatement.setDouble(4, score_uts);
            preparedStatement.setDouble(5, score_uas);
            preparedStatement.setDouble(6, average);
            preparedStatement.setString(7, grade);
            preparedStatement.setString(8, description);
            preparedStatement.setString(9, nim);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    /**
     * Checks if a student with the specified NIM exists in the database.
     *
     * @param nim the NIM of the student to check
     * @return true if the student exists, false otherwise
     */
    public boolean studentExists(String nim) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT * FROM students WHERE nim = ?");
            preparedStatement.setString(1, nim);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return false;
    }
    
    /**
    * Deletes a student with the specified NIM from the database.
    *
    * @param nim the NIM of the student to delete
    */
   public void deleteStudent(String nim) {
       try {
           PreparedStatement preparedStatement = dbConnection.prepareStatement("DELETE FROM students WHERE nim = ?");
           preparedStatement.setString(1, nim);
           preparedStatement.executeUpdate();
       } catch (SQLException error) {
           error.printStackTrace();
       }
   }
}
