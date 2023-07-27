package MineSweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class MineSweeperPage{
	JFrame frm; //default 같은 패키지 내
	private Client session;
	private List<List<Integer>> mineMatrix;
	private List<List<String>> currentMatrix;
	private List<List<Integer>> selectMatrix;
	private List<List<JButton>> JButtonMatrix;//해당 리스트들은 collect(Collectors.toList()를 호출 하게 된다면 기본적으로 ArrayList가 리턴된다.)
	private int normalNumber = 0;
	private int mineNumber = 0;
	MineSweeperPage(Client client){
		session= client;
		int num=0;
		if(session.getGrade().equals("UnrankedPlayer")) num=3;
		else if(session.getGrade().equals("BeginnerPlayer")) num=4;
		else if(session.getGrade().equals("AdvancedPlayer")) num=5;
		else if(session.getGrade().equals("MasterPlayer")) num=6;
		else {num =-1;}
		try {mineMatrix=createMatrix(num);
			selectMatrix=createSelectMatrix(num);
			currentMatrix=createDefaultMatrix(num);
			JButtonMatrix=createJButtonMatrix();
			mineCount();
			matrixView(mineMatrix);
		}catch(IndexOutOfBoundsException e){
			System.out.println("정상적인 등급이 아닙니다 운영자에게 문의하십시오.");
			frm.setVisible(false);
			new ClientPage(session).frm.setVisible(true);
		}
		renewContent();
		currectMatrixOperation();
		JButtonMatrix=createJButtonMatrix();
		addButton();
	}
	void renewContent() {
		frm = new JFrame("★★★지뢰 찾기★★★");
		ImageIcon img = new ImageIcon("src/image/mine.png");
		frm.setIconImage(img.getImage());
		frm.setSize(700, 700);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setLocationRelativeTo(null);
		frm.getContentPane().setLayout(null);
		JLabel jl =new JLabel("★★★지뢰 찾기★★★");
		jl.setFont(new Font("Serif", Font.BOLD,30));
		jl.setBounds(200,30,400,50);
		jl.setForeground(Color.BLUE);
		frm.add(jl);
		JLabel jl2 =new JLabel(String.format("지뢰 수:%s",mineNumber));
		jl2.setFont(new Font("Serif", Font.BOLD,30));
		jl2.setBounds(100,600,400,50);
		jl2.setForeground(Color.BLACK);
		frm.add(jl2);
		JButton back = new JButton("돌아 가기"); 
		back.setBounds(500, 600, 200, 70);
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frm.setVisible(false);
				new ClientPage(session).frm.setVisible(true);
			}
		});
		frm.add(back);
	}
	void addButton(){
		int col=0 ,row = 0;
		for(int i=0;i<JButtonMatrix.size();i++) {
			row = 0;
			col+=70;
			for(int j=0;j<JButtonMatrix.size();j++) {
				JButton bu = JButtonMatrix.get(i).get(j);
				bu.setBounds(100+row, 30+col, 60, 60);
				frm.getContentPane().add(bu);
				row+=70;
			}
		}
	}
	void renewButton() {
		renewContent();
		addButton();
	}
	List<List<JButton>> createJButtonMatrix(){
		return IntStream.rangeClosed(0, currentMatrix.size()-1)
				.boxed()
				.map(s-> IntStream.rangeClosed(0, currentMatrix.get(s)
						.size()-1).mapToObj(p-> {
							JButton but;
							if(currentMatrix.get(s).get(p).equals("1")) {
								but=new RoundedButton(" ");
								but.setBackground(Color.WHITE);
							}
							else if(currentMatrix.get(s).get(p).equals("2")){
								 ImageIcon icon = new ImageIcon("src\\image\\mine.png");
							        Image img = icon.getImage();
							        Image scaledImg = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
								but=new RoundedButton(new ImageIcon(scaledImg));
							}else if(currentMatrix.get(s).get(p).equals("*")){
								ImageIcon icon = new ImageIcon("src\\image\\question_mark.png");
						        Image img = icon.getImage();
						        Image scaledImg = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
							but=new JButton(new ImageIcon(scaledImg));
							but.setBackground(Color.WHITE);
							}else {
								but=new RoundedButton("error");//잘못된 값이 들어가면 error 표시
							}
							but.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e){
									if(!selectSerface(p,s)) {
										JOptionPane.showMessageDialog(null, "Defeat!");
										new DAO().updateGameDataSQL(false,session);
										frm.setVisible(false);
										new GameResultPage(session,mineMatrix,false).frm.setVisible(true);
									}else {
										frm.setVisible(false);
										//버튼 지우는 작업
										frm.removeAll();
										currectMatrixOperation();
										JButtonMatrix=createJButtonMatrix();
										addButton();
										renewButton();
										//다시 추가
										frm.setVisible(true);
										if(normalNumber==0) {
											JOptionPane.showMessageDialog(null, "Victory!");
											new DAO().updateGameDataSQL(true,session);
											frm.setVisible(false);
											new GameResultPage(session,mineMatrix,true).frm.setVisible(true);
										}
									}
								}
							});
							return but;
						}).collect(Collectors.toList())).collect(Collectors.toList());
	}//JButton리스트 생성 메소드(스트림 활용)
	
	boolean selectSerface(int x, int y){
		if(selectMatrix.get(y).get(x)==1) {
			JOptionPane.showMessageDialog(null,"이미 오픈된 땅!");
			return true; //이미 통과한 땅이라 true 반환
		}
		else if(selectMatrix.get(y).get(x)==0){
			List<Integer> modilist = selectMatrix.get(y);
			if(mineMatrix.get(y).get(x)==1) normalNumber--;
			modilist.set(x, 1);
			selectMatrix.set(y,modilist);
			if(mineMatrix.get(y).get(x)==1) {
				chainOpen(x,y);
				return true;
			}else if(mineMatrix.get(y).get(x)==2){
				return false;
			}else {
				JOptionPane.showMessageDialog(null,"프로그램 문제 발생! 자료구조에 유효하지 않은 값이 저장되었습니다.");
				return false;
			}
		}
		else {
			JOptionPane.showMessageDialog(null,"프로그램 문제 발생! 자료구조에 유효하지 않은 값이 저장되었습니다.");
			return false;
		}
	}//좌표 값 선택 시 지정 여부를 selectMatrix에 저장하는 형태의 메소드
	List<List<Integer>> createMatrix(int num){
		List<List<Integer>> list=IntStream.rangeClosed(1, num)
				.boxed()
				.map(s-> IntStream.concat(IntStream.generate(()-> 2).limit(1),new Random()
						.ints(1,1000)
						.limit(num-1)
						.map(u-> (u>=700)? 2 : 1)
						).boxed().collect(Collectors.toList())).collect(Collectors.toList());
				list.stream().forEach(Collections::shuffle);
				return list;
	}
	//1이 일반땅, 2가 폭탄 땅을 의미한다.(70% 확률로 일반땅, 30%확률로 폭탄땅이 나온다.)
	List<List<Integer>> createSelectMatrix(int num){
		return IntStream.rangeClosed(1, num)
				.boxed()
				.map(s-> IntStream.generate(()->0)
						.boxed()
						.limit(num)
						.collect(Collectors.toList())).collect(Collectors.toList());
	}
	List<List<String>> createDefaultMatrix(int num){
		return IntStream.rangeClosed(1, num)
				.boxed()
				.map(s-> Stream
						.generate(()->"*")
						.limit(num)
						.collect(Collectors.toList())).collect(Collectors.toList());
	}

	<T> void matrixView(List<List<T>> list){
		list.stream().forEach(s-> System.out.println(s.stream().map(String::valueOf).collect(Collectors.joining(" "))));
		System.out.println();
	}//제네릭 메소드(원소가 Integer든 String이든 Matrix형식으로 조회 가능)


	final void chainOpen(int x,int y) { //템플릿 메소드 폭탄 땅이 있을 시까지 열리는 메소드
		selectChainrow(x,y);
		selectChaincol(x,y);
		selectChainrowpl(x,y);
		selectChaincolpl(x,y);
	}

	void selectChainrow(int x, int y){
		if((selectMatrix.get(y).get(x)==0)&&(mineMatrix.get(y).get(x)==1)){
			List<Integer> modilist = selectMatrix.get(y);
			modilist.set(x, 1);
			selectMatrix.set(y, modilist);
			normalNumber--;
		}
		if(mineMatrix.get(y).get(x)==1) {
			if(x >0) selectChainrow(x-1,y);
		}

	}
	void selectChaincol(int x, int y){
		if((selectMatrix.get(y).get(x)==0)&&(mineMatrix.get(y).get(x)==1)){
			List<Integer> modilist = selectMatrix.get(y);
			modilist.set(x, 1);
			selectMatrix.set(y, modilist);
			normalNumber--;
		}
		if(mineMatrix.get(y).get(x)==1){
			if(y >0) selectChaincol(x,y-1);
		}	

	}
	void selectChainrowpl(int x, int y){
		if((selectMatrix.get(y).get(x)==0)&&(mineMatrix.get(y).get(x)==1)){
			List<Integer> modilist = selectMatrix.get(y);
			modilist.set(x, 1);
			selectMatrix.set(y, modilist);
			normalNumber--;
		}
		if(mineMatrix.get(y).get(x)==1){
			if(x <mineMatrix.size()-1)selectChainrowpl(x+1,y);
		}
	}
	void selectChaincolpl(int x, int y){
		if((selectMatrix.get(y).get(x)==0)&&(mineMatrix.get(y).get(x)==1)){
			List<Integer> modilist = selectMatrix.get(y);
			modilist.set(x, 1);
			selectMatrix.set(y, modilist);
			normalNumber--;
		}
		if(mineMatrix.get(y).get(x)==1){
			if(y <mineMatrix.get(0).size()-1)selectChaincolpl(x,y+1);
		}
	}
	void currectMatrixOperation(){
		List<String> modilist = null;
		for (int i = 0; i < selectMatrix.size(); i++) {
			for (int j = 0; j < selectMatrix.get(i).size(); j++){
				modilist = currentMatrix.get(j);
				if(selectMatrix.get(j).get(i)==0){
					modilist.set(i, "*");
					currentMatrix.set(j, modilist);
				}else if(selectMatrix.get(j).get(i)==1){
					modilist.set(i, mineMatrix.get(j).get(i)+""); //+문자열 붙여서 바로 형변환
					currentMatrix.set(j, modilist);
				}else{}
			}
		}
	} //얘는 어쩔 수 없이 for문 써야함, 본래 3중 자료구조 사용할랬는데 시도하면 힙 영역 부족하다고 뜨고 Out Of Memory 에러가 뜬다.
	void mineCount(){
		mineMatrix.stream().forEach(s-> s.stream().forEach(p->{
			if(p==1)normalNumber++;
			if(p==2)mineNumber++;
		}));
	}//일반 땅, 폭탄 땅을 카운트 하는 메소드(게임 종료시의 변수로 사용)
}
