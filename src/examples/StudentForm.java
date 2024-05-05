package examples;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author didikmaulanaardiansyah
 */
public class StudentForm extends javax.swing.JFrame {
    
    // Error Message Properties
    private static final String ERROR_ONLY_LETTERS = "%s hanya boleh mengandung huruf";
    private static final String ERROR_EMPTY = "%s tidak boleh kosong";
    private static final String ERROR_NAME_TOO_SHORT = "Nama tidak boleh kurang dari 2 karakter";
    private static final String ERROR_NAME_TOO_LONG = "Nama tidak boleh lebih dari 100 karakter";
    private static final String ERROR_NOT_NUMERIC = "%s hanya boleh berisi angka";
    private static final String ERROR_NIM_INVALID_LENGTH = "NIM harus terdiri dari 10 angka";
    private static final String ERROR_NOT_A_NUMBER = "Nilai %s harus berupa angka";
    private static final String ERROR_SCORE_OUT_OF_RANGE = "Nilai %s harus berada di antara 0 dan 100";
    
    // Database Connection
    private DatabaseConnection dbConnection;
    
    // Student DAO
    private StudentDAO studentDAO;
    
    // Student Properties
    private String name;
    private String nim;
    private double scoreTugas;
    private double scoreQuiz;
    private double scoreUTS;
    private double scoreUAS;
    
    /**
     * Creates new form StudentForm
     */
    public StudentForm() {
        initComponents();
        connectDatabase();
        initStudentDAO();
        setStudentsTableListener();
        loadData();
    }
    
    /**
    * This method establishes a connection to the database by creating a new DatabaseConnection object
    * and calling its connect method.
    */
    private void connectDatabase() {
        dbConnection = new DatabaseConnection();
        dbConnection.connect();
    }
    
    /**
    * This method initializes the StudentDAO object which is used to interact with the database.
    * It uses the connection established in the connectDatabase method.
    */
    private void initStudentDAO() {
        studentDAO = new StudentDAO(dbConnection.getConnection());
    }
    
    /**
    * This method sets a mouse click listener for the students table. When a row in the table is clicked,
    * it fetches the student data and binds it to the UI.
    */
    private void setStudentsTableListener() {
        studentsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = studentsTable.getSelectedRow();
                String nim = studentsTable.getValueAt(selectedRow, 0).toString();
                bindStudentToUI(nim);
            }
        });
    }
    
    /**
    * This method fetches a student's data from the database using their NIM and displays it in the UI.
    * It retrieves the data from the ResultSet and sets the text fields and labels in the UI.
    * @param nim The NIM of the student whose data is to be fetched.
    */
    private void bindStudentToUI(String nim) {
        ResultSet resultSet = studentDAO.getStudent(nim);
        try {
            if (resultSet.next()) {
                this.nim = nim;
                name = resultSet.getString("name");
                scoreTugas = resultSet.getDouble("score_tugas");
                scoreQuiz = resultSet.getDouble("score_quiz");
                scoreUTS = resultSet.getDouble("score_uts");
                scoreUAS = resultSet.getDouble("score_uas");
                
                double average = resultSet.getDouble("average");
                String grade = resultSet.getString("grade");
                String description = resultSet.getString("description");
                
                nameTextField.setText(name);
                nimTextField.setText(nim);
                tugasTextField.setText(Double.toString(scoreTugas));
                quizTextField.setText(Double.toString(scoreQuiz));
                utsTextField.setText(Double.toString(scoreUTS));
                uasTextField.setText(Double.toString(scoreUAS));
                
                nameLabel.setText(name);
                nimLabel.setText(nim);
                averageLabel.setText(String.valueOf(average));
                gradeLabel.setText(grade);
                descriptionLabel.setText(description);
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }
    
    /**
    * This method calculates the average score of a student.
    * @return The average score.
    */
    private double getAverage() {
        return (scoreTugas + scoreQuiz + scoreUTS + scoreUAS) / 4;
    }
    
    /**
    * This method assigns a grade to a student based on their average score.
    * @return The grade.
    */
    private String getGrade() {
        String grade;
        double score = getAverage();
        
        if (score >= 90) {
            grade = "A";
        } else if (score >= 85) {
            grade = "A-";
        } else if (score >= 80) {
            grade = "B+";
        } else if (score >= 75) {
            grade = "B";
        } else if (score >= 70) {
            grade = "B-";
        } else if (score >= 65) {
            grade = "C+";
        } else if (score >= 60) {
            grade = "C";
        } else if (score >= 55) {
            grade = "C-";
        } else if (score >= 50) {
            grade = "D";
        } else {
            grade = "E";
        }
        return grade;
    }
    
    /**
    * This method determines whether a student has passed or failed based on their average score.
    * @return The pass/fail status.
    */
    private String getDescription() {
        if (getAverage() >= 60) {
            return "Dinyatakan Lulus";
        } 
        return "Dinyatakan Tidak Lulus";   
    }
    
    /**
    * This method clears all the text fields in the UI.
    */
    private void resetTextFields() {
        nameTextField.setText("");
        nimTextField.setText("");
        tugasTextField.setText("");
        quizTextField.setText("");
        utsTextField.setText("");
        uasTextField.setText("");
    }
    
    /**
    * This method sets the text of the nameLabel to the text of the nameTextField.
    */
    private void setNameLabel() {
        nameLabel.setText(nameTextField.getText());
    }
    
    /**
    * This method sets the text of the nimLabel to the text of the nimTextField.
    */
    private void setNIMLabel() {
        nimLabel.setText(nimTextField.getText());
    }
    
    /**
    * This method resets all the labels in the UI to their default values.
    */
    private void resetLabels() {
        nameLabel.setText("-");
        nimLabel.setText("-");
        averageLabel.setText("-");
        gradeLabel.setText("-");
        descriptionLabel.setText("-");
    }
    
    /**
    * This method validates the name entered by the user. It checks if the name is not null, not empty,
    * only contains letters, and is within the valid length range. If the name is invalid, it displays
    * an error message and returns false. Otherwise, it returns true.
    * @return Whether the name is valid.
    */
    private boolean validateName() {
        name = nameTextField.getText();

        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, String.format(ERROR_EMPTY, "Nama"));
            return false;
        }
        
        if (!name.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(null, String.format(ERROR_ONLY_LETTERS, "Nama"));
            return false;
        }
        
        if (name.length() < 3) {
            JOptionPane.showMessageDialog(null, ERROR_NAME_TOO_SHORT);
            return false;
        }
        
        if (name.length() > 100) {
            JOptionPane.showMessageDialog(null, ERROR_NAME_TOO_LONG);
            return false;
        }
        return true;
    }

    /**
    * This method validates the NIM entered by the user. It checks if the NIM is not null, not empty,
    * is numeric, and is of the valid length. If the NIM is invalid, it displays an error message and
    * returns false. Otherwise, it returns true.
    * @return Whether the NIM is valid.
    */
    private boolean validateNim() {
        nim = nimTextField.getText();

        if (nim == null || nim.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, String.format(ERROR_EMPTY, "NIM"));
            return false;
        }

        if (!nim.matches("[0-9]+")) {
            JOptionPane.showMessageDialog(null, String.format(ERROR_NOT_NUMERIC, "NIM"));
            return false;
        }

        if (nim.length() != 10) {
            JOptionPane.showMessageDialog(null, ERROR_NIM_INVALID_LENGTH);
            return false;
        }
        return true;
    }

    /**
    * This method validates a score entered by the user. It checks if the score is a number and is within
    * the valid range (0 to 100). If the score is invalid, it displays an error message and returns false.
    * Otherwise, it assigns the score to the corresponding property and returns true.
    * @param scoreStr The score as a string.
    * @param fieldName The name of the field (used in the error message).
    * @return Whether the score is valid.
    */
    private boolean validateScore(String scoreStr, String fieldName) {
        try {
            double score = Double.parseDouble(scoreStr);
            
            if (score < 0 || score > 100) {
                JOptionPane.showMessageDialog(null, String.format(ERROR_SCORE_OUT_OF_RANGE, fieldName));
                return false;
            }
            
            switch (fieldName) {
                case "Tugas":
                    scoreTugas = score;
                    break;
                case "Kuis":
                    scoreQuiz = score;
                    break;
                case "UTS":
                    scoreUTS = score;
                    break;
                case "UAS":
                    scoreUAS = score;
                    break;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, String.format(ERROR_NOT_A_NUMBER, fieldName));
            return false;
        }

        return true;
    }
    
    /**
    * This method validates all the scores entered by the user. It calls the validateScore method for each
    * score. If any score is invalid, it returns false. Otherwise, it returns true.
    * @return Whether all the scores are valid.
    */
    private boolean validateScores() {
        if (!validateScore(tugasTextField.getText(), "Tugas")) {
            return false;
        }

        if (!validateScore(quizTextField.getText(), "Kuis")) {
            return false;
        }

        if (!validateScore(utsTextField.getText(), "UTS")) {
            return false;
        }

        return validateScore(uasTextField.getText(), "UAS");
    }

    /**
    * This method validates all the text fields in the UI. It calls the validateName, validateNim, and
    * validateScores methods. If any field is invalid, it returns false. Otherwise, it returns true.
    * @return Whether all the text fields are valid.
    */
    private boolean validateTextFields() {
        if (!validateName()) {
            return false;
        }

        if (!validateNim()) {
            return false;
        }
        
        return validateScores();
    }
    
    /**
    * This method calculates the average score, grade, and pass/fail status of a student and displays them in the UI.
    * It calls the getAverage, getGrade, and getDescription methods and sets the labels in the UI.
    */
    private void calculateScores() {
        String averageText = String.valueOf(getAverage());
        averageLabel.setText(averageText);
        gradeLabel.setText(getGrade());
        descriptionLabel.setText(getDescription());
    }
    
    /**
    * This method saves the student data to the database if the input fields are valid. It validates the input fields,
    * calculates the scores, and then either updates the existing student data or inserts new student data into the database.
    * After saving the data, it reloads the data in the UI and displays a success message.
    */
    private void saveData() {
        if (validateTextFields()) {
            setNameLabel();
            setNIMLabel();
            calculateScores();
            
            if (studentDAO.studentExists(nim)) {
                studentDAO.updateStudent(nim, name, scoreTugas, scoreQuiz, scoreUTS, scoreUAS, getAverage(), getGrade(), getDescription());
            } else {
                studentDAO.insertStudent(nim, name, scoreTugas, scoreQuiz, scoreUTS, scoreUAS, getAverage(), getGrade(), getDescription());
            }
            loadData();
            JOptionPane.showMessageDialog(null, "Berhasil menyimpan data.");
        }
    }
    
    /**
    * This method fetches all students data from the database and displays it in the table. It creates a new row in the table
    * for each student and sets the cells in the row to the student's data.
    */
    private void loadData() {
        try {
            ResultSet resultSet = studentDAO.getStudents();
            DefaultTableModel model = (DefaultTableModel) studentsTable.getModel();
            model.setRowCount(0);
            while (resultSet.next()) {
                Object[] row = new Object[5];
                row[0] = resultSet.getString("nim");
                row[1] = resultSet.getString("name");
                row[2] = resultSet.getDouble("average");
                row[3] = resultSet.getString("grade");
                row[4] = resultSet.getString("description");
                model.addRow(row);
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }
       
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        nimTextField = new javax.swing.JTextField();
        tugasTextField = new javax.swing.JTextField();
        quizTextField = new javax.swing.JTextField();
        utsTextField = new javax.swing.JTextField();
        uasTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        nimLabel = new javax.swing.JLabel();
        averageLabel = new javax.swing.JLabel();
        gradeLabel = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        calculateButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        studentsTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Nama");

        jLabel2.setText("NIM");

        jLabel3.setText("Nilai Tugas");

        jLabel4.setText("Nilai Kuis");

        jLabel5.setText("Nilai UTS");

        jLabel6.setText("Nilai UAS");

        nameTextField.setToolTipText("");
        nameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextFieldActionPerformed(evt);
            }
        });

        nimTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nimTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nimTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                    .addComponent(nameTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(quizTextField)
                    .addComponent(tugasTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(uasTextField)
                    .addComponent(utsTextField, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(nimTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(tugasTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(quizTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(utsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(uasTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel7.setText("Nama");

        jLabel8.setText("NIM");

        jLabel9.setText("Rerata");

        jLabel10.setText("Grade");

        jLabel11.setText("Keterangan");

        jLabel12.setText(":");

        jLabel13.setText(":");

        jLabel14.setText(":");

        jLabel15.setText(":");

        jLabel16.setText(":");

        nameLabel.setText("-");

        nimLabel.setText("-");

        averageLabel.setText("-");

        gradeLabel.setText("-");

        descriptionLabel.setText("-");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 6, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nimLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .addComponent(averageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .addComponent(gradeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .addComponent(descriptionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel16)
                    .addComponent(nameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel15)
                    .addComponent(nimLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel14)
                    .addComponent(averageLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13)
                    .addComponent(gradeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(descriptionLabel))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        calculateButton.setText("HITUNG");
        calculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateButtonActionPerformed(evt);
            }
        });

        resetButton.setText("RESET");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        saveButton.setText("SIMPAN");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        exitButton.setText("KELUAR");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(calculateButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(resetButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(exitButton)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(calculateButton)
                    .addComponent(resetButton)
                    .addComponent(saveButton)
                    .addComponent(exitButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        studentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "NIM", "Nama", "Rerata", "Grade", "Keterangan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(studentsTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextFieldActionPerformed
        setNameLabel();
    }//GEN-LAST:event_nameTextFieldActionPerformed

    private void nimTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nimTextFieldActionPerformed
        setNIMLabel();
    }//GEN-LAST:event_nimTextFieldActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        saveData();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        dispose();
    }//GEN-LAST:event_exitButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Silakan pilih data yang ingin direset.", "Tidak ada data yang dipilih", JOptionPane.WARNING_MESSAGE);
        } else {
            Object[] options = {"Ya", "Tidak"};
            int confirm = JOptionPane.showOptionDialog(null, "Apakah Anda yakin ingin mereset data?", "Konfirmasi Reset", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (confirm == JOptionPane.YES_OPTION) {
                String nim = studentsTable.getValueAt(selectedRow, 0).toString();
                studentDAO.deleteStudent(nim);
                resetTextFields();
                resetLabels();
                studentsTable.clearSelection();
                loadData();
            }
        }
    }//GEN-LAST:event_resetButtonActionPerformed

    private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateButtonActionPerformed
        if (validateScores()) {
            setNameLabel();
            setNIMLabel();
            calculateScores();
        }
    }//GEN-LAST:event_calculateButtonActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StudentForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel averageLabel;
    private javax.swing.JButton calculateButton;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel gradeLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel nimLabel;
    private javax.swing.JTextField nimTextField;
    private javax.swing.JTextField quizTextField;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JTable studentsTable;
    private javax.swing.JTextField tugasTextField;
    private javax.swing.JTextField uasTextField;
    private javax.swing.JTextField utsTextField;
    // End of variables declaration//GEN-END:variables
}
