import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class TextDriver {

	public static void main(String[] args) throws Exception {

		/*
		 * Validate that two arguments were passed from the command line.
		 */
		Configuration conf = new Configuration();
	    GenericOptionsParser optionParser = new GenericOptionsParser(conf, args);
	    
	    String[] remainingArgs = optionParser.getRemainingArgs();
	    if (remainingArgs.length != 2 ) {
	    	System.err.println("Usage: StubDriver <in> <out> [-skip skipPatternFile]");
			System.exit(2);
	    }	    
		/*
		 * Instantiate a Job object for your job's configuration.
		 */
	    Job job = Job.getInstance(conf, "Stub Driver");

		/*
		 * Specify the jar file that contains your driver, mapper, and reducer.
		 * Hadoop will transfer this jar file to nodes in your cluster running
		 * mapper and reducer tasks.
		 */
		job.setJarByClass(TextDriver.class);

		
		/*
		 * TODO implement
		 */
		job.setMapperClass(TextMapper.class);
		job.setReducerClass(TextReducer.class);
		job.setCombinerClass(TextReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		job.setPartitionerClass(TextPartitioner.class);
		job.setNumReduceTasks(4);
	
		FileInputFormat.addInputPath(job, new Path(remainingArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(remainingArgs[1]));
		
		job.setInputFormatClass(TextInputFormat.class);

		/*
		 * Start the MapReduce job and wait for it to finish. If it finishes
		 * successfully, return 0. If not, return 1.
		 */
		boolean success = job.waitForCompletion(true);
		
		System.out.println( job.getCounters().findCounter(TestCounter.GOOD).getName() + " = " + job.getCounters().findCounter(TestCounter.GOOD).getValue());
		System.exit(success ? 0 : 1);
		
	}
}
