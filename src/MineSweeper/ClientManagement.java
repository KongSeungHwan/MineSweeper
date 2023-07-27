package MineSweeper;

import java.io.IOException;
import javax.swing.JOptionPane;

//해당 클래스는 외부(DB)에서 가져온 정보와 회원 정보를 대조하여 데이터를 처리하는 클래스
public class ClientManagement{
	TransferData cList;

	ClientManagement(){
		cList = new TransferData();
	}
	Client checkInfo(String id, String pw) throws IOException{
		if(cList.dbList.isEmpty()) {
			JOptionPane.showMessageDialog(null, "존재하지 않는 계정..");
			return null;
		}
		else{
			if(cList.dbList.get(id)!=null){
				if(cList.dbList.get(id).getPw().equals(pw)) {
					JOptionPane.showMessageDialog(null, "로그인 성공!");
					return cList.dbList.get(id);
				}else {
					JOptionPane.showMessageDialog(null, "비밀번호 틀림..");
					return null;
				}
			}else return null;	
		}
	}
	boolean signUp(String name,String id,String pw) throws IOException{
		if((name==null)||(name.equals(""))||(name.equals("이름을 입력해주세요(공백 불가)")))
				{
			JOptionPane.showMessageDialog(null, "이름을 입력하지 않았거나 공백입니다.");
		};
		if(cList.dbList.get(id)!=null) {
			JOptionPane.showMessageDialog(null, "중복되는 아이디가 있습니다 다른 아이디를 입력해주세요");
			return false; //중복 제거
		}
		if(checkIdCondition(id)!=true) {
			JOptionPane.showMessageDialog(null, "아이디 조건을 확인하십시오.");
			return false;
		}
		if(checkPwCondition(pw)!=true){
			JOptionPane.showMessageDialog(null, "비밀번호 조건을 확인하십시오.");
			return false;
		}
		return cList.addClientByDB(name, id, pw);
	}
	boolean checkPwCondition(String id){ //정규 표현식으로 적용하여 pw가 영어/숫자/특수문자 조합인지 확인하며 총 글자 수는 몇인가 조건에 부합하는지 확인하는 메소드
		//최소 8자 부터 24자 까지 허용 시간 복잡도는 O(n) 적당? 굳이 메소드 구현할 필요없이
		//짧음
		return id.matches("^[a-zA-Z\\d`~!@#$%^&*()-_=+]{8,24}$") ? true:false; //삼항 연산자로 처리
	}
	boolean checkIdCondition(String pw){
		// id 얘는 아스키 코드로 해본다! 규칙 최소 숫자 한개를 포함하며  아스키코드 33-64 즉 허용 영문자+(특수문자)+숫자 조합 확인
		char[] check = pw.toCharArray(); //String 배열을 쓰면 메모리 공간 효율이 많이 떨어지므로 char 배열을 활용 char 크기 1byte String 크기 4byte
		int charCount=0;
		int numCount=0; //최소 몇글자인지 판별하는 변수
		int spaceCount =0;
		for (char c : check) {
			if((c>=48)&&(c<=57)) ++numCount; 
			if((c>=65)&&(c<=90)||((c>=97)&&(c<=122))) ++charCount;
			if(c==' ')++spaceCount;
		}
		return ((numCount>=1) && (charCount>=1) && ((pw.length()>=8)&&(pw.length()<=24)) && (spaceCount==0))? true : false;
		//최소 8글자 이상 최대 24글자 숫자 영문자 조합이어야하는 알고리즘+(공백 제거) 시간 복잡도 O(n) 
	}
	void Withdrawal(Client cl) {
		if(cList.deleteClientByDB(cl)){
			JOptionPane.showMessageDialog(null, "탈퇴 성공!");
		}else {
			JOptionPane.showMessageDialog(null, "존재하지 않는 계정입니다 다시시도해주십시오.");
		}
	}
	void UpdateData(Client cl,String col,String val) {
		if(cList.UpateClientByDB(cl,col,val)) {
			JOptionPane.showMessageDialog(null, "변경 성공!");
		}else {
			JOptionPane.showMessageDialog(null, "변경 실패!");
		}
	}
}
