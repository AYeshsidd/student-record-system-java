import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

class Student {
    String name;
    int rollNo;
    int marks;

    public Student(String name, int rollNo, int marks) {
        this.name = name;
        this.rollNo = rollNo;
        this.marks = marks;
    }
}

public class StudentRecordSystem extends JFrame {
    private ArrayList<Student> studentList = new ArrayList<>();
    
    private Stack<Student> undoStack = new Stack<>();
    
    private DefaultTableModel tableModel;
    
    private JTable table;

    public StudentRecordSystem() {   //GUI SETUP
        setTitle("Student Record Management System - Total Students Now: 0");
        setSize(700, 500);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);   //Screen ke center mein window open karta hai

        tableModel = new DefaultTableModel(new Object[]{"Name", "Roll No", "Marks"}, 0);
        table = new JTable(tableModel);
        

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        
        add(panel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();

        JTextField nameField = new JTextField(10);
        JTextField rollField = new JTextField(5);
        JTextField marksField = new JTextField(5);
        JButton addButton = new JButton("Add Student");

        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(nameField);
        
        inputPanel.add(new JLabel("Roll No:"));
        inputPanel.add(rollField);
        
        inputPanel.add(new JLabel("Obtain Marks:"));
        inputPanel.add(marksField);
        inputPanel.add(addButton);

        JPanel buttonPanel = new JPanel();
       
        JButton searchButton = new JButton("Search by Name ");

        JButton undoButton = new JButton("Delete Last Entry ");
        undoButton.setForeground(Color.RED);

        JButton sortMarksButton = new JButton("Sort by Marks ");

        JButton sortNameButton = new JButton("Sort by Name ");

        JButton sortRollButton = new JButton("Sort by Roll ");

        buttonPanel.add(searchButton);
        buttonPanel.add(sortMarksButton);
        buttonPanel.add(sortNameButton);
        buttonPanel.add(sortRollButton);
        buttonPanel.add(undoButton);

        panel.add(inputPanel);
        panel.add(buttonPanel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int roll = Integer.parseInt(rollField.getText());
                int marks = Integer.parseInt(marksField.getText());
                
                Student student = new Student(name, roll, marks);
                studentList.add(student);
                undoStack.push(student);
                updateTable();
                nameField.setText(""); rollField.setText(""); marksField.setText("");
            } 
            
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.");
            }
        }
        );

        searchButton.addActionListener(e -> {
            String target = JOptionPane.showInputDialog(this, "Enter name to search:");
            if (target != null) {
                boolean found = false;
                for (Student s : studentList) {
                    if (s.name.equalsIgnoreCase(target.trim())) {
                        JOptionPane.showMessageDialog(this, "Found: " + s.name + ", Roll No: " + s.rollNo + ", Marks: " + s.marks);
                        found = true;
                        break;
                    }
                }
                if (!found) JOptionPane.showMessageDialog(this, "Student not found.");
            }
        }
        );

        undoButton.addActionListener(e -> {
            if (!undoStack.isEmpty()) {
                Student last = undoStack.pop();
                studentList.remove(last);
                updateTable();
            } 
            
            else {
                JOptionPane.showMessageDialog(this, "No entries to undo.");
            }
        });

        sortMarksButton.addActionListener(e -> {
            long start = System.nanoTime();
            bubbleSort();
            
            long end = System.nanoTime();
            updateTable();
            showExecutionTime(start, end);
        });

        sortNameButton.addActionListener(e -> {
            long start = System.nanoTime();
            selectionSort();
            
            long end = System.nanoTime();
            updateTable();
            showExecutionTime(start, end);
        });

        sortRollButton.addActionListener(e -> {
            long start = System.nanoTime();
            insertionSort();
           
            long end = System.nanoTime();
            updateTable();
            showExecutionTime(start, end);
        });

        setVisible(true);
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Student s : studentList) {
            tableModel.addRow(new Object[]{s.name, s.rollNo, s.marks});
        }
        
        setTitle("Student Record Management System - Total Students now: " + studentList.size());
    }

    private void showExecutionTime(long start, long end) {
        long duration = end - start;
        JOptionPane.showMessageDialog(this, "Sort completed in: " + duration / 1_000_000.0 + " ms");
    }

    private void bubbleSort() {
        for (int i = 0; i < studentList.size() - 1; i++) {
            for (int j = 0; j < studentList.size() - i - 1; j++) {
                if (studentList.get(j).marks > studentList.get(j + 1).marks) {
                    Collections.swap(studentList, j, j + 1);
                }
            }
        }
    }

    private void selectionSort() {
        for (int i = 0; i < studentList.size(); i++) {
            int minIdx = i;
            for (int j = i + 1; j < studentList.size(); j++) {
                if (studentList.get(j).name.compareToIgnoreCase(studentList.get(minIdx).name) < 0) {
                    minIdx = j;
                }
            }
            Collections.swap(studentList, i, minIdx);
        }
    }

    private void insertionSort() {
        for (int i = 1; i < studentList.size(); i++) {
            Student key = studentList.get(i);
            int j = i - 1;
            
            while (j >= 0 && studentList.get(j).rollNo > key.rollNo) {
                studentList.set(j + 1, studentList.get(j));
                j--;
            }
            studentList.set(j + 1, key);
        }
    }
  public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRecordSystem());
    }
}





