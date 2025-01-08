import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.*;
import java.sql.*;

public class Stu extends JFrame implements ActionListener{
    JTextField searchField;
    JButton searchButton, insertButton, defaultButton;
    JTable resultTable;
    DefaultTableModel tableModel;
    JPopupMenu contextMenu;
    static JPanel detailsPanel;
    static JLabel detailsLabel;
    String DB_URL = "jdbc:mysql://localhost:3306/cms";
    String DB_USER = "root";
    String DB_PASSWORD = "";

    public Stu() {
        setTitle("Student Information");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        defaultButton = new JButton("Default ");
        searchField = new JTextField(15);
        searchButton = new JButton("Search");
        insertButton = new JButton("Insert");
        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        contextMenu = new JPopupMenu();

        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        JMenuItem editMenuItem = new JMenuItem("Edit");

        contextMenu.add(deleteMenuItem);
        contextMenu.add(editMenuItem);

        setLayout(new BorderLayout());
        JMenu jm = new JMenu("Departments");
        JMenuBar mb = new JMenuBar();
        JMenuItem m1 = new JMenuItem("CSN");
        JMenuItem m2 = new JMenuItem("CSM");
        JMenuItem m3 = new JMenuItem("CSE");
        JMenuItem m4 = new JMenuItem("CSD");
        JMenuItem m5 = new JMenuItem("CSO");
        jm.add(m1);
        jm.add(m2);
        jm.add(m3);
        jm.add(m4);
        jm.add(m5);
        mb.add(jm);
        JPanel searchPanel = new JPanel();
        searchPanel.add(defaultButton);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(mb);
        searchPanel.add(searchButton);
        searchPanel.add(insertButton);
        add(searchPanel, BorderLayout.NORTH);
        m1.addActionListener(this);
        m2.addActionListener(this);
        m3.addActionListener(this);
        m4.addActionListener(this);
        m5.addActionListener(this); 
        JScrollPane scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);
        detailsPanel=new JPanel();
        detailsPanel.setPreferredSize(new Dimension(300,100));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        detailsLabel=new JLabel();
        detailsLabel.setVerticalAlignment(SwingConstants.TOP);
        detailsPanel.add(detailsLabel);
        add(detailsPanel,BorderLayout.EAST);
        tableModel.addColumn("Name");
        tableModel.addColumn("Id");
        tableModel.addColumn("Password");
        tableModel.addColumn("Department");
        searchDefaultStudent(); 
        defaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchDefaultStudent();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });

        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertStudent();
            }
        });

        resultTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               
                    int row = resultTable.rowAtPoint(e.getPoint());
                    resultTable.getSelectionModel().setSelectionInterval(row, row);

                    if (row >= 0 && row < resultTable.getRowCount()) {
                        contextMenu.show(resultTable, e.getX(), e.getY());
                    }
                
            }
        });

        deleteMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedStudent();
            }
        });

        editMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editSelectedStudent();
                
            }
        });
        resultTable.addMouseMotionListener(new MouseInputAdapter()
        		{
        			public void mouseMoved(MouseEvent me)
        			{
        				int row = resultTable.rowAtPoint(me.getPoint());
        				if(row!=-1)
        				{
        					String studentId=(String)resultTable.getValueAt(row, 1);
        					System.out.println(studentId);
        					showStudentDetails(studentId);
        				}
        			}
        		});
    } 
    public void showStudentDetails(String studentId) {
        try {
        	
        	Connection conn=DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        	
            String query = "SELECT * FROM student WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Format details to display
                String details = "<html><b>ID:</b> " + rs.getString("Id") +
                        "<br><b>Name:</b> " + rs.getString("Name") +
                        "<br><b>Dept:</b> " + rs.getString("dept") + "</html>";
                detailsLabel.setText(details);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    } 
    public void actionPerformed(ActionEvent e)
    {
    	if(e.getActionCommand()=="CSN")
    	{
    		searchByDep("CSN");
    	}
    	else if(e.getActionCommand()=="CSM")
    	{
    		searchByDep("CSM");
    	}
    	else if(e.getActionCommand()=="CSE")
    	{
    		searchByDep("CSE");
    	}
    	else if(e.getActionCommand()=="CSD")
    	{
    		searchByDep("CSD");
    	}
    	else
    	{
    		searchByDep("CSO");
    	}
    }
    public void searchByDep(String dept)
    {
    	
        tableModel.setRowCount(0);

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM student WHERE dept='"+dept+"'");) 
	{
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                String dep = resultSet.getString("dept");

                tableModel.addRow(new Object[]{name, id, password, dep});
                
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error in searching students.");
        }
    }

    private void searchStudent() {
        String searchTerm = searchField.getText();

        tableModel.setRowCount(0);

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM student WHERE id=?");) 
	{
            preparedStatement.setString(1,searchTerm);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                String dep = resultSet.getString("dept");

                tableModel.addRow(new Object[]{name, id, password, dep});
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error in searching students.");
        }
    }
  private void searchDefaultStudent() {
        

        tableModel.setRowCount(0);

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM student");) 
	{
            
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                String dep = resultSet.getString("dept");

                tableModel.addRow(new Object[]{name, id, password, dep});
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error in searching students.");
        }
    }
    private void insertStudent() {
        
        showInsertDialog();
    }

    private void deleteSelectedStudent() {
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow >= 0) {
            String studentId = (String) resultTable.getValueAt(selectedRow, 1);
              try
		{
                   Connection con=DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		   PreparedStatement pst=con.prepareStatement("Delete from student where id='"+studentId+"'");
                   pst.executeUpdate();
                   con.close();
		   JOptionPane.showMessageDialog(this, studentId + "Deleted Successfully.");
                   searchDefaultStudent(); 
                }
             catch(Exception ea)
                {
                  JOptionPane.showMessageDialog(null,ea);
                }
            
        }
    }

    private void editSelectedStudent() {
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow >= 0) {
            String studentId = (String) resultTable.getValueAt(selectedRow, 1);
            JDialog insertDialog = new JDialog(this, "Insert Student", true);
            insertDialog.setSize(1200, 300);
            insertDialog.setLayout(new FlowLayout());

            JTextField idField = new JTextField(15);
            
            JTextField nameField = new JTextField(15);
            JTextField passwordField = new JTextField(15);
            JTextField departmentField = new JTextField(15);
            JButton submitButton = new JButton("Submit");
            idField.setEditable(false);
            insertDialog.add(new JLabel("UserID:"));
            insertDialog.add(idField);
            insertDialog.add(new JLabel("Name:"));
            insertDialog.add(nameField);
            insertDialog.add(new JLabel("Password:"));
            insertDialog.add(passwordField);
            insertDialog.add(new JLabel("Department:"));
            insertDialog.add(departmentField);
            insertDialog.add(submitButton);

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM student where id='"+studentId+"'");) 
    	{
                
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    String password = resultSet.getString("password");
                    String dep = resultSet.getString("dept");

                   idField.setText(id);
                   nameField.setText(name);
                   passwordField.setText(password);
                   departmentField.setText(dep);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error in searching students.");
            }
           

            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                   try {
                    String id = idField.getText();
                    String name = nameField.getText();
                    String password = passwordField.getText();
                    String department = departmentField.getText();
                    if(!id.isEmpty() && !name.isEmpty() && !password.isEmpty() && !department.isEmpty())
                    {
                    	updateStudent(id, name, password, department);
                        searchDefaultStudent();
                        insertDialog.dispose();
                    }
                    else
                    {
                    	JOptionPane.showMessageDialog(null,"Please, do fill all the textfields!");
                    }
                     
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(insertDialog, "Invalid ID. Please enter a valid integer.");
                }
                }
            });

            insertDialog.setVisible(true);
            
            JOptionPane.showMessageDialog(this, "Edited Successfully.");
            searchDefaultStudent(); 
        }
    }
    public void updateStudent(String id, String name, String password, String dept)
    {
    	 try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement("update student set name = '"+name+"',password='"+password+"',dept='"+dept+"' where id = '"+id+"'");) 
    	{
             
             preparedStatement.executeUpdate();

               

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error in searching employees.");
            }
    }
    
    
    
    private void showInsertDialog() {
        JDialog insertDialog = new JDialog(this, "Insert Student", true);
        insertDialog.setSize(1200, 300);
        insertDialog.setLayout(new FlowLayout());

        JTextField idField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField passwordField = new JTextField(15);
        JTextField departmentField = new JTextField(15);
        JButton nextButton = new JButton("Next");
        JButton submitButton = new JButton("Submit");

        insertDialog.add(new JLabel("UserID:"));
        insertDialog.add(idField);
        insertDialog.add(new JLabel("Name:"));
        insertDialog.add(nameField);
        insertDialog.add(new JLabel("Password:"));
        insertDialog.add(passwordField);
        insertDialog.add(new JLabel("Department:"));
        insertDialog.add(departmentField);
        insertDialog.add(nextButton);
        insertDialog.add(submitButton);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String name = nameField.getText();
                String password = passwordField.getText();
                String dep = departmentField.getText();
                if(!id.isEmpty() && !name.isEmpty() && !password.isEmpty() && !dep.isEmpty())
                {
                	insertStudent(id, name, password, dep);
                    searchDefaultStudent();
                    idField.setText("");
                    nameField.setText("");
                    passwordField.setText("");
                    departmentField.setText("");
                }
                else
                {
                	JOptionPane.showMessageDialog(null,"Please, do fill all the textfields!");
                }
                 
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try {
                String id = idField.getText();
                String name = nameField.getText();
                String password = passwordField.getText();
                String department = departmentField.getText();
                if(!id.isEmpty() && !name.isEmpty() && !password.isEmpty() && !department.isEmpty())
                {
                	insertStudent(id, name, password, department);
                    searchDefaultStudent();
                    insertDialog.dispose();
                }
                else
                {
                	JOptionPane.showMessageDialog(null,"Please, do fill all the textfields!");
                }
                 
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(insertDialog, "Invalid ID. Please enter a valid integer.");
            }
            }
        });

        insertDialog.setVisible(true);
    }
    private void insertStudent(String id, String name, String password, String department) {
    String insertQuery = "INSERT INTO student VALUES ('"+name+"','"+id+"','"+password+"','"+department+"')";

    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) 
    {
    	
    	preparedStatement.executeUpdate();
    	connection.close();

    	

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error in inserting employee into the database.");
    }

  }
    public static void main(String[] args) {
               new Stu().setVisible(true);
    }
}
