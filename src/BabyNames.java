// First, read all values from local storages directory path into two (one for each gender) correctly sorted multi-dimensional arrays
// Second, prompt user for the year, gender, and name to store in class data fields to later be compared
// Third, use nested for loops and nested if statements to search through the appropriate multidimensional (dictated by users year and gender) to find the key (their requested name), and print rank to the screen after name's position is found in multidimensional array

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class BabyNames {
	
	// This program assumes there are 10 files that contain census data
	// and that each file has 1000 rows
	final int NUMBER_OF_FILES_IN_DIRECTORY = 10;
	final int ROWS_IN_FILE = 1000;
	
	// Used later in findRank() method for ensuring user requested a year for which census data is kept
	ArrayList <Integer> eligibleYears = new ArrayList<Integer>();
	
	// Create two arrays for each gender. Each comes from creating an array of ranks that correlate to names, 
	// which is inside another array that corresponds to a year
	String[][] boyNames = new String[NUMBER_OF_FILES_IN_DIRECTORY][ROWS_IN_FILE];
	String[][] girlNames = new String[NUMBER_OF_FILES_IN_DIRECTORY][ROWS_IN_FILE];
	
	// Declare the following variables here so new memory locations aren't needed when 
	// storing into multidimensional array later
	String nameRank = "";
	String boyName = ""; 
	String girlName = "";
	String year = "";
	String gender = "";
	String name = "";
	
	// Fill values into boyNames[][] and girlNames[][] in readNamesIntoArray() method
	public void readNamesIntoArray() throws FileNotFoundException {
		
		Scanner requestFilePath = new Scanner(System.in);
		System.out.println("Please enter the file path of the folder holding your census data files");
		System.out.println("(For example: /home/MyName/CensusData)\n");
		String censusDataFilePath = requestFilePath.nextLine();
		
		// Create a File (the data type) which consists of a directory that holds .txt files
		File directory = new File(censusDataFilePath);
		// Add println for spacing in console
		System.out.println();
		
		// Create empty array with length equal to number of .txt files in directory
		File filesArray[] = new File[NUMBER_OF_FILES_IN_DIRECTORY];
		
		// Clone .txt files into ArrayList filesArray. The files currently are not sorted by year and the following
		// code will accomplish this by creating a new empty ArrayList, putting the .txt files from filesArray into 
		// an ArrayList, then sorting them
		filesArray = directory.listFiles().clone();
		ArrayList <File> filesArrayList = new ArrayList<>();
		for(int z = 0; z < NUMBER_OF_FILES_IN_DIRECTORY; z++) {
			filesArrayList.add(filesArray[z]);
		}
		Collections.sort(filesArrayList);
		
		// Convert sorted ArrayList filesArrayList back to an array by passing in a new empty File array 
		// with length equal to number of files in directory
		File allFiles[] = filesArrayList.toArray(new File[NUMBER_OF_FILES_IN_DIRECTORY]);
		
		// Outer for-loop through each .txt file to read data into outer array of multidimensional arrays,
		// then inner for-loop through each row to read data into inner array of multidimensional array
		for(int i = 0; i < allFiles.length; i++) {
			
			// Create scanner to read in input from current iteration's .txt file
			Scanner fileInput = new Scanner(allFiles[i]);
			
			// Get the name of the .txt file and extract the year (like "2001"), 
			// and assign it to a string used only in this outer for loop
			String fileName = allFiles[i].getName();
			String filesYear = fileName.substring(15, 19);
			
			// Add that extracted fileYear to eligibleYears array (for ensuring valid year requested by user)
			eligibleYears.add(Integer.parseInt(filesYear));
			
			// Inner for loop through each row in this current outer loops iteration's .txt file to read data
			// from each row into inner array of multidimensional array
			for(int j = 0; j < ROWS_IN_FILE; j++) {
				
				// Consume what can be thought of as column 1 (rank) 
				fileInput.next();
				
				// Assign and consume what can be thought of as column 2 to String boyName
				boyName = fileInput.next();
				
				// Consume what can be thought of as column 3 (# of boys with that name)
				fileInput.next();
				
				// Assign and consume what can be thought of as column 4 to String girlName
				girlName = fileInput.next();
				
				// Consume what can be thought of as column 5 (# of girls with that name)
				fileInput.next();
				
				// Assign both boyName and girlName to their appropriate position in their respective multi-dimensional arrays
				boyNames[i][j] = boyName;
				girlNames[i][j] = girlName;
		 	}
			
			// Close scanner to remove warnings and keep from possibly corrupting files
			fileInput.close();
		}
	}

	// Ask user to request year, gender, and name. Use Scanner(System.in) and nextLine() to assign 
	// what they input to each appropriate String variable (all are class data fields) and 
	// close user's input Scanner
	public void promptUser() {
		Scanner userInput = new Scanner(System.in);
		System.out.print("Year: ");
		year = userInput.nextLine();
		System.out.print("Gender: ");
		gender = userInput.nextLine();
		System.out.print("Name: ");
		name = userInput.nextLine();
		userInput.close();
	}
	
	// Find the rank of the name with the data provided thus far
	public void findRank() {
		
		// Declare an arrayToSearchIn without a reference. (Reference added next using if statement)
		String[][] arrayToSearchIn = null;
		
		// Assign reference to arrayToSearchIn based off of whether user previously requested boys or girls ("M" or "F") in 
		// promptUser() method. Close program with warning for next time if request was not valid.
		if(gender.equals("M")) {
			arrayToSearchIn = boyNames; }
		else if(gender.equals("F")) {
			arrayToSearchIn = girlNames; }
		else {
			System.out.println("Please enter M or F for gender");
			System.exit(0);
		}
		
		// Create boolean flag to indicate whether user requested an eligible year. Initialize to false 
		boolean isEligibleYear = false;
		
		// Outer for-loop through each .txt file in directory to validate year
		for(int i = 0; i < NUMBER_OF_FILES_IN_DIRECTORY; i++) {
			
			// Check if the year the user requested matches part of a name for any files held in the directory.
			// Previously in readNamesIntoArray() method, those substrings (the years) were stored in ArrayList eligibleYears.
			// If not a match, the boolean flag for eligible year will remain false and will be handled later
			if (Integer.toString(eligibleYears.get(i)).equals(year)) {
				
				// Change boolean flag for year to true - users request was an eligible year
				isEligibleYear = true;
				
				// Create boolean flag to indicate whether a name was found in the previously validated year, and 
				// initialize to false as well
				boolean nameFound = false;
				
				// Inner for loop through this .txt file's (ie. this year's) 1000 rows to validate name
				for(int j = 0; j < ROWS_IN_FILE; j++) {
					
					// Check if the name the user requested matches this elements position within the appropriate gender's 
					// multi-dimensional array (ie. the name held in this iterations of the year's row, according to the 
					// requested gender)
					if(name.equals(arrayToSearchIn[i][j])) {
						
						// Name was found, so change boolean flag for name to true as well - users request was eligible name 
						nameFound = true;
						
						// Tell users that name's rank, and in which year it was ranked.
						// The value of rank is printed as j + 1 in print statement because the earliest position in ranking 
						// system is one, but the earliest position in an array is 0. So 1 is added to value of position in 
						// array so that the correct "value" of position in rank is printed.
						System.out.println("\n" + name + " was ranked #" + (j + 1) + " in year " + eligibleYears.get(i));
					}
				}
				
				// The flag for name may not have been changed to true at this point (requested year was invalid).
				// If not, inform user name not found in this .txt document (ie. name not recorded in this year's ranking)
				if(!nameFound) {
					System.out.println("The name " + name + " is not ranked in year " + year);
				}
			}
		}
		// The flag for the year may not have been changed to true at this point, If not instruct user of the years to choose from.
		if(!isEligibleYear) {
			System.out.println();
			System.out.println("Not an eligible year");
			System.out.println("Please enter one of the following...\n");
			
			// Print the years that census data is held in directory for which they can select from.
			// (NOTE: This is under the assumption that the file names match the format of those used previously)
			for(int i = 0; i < eligibleYears.size(); i++) {
				System.out.println(eligibleYears.get(i));
			}
		}
	}
}
