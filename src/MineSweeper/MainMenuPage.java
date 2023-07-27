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

public class MainMenuPage{
	JFrame frm;
	MainMenuPage(){
		frm = new JFrame("★★★지뢰 찾기★★★");
		ImageIcon img = new ImageIcon("src/image/mine.png");
		ImageIcon backGround = new ImageIcon("src/image/backGroundImage.jpg");
		JLabel BackgroundLabel = new JLabel(backGround);
		frm.setContentPane(BackgroundLabel);
		frm.setIconImage(img.getImage());
		frm.setSize(700, 700);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setLocationRelativeTo(null);
		frm.getContentPane().setLayout(null);
		JLabel jl =new JLabel("★★★지뢰 찾기★★★");
		jl.setFont(new Font("Serif", Font.BOLD,30));
		jl.setBounds(200,10,400,50);
		jl.setForeground(Color.BLUE);
		frm.add(jl);
		JButton loginPage = new RoundedButton("로그인하기");
		loginPage.setBounds(200, 300, 100, 50);
		loginPage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frm.setVisible(false);
				new LoginPage().frm.setVisible(true);
			}
		});
		JButton joinPage = new RoundedButton("회원가입");
		joinPage.setBounds(400, 300, 100, 50);
		joinPage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frm.setVisible(false);
				new JoinPage().frm.setVisible(true);
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
		frm.add(loginPage);
		frm.add(joinPage);
		frm.add(exitButton);
	}
}
