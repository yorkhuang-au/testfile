import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class CKey implements WritableComparable {
	public String word1;
	public String word2;
	
	public CKey(String w1, String w2) {
		word1 = w1;
		word2 = w2;
	}
	public CKey() {
		word1 = "";
		word2 = "";
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		word1 = in.readUTF();
		word2 = in.readUTF();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeUTF(word1);
		out.writeUTF(word2);
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		
		if( !(o instanceof CKey))
			return -1;
				
		CKey other = (CKey) o;
		
		int r = word1.compareTo(other.word1);
		if( r ==0 ) {
			r = word2.compareTo(other.word2);
		}
		
		return r;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if( !(obj instanceof CKey))
			return false;
		
		CKey other = (CKey) obj;
		
		return word1.equals( other.word1) && word2.equals(other.word2);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return word1 + "->" + word2;
	}
	
}
