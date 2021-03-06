package muaqua;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystemNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class hmm {
	private double [][] transitionProbMatrix;
	private double [][] emissionProMatrix;
	private HashMap<String, Integer> listState;
	private ArrayList<String> listSentences;
	private HashMap<String, ArrayList<String>> dic;
	private Word[] inputAferFilter;
	private Word[] input;
	
	public ArrayList<String> getListSentences() {
		return listSentences;
	}

	public void setListSentences(ArrayList<String> listSentences) {
		this.listSentences = listSentences;
	}
	
	public hmm() {
		listState = new  HashMap<>();
		listSentences = new ArrayList<>();
		CreateDictionary();
	}
	
	private void CreateDictionary() {
		try {
		dic = new HashMap<>();
		FileInputStream fstream = new FileInputStream("/home/quoccuong/eclipse-workspace/Part of speech _vesion2/vn-dic.dic");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String line;
		ArrayList<String> arrTmp;
		String []tmp;
		while ((line = br.readLine()) != null) {
			arrTmp = new ArrayList<>();
			tmp = line.split(" ");
			for (int i = 1; i < tmp.length; i++) {
				arrTmp.add(tmp[i]);
			}
			dic.put(tmp[0], arrTmp);
		}
		}catch (Exception e) {
			System.out.println("Không tìm thấy file");
		}
	}
	
	public void ReadFile(String input) throws IOException {
		FileInputStream fstream = new FileInputStream(input);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;
		while ((strLine = br.readLine()) != null) {
			strLine = "st/st " + strLine + " sf/sf";
			this.listSentences.add(strLine);
		}
		br.close();
	}
	
	public void FindUniqueStateAndCount(ArrayList<String> arrSentences) {
		String[] tmp1;
		String[] tmp2;
		int dem = 0;
		for (String string : arrSentences) {
			tmp1 = string.split(" ");
			for (int i = 0; i < tmp1.length; i++) {
				tmp2 = tmp1[i].split("/");
				if (tmp2[0].equals("oOo")) {
					System.out.println(string);
				}
				if (isLetters(tmp2[1])) {
					this.listState = AddState(tmp2[1]);
				}
			}
		}
	}
	
	private boolean isLetters(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isLetter(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	public HashMap<String, Integer> AddState(String state) {
		if (this.listState.containsKey(state)) {
			this.listState.put(state, this.listState.get(state) + 1);
		}
		else {
			this.listState.put(state, 1);
		}
		return this.listState;
	}
	
	//--------------------------------------------------------------------------------------
	// Kiểm tra lỗi chạy bài
	//--------------------------------------------------------------------------------------
	
	public void PrintArrState() {
		for (String string : this.listState.keySet()) {
			String key = string.toString();
			String value = this.listState.get(string).toString();
			System.out.println(key + " " + value);
		}
	}
	
	public void PrintSentences() {
		for (String string : listSentences) {
			System.out.println(string);
		}
	}
	
	public void PrintEmissionProMatrix() {
		double[][] matrix = this.emissionProMatrix;
		for (int i = 0; i < this.listState.size(); i++) {
			for (int j = 0; j < this.input.length; j++) {
				System.out.print(matrix[i][j] + "  ");
			}
			System.out.println();
		}
	}
	
	public void PrintViterbiMatrix() {
		double[][] matrix = CreateViterbiMatrix();
		for (int i = 0; i < this.listState.size(); i++) {
			for (int j = 0; j < this.input.length; j++) {
				System.out.print(matrix[i][j] + "  ");
			}
			System.out.println();
		}
	}
	
	public void PrintTransionPro() {
		double[][] matrix = this.transitionProbMatrix;
		for (int i = 0; i < this.listState.size(); i++) {
			for (int j = 0; j < this.listState.size(); j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public void CheckResult() {
		for (int i = 0; i < this.listState.size(); i++) {
			for (int j = 0; j < this.listState.size(); j++) {
				if (this.transitionProbMatrix[i][j] > 1) System.out.println("false");
			}
		}
		System.out.println("true");
	}
	
	public void PrintDic() {
		for (String string : this.dic.keySet()) {
			String key = string;
			System.out.println(key);
			for (int i = 0; i < this.dic.get(key).size(); i++) {
				System.out.print(this.dic.get(key).get(i) + " ");
			}
			System.out.println();
		}
	}
	
	public void PrintInput() {
		for (int i = 0; i < this.inputAferFilter.length && this.inputAferFilter[i] != null; i++) {
			System.out.println(this.inputAferFilter[i].getWord());
		}
	}
	
	//*****************************************************************************************
	
	
	//------------------------------------------------------------------------------------------
	// Tính ma trận transition probability
	//------------------------------------------------------------------------------------------
	public double[][] CreateMatrixCountStateLinkStateBefore() {
		int sizeOfMatrix = this.listState.size();
		double[][] matrixCountStateLinkStateBefore = new double[sizeOfMatrix][sizeOfMatrix];
		for (int i = 0; i < sizeOfMatrix; i++) {
			for (int j = 0; j < sizeOfMatrix; j++) {
				matrixCountStateLinkStateBefore[i][j] = 0;
			}
		}
		matrixCountStateLinkStateBefore = FillInMatrixCountStateLinkStateBefore(matrixCountStateLinkStateBefore);
		return matrixCountStateLinkStateBefore;	
	}
	
	public double[][] FillInMatrixCountStateLinkStateBefore(double[][] transitionProMatrix) {
		String wordWithTag[];
		String stringTmp1[];
		String stringTmp2;
		int col = 0;
		int row = 0;
		for (String string : listSentences) {
			wordWithTag = string.split(" ");
			for (int i = 1; i < wordWithTag.length; i++) {
				stringTmp1 = wordWithTag[i].split("/");
				stringTmp2 = FindWordBeforeInSentence(wordWithTag, i);
				if (isLetters(stringTmp1[1])) {
					row = FindPositionInListState(stringTmp2);
					col = FindPositionInListState(stringTmp1[1]);
					transitionProMatrix[row][col]++;
				}
			}
		}
		return transitionProMatrix;
	}
	
	private String FindWordBeforeInSentence(String[] wordWithTag, int posWordCurrent) {
		String []tmp;
		for (int i = posWordCurrent - 1; i >= 0; i--) {
			tmp = wordWithTag[i].split("/");
			if (isLetters(tmp[1])) {
				return tmp[1];
			}
		}
		return null;
	}
	
	private int FindPositionInListState(String key) {
		int pos = 0;
		for (String string : this.listState.keySet()) {
			if (string.toString().equals(key)) {
				return pos;
			}
			pos++;
		}
		return -1;
	}
	
	private double ComputeTransitionProbability(int row, int col, String stateBefore, double[][] matrixCountStateLinkStateBefore) {
		double result = 0;
		double numberOfStateBefore = this.listState.get(stateBefore);
		result = (matrixCountStateLinkStateBefore[row][col] + 0.000000001) / (numberOfStateBefore + this.listState.size() * 0.00000001);
		//result = result * 10000000;
		return result;
	}
	
	public void CreateTransitionProbabilityMatrix() {
		double[][] matrixCountStateLinkStateBefore = CreateMatrixCountStateLinkStateBefore();
		int sizeOfMatrix = this.listState.size();
		this.transitionProbMatrix = new double[sizeOfMatrix][sizeOfMatrix];
		int row, col;
		for (String string1 : listState.keySet()) {
			for (String string2 : listState.keySet()) {
				row = FindPositionInListState(string1);
				col = FindPositionInListState(string2);
				this.transitionProbMatrix[row][col] = ComputeTransitionProbability(row, col, string1, matrixCountStateLinkStateBefore);
			}
		}
	}
	//****************************************************************************************
	
	
	
	
	//---------------------------------------------------------------------------------
	// Xử lý input
	//--------------------------------------------------------------------------------
	private void AddInput(String input) throws IOException {
		input = "st " + input + " sf";
		String[] tmp = input.split(" ");
		this.input = new Word[tmp.length];
		
		for (int i = 0; i < tmp.length; i++) {
			this.input[i] = new Word();
			this.input[i].setWord(tmp[i]);
		}
	}
	
	private void FilterInput() {
		int size = this.input.length;
		this.inputAferFilter = new Word[size];
		int j = 0;
		for (int i = 0; i < this.input.length; i++) {
			if (Character.isLetter(this.input[i].getWord().charAt(0)) || Character.isDigit(this.input[i].getWord().charAt(0))) {
				this.inputAferFilter[j] = new Word();
				this.inputAferFilter[j].setWord(this.input[i].getWord());
				j++;
			}
			else {
				this.input[i].setTag(this.input[i].getWord());
			}
		}
	}
	
	public void HandleInput(String input) throws IOException {
		AddInput(input);
		FilterInput();
	}
	
	
	//*********************************************************************************
	
	
	//-----------------------------------------------------------------------------
	// Tính ma trận emission probability
	//-----------------------------------------------------------------------------
	public void CreateEmissionProMatrix() {
		 int sizeOfListState = this.listState.size();
		 int sizeOfInput = this.inputAferFilter.length;
		 
		 this.emissionProMatrix = new double[sizeOfListState][sizeOfInput];
		 for (int i = 0; i < sizeOfListState; i++) {
			 for (int j = 0; j < sizeOfInput; j++) {
				 this.emissionProMatrix[i][j] = 0;
			 }
		 }
		 
		 int[][] matrix = CountEmissionMatrix(sizeOfListState, sizeOfInput);
		 int row = 0;
		 for (String string : this.listState.keySet()) {
			 for (int col = 0; col < sizeOfInput && this.inputAferFilter[col] != null; col++) {
				 this.emissionProMatrix[row][col] = ComputeEmissionProbalityEachWord(this.listState.get(string), matrix[row][col], CheckWordInDic(this.inputAferFilter[col].getWord(), string));
			 }
			 row++;
		 }
	}
	
	private int CheckWordInDic(String word, String tag) {
		if (this.dic.containsKey(word)) {
			ArrayList<String> tmp = this.dic.get(word);
			for (int i = 0; i < tmp.size(); i++) {
				if (tmp.get(i).equals(tag)) {
					return 1;
				}
			}
		}
		return 0;
		
	}
	
	private int[][] CountEmissionMatrix(int nRow, int nCol) {
		int[][] matrix = new int[nRow][nCol];
		for (int i = 0; i < nRow; i++) {
			for (int j = 0; j < nCol; j++) {
				matrix[i][j] = 0;
			}
		}
		
		int row, col;
		String[] tmp1;
		String[] tmp2;
		for (int i = 0; i < this.listSentences.size(); i++) {
			tmp1 = this.listSentences.get(i).split(" ");
			for (int j = 0; j < tmp1.length; j++) {
				tmp2 = tmp1[j].split("/");
				row = FindPositionInListState(tmp2[1]);
				col = FindPosOfWordInInput(tmp2[0], this.inputAferFilter);
				if (row != -1 && col != -1) {
					matrix[row][col]++;
				}
			}
		}
		return matrix;
	}
	
	private int FindPosOfWordInInput(String key, Word[] input) {
		String[] tmp;
		for (int i = 0; i < input.length && input[i] != null; i++) {
			tmp = input[i].getWord().split("/");
			if (tmp[0].equals(key)) {
				return i;
			}
		}
		return -1;
	}
	
	private double ComputeEmissionProbalityEachWord(int tag, int wordWithTag, int existDic) {
		double result = 0;
		result = (double)(wordWithTag + 0.000000001 + existDic) / (tag + existDic + this.inputAferFilter.length * 0.000000001);
		//result = result * 10000000;
		return result;
	}
	//***********************************************************************************************************
	
	
	//---------------------------------------------------------------------------------------------------
	// Tạo viterbi matrix
	//---------------------------------------------------------------------------------------------------
	public double[][] CreateViterbiMatrix() {
		int nRow = this.listState.size();
		int nCol = this.inputAferFilter.length;
		
		double[][] viterbiMatrix = new double[nRow][nCol];
		
		// Cột đầu tiên và cuối cùng bằng 0
		for (int i = 0; i < nRow; i++) {
			viterbiMatrix[i][0] = 0;
			viterbiMatrix[i][nCol - 1] = 0;
		}
		
		viterbiMatrix = ComputeFirstWordOfLine(viterbiMatrix, nRow);
		viterbiMatrix = ComputeOtherWordOfLine(viterbiMatrix, nRow);
		PartOfSpeechWord(viterbiMatrix, nRow, nCol);
		
		return viterbiMatrix;
	}
	
	private double[][] ComputeFirstWordOfLine(double[][] viterbiMatrix, int nRow) {
		int row = FindPositionInListState("st");
		for (int i = 0; i < nRow; i++) {
			viterbiMatrix[i][1] = this.transitionProbMatrix[row][i] * this.emissionProMatrix[i][1];
			//viterbiMatrix[i][1] = (double) Math.round(viterbiMatrix[i][1] * 100) / 100;
		}
		return viterbiMatrix;
	}
	
	private double[][] ComputeOtherWordOfLine(double[][] viterbiMatrix, int nRow) {
		for (int i = 2; i < this.inputAferFilter.length - 1 && this.inputAferFilter[i] != null; i++) {
			for (int j = 0; j < nRow; j++) {
				viterbiMatrix[j][i] = MaxProOfWordWithTag(viterbiMatrix, nRow, j, i);
			}
		}
		return viterbiMatrix;
	}
	
	private double MaxProOfWordWithTag(double[][] viterbiMax, int nRow, int posOfStateWithWord, int posOfWordInInput) {
		double result;
		double max = 0;
		for (int i = 0; i < nRow; i++) {
			result = this.transitionProbMatrix[i][posOfStateWithWord] * this.emissionProMatrix[posOfStateWithWord][posOfWordInInput] * viterbiMax[i][posOfWordInInput - 1];
			result = result * 1000000000;
			if (max < result) {
				max = result;
			}
		}		
		return max;
	}
	
	private String FindKeyFromPos(HashMap<String, Integer> hashmap, int pos) {
		int tmp = -1;
		for (String string : hashmap.keySet()) {
			tmp++;
			if (tmp == pos) {
				return string;
			}
		}
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------
	// Xử lý output
	//-----------------------------------------------------------------------------------------------
	public void PartOfSpeechWord(double[][] viterbiMax, int nRow, int nCol) {
		int row = -1;
		for (int i = 1; i < nCol - 1 && this.inputAferFilter[i] != null; i++) {
			double max = -1;
			for (int j = 0; j < nRow; j++) {
				if (max < viterbiMax[j][i]) {
					max = viterbiMax[j][i];
					row = j;
				}
			}
			this.inputAferFilter[i].setTag(FindKeyFromPos(this.listState, row));
		}
	}
	
	public void PrintOutput() {
		String a;
		int j = 1;
		for (int i = 1; i < this.input.length - 1; i++) {
			if (this.input[i].getTag() == null) {
				a = this.inputAferFilter[j].getWord() + "/" + this.inputAferFilter[j].getTag();
				j++;
			}
			else {
				a = this.input[i].getWord() + "/" + this.input[i].getTag();
			}
			if ((i + 1) < (this.input.length - 1)) {
				a = a + " ";
			}
			else {
				a = a + "\n";
			}
			System.out.print(a);
		}
	}
}
