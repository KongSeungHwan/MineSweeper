package MineSweeper;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPage{
	JFrame frm;
	private ClientManagement clim;
	private Client session;
	private Thread dbThread = new Thread(){
		@Override
		public void run() {
			while(true) {
				try {Thread.sleep(5000); //1000분의 1초니까 10초단위로 DB데이터 갱신
					clim = new ClientManagement();}catch (InterruptedException e) {}
			}
		}
	};
	LoginPage(){
		if(!dbThread.isAlive())dbThread.start();
		clim = new ClientManagement();
		frm =new JFrame("★★★로그인★★★");
		ImageIcon img = new ImageIcon("src/image/mine.png");
		frm.setIconImage(img.getImage());
		frm.setSize(700, 700);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setLocationRelativeTo(null);
		frm.getContentPane().setLayout(null);
		JLabel l1= new JLabel("★★★로그인★★★");
		l1.setBounds(200,30,400,50);
		l1.setFont(new Font("Serif", Font.BOLD,30));
		frm.getContentPane().add(l1);
		JLabel l2 = new JLabel("아이디");
		l2.setBounds(150,200,200,50);
		l2.setFont(new Font("Serif", Font.BOLD,20));
		JTextField id = new RoundJTextField(24);
		id.setBounds(250,200,300,50);
		JLabel l3 = new JLabel("비밀번호");
		l3.setBounds(150,250,200,50);
		l3.setFont(new Font("Serif", Font.BOLD,20));
		JPasswordField pw = new RoundJPasswordField(24);
		pw.setBounds(250,250,300,50);
		frm.getContentPane().add(l2);
		frm.getContentPane().add(id);
		frm.getContentPane().add(l3);
		frm.getContentPane().add(pw);
		JButton mainMenuPage = new RoundedButton("돌아가기(메인 메뉴)");
		mainMenuPage.setBounds(0, 600, 150, 70);
		mainMenuPage.addActionListener(new ActionListener(){
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e){
				frm.setVisible(false);
				dbThread.stop();
				new MainMenuPage().frm.setVisible(true);
			}
		});
		JButton login = new RoundedButton("로그인!");
		login.setBounds(500, 450, 100, 50);
		login.addActionListener(new ActionListener(){
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e){
				frm.setVisible(false);
				if (e.getSource() == login) {
		            try {
						if((session=clim.checkInfo(id.getText(),pw.getText()))==null) {
							JOptionPane.showMessageDialog(null, "로그인 실패 다시시도해주십시오.");
							new LoginPage().frm.setVisible(true);
						}
						else new ClientPage(session).frm.setVisible(true);
					} catch (HeadlessException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
		        }
			}
		});
		frm.add(mainMenuPage);
		frm.add(login);
	}
}
