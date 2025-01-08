import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
public class Employee 
{	
	JFrame jf;
	JPanel p1;
	JPanel p2;
	JPanel p3;
	JPanel p4;
    JLabel l1;
    static JLabel l2;
    JLabel l3;
    static JLabel l4;
    JLabel l5;
    static JLabel l6;
    JLabel l7;
    String DB_URL = "jdbc:mysql://localhost:3306/cms";
    String DB_USER = "root";
    String DB_PASSWORD = "";
    String name;
    String password;
    String dept;
    public static void main(String[] args)
	{
		new Employee();
	}
	public Employee()
	{
		jf=new JFrame("Employee");
		jf.setLayout(new GridLayout(1,3,10,20));
		jf.setExtendedState(jf.MAXIMIZED_BOTH);
		jf.setDefaultCloseOperation(jf.DISPOSE_ON_CLOSE);
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		p4 = new JPanel();
		//p3.setLayout(new FlowLayout());
		p1.setLayout(new FlowLayout());
		p4.setLayout(new GridLayout(4,2));
		p1.setBackground(new Color(135,206,235));
		p2.setBackground(new Color(255,255,255));
		p3.setBackground(new Color(135,206,235));
		//p4.setBackground(new Color(135,206,25));
		//p1.setBounds(840,20,300,120);
		p1.setPreferredSize(new Dimension(300,200));
		p4.setPreferredSize(new Dimension(300,200));
		JButton b1 = new JButton("Attendance");
		JButton b2 = new JButton("Course Related");
		p1.add(b1);
		p1.add(b2);
		jf.add(p1);
		jf.add(p2);
		l1 = new JLabel("Email");
		l2 = new JLabel();
		l3 = new JLabel("Name");
		l4 = new JLabel();
		l5 = new JLabel("Department");
		l6 = new JLabel();
		JButton jb = new JButton("Edit");
		/*l1.setBounds(860,30,100,30);
		l2.setBounds(960,30,100,30);
		l3.setBounds(860,60,100,30);
		l4.setBounds(960,60,100,30);
		l5.setBounds(860,90,100,30);
		l6.setBounds(960,90,100,30);
		jb.setBounds(940,120,60,30);*/
		p4.add(l1);
		p4.add(l2);
		p4.add(l3);
		p4.add(l4);
		p4.add(l5);
		p4.add(l6);
		p4.add(jb);
		p3.add(p4);
		jf.add(p3);
		showDetails();
		jf.setVisible(true);
		
		jb.addActionListener(new ActionListener()
				{
			public void actionPerformed(ActionEvent e)
			{
				  
		            submit();
		            
			}
				});
	}
	public void submit(){
		{
			JFrame j = new JFrame();
			JDialog editDialog = new JDialog( j, "Edit Employee");
            editDialog.setBounds(800,20,300, 300);
            editDialog.setLayout(new FlowLayout());

            JTextField idField = new JTextField(15);
            
            JTextField nameField = new JTextField(15);
            JTextField passwordField = new JTextField(15);
            JTextField departmentField = new JTextField(15);
            JButton submitButton = new JButton("Submit");
            idField.setEditable(false);
            editDialog.add(new JLabel("UserID:"));
            editDialog.add(idField);
            editDialog.add(new JLabel("Name:"));
            editDialog.add(nameField);
            editDialog.add(new JLabel("Password:"));
            editDialog.add(passwordField);
            editDialog.add(new JLabel("Department:"));
            editDialog.add(departmentField);
            editDialog.add(submitButton);
            editDialog.setVisible(true);
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
      	             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employee WHERE id='"+Login.email+"'");) 
      		{
      	            ResultSet rs = preparedStatement.executeQuery();
      	            

      	            while (rs.next()) {
      	                 idField.setText(rs.getString("id"));
      	                 nameField.setText(rs.getString("name"));
      	                 departmentField.setText(rs.getString("dept"));
      	                 passwordField.setText(rs.getString("password"));
      	                 
      	                
      	            }
      	           

      	        } catch (SQLException ex) {
      	            ex.printStackTrace();
      	            JOptionPane.showMessageDialog(null, "Error in searching employees.");
      	        }
            submitButton.addActionListener(new ActionListener()
			{
            	public void actionPerformed(ActionEvent e)
            	{
            		name=nameField.getText();
  	                dept=departmentField.getText();
  	                password=passwordField.getText();
            		//System.out.println("HElllooooo");
            		updateEmployee( name, password, dept);
            		showDetails();   
            		editDialog.dispose();
			
            	}	

			});
             
		}
	}
	
		public void updateEmployee(String name, String password, String dept)
		{
			
		    	 try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		                 PreparedStatement preparedStatement = connection.prepareStatement("update employee set name = '"+name+"',password='"+password+"',dept='"+dept+"' where id = '"+Login.email+"'");) 
		    	{
		    		 
		             preparedStatement.executeUpdate();

		        }
		    	catch (SQLException ex) {
		                ex.printStackTrace();
		                JOptionPane.showMessageDialog(null, "Error in searching employees.");
		            }
		    }
	
	public void showDetails()
	{
		 //l2.setText("");
         //l4.setText("");
         //l6.setText("");
		 try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employee WHERE id='"+Login.email+"'");) 
		{
	            ResultSet rs = preparedStatement.executeQuery();

	            while (rs.next()) {
	                 l2.setText(rs.getString("id"));
	                 l4.setText(rs.getString("name"));
	                 l6.setText(rs.getString("dept"));
	                 
	                
	            }

	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(null, "Error in searching employees.");
	        }
	}
}
	 
		
		