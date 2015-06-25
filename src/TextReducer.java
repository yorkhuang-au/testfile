import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TextReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

  @Override
  public void reduce(Text key, Iterable<IntWritable> values, Context context)
      throws IOException, InterruptedException {

    /*
     * TODO implement
     */
	  
	  Iterator<IntWritable> it = values.iterator();
	  int sum =0;
	  while(it.hasNext())
	  {
		  sum += ((IntWritable)it.next()).get();
	  }
	  
	  context.write(key, new IntWritable(sum));

  }
}