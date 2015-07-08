import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;


public class TextPartitioner extends Partitioner<Text, IntWritable> {

	@Override
	public int getPartition(Text key, IntWritable value, int numPartitions) {
		// TODO Auto-generated method stub
		if(numPartitions <2) {
			return 0;
		}
		
		if(key != null && key.toString().startsWith("a")) {
			return 0;
		}
		return 1;
	}

}
