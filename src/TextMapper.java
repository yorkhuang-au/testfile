import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TextMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	
  private final static IntWritable one = new IntWritable(1);
  private static Text text = new Text();

  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    /*
     * TODO implement
     */
	  
	  StringTokenizer it = new StringTokenizer( value.toString().replaceAll("[^A-Za-z]", " ") );
	  while(it.hasMoreTokens())
	  {
		  text.set(it.nextToken().toLowerCase());
		  if( !text.toString().isEmpty()) {
			  context.getCounter(TestCounter.GOOD).increment(1);
			  context.write(text, one);
		  }
	  }
  }
}
