//Matthew Gutkin
//CS0445 Assignment 3
//Dr. John C. Ramirez

import java.util.*;
import java.io.*;

public class Assig3 {
	public static void main(String[] args) throws IOException {
		Scanner inScan = new Scanner(System.in);
		System.out.print("Please enter grid filename: ");
		String tmp = inScan.nextLine();
		File txtFile = new File(tmp);
		if (!txtFile.exists()) throw new FileNotFoundException("File does not exist. Check your spelling?");
		Scanner input = new Scanner(txtFile);
		String line = input.nextLine();
		String[] split;
		split = line.split(" ");
		int rows = Integer.parseInt(split[0]), cols = Integer.parseInt(split[1]);
		char[][] grid = new char[rows][cols];
		for (int i = 0; input.hasNext(); i++) {
			line = input.nextLine();
			for (int j = 0; j < line.length(); j++) {
				grid[i][j] = line.charAt(j);
			}
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < rows; i++) {
			sb = new StringBuilder();
			for (int j = 0; j < cols; j++) {
				sb.append(grid[i][j]);
				sb.append(" ");
			}
			System.out.println(sb.toString());
		}
		System.out.println();
		System.out.print("Please enter phrase (sep. by single spaces): ");
		String phrase = inScan.nextLine().toLowerCase();
		while (!(phrase.equals("")))
		{
			String[] words = phrase.split(" ");
			int wordNum = words.length;
			String[] index = new String[(words.length)*2];
			System.out.println("Looking for: " + phrase);
			System.out.println("containing " + wordNum + " words");
			boolean found = false;
			for (int r = 0; (r < rows && !found); r++)
			{
				for (int c = 0; (c < cols && !found); c++)
				{
				found = findPhrase(r, c, words, 0, index, grid);

				}
			}
			if (found)
			{
				System.out.println("The phrase: " + phrase);
				System.out.println("was found:");
				for (int i=0; i<wordNum; i++){
					System.out.println(words[i] + ": " + index[i*2] + " to " + index[(i*2)+1]);
				}


				for (int i = 0; i < rows; i++)
				{
					for (int j = 0; j < cols; j++)
					{
						System.out.print(grid[i][j] + " ");
						grid[i][j] = Character.toLowerCase(grid[i][j]);
					}
					System.out.println();
				}
			}
			else
			{
				System.out.println("The phrase: " + phrase);
				System.out.println("was not found");

			}
			System.out.println();
			System.out.print("Please enter the phrase to search for: ");
        	phrase = (inScan.nextLine()).toLowerCase();
		}
	}

	private static boolean findPhrase(int r, int c, String[] words, int wordNum, String[] index, char [][] bo )
	{
		boolean myfound=false;
		for (int i=0; i<4; i++){
			myfound = findWord(r, c, words, 0, i, 0, index, bo );
			if (myfound){
				break;
			}
		}
		return myfound;

	}


	private static boolean findWord(int r, int c, String[] words, int loc, int dir, int wordNum, String[] index,  char [][] bo)
	{

		if(loc==0){
					index[(wordNum*2)]="(" + r + "," + c + ")";
				}
		if (r >= bo.length || r < 0 || c >= bo[0].length || c < 0){
			return false;
		}
		else if (bo[r][c] != words[wordNum].charAt(loc)){
			return false;
		}
		else { 											
			bo[r][c] = Character.toUpperCase(bo[r][c]); 
			boolean answer=false;
			if ( (wordNum==(words.length-1)) && (loc == (words[words.length-1]).length()-1) ){
				answer = true;																		
				index[(wordNum*2)+1]="(" + r + "," + c + ")";
			}
			else if (loc==words[wordNum].length()-1){
				index[(wordNum*2)+1]="(" + r + "," + c + ")";
				if (!answer)
					answer = findWord(r, c+1, words, 0, 0, wordNum+1, index, bo);
				if (!answer)
					answer = findWord(r+1, c, words, 0, 1, wordNum+1, index, bo);
				if (!answer)
					answer = findWord(r, c-1, words, 0, 2, wordNum+1, index, bo);
				if (!answer)
					answer = findWord(r-1, c, words, 0, 3, wordNum+1, index, bo);

				if(!answer){
					bo[r][c]= Character.toLowerCase(bo[r][c]);
				}
			}
			else
			{		
				if ( (!answer && dir==0) || (!answer && loc==0 && wordNum==0) )
					answer = findWord(r, c+1, words, loc+1, 0, wordNum, index, bo);
				if ( (!answer && dir==1) || (!answer && loc==0 && wordNum==0) )
					answer = findWord(r+1, c, words, loc+1, 1, wordNum, index, bo);
				if ( (!answer && dir==2) || (!answer && loc==0 && wordNum==0) )
					answer = findWord(r, c-1, words, loc+1, 2, wordNum, index, bo);
				if ( (!answer && dir==3) || (!answer && loc==0 && wordNum==0) )
					answer = findWord(r-1, c, words, loc+1, 3, wordNum, index, bo);
				if (!answer){
					bo[r][c] = Character.toLowerCase(bo[r][c]);
				}
			}
			return answer;
		}
	}
}