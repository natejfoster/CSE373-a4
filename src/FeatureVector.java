/**
 * Nathan Foster, Govind Pillai
 *
 */

import java.lang.Math;
public class FeatureVector {

	/**
	 * FeatureVector is a class for storing the results of
	 * counting the occurrences of colors.
	 * <p>
	 * Unlike the hash table, where the information can be
	 * almost anyplace with the array(s) (due to hashing), in the FeatureVector,
	 * the colors are in their numerical order and each count
	 * is in the array position for its color.
	 * <p>
	 * Besides storing the information, the class provides methods
	 * for getting the information (getTheCounts) and for computing
	 * the similarity between two vectors (cosineSimilarity).
	 */
	long[] colorCounts;
	int bitsPerPixel;
	int keySpaceSize;

	/**
	 * Constructor of a FeatureVector.
	 * 
	 * This creates a FeatureVector instance with an array of the
	 * proper size to hold a count for every possible element in the key space.
	 * 
	 * @param bpp	Bits per pixel. This controls the size of the vector.
	 * 				The keySpace Size is 2^k where k is bpp.
	 * 
	 */
	public FeatureVector(int bpp) {
		bitsPerPixel = bpp;
		keySpaceSize = 1 << bpp;
		colorCounts = new long[keySpaceSize];
	}

	public void getTheCounts(ColorHash ch) {
		// You should implement this method.
		// It will go through all possible key values in order,
		// get the count from the hash table and put it into this feature vector.
		for (int i = 0; i < colorCounts.length; i++) {
			ColorKey key = new ColorKey(i, bitsPerPixel);
			ResponseItem ri = ch.colorHashGet(key);
			colorCounts[i] = ri.value;
		}
	}
	public double cosineSimilarity(FeatureVector other) {
		// Implement this method. Use the formula given in the A3 spec,
		// which is also explained at
		// https://en.wikipedia.org/wiki/Cosine_similarity
		// where A is this feature vector and B is the other feature vector.
		// When multiplying in the dot product, convert all the long values
		// to double before doing the multiplication.

		// Hint: you may wish to write some private methods here to help
		// computing the cosine similarity.  For example, it could be
		// nice to have a dot product method and a vector magnitude method.
		double dotProduct = getDotProduct(other.colorCounts);
		double magnitudeProduct = this.getMagnitude() * other.getMagnitude();

		return dotProduct / magnitudeProduct; // Change this to return the actual value.
	}
	
	private double getDotProduct(long[] b) {
		double sum = 0.0;
		for (int i = 0; i < colorCounts.length; i++) {
			sum += (double)(colorCounts[i]) * (double)(b[i]);
		}
		return sum;
	}
	
	private double getMagnitude() {
		double sumSquares = 0.0;
		for (int i = 0; i < colorCounts.length; i++) {
			sumSquares += (double)(colorCounts[i] * colorCounts[i]);
		}
		return Math.sqrt(sumSquares);
	}

	/**
	 * Optional main method for your own tests of these methods.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
