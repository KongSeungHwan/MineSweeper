package MineSweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class GameResultPage {
	JFrame frm;
	private Client session;
	private List<List<Integer>> list;
	private List<List<JButton>> Buttonlist;
	private ClientManagement clim;
	GameResultPage(Client client,List<List<Integer>> matrix,boolean vic){
		session=client;
		clim= new ClientManagement();
		list = matrix;
		ResultList();
		frm = new JFrame("★★★Result★★★");
		ImageIcon img = new ImageIcon("src/image/mine.png");
		frm.setIconImage(img.getImage());
		frm.setSize(700, 700);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setLocationRelativeTo(null);
		frm.getContentPane().setLayout(null);
		JLabel jl =new JLabel("★★★게임 결과★★★");
		jl.setFont(new Font("Serif", Font.BOLD,30));
		jl.setBounds(200,30,400,50);
		jl.setForeground(Color.BLUE);
		frm.add(jl);
		JLabel jl2 =new JLabel(String.format("%s님 %s !!",clim.cList.dbList.get(session.getId()).getName(), vic ? "승리":"패배"));
		jl2.setFont(new Font("Serif", Font.BOLD,30));
		jl2.setBounds(200,100,400,50);
		jl2.setForeground(Color.BLACK);
		frm.add(jl2);
		JLabel jl3 =new JLabel(String.format("승률: %.2f %s",clim.cList.dbList.get(session.getId()).getvRate()*100,(char)37));
		jl3.setFont(new Font("Serif", Font.BOLD,30));
		jl3.setBounds(200,170,400,50);
		jl3.setForeground(Color.BLACK);
		frm.add(jl3);
		JLabel jl4 =new JLabel(String.format("등급: %s",clim.cList.dbList.get(session.getId()).getGrade()));
		jl4.setFont(new Font("Serif", Font.BOLD,30));
		jl4.setBounds(200,200,400,50);
		jl4.setForeground(Color.BLACK);
		frm.add(jl4);
		addButton();
		JButton back = new RoundedButton("돌아가기");
		back.setBounds(0, 600, 100, 70);
		back.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				frm.setVisible(false);
				new ClientPage(session).frm.setVisible(true);
			}
		});
		frm.add(back);
		frm.setVisible(true);
	}
	void ResultList() {
		Buttonlist= IntStream.rangeClosed(0, list.size()-1)
		.boxed()
		.map(s-> IntStream.rangeClosed(0, list.get(s)
				.size()-1).mapToObj(p-> {
					JButton but;
					if(list.get(s).get(p).equals(1)) {
						but=new RoundedButton(" ");
						but.setBackground(Color.WHITE);
					}
					else if(list.get(s).get(p).equals(2)){
						 ImageIcon icon = new ImageIcon("src\\image\\mine.png");
					        Image img = icon.getImage();
					        Image scaledImg = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
						but=new JButton(new ImageIcon(scaledImg));
						but.setBackground(Color.WHITE);
					}else {
						but=new RoundedButton("error");//잘못된 값이 들어가면 error 표시
					}
					return but;
				}).collect(Collectors.toList())).collect(Collectors.toList());
	}
	void addButton() {
		int col=0 ,row = 0, startCol=0,startRow=0;
		if(Buttonlist.size()==3) {
			startCol=250;startRow=350;
		}else if(Buttonlist.size()==4){
			startCol=200;startRow=300;
		}else if(Buttonlist.size()==5) {
			startCol=170;startRow=250;
		}else if(Buttonlist.size()==6) {
			startCol=120;startRow=180;
		}else{
			JOptionPane.showMessageDialog(null, "잘못된 접근입니다.운영자에게 문의하십시오");
			frm.setVisible(false);
			new ClientPage(session).frm.setVisible(true);
		}
		try {
			for(int i=0;i<Buttonlist.size();i++) {
				row = 0;
				col+=70;
				for(int j=0;j<Buttonlist.size();j++) {
					JButton bu = Buttonlist.get(i).get(j);
					bu.setBounds(startCol+row, startRow+col, 60, 60);
					frm.getContentPane().add(bu);
					row+=70;
				}
			}
	}catch(IndexOutOfBoundsException e){
		System.out.println("잘못된 접근입니다 운영자에게 문의하십시오.");
		frm.setVisible(false);
		new ClientPage(session).frm.setVisible(true);
	}
	}
	
}
