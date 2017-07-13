package muaqua;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class program {

	public static void main(String[] args) throws IOException {
		hmm hm = new hmm();
		for (int i = 1; i <= 142; i++) {
			hm.ReadFile("/home/quoccuong/eclipse-workspace/Part of speech/data/" + i + ".pos");
		}
//		hm.FindUniqueStateAndCount(hm.getListSentences());
//		hm.CreateTransitionProbabilityMatrix();
//		hm.PrintMatrix();
//		hm.PrintArrState();
//		hm.AddInput(ReadInput("/home/quoccuong/eclipse-workspace/Part of speech/test/input.txt"));
//		hm.CreateEmissionProMatrix();
//		hm.PrintTransionPro();
//		System.out.println();
//		hm.PrintEmissionProMatrix();
//		System.out.println();
//		hm.PrintArrState();
		
		
		
		hmm hm1 = new hmm();
		hm1.ReadFile("/home/quoccuong/eclipse-workspace/Part of speech/data/1000.pos");
		hm1.FindUniqueStateAndCount(hm1.getListSentences());
		hm1.AddInput(ReadInput("/home/quoccuong/eclipse-workspace/Part of speech/test/input.txt"));
		hm1.CreateTransitionProbabilityMatrix();
		hm1.PrintTransionPro();
		System.out.println();
		hm1.CreateEmissionProMatrix();
		hm1.PrintEmissionProMatrix();
		System.out.println();
		hm1.PrintViterbiMatrix();
		hm1.PrintArrState();
		
		
		
		
		
		
//		for (int i = 1; i <=142; i++) {
//			FileInputStream fstream = new FileInputStream("/home/quoccuong/eclipse-workspace/Part of speech/data/" + i + ".pos");
//			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
//				String string;
//			int vt = 0;
//				String a = "CĐ/Ny Kinh_tế/N -/- công_nghệ/N TP/Ny ";
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

	private static String ReadInput(String path) throws IOException {
		FileInputStream fstream = new FileInputStream(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String str = br.readLine();
		return str;
	}
}
