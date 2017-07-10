package muaqua;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class hmm {
	private double [][] transitionProbMatrix;
	private HashMap<String, Integer> listState;
	private ArrayList<String> listSentences;
	
	public ArrayList<String> getListSentences() {
		return listSentences;
	}

	public void setListSentences(ArrayList<String> listSentences) {
		this.listSentences = listSentences;
	}

	public double[][] EstimateTransitionPro() {
		
		return null;
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
				if (tmp1[i].equals("/")) {
					this.listState = AddState("/");
				}
				else {
					tmp2 = tmp1[i].split("/");
					this.listState = AddState(tmp2[1]);
				}
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
		double[][] matrix = CreateMatrixCountStateLinkStateBefore();
		for (int i = 0; i < this.listState.size(); i++) {
			for (int j = 0; j < this.listState.size(); j++) {
				System.out.print(matrix[i][j] + " ");
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
				if (wordWithTag[i].equals("/")) {
					stringTmp2 = wordWithTag[i - 1].split("/");
					row = FindPositionInListState(stringTmp2[1]);
					col = FindPositionInListState("/");
				}
				else {
					try {
					stringTmp1 = wordWithTag[i].split("/");
					
					if (wordWithTag[i - 1].equals("/")) {
						row = FindPositionInListState("/");
						col = FindPositionInListState(stringTmp1[1]);
					}
					else {
						stringTmp2 = wordWithTag[i - 1].split("/");
						row = FindPositionInListState(stringTmp2[1]);
						col = FindPositionInListState(stringTmp1[1]);
					}
					}catch (Exception e) {
						System.out.println(string);
					}
				}
				
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
}
