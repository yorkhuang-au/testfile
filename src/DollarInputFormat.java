import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 * 
 */

/**
 * @author cloudera
 *
 */
public class DollarInputFormat extends FileInputFormat<LongWritable, Text> {

	@Override
	public RecordReader<LongWritable, Text> createRecordReader(InputSplit split,
<<<<<<< HEAD
			TaskAttemptContext context) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return new DollarLineRecordReader();
=======
			TaskAttemptContext arg1) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		return new DollarLineRecordReader((FileSplit) split);
>>>>>>> 2103227b08dd814467a38bbfe00dd1af7bebc4a4
	}

}
