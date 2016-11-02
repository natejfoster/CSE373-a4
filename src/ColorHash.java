/**
 * Nathan Foster, Govind Pillai
 *
 */
public class ColorHash {
	// Implement this class, including whatever data members you need and all
	// of the public methods below.  You may create any number of private methods
	// if you find them to be helpful.
	private ColorKey[] keys;
	private long[] values;
	private String resolution;
	private int tableSize;
	private double loadFactor;
	private int elements;
	
	public ColorHash(int tableSize, int bitsPerPixel, String collisionResolutionMethod, double rehashLoadFactor){
		this.tableSize = tableSize;
		this.resolution = collisionResolutionMethod;
		this.loadFactor = rehashLoadFactor;
		keys = new ColorKey[tableSize];
		values = new long[tableSize];
		elements = 0;
	}
	
	/* This method will do a linear probe of the table. It will either look for the given key
	 * or will look for an empty space.
	 */
	private int[] linearProbe(ColorKey key, int index) {
		int newIndex = index;
		int collisions = 0;
		int count = 1;
		while (keys[newIndex] != key) {
			count++;
			collisions++;
			if (newIndex == keys.length) {
				newIndex = 0;
			} else {
				newIndex++;
				if (count == tableSize) {
					return null;
				}
			}
		}
		return new int[]{newIndex, collisions};
	}
	
	/* This method will do a quadratic probe of the table. It will either look for the specified
	 * key or it will look for an empty space in the table.
	 */
	private int[] quadraticProbe(ColorKey key, int index) {
		int newIndex = index;
		int collisions = 0;
		int count = 0;
		while (keys[newIndex] != key) {
			if (key != null) {
				newIndex = key.hashCode() % (tableSize - 1);
			}
			collisions++;
			count++;
			System.out.println("Original newIndex = " + newIndex);
			newIndex += (count * count);
			System.out.println("count = " + count);
			newIndex = newIndex % (tableSize - 1);
			if (count == tableSize) {
				return null;
			}
		}
		return new int[]{newIndex, collisions};
	}
	
	/* This method does a check of the key passed in and whether it is in the table 
	 * or not. It will either call a linear or quadratic probe based on what is specified.
	 */
	private int[] check(ColorKey key, int index) {
		if (keys[index] != key) {
			if (resolution.equals("Linear Probing")) {
				return linearProbe(key, index);
			} else if (resolution.equals("Quadratic Probing")) {
				return quadraticProbe(key, index);
			}
		}
		return new int[]{index, 0};
	}
	
	/* This method puts a specified key and value into the table. */
	public ResponseItem colorHashPut(ColorKey key, long value) {
		boolean didUpdate = false;
		boolean didRehash = false;
		int collisions = 0;
		if ((double)((elements + 1) / tableSize) >= loadFactor) {
			collisions += resize();
			didRehash = true;
		}
		int[] array;
		int index = key.hashCode() % tableSize;
		if (getCount(key) > 0) {
			didUpdate = true;
			array = check(key, index);
		} else {
			array = check(null, index);
		}
		index = array[0];
		keys[index] = key;
		values[index] = value;
		elements++;
		collisions += array[1];
		return new ResponseItem(value, collisions, didRehash, didUpdate);
	}
	
	/* This method increments the value of a specified key. */
	public ResponseItem increment(ColorKey key){
		if (getCount(key) == 0) {
			return colorHashPut(key, 1);
		}
		int[] array = check(key, key.hashCode() % tableSize);
		values[array[0]] = values[array[0]] + 1;
		return new ResponseItem(values[array[0]], array[1], false, true);
	}
	
	/* This method gets a specified key from the table. */
	public ResponseItem colorHashGet(ColorKey key) throws MissingColorKeyException{
		if (getCount(key) == 0) {
			try {
				throw new MissingColorKeyException("MissingColorKeyException");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int[] array = check(key, key.hashCode() % tableSize);
		return new ResponseItem(values[array[0]], array[1], false, false);
	}
	
	/* This method gets the count of the specified key. */ 
	public long getCount(ColorKey key){
		int[] array = check(key, key.hashCode() % tableSize);
		if (array == null) {
			return 0;
		}
		return values[array[0]];
	}
	/* This method returns the key from a specific index. */
	public ColorKey getKeyAt(int tableIndex) {
		return keys[tableIndex];
	}
	/* This method returns the value from a specific index. */
	public long getValueAt(int tableIndex) {
		return values[tableIndex];
	}
	
	/* This method returns the load factor of the table. */
	public double getLoadFactor() {
		return loadFactor;
	}
	
	/* This method returns the table size. */
	public int getTableSize() {
		return tableSize;
	}
	
	/* This method does a rehash of the entire table. */
	public int resize() {
		int newSize = tableSize * 2;
		while (!(IsPrime.isPrime(newSize))) {
			newSize++;
		}
		ColorKey[] tempKeys = keys;
		long[] tempValues = values;
		ColorKey[] newKeys = new ColorKey[newSize];
		long[] newValues = new long[newSize];
		int collisions = 0;
		for (int i = 0; i < tempKeys.length; i++) {
			if (tempKeys[i] != null) {
				int[] array = check(tempKeys[i], tempKeys[i].hashCode() % tableSize);
				int index = array[0];
				newKeys[index] = tempKeys[i];
				newValues[index] = tempValues[i];
				collisions += array[1];
			}
		}
		keys = newKeys;
		values = newValues;
		tableSize = newSize;
		for (int i = 0; i < keys.length; i++) {
			System.out.println(keys[i]);
			System.out.println(values[i]);
		}
		
		return collisions;
	}
	
	


}
