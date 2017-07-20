package muaqua;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Clock;

public class program {

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		hmm hm = new hmm();
		for (int i = 9; i <= 142; i++) {
			hm.ReadFile("/home/quoccuong/eclipse-workspace/Part of speech _vesion2/data/" + i + ".pos");
		}
		hm.FindUniqueStateAndCount(hm.getListSentences());
		hm.CreateTransitionProbabilityMatrix();
//		hm.HandleInput(ReadInput("/home/quoccuong/eclipse-workspace/Part of speech/test/input.txt"));
//		hm.CreateEmissionProMatrix();
//		hm.CreateEmissionProMatrix();
//		hm.CreateViterbiMatrix();
//		hm.PrintOutput();
//		hm.CreateTransitionProbabilityMatrix();
//		hm.PrintEmissionProMatrix();
//		hm.PrintArrState();
//		hm.PrintOutput();
		
		//String []input = ReadInput("/home/quoccuong/eclipse-workspace/Part of speech/test/input.txt");
		FileInputStream fstream = new FileInputStream("/home/quoccuong/eclipse-workspace/Part of speech _vesion2/test/input.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String line;
		while ((line = br.readLine()) != null) {
			XuLy(hm, line);
		}
		
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000);
		
		
//		hmm hm1 = new hmm();
//		hm1.ReadFile("/home/quoccuong/eclipse-workspace/Part of speech _vesion2/data/1000.pos");
//		hm1.FindUniqueStateAndCount(hm1.getListSentences());
//		hm1.HandleInput(ReadInput("/home/quoccuong/eclipse-workspace/Part of speech _vesion2/test/input.txt"));
//		hm1.CreateTransitionProbabilityMatrix();
//		hm1.PrintTransionPro();
//		System.out.println();
//		hm1.CreateEmissionProMatrix();
//		hm1.PrintEmissionProMatrix();
//		System.out.println();
//		hm1.PrintViterbiMatrix();
//		hm1.PrintArrState();
//		hm1.PrintOutput();
		
		
		
		
		
		
//		for (int i = 1; i <=142; i++) {
//			FileInputStream fstream = new FileInputStream("/home/quoccuong/eclipse-workspace/Part of speech/data/" + i + ".pos");
//			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
//				String string;
//			int vt = 0;
//				String a = "oOo/oOo";
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
	private static void XuLy(hmm hm, String str) throws IOException {
		hm.HandleInput(str);
		hm.CreateEmissionProMatrix();
		hm.CreateEmissionProMatrix();
		hm.CreateViterbiMatrix();
		hm.PrintOutput();
	}

	private static String ReadInput(String path) throws IOException {
		FileInputStream fstream = new FileInputStream(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String str = br.readLine();
		return str;
	}
}
