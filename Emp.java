import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Emp extends JFrame implements ActionListener{
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

    public Emp() {
        setTitle("Employee Information");
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
        searchDefaultEmployees(); 
        defaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchDefaultEmployees();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchEmployees();
            }
        });

        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertEmployee();
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
        resultTable.addMouseMotionListener(new MouseInputAdapter()
		{
			public void mouseMoved(MouseEvent me)
			{
				int row = resultTable.rowAtPoint(me.getPoint());
				if(row!=-1)
				{
					String studentId=(String)resultTable.getValueAt(row, 1);
					
					showEmployeeDetails(studentId);
				}
				else
				{
					detailsPanel.setVisible(false);
				}
			}
		});

        deleteMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedEmployee();
            }
        });

        editMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editSelectedEmployee();
                
            }
        });
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
    public void showEmployeeDetails(String studentId) {
        try {
        	
        	Connection conn=DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        	
            String query = "SELECT * FROM employee WHERE id = ?";
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
    public void searchByDep(String dept)
    {
    	
        tableModel.setRowCount(0);

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employee WHERE dept='"+dept+"'");) 
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
            JOptionPane.showMessageDialog(this, "Error in searching employees.");
        }
    }

    private void searchEmployees() {
        String searchTerm = searchField.getText();

        tableModel.setRowCount(0);

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employee WHERE id=?");) 
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
            JOptionPane.showMessageDialog(this, "Error in searching employees.");
        }
    }
  private void searchDefaultEmployees() {
        

        tableModel.setRowCount(0);

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employee");) 
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
            JOptionPane.showMessageDialog(this, "Error in searching employees.");
        }
    }
    private void insertEmployee() {
        
        showInsertDialog();
    }

    private void deleteSelectedEmployee() {
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow >= 0) {
            String employeeId = (String) resultTable.getValueAt(selectedRow, 1);
              try
		{
                   Connection con=DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		   PreparedStatement pst=con.prepareStatement("Delete from employee where id='"+employeeId+"'");
                   pst.executeUpdate();
                   con.close();
		   JOptionPane.showMessageDialog(this, employeeId + "Deleted Successfully.");
                   searchDefaultEmployees(); 
                }
             catch(Exception ea)
                {
                  JOptionPane.showMessageDialog(null,ea);
                }
            
        }
    }

    private void editSelectedEmployee() {
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow >= 0) {
            String employeeId = (String) resultTable.getValueAt(selectedRow, 1);
            JDialog insertDialog = new JDialog(this, "Insert Employee", true);
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
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employee where id='"+employeeId+"'");) 
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
                JOptionPane.showMessageDialog(this, "Error in searching employees.");
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
                    	updateEmployee(id, name, password, department);
                        searchDefaultEmployees();
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
            searchDefaultEmployees(); 
        }
    }
    public void updateEmployee(String id, String name, String password, String dept)
    {
    	 try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement("update employee set name = '"+name+"',password='"+password+"',dept='"+dept+"' where id = '"+id+"'");) 
    	{
             
             preparedStatement.executeUpdate();

               

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error in searching employees.");
            }
    }
    private void showInsertDialog() {
        JDialog insertDialog = new JDialog(this, "Insert Employee", true);
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
                	insertEmployee(id, name, password, dep);
                    searchDefaultEmployees();
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
                	insertEmployee(id, name, password, department);
                    searchDefaultEmployees();
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
    private void insertEmployee(String id, String name, String password, String department) {
    String insertQuery = "INSERT INTO employee VALUES ('"+name+"','"+id+"','"+password+"','"+department+"')";

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
               new Emp().setVisible(true);
    }
}
