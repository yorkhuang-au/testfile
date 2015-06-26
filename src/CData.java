import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;


public class CData implements Writable {
	public String word;
	public int sentenceLen;
	
	public CData(String w, int len) {
		word = w;
		sentenceLen = len;
	}
	public CData() {
		word = "";
		sentenceLen = 0;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		word = in.readUTF();
		sentenceLen = in.readInt();

	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeUTF(word);
		out.writeInt(sentenceLen);

	}
	@Override
	public String toString() {
		return word + "-" + Integer.toString(sentenceLen);
	}

}
