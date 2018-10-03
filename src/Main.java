import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) {
		
		BabyNames bn = new BabyNames();
		
		try {
			bn.readNamesIntoArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bn.promptUser();
		bn.findRank();
	}
}