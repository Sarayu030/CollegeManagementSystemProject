import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Admin implements ActionListener
{	JButton jb1;
	JButton jb2;
	JPanel p1;
	JPanel p2;
	JLabel l1;
	JLabel l2;
	ImageIcon ic1;
	ImageIcon ic2;
	public static void main(String[] args)
	{
		new Admin();
	}
	public Admin()
	{
		JFrame jf=new JFrame("Admin");
		jf.setLayout(new GridLayout(1,2,20,50));
		jf.setExtendedState(jf.MAXIMIZED_BOTH);
		jf.setDefaultCloseOperation(jf.DISPOSE_ON_CLOSE);
		p1 = new JPanel();
		p2 = new JPanel();
		//p1.setBounds(150,200,400,300);
		p1.setBackground(new Color(135,206,235));
		//p2.setBounds(700,200,400,300);
		p2.setBackground(new Color(135,206,235));
		p1.setLayout(new BorderLayout());
		p2.setLayout(new BorderLayout());
		jf.add(p1);
		jf.add(p2);
		ic1 = new ImageIcon("C:/Users/Saray/Downloads/faculty.png");
		ic2 = new ImageIcon("C:/Users/Saray/Downloads/student (2).png");
		l1 = new JLabel(ic1);
		l2 = new JLabel(ic2);
		p1.add(l1);
		p2.add(l2);
		jb1=new JButton("Employee");
		jb2=new JButton("Student");
		p1.add(jb1, BorderLayout.SOUTH);
		p2.add(jb2, BorderLayout.SOUTH);
		jb1.setPreferredSize(new Dimension(10,30));
		jb1.setBounds(400,330,100,140);
		jb2.setBounds(540,330,100,140);
		jb1.setForeground(Color.white);
		jb2.setForeground(Color.white);
		jb1.setBackground(Color.BLUE.darker());
		jb2.setBackground(Color.GREEN.darker());
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jf.setVisible(true);
		

	}
	
	public void actionPerformed(ActionEvent ae)
	{
		JButton jb=(JButton)ae.getSource();
		if(jb1==jb)
  		{
			new Emp().setVisible(true);
        }
		else
		{
			new Stu().setVisible(true);
		}
	}
}