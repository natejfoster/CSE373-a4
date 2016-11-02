/**
 * @author Nathan Foster & Govind Pillai
 *
 */
public class ColorHashV1 {
	// Implement this class, including whatever data members you need and all
	// of the public methods below.  You may create any number of private methods
	// if you find them to be helpful.
	private ColorKey[] keys; //stores the colorKeys
	private long[] values; // stores the values associated with the colorKeys 
	private String collisionResolutionMethod; // determines which collision resolution to use
	private int tableSize; // stores the current table size
	private double rehashLoadFactor; // stores the specified load factor
	private int elements; //stores the current number of elements in the table
	
	// Creates a new colorHash
	public ColorHashV1(int tableSize, int bitsPerPixel, String collisionResolutionMethod, double rehashLoadFactor){
		this.collisionResolutionMethod = collisionResolutionMethod;
		this.tableSize = tableSize;
		this.rehashLoadFactor = rehashLoadFactor;
		keys = new ColorKey[tableSize];
		values = new long[tableSize];
		elements = 0;
	}
	
	// Adds the key to the table with the given value. If the key already exists in the table
	// the stored value is updated to the passed in value
	public ResponseItem colorHashPut(ColorKey key, long value) throws Exception {
		ResponseItem response = new ResponseItem(value, 0, false, false);
		int initialIndex = key.hashCode() % tableSize;
		if (keys[initialIndex] == key) {
			response.didUpdate = true;
			values[initialIndex] = value;
		} else {
			elements++;
			if (((double) (elements) / tableSize) > rehashLoadFactor) {
				response.didRehash = true;
				resize();
			}
			if (keys[initialIndex] == null) {
				keys[initialIndex] = key;
				values[initialIndex] = value;
			} else {
				int[] update = {initialIndex, 0};
				update = selectResolutionPut(update);
				response.nCollisions = update[1];
				keys[update[0]] = key;
				values[update[0]] = value;
			}
		}
		return response;
	}	
	
	public ResponseItem increment(ColorKey key) {
		ResponseItem response = new ResponseItem(1, 0, false, false);
		int initialIndex = key.hashCode() % tableSize;
		if (keys[initialIndex] == key) {
			response.value = 1 + values[initialIndex];
			response.didUpdate = true;
			values[initialIndex] = 1 + values[initialIndex];
		} else if (true) {
			int[] update = {initialIndex, 0};
			update = selectResolutionGet(update);
			response.nCollisions = update[1];
		} else {
			colorHashPut(key, 1);
		}
		return response;
	}
	
	// Gets the value associated with the passed in key
	public ResponseItem colorHashGet(ColorKey key)  throws Exception{
		if (getCount(key) == 0) {
			throw new Exception("MissingColorKeyException");
		}
		ResponseItem response = new ResponseItem(-1, 0, false, false);
		int initialIndex = key.hashCode() % tableSize;
		if (keys[initialIndex] == key) {
			response.value = values[initialIndex];
		} else {
			int[] update = {initialIndex, 0};
			update = selectResolutionGet(key, update);
		}
		return response;
	}
	
	// If the ColorKey exists in the colorHash, the associated value is returned,
	// otherwise 0 is returned
	public long getCount(ColorKey key) throws Exception {
		long value = 0;
		int initialIndex = key.hashCode() % tableSize;
		if (keys[initialIndex] == key) {
			value = values[initialIndex];
		} else {
			int[] update = {initialIndex, 0};
			value = values[selectResolutionGet(key, update)[0]];
		}
		return value;
	}
	
	// Return the key at the passed in tableIndex
	public ColorKey getKeyAt(int tableIndex){
		return keys[tableIndex];
	}
	
	// Return the value at the passed in tableIndex
	public long getValueAt(int tableIndex){
		return values[tableIndex];
	}
	
	// Returns the current load factor
	public double getLoadFactor(){
		return rehashLoadFactor;
	}
	
	// Returns the current table size
	public int getTableSize(){
		return tableSize;
	}
	
	public void resize(){
		
	}
	
	// Selects the collision resolution method for adding elements to the table. 
	// If the specified method does not exist, an Exception is thrown
	private int[] selectResolutionPut(int[] update) throws Exception {
		if (collisionResolutionMethod.equals("Linear Probing")) {
			return linearProbePut(update);
		} else if (collisionResolutionMethod.equals("Quadratic Probing")) {
			return quadraticProbePut(update);
		} else {
			throw new Exception("CollisionMethodNotFoundException");
		}
	}

	// Selects the collision resolution method for getting elements from the table. 
	// If the specified method does not exist, an Exception is thrown
	private int[] selectResolutionGet(ColorKey key, int[] update) throws Exception {
		if (collisionResolutionMethod.equals("Linear Probing")) {
			return linearProbeGet(key, update);
		} else if (collisionResolutionMethod.equals("Quadratic Probing")) {
			return quadraticProbeGet(key, update);
		} else {
			throw new Exception("CollisionMethodNotFoundException");
		}
	}

	// Performs linear probing for adding elements to the table
	private int[] linearProbePut(int[] update) {
		int initialIndex = update[0];
		int count = 1;
		while (keys[(initialIndex + count)] != null ) {
			count = (count + 1) % tableSize;
		}
		update[1] = count;
		update[0] = initialIndex + count;
		return update;
		
		
	}

	private int[] quadraticProbePut(int[] update) {
	
		return update;
	}
	
	
	// Performs linear probing for the getting the value of the key
	private int[] linearProbeGet(ColorKey key, int[] update) {
		int initialIndex = update[0];
		int count = 1;
		while (keys[(initialIndex + count)] != key && initialIndex + count != initialIndex) {
			count = (count + 1) % tableSize;
		}
		update[1] = count;
		update[0] = initialIndex + count;
		return update;
		
		
	}
	
	private int[] quadraticProbeGet(ColorKey key, int[] update) {
		int initialIndex = update[0];
		int count = 1;
		while (keys[(initialIndex + count)] != key) {
			count = (count * count) % tableSize;
		}
		update[1] = count;
		update[0] = initialIndex + count;
		return update;
	}

}
