import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
public class Login {
	/**
	 * @param args
	 * 
	 */
	static JFrame fr;
   static 	String email;
   static String password;
	public static void main(String args[])
	{
		fr = new JFrame("Login");
		
	
		
		fr.setExtendedState(fr.MAXIMIZED_BOTH);
		fr.setLayout(null);
		fr.setDefaultCloseOperation(3);
		JLabel l1 = new JLabel("Enter your Email");
		JLabel l2 = new JLabel("Enter your Password");
		JLabel l3 = new JLabel("Forgot your Password?");	
		JLabel l4 = new JLabel("LOGIN FORM");
		JTextField jtf1 = new JTextField(15);
		final JPasswordField jtf2 = new JPasswordField(15);
		JButton jb1 = new JButton("Sign in");
		JPanel jp = new JPanel();
		jp.setLayout(null);
		fr.add(jp, BorderLayout.CENTER);
	
		jp.setBounds(330,180,600,260);
		jp.setBackground(new Color(135,206,235));
		jb1.setBackground(Color.black);
		jb1.setForeground(Color.white);
		l1.setBounds(100,40,240,40);
		l2.setBounds(100,80,240,40);
		l3.setBounds(190,180,240,40);
		l4.setBounds(520,20,400, 70);
		jtf1.setBounds(300,50,240,20);
		jtf2.setBounds(300,90,240,20);
		jb1.setBounds(220,130,100,40);
		l1.setFont(new Font("Times new Roman",Font.BOLD, 20));
		l2.setFont(new Font("Times new Roman",Font.BOLD, 20));
		l3.setFont(new Font("Times new Roman",Font.BOLD, 16));
		l4.setFont(new Font("Times new Roman",Font.BOLD, 30));
		jb1.setFont(new Font("Times new Roman",Font.BOLD, 18));
		jp.add(l1);
		jp.add(l2);
		jp.add(l3);
		fr.add(l4);
		jp.add(jtf1);
		jp.add(jtf2);
		jp.add(jb1);
		l3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		l3.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent e)
					{
						String email = JOptionPane.showInputDialog(null,"Enter your email");
						if(!email.isEmpty())
						{
							 check(email);
						}
						
					}
				});
		
		
		l3.setForeground(Color.blue.darker());
		jb1.addActionListener(new ActionListener()
				{
			public void actionPerformed(ActionEvent e)
			{
				 email = jtf1.getText();
				 password = new String(jtf2.getPassword());
				//System.out.println(email);
				//System.out.println(password);
				try {
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms","root","");
					String query= "select * from admin where id='"+email+"' and password='"+password+"'";
					PreparedStatement ps =con.prepareStatement(query);
					ResultSet rs =ps.executeQuery();
					boolean t= false; 
					while(rs.next())
					{
						t=true;
						 
					}
					if(t==true)
					{
						new Admin();
						 
					}
					else
					{
						String query1 = "select * from employee where id='"+email+"'and password = '"+password+"'";
						PreparedStatement ps1 =con.prepareStatement(query1);
						ResultSet rs1 = ps1.executeQuery();
						boolean f= false;
						while(rs1.next())
						{
							f=true;
						}
						if(f==true)
						{
							new Employee();
						}
						else
						{
							String query2 = "select * from student where id='"+email+"'and password = '"+password+"'";
							PreparedStatement ps2 =con.prepareStatement(query2);
							ResultSet rs2 = ps2.executeQuery();
							boolean s= false;
							while(rs2.next())
							{
								s=true;
							}
							if(s==true)
							{
								new Student();
							}
							else
							{
								JOptionPane.showMessageDialog(null,"Sorry, No results found!");
							}
						}
					}
				}
				catch(Exception ae)
				{
					JOptionPane.showMessageDialog(null,ae);
				}
				
				
						
				
			}
				});
		fr.setVisible(true);
		
		
		
		
	}
	public static void check(String email)
	{
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms","root","");
			String query= "select * from admin where id='"+email+"'";
			PreparedStatement ps =con.prepareStatement(query);
			ResultSet rs =ps.executeQuery();
			boolean t= false; 
			while(rs.next())
			{
				t=true;
				 
			}
			if(t==true)
			{
				reSetPassword(email,1);				 
			}
			else
			{
				String query1 = "select * from employee where id='"+email+"'";
				PreparedStatement ps1 =con.prepareStatement(query1);
				ResultSet rs1 = ps1.executeQuery();
				boolean f= false;
				while(rs1.next())
				{
					f=true;
				}
				if(f==true)
				{
					reSetPassword(email,2);	
				}
				else
				{
					String query2 = "select * from student where id='"+email+"'";
					PreparedStatement ps2 =con.prepareStatement(query2);
					ResultSet rs2 = ps2.executeQuery();
					boolean s= false;
					while(rs2.next())
					{
						s=true;
					}
					if(s==true)
					{
						reSetPassword(email,3);	
					}
					else
					{
						JOptionPane.showMessageDialog(null,"Email id doesn't exists");
					}
				}
			}
		}
		catch(Exception ae)
		{
			JOptionPane.showMessageDialog(null,ae);
		}
	}
	public static void reSetPassword(String email, int token)
	{
		JFrame j = new JFrame();
		JDialog editDialog = new JDialog( j, "Reset your password");
        editDialog.setBounds(800,20,300, 300);
        editDialog.setLayout(new FlowLayout());

        JTextField passwordField1 = new JTextField(15);
        
        JTextField passwordField2 = new JTextField(15);
       
        JButton submitButton = new JButton("Submit");
        
        editDialog.add(new JLabel("New Password:"));
        editDialog.add(passwordField1);
        editDialog.add(new JLabel("Confirm Password:"));
        editDialog.add(passwordField2);
        
        editDialog.add(submitButton);
        editDialog.setVisible(true);
        submitButton.addActionListener(new ActionListener()
        		{
        			public void actionPerformed(ActionEvent e)
        			{
        				String p1= passwordField1.getText();
        				String p2= passwordField2.getText();
        				if(p1.isEmpty() || p2.isEmpty()) {
        					JOptionPane.showMessageDialog(null,"Password field connot be empty");
        				}
        				else if(!p1.equals(p2))
        				{
        					JOptionPane.showMessageDialog(null,"Password should be same");
        				}
        				else
        				{
        					updatePassword(email, p1,token);
        				}
        				editDialog.dispose();
        			}
        		});
	}
	public static void updatePassword(String email, String password, int token)
	{ 
		String DB_URL = "jdbc:mysql://localhost:3306/cms";
	    String DB_USER = "root";
	    String DB_PASSWORD = "";
		
	    if(token==1)
	    {
	    	try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	                PreparedStatement preparedStatement = connection.prepareStatement("update admin set password='"+password+"' where id = '"+email+"'");) 
	    			{
	    		 
	    				preparedStatement.executeUpdate();

	    			}
	    			catch (SQLException ex) {
	    			
	    				JOptionPane.showMessageDialog(null, ex);
	    			}
	    }
	    else if(token==2)
	    {
	    	try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	                PreparedStatement preparedStatement = connection.prepareStatement("update employee set password='"+password+"' where id = '"+email+"'");) 
	    			{
	    		 
	    				preparedStatement.executeUpdate();

	    			}
	    			catch (SQLException ex) {
	    				JOptionPane.showMessageDialog(null,ex);
	    			}
	    }
	    else
	    {
	    	try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	                PreparedStatement preparedStatement = connection.prepareStatement("update student set password='"+password+"' where id = '"+email+"'");) 
	    			{
	    		 
	    				preparedStatement.executeUpdate();

	    			}
	    			catch (SQLException ex) {
	    				JOptionPane.showMessageDialog(null, ex);
	    			}
	    }
	     
	}

}
