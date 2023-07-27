package MineSweeper;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class InquiryPage {
	private Client session;
	private	ClientManagement clim;
	JFrame frm;
	private Thread dbThread = new Thread(){
		@Override
		public void run() {
			while(true) {
				try {Thread.sleep(5000); //1000분의 1초니까 10초단위로 DB데이터 갱신
					clim =new ClientManagement();
					session = clim.cList.dao.accessAllDataSQL().get(session.getId());}catch (InterruptedException e) {}
			}
		}
	};
	InquiryPage(Client client){
		if(!dbThread.isAlive()) dbThread.start();
		clim=new ClientManagement();
		session=client;
		frm=new JFrame("★★★회원 정보!★★★");
		ImageIcon img = new ImageIcon("src/image/mine.png");
		frm.setIconImage(img.getImage());
		frm.setSize(700, 700);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setLocationRelativeTo(null);
		frm.getContentPane().setLayout(null);
		JLabel l1= new JLabel(String.format("★★★%s님의 정보★★★",session.getName()));
		l1.setBounds(200,30,400,50);
		l1.setFont(new Font("Serif", Font.BOLD,30));
		frm.getContentPane().add(l1);
		JLabel l2 = new JLabel(String.format("등급: %s",session.getGrade()));
		l2.setBounds(300,100,300,50);
		l2.setFont(new Font("Serif", Font.BOLD,20));
		frm.add(l2);
		JLabel l3 = new JLabel(String.format("게임 ID: %s",session.getId()));
		l3.setBounds(100,360,300,50);
		l3.setFont(new Font("Serif", Font.BOLD,20));
		frm.add(l3);
		JLabel l4 = new JLabel(String.format("승리 판 수: %s, 총 판 수: %s",(int)(session.getvRounds()),(int)(session.gettRounds())));
		l4.setBounds(100,420,300,50);
		l4.setFont(new Font("Serif", Font.BOLD,20));
		frm.add(l4);
		JLabel l5 = new JLabel(String.format("승률: %.2f %s",session.getvRate()*100,(char)37));
		l5.setBounds(100,480,300,50);
		l5.setFont(new Font("Serif", Font.BOLD,20));
		frm.add(l5);
		JButton back = new RoundedButton("<-");
		back.setBounds(0, 0, 50, 50);
		back.addActionListener(new ActionListener(){
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e){
				frm.setVisible(false);
				dbThread.stop();
				new ClientPage(session).frm.setVisible(true);
			}
		});
		JButton withdrawal = new RoundedButton("탈퇴");
		withdrawal.setBounds(500, 500, 50, 50);
		withdrawal.addActionListener(new ActionListener(){
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e){
				if(JOptionPane.showConfirmDialog(withdrawal, "정말 탈퇴하시겠습니까?","확인 창",JOptionPane.YES_NO_CANCEL_OPTION)==0){
				frm.setVisible(false);
				dbThread.stop();
				clim.Withdrawal(session);//탈퇴 메소드
				 new LoginPage().frm.setVisible(true);
				}
			}
		});
		JButton changeName = new RoundedButton("이름 변경");
		changeName.setBounds(400, 500, 50, 50);
		changeName.addActionListener(new ActionListener(){
			@SuppressWarnings("null")
			public void actionPerformed(ActionEvent e){
				String name=JOptionPane.showInputDialog(null,"변경할 이름(공백불가)","");
					if(name!=null){
						if(session.getName().equals(name)) {
							JOptionPane.showMessageDialog(null, "기존 이름과 같습니다.");
						}else if(name.equals("")) {
							JOptionPane.showMessageDialog(null, "공백 불가!");
						}else {
							clim.UpdateData(session,"name",name);
						}
					}
				}
		});
		JButton changePassword = new RoundedButton("비밀번호 변경");
		changePassword.setBounds(290, 500, 80, 50);
		changePassword.addActionListener(new ActionListener(){
			@SuppressWarnings("null")
			public void actionPerformed(ActionEvent e){
				String password =JOptionPane.showInputDialog(null,"변경할 비밀번호(영어/숫자/특수문자 조합 8~24 글자)","");
				if(password!=null){
					if(clim.checkPwCondition(password)) {
						JOptionPane.showMessageDialog(null, "기존 비밀번호와 같습니다.");
					}else if(clim.checkPwCondition(password)) {
						clim.UpdateData(session,"pw", password);
					}else JOptionPane.showMessageDialog(null, "비밀번호 조건을 확인하십시오.");
					}else {}
			}
		});
		JButton exitButton = new RoundedButton("종료");
		exitButton.setBounds(500, 610, 100, 50);
		exitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(JOptionPane.showConfirmDialog(exitButton, "종료하시겠습니까?","확인 창",JOptionPane.YES_NO_CANCEL_OPTION)==0){
				frm.setVisible(false);
				System.exit(0);
				}
			}
		});
		frm.add(back);
		frm.add(withdrawal);
		frm.add(changeName);
		frm.add(changePassword);
		frm.add(exitButton);
	}
}

