package MineSweeper;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class JoinPage {
	JFrame frm;
	private ClientManagement clim;
	private Thread dbThread = new Thread(){
		@Override
		public void run() {
			while(true) {
				try {
					Thread.sleep(5000); //1000분의 1초니까 10초단위로 DB데이터 갱신
					clim = new ClientManagement();
				} catch (InterruptedException e) {
				}
			}
		}
	};
	JoinPage(){
		if(!dbThread.isAlive())dbThread.start();
		clim=new ClientManagement();
		frm =new JFrame("★★★회원가입★★★");
		ImageIcon img = new ImageIcon("src/image/mine.png");
		frm.setIconImage(img.getImage());
		frm.setSize(700, 700);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setLocationRelativeTo(null);
		frm.getContentPane().setLayout(null);
		JLabel l1= new JLabel("★★★회원가입★★★");
		l1.setBounds(200,30,400,50);
		l1.setFont(new Font("Serif", Font.BOLD,30));
		frm.getContentPane().add(l1);
		JLabel l2 = new JLabel("이름");
		l2.setBounds(150,200,100,50);
		l2.setFont(new Font("Serif", Font.BOLD,20));
		JTextField name = new RoundJTextField("이름을 입력해주세요(공백 불가)",24);
		name.setBounds(250,200,200,50);
		JLabel l3 = new JLabel("아이디");
		l3.setBounds(150,250,200,50);
		l3.setFont(new Font("Serif", Font.BOLD,20));
		JTextField id = new RoundJTextField("아이디(영문자+숫자+특수문자 조합 8~24)",24);
		id.setBounds(250,250,300,50);
		JLabel l4 = new JLabel("비밀번호");
		l4.setBounds(150,300,200,50);
		l4.setFont(new Font("Serif", Font.BOLD,20));
		JTextField pw = new RoundJTextField("비밀번호(영문자+숫자+(특수문자) 조합 8~24)",24);
		pw.setBounds(250,300,300,50);
		frm.getContentPane().add(l2);
		frm.getContentPane().add(name);
		frm.getContentPane().add(l3);
		frm.getContentPane().add(id);
		frm.getContentPane().add(l4);
		frm.getContentPane().add(pw);
		JButton submit = new RoundedButton("가입!");
		submit.setBounds(300, 400, 70, 40);
		JOptionPane.showMessageDialog(null, "※회원가입 값을 이상하게 입력하여 가입되었으면 꼭 운영자에게 문의해주십시오!※");
		submit.addActionListener(new ActionListener(){
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e){
				try {
					if(clim.signUp(name.getText(),id.getText(),pw.getText())) {
						dbThread.stop();
						frm.setVisible(false);
						new MainMenuPage().frm.setVisible(true);
						JOptionPane.showMessageDialog(null, "가입성공!");
					}else JOptionPane.showMessageDialog(null, "가입실패! 다시 시도해 주십시오.");
					
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "입출력 예외 발생! 운영자에게 문의하십시오.");
				}
			}
		});
		JButton back = new RoundedButton("<-");
		back.setBounds(0, 0, 50, 50);
		back.addActionListener(new ActionListener(){
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e){
				dbThread.stop();
				frm.setVisible(false);
				new MainMenuPage().frm.setVisible(true);
			}
		});
		frm.add(submit);
		frm.add(back);
	}
}
