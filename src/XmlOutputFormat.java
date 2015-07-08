import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 
 */

/**
 * @author cloudera
 * @param <V>
 * @param <K>
 *
 */
public class XmlOutputFormat<V, K> extends FileOutputFormat<K, V> {
	protected static class XmlRecordWriter<K, V> extends RecordWriter<K,V> {
		private static final String utf8 = "UTF-8";
		private static final byte [] rootBeginTag;
		private static final byte [] rootEndTag;
		static {
			try {
				rootBeginTag = "<results>\n".getBytes(utf8);
				rootEndTag = "</results>\n".getBytes(utf8);
			} catch (UnsupportedEncodingException uee) {
				throw new IllegalArgumentException("can't find " + utf8 + " encoding");
			}
		}
		
		protected DataOutputStream out;
		
		public XmlRecordWriter(DataOutputStream out) throws IOException{
			this.out = out;
			out.write(rootBeginTag);
		}
		
		private void writeObject(Object o) throws IOException {
			if( o instanceof Text) {
				Text to = (Text) o;
				out.write(to.getBytes(), 0, to.getLength());
			} else {
				out.write(o.toString().getBytes(utf8));
			}
		}
		private void writeKey(Object o, boolean closing) throws IOException {
			out.writeBytes("<");
			if( closing) {
				out.writeBytes("/");
			}
			writeObject(o);
			out.writeBytes(">");
			if( closing) {
				out.writeBytes("\n");
			}
		}
		
		@Override
		public void write(K key, V value) throws IOException {
			boolean nullKey = key == null || key instanceof NullWritable;
			boolean nullValue = value == null || value instanceof NullWritable;
			
			if(nullKey && nullValue) {
				return;
			}
			Object keyObj = key;
			if(nullKey) {
				keyObj = "value";
			}
			writeKey(keyObj, false);
			
			if( !nullValue) {
				writeObject(value);
			}
			writeKey(keyObj, true);
		}
	
		@Override
		public void close(TaskAttemptContext context) throws IOException,
				InterruptedException {
			// TODO Auto-generated method stub
			out.close();
		}
	} // end class XmlRecordWriter

	@Override
	public RecordWriter<K, V> getRecordWriter(TaskAttemptContext job)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		Configuration conf = job.getConfiguration();
		
		String extension = "";
		Path file = getDefaultWorkFile(job, extension);
		FileSystem fs = file.getFileSystem(conf);
		FSDataOutputStream fileOut = fs.create(file, false);
		return new XmlRecordWriter<K,V>( fileOut);
		
	}

}
