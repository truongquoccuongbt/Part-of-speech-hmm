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
	private double [][] stateObservationLikelihoodMatrix;
	private HashMap<String, Integer> listState;
	private ArrayList<String> listSentences;
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
		for (String string : arrSentences) {
			tmp1 = string.split(" ");
			for (int i = 0; i < tmp1.length; i++) {
				tmp2 = tmp1[i].split("/");
				this.listState = AddState(tmp2[1]);
			}
		}
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
	
	public void PrintMatrix() {
		double[][] matrix = this.transitionProbMatrix;
		for (int i = 0; i < this.listState.size(); i++) {
			for (int j = 0; j < this.listState.size(); j++) {
				System.out.print(matrix[i][j] + "  ");
			}
			System.out.println();
		}
	}
	
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
		String stringTmp2[];
		int col = 0;
		int row = 0;
		for (String string : listSentences) {
			wordWithTag = string.split(" ");
			for (int i = 1; i < wordWithTag.length; i++) {
				stringTmp1 = wordWithTag[i].split("/");
				stringTmp2 = wordWithTag[i - 1].split("/");
				row = FindPositionInListState(stringTmp2[1]);
				col = FindPositionInListState(stringTmp1[1]);
				transitionProMatrix[row][col]++;
			}
			stringTmp1 = wordWithTag[0].split("/");
			row = FindPositionInListState(stringTmp1[1]);
			col = FindPositionInListState(stringTmp1[1]);
			transitionProMatrix[row][col]++;
		}
		return transitionProMatrix;
	}
	
	private int FindPositionInListState(String key) {
		int pos = 0;
		for (String string : this.listState.keySet()) {
			if (string.toString().equals(key)) {
				return pos;
			}
			pos++;
		}
		return pos;
	}
	
	private double ComputeTransitionProbability(int row, int col, String stateBefore, double[][] matrixCountStateLinkStateBefore) {
		double result = 0;
		int numberOfStateBefore = this.listState.get(stateBefore);
		result = (matrixCountStateLinkStateBefore[row][col] + this.listState.size() * 0.5) / (numberOfStateBefore + this.listState.size() * 0.5 * 1000);
		result = (double)Math.round(result * 100000) / 100000; 
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
	
	public void CheckResult() {
		for (int i = 0; i < this.listState.size(); i++) {
			for (int j = 0; j < this.listState.size(); j++) {
				if (this.transitionProbMatrix[i][j] > 1) System.out.println("false");
			}
		}
		System.out.println("true");
	}
	
	public void AddInput(String input) throws IOException {
		String[] tmp = input.split(" ");
		this.input = new Word[tmp.length];
		
		for (int i = 0; i < tmp.length; i++) {
			this.input[i] = new Word();
			this.input[i].setWord(tmp[i]);
		}
	}
}
