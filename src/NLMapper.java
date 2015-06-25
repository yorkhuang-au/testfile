import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class NLMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	
  private final static IntWritable one = new IntWritable(1);
  private static Text text = new Text();

  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    /*
     * TODO implement
     */
	  
	  String lines = value.toString();
	  String []lineArr = lines.split("\n");
	  int lcount = lineArr.length;
	  context.write( /*new Text( new Integer(lcount).toString())*/ value, new IntWritable(1));


  }
}
