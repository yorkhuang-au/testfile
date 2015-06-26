import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CDataReducer extends Reducer<CKey, CData, CKey, CData> {

  @Override
  public void reduce(CKey key, Iterable<CData> values, Context context)
      throws IOException, InterruptedException {

    /*
     * TODO implement
     */
	  
	  Iterator<CData> it = values.iterator();
	  int sum =0;
	  String s="";
	  while(it.hasNext())
	  {
		  //sum += ((CData) it.next()).sentenceLen;
//		  s = s+"," + Integer.toString(((CData) it.next()).sentenceLen );
		  context.write(key, ((CData)it.next()) );
	  }
	  
//	  context.write(key, new Text(s ) );

  }
}