import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CDataMapper extends Mapper<LongWritable, Text, CKey, CData> {
	
  private final static IntWritable one = new IntWritable(1);
  private static Text text = new Text();

  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    /*
     * TODO implement
     */
	  
	  StringTokenizer it = new StringTokenizer( value.toString());
	  while(it.hasMoreTokens())
	  {
		  text.set(it.nextToken().replaceAll("[^A-Za-z]", ""));
//		  if( !text.toString().isEmpty())
//			  context.write( text, new CData(text.toString(), value.toString().length() ) );
//			  context.write( text, one );
		  context.write(new CKey(text.toString(), text.toString().toUpperCase()), new CData(text.toString(), value.toString().length() ));
		  
	  }


  }
}
