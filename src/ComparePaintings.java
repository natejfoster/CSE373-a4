import com.sun.javafx.tk.ImageLoader;

/**
 * Nathan Foster, Govind Pillai
 *
 */
public class ComparePaintings {

	private int collisions;
	public ComparePaintings(){
		collisions = 0;
	}; // constructor.
	
	// Load the image, construct the hash table, count the colors.
	ColorHash countColors(String filename, int bitsPerPixel) {
		// Implement this.
		ImageLoader img = new ImageLoader(filename);
		ColorHash table = new ColorHash(3, bitsPerPixel, "Linear Probing", 0.5);
		int width = img.getWidth();
		int height = img.getHeight();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				ResponseItem ri = table.colorHashPut(new ColorKey(i, j, bitsPerPixel));
				collisions += ri.nCollisions;
			}
		}
		return table; // Change this to return the real result.
	}

//	Starting with two hash tables of color counts, compute a measure of similarity based on the cosine distance of two vectors.
	double compare(ColorHash painting1, ColorHash painting2) {
		// Implement this.
		FeatureVector a = new FeatureVector(bitsPerPixel);
		FeatureVector b = new FeatureVector(bitsPerPixel);
		
		a.getTheCounts(painting1);
		b.getTheCounts(painting2);
		return a.cosineSimilarity(b); // Change this to return the actual similarity value.
	}

	//	A basic test for the compare method: S(x,x) should be 1.0, so you should compute the similarity of an image with itself and print out the answer. If it comes out to be 1.0, that is a good sign for your implementation so far.
	void basicTest(String filename) {
		// Implement this.		
	}

	//		Using the three given painting images and a variety of bits-per-pixel values, compute and print out a table of collision counts in the following format:
	void CollisionTests() {
		// Implement this.				
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
	 */
	public static void main(String[] args) {
		ComparePaintings cp = new ComparePaintings();
		cp.imageLoadingTest();
	}

}
