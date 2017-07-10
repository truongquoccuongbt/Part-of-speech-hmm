package muaqua;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class program {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		hmm hm = new hmm();
		for (int i = 1; i <= 142; i++) {
			hm.ReadFile("/home/quoccuong/eclipse-workspace/Truamua/data/" + i + ".pos");
		}
		hm.FindUniqueStateAndCount(hm.getListSentences());
		hm.PrintMatrix();
		hm.PrintArrState();
		
		
//		hmm hm1 = new hmm();
//		hm1.ReadFile("/home/quoccuong/eclipse-workspace/Truamua/data/1000.pos");
//		hm1.FindUniqueStateAndCount(hm1.getListSentences());
//		hm1.PrintMatrix();
//		hm1.PrintArrState();
		
		
		
		
		
		
//		for (int i = 1; i <=142; i++) {
//			FileInputStream fstream = new FileInputStream("/home/quoccuong/eclipse-workspace/Truamua/data/" + i + ".pos");
//			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
//				String string;
//			int vt = 0;
//				String a = "Bà/Nc Nguyễn_Thị_Rảnh/Np ,/, 62/M tuổi/N ,/, ngang/V tuổi/N bà/N nội/N tôi/P ,/, chân/N chậm_chạp/A ,/, mắt/N đã/R mờ/A nhiều/A vẫn/R cố/V ghé/V N/vai/V vào/R những/L bao/N hàng/N nặng_trịch/A bỏ/V lên/E xe/N đẩy/V ./.";
//				while ((string = br.readLine()) != null) {
//					vt++;
//					try {
//					if (a.equals(string)) {
//						throw new Exception("aa");
//					}
//				}catch (Exception e) {
//						System.out.println(i);
//						System.out.println(vt);
//					}
//				}
//		}
	}

}
