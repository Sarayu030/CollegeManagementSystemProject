import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
public class Student
{	
	JFrame jf;
	JPanel jp;
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
	public Student()
	{
		jf=new JFrame("Student");
		jf.setLayout(null);
		jf.setExtendedState(jf.MAXIMIZED_BOTH);
		jf.setDefaultCloseOperation(jf.DISPOSE_ON_CLOSE);
		jp = new JPanel();
		jp.setLayout(null);
		jp.setBounds(840,20,300,120);
		jf.setContentPane(jp);
		l1 = new JLabel("Email");
		l2 = new JLabel();
		l3 = new JLabel("Name");
		l4 = new JLabel();
		l5 = new JLabel("Department");
		l6 = new JLabel();
		JButton jb = new JButton("Edit");
		l1.setBounds(860,30,100,30);
		l2.setBounds(960,30,200,30);
		l3.setBounds(860,60,100,30);
		l4.setBounds(960,60,100,30);
		l5.setBounds(860,90,100,30);
		l6.setBounds(960,90,100,30);
		jb.setBounds(940,120,60,30);

		jp.add(l1);
		jp.add(l2);
		jp.add(l3);
		jp.add(l4);
		jp.add(l5);
		jp.add(l6);
		jp.add(jb);
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
			JDialog editDialog = new JDialog( j, "Edit Student");
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
      	             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM student WHERE id='"+Login.email+"'");) 
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
      	            JOptionPane.showMessageDialog(null, "Error in searching students.");
      	        }
            submitButton.addActionListener(new ActionListener()
			{
            	public void actionPerformed(ActionEvent e)
            	{
            		name=nameField.getText();
  	                dept=departmentField.getText();
  	                password=passwordField.getText();
            		//System.out.println("HElllooooo");
            		updateStudent( name, password, dept);
            		showDetails();   
            		editDialog.dispose();
			
            	}	

			});
             
		}
	}
	
		public void updateStudent(String name, String password, String dept)
		{
			
		    	 try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		                 PreparedStatement preparedStatement = connection.prepareStatement("update student set name = '"+name+"',password='"+password+"',dept='"+dept+"' where id = '"+Login.email+"'");) 
		    	{
		    		 
		             preparedStatement.executeUpdate();

		        }
		    	catch (SQLException ex) {
		                ex.printStackTrace();
		                JOptionPane.showMessageDialog(null, "Error in searching students.");
		            }
		    }
	
	public void showDetails()
	{
		 //l2.setText("");
         //l4.setText("");
         //l6.setText("");
		 try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM student WHERE id='"+Login.email+"'");) 
		{
	            ResultSet rs = preparedStatement.executeQuery();

	            while (rs.next()) {
	                 l2.setText(rs.getString("id"));
	                 l4.setText(rs.getString("name"));
	                 l6.setText(rs.getString("dept"));
	                 
	                
	            }

	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(null, "Error in searching students.");
	        }
	}
}
	 
		
		