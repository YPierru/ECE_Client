package Client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import jdk.internal.dynalink.linker.LinkerServices.Implementation;

public class JF_main extends JFrame{
	
	private String username;
	private JButton btnNewRoom;
	private JButton btnJoinRoom;
	private JButton btnDelRoom;
	private JButton btnPVP;
	private JLabel labelTest;
	private MyActionListener mActionListener;

	public JF_main(String user){
		super("Client - "+user);
		username=user;
		
		mActionListener = new MyActionListener();
		
		btnNewRoom = new JButton("Nouvelle salle");
		btnJoinRoom = new JButton("Rejoindre salle");
		btnDelRoom = new JButton("Supprimer salle");
		btnPVP = new JButton("Message privé (2 users)");
		
		labelTest=new JLabel("");
		
		setLayout(new FlowLayout());
		setSize(800, 70);
		add(btnNewRoom);
		add(btnJoinRoom);
		add(btnDelRoom);
		add(btnPVP);
		add(labelTest);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btnNewRoom.addActionListener(mActionListener);
		btnJoinRoom.addActionListener(mActionListener);
		btnDelRoom.addActionListener(mActionListener);
		btnPVP.addActionListener(mActionListener);
		
		setVisible(true);
	}
	
	
	private class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btn=(JButton)e.getSource();
			labelTest.setText(btn.getText());
		}
		
	}
	
}
