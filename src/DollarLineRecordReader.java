import java.io.IOException;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * 
 */

/**
 * @author cloudera
 *
 */
public class DollarLineRecordReader extends RecordReader<LongWritable, Text> {
	private static final Log LOG = LogFactory.getLog(DollarLineRecordReader.class);

//	private CompressionCodecFactory compressionCodecs = null;
	private long start;
	private long pos;
	private long end;
	private DollarLineReader in;
	private int maxLineLength;
	private LongWritable key = null;
	private Text value = null;	

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		if( in != null) {
			in.close();
		}
	}

	@Override
	public LongWritable getCurrentKey() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return key;
	}

	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		if( start == end ) { 
			return 0.0f;
		} else {
			return Math.min(1.0f,  (pos - start) / (float)(end - start) );
		}
	}

	@Override
	public void initialize(InputSplit genericSplit, TaskAttemptContext context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		FileSplit split = (FileSplit) genericSplit;
		Configuration job = context.getConfiguration();
		this.maxLineLength = job.getInt("mapreduce.dollarlinereader.maxlength",  Integer.MAX_VALUE);
		
		start = split.getStart();
		end = start + split.getLength();
		final Path file = split.getPath();
		FileSystem fs = file.getFileSystem(job);
		
		FSDataInputStream fileIn = fs.open(file);
		boolean skipFirstLine = false;
		
		if( start != 0) {
			skipFirstLine = true;
			--start;
			fileIn.seek(start);
		}
		
		in = new DollarLineReader(fileIn, job);
		
		if( skipFirstLine) {
			start += in.readLine(new Text(), 0, (int) Math.min((long)Integer.MAX_VALUE, end - start));
		}
		this.pos = start;
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

}
