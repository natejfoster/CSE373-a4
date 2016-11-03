/**
 * Nathan Foster, Govind Pillai
 *
 */

public class ComparePaintings {

	private int collisions;
	private String resolution;
	/* Constructor, where collisions and the collision resolution method are initialized. */
	public ComparePaintings(){
		collisions = 0;
		resolution = "Linear Probing";
	}
	
	/* Method for setting the collision resolution method. */
	void setResolution(String r) {
		resolution = r;
	}
	// Load the image, construct the hash table, count the colors.
	ColorHash countColors(String filename, int bitsPerPixel) {
		collisions = 0;
		ImageLoader img = new ImageLoader(filename); // Loads the image
		ColorHash table = new ColorHash(3, bitsPerPixel, this.resolution, 0.5);
		int width = img.getWidth();
		int height = img.getHeight();
		// Adds each pixel from the loaded image to the hash table and keeps track
		// of the collisions.
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				ResponseItem ri = table.increment(img.getColorKey(i, j, bitsPerPixel));
				collisions += ri.nCollisions;
			}
		}
		return table;
	}

//	Starting with two hash tables of color counts, compute a measure of similarity based on the cosine distance of two vectors.
	double compare(ColorHash painting1, ColorHash painting2) throws Exception {
		FeatureVector a = new FeatureVector(painting1.bitsPerPixel);
		FeatureVector b = new FeatureVector(painting2.bitsPerPixel);
		a.getTheCounts(painting1);
		b.getTheCounts(painting2);
		return a.cosineSimilarity(b);
	}

	//	A basic test for the compare method: S(x,x) should be 1.0, so you should compute the similarity of an image with itself and print out the answer. If it comes out to be 1.0, that is a good sign for your implementation so far.
	void basicTest(String filename) throws Exception {	
		ColorHash table1 = countColors(filename, 3);
		System.out.println(compare(table1, table1));
	}

	// Using the three given painting images and a variety of bits-per-pixel values, compute and print out a table of collision counts in the following format:
	void CollisionTests() {
		Object[][] table = new Object[9][7];
		table[0] = new String[] {"Bits Per Pixel", "C(Mona,linear)", "C(Mona,quadratic)", "C(Starry,linear)", "C(Starry,quadratic)", "C(Christina,linear)", "C(Christina,quadratic)"};
		int bpp = 24;
		int column = 0;
		// Builds the table
		for (int i = 1; i <= 8; i++) {
			table[i] = new Integer[7];
			table[i][column] = bpp;
			bpp -= 3;
		}
		// Adds the number of collisions to each table element depending on the image and the collision resolution method.
		column++;
		setResolution("Linear Probing");
		completeCollisionColumn(table, column, "MonaLisa.jpg");
		column++;
		setResolution("Quadratic Probing");
		completeCollisionColumn(table, column, "MonaLisa.jpg");
		column++;
		setResolution("Linear Probing");
		completeCollisionColumn(table, column, "StarryNight.jpg");
		column++;
		setResolution("Quadratic Probing");
		completeCollisionColumn(table, column, "StarryNight.jpg");
		column++;
		setResolution("Linear Probing");
		completeCollisionColumn(table, column, "ChristinasWorld.jpg");
		column++;
		setResolution("Quadratic Probing");
		completeCollisionColumn(table, column, "ChristinasWorld.jpg");
		// Formats table.
		for (Object[] row : table) {
			System.out.format("%25s%25s%25s%25s%25s%25s%25s\n", row);
		}
		
	}
	
	// Creates a table of the different similarity values between different combinations of images.
	void fullSimilarityTests() throws Exception {
		Object[][] table = new Object[9][4];
		table[0] = new String[] {"Bits Per Pixel", "S(Mona,Starry)", "S(Mona,Christina)", "S(Starry,Christina)"};
		int bpp = 24;
		int column = 0;
		// Builds the table.
		for (int i = 1; i <= 8; i++) {
			table[i] = new Object[4];
			table[i][column] = bpp;
			bpp -= 3;
		}
		// Using Quadratic Probing, computes the similarity value and adds it to each table element.
		setResolution("Quadratic Probing");
		column++;
		completeSimilarityColumn(table, column, "MonaLisa.jpg", "StarryNight.jpg");
		column++;
		completeSimilarityColumn(table, column, "MonaLisa.jpg", "ChristinasWorld.jpg");
		column++;
		completeSimilarityColumn(table, column, "StarryNight.jpg", "ChristinasWorld.jpg");
		// Formats table.
		for (Object[] row : table) {
			System.out.format("%30s%30s%30s%30s\n", row);
		}
	}
	
	// Private helper method to complete each column of the Similarity Table.
	private void completeSimilarityColumn(Object[][] table, int column, String filename1, String filename2) throws Exception {
		int bpp = 24;
		for (int i = 1; i <= 8; i++) {
			ColorHash table1 = countColors(filename1, bpp);
			ColorHash table2 = countColors(filename2, bpp);
			table[i][column] = compare(table1, table2);
			bpp -= 3;
		}
	}
	
	// Private helper method to complete each column of the Collision Table.
	private void completeCollisionColumn(Object[][] table, int column, String filename) {
		int bpp = 24;
			for (int i = 1; i <= 8; i++) {
				countColors(filename, bpp);
				table[i][column] = this.collisions;
				bpp -= 3;
			}
	 }
		
// This simply checks that the images can be loaded, so you don't have 
// an issue with missing files or bad paths.
	void imageLoadingTest() {
		ImageLoader mona = new ImageLoader("MonaLisa.jpg");
		ImageLoader starry = new ImageLoader("StarryNight.jpg");
		ImageLoader christina = new ImageLoader("ChristinasWorld.jpg");
		System.out.println("It looks like we have successfully loaded all three test images.");
	}
	/**
	 * This is a basic testing function, and can be changed.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ComparePaintings cp = new ComparePaintings();
		cp.imageLoadingTest();
		cp.basicTest("ChristinasWorld.jpg");
		cp.CollisionTests();
		cp.fullSimilarityTests();
	}

}
