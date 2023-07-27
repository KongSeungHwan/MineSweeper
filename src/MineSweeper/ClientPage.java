package MineSweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ClientPage {
	JFrame frm;
	private Client session;
	 ClientPage(Client client){
		session =client;
		frm = new JFrame("★★★★");
		ImageIcon img = new ImageIcon("src/image/mine.png");
		frm.setIconImage(img.getImage());
		frm.setSize(700, 700);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setLocationRelativeTo(null);
		frm.getContentPane().setLayout(null);
		JLabel jl =new JLabel("★★★환영합니다★★★");
		jl.setFont(new Font("Serif", Font.BOLD,30));
		jl.setBounds(200,30,400,50);
		jl.setForeground(Color.BLUE);
		frm.add(jl);
		
		JButton inquiry = new RoundedButton("개인정보조회");
		inquiry.setBounds(100, 250, 150, 50);
		inquiry.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frm.setVisible(false);
				new InquiryPage(session).frm.setVisible(true);
			}
		});
		JButton gamePage = new RoundedButton("게임하기");
		gamePage.setBounds(300, 250, 100, 50);
		gamePage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frm.setVisible(false);
				new MineSweeperPage(session).frm.setVisible(true);
			}
		});
		JButton logout = new RoundedButton("로그아웃");
		logout.setBounds(500, 250, 100, 50);
		logout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(JOptionPane.showConfirmDialog(logout, "로그아웃하시겠습니까?","확인 창",JOptionPane.YES_NO_CANCEL_OPTION)==0){
				frm.setVisible(false);
				new LoginPage().frm.setVisible(true);
				}
			}
		});
		JButton exitButton = new RoundedButton("종료");
		exitButton.setBounds(700, 250, 100, 50);
		exitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frm.setVisible(false);
				System.exit(0);
			}
		});
		frm.add(inquiry);
		frm.add(logout);
		frm.add(gamePage);
		frm.add(exitButton);
	}
}
