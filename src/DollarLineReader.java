
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;

/**
 * A class that provides a dollar line reader from an input stream.
 */
public class DollarLineReader {
  private static final int DEFAULT_BUFFER_SIZE = 64 * 1024;
  private int bufferSize = DEFAULT_BUFFER_SIZE;
  private InputStream in;
  private byte[] buffer;
  // the number of bytes of real data in the buffer
  private int bufferLength = 0;
  // the current position in the buffer
  private int bufferPosn = 0;

  private static final byte DELIMITER = '$';

  /**
   * Create a line reader that reads from the given stream using the
   * default buffer-size (64k).
   * @param in The input stream
   * @throws IOException
   */
  public DollarLineReader(InputStream in) {
    this(in, DEFAULT_BUFFER_SIZE);
  }

  /**
   * Create a line reader that reads from the given stream using the 
   * given buffer-size.
   * @param in The input stream
   * @param bufferSize Size of the read buffer
   * @throws IOException
   */
  public DollarLineReader(InputStream in, int bufferSize) {
    this.in = in;
    this.bufferSize = bufferSize;
    this.buffer = new byte[this.bufferSize];
  }

  /**
   * Create a line reader that reads from the given stream using the
   * <code>io.file.buffer.size</code> specified in the given
   * <code>Configuration</code>.
   * @param in input stream
   * @param conf configuration
   * @throws IOException
   */
  public DollarLineReader(InputStream in, Configuration conf) throws IOException {
    this(in, conf.getInt("io.file.buffer.size", DEFAULT_BUFFER_SIZE));
  }

  /**
   * Close the underlying stream.
   * @throws IOException
   */
  public void close() throws IOException {
    in.close();
  }
  
  /**
   * Read one line from the InputStream into the given Text.  A line
   * can be terminated by one of the following: '$' (DOLLAR).  EOF also terminates an otherwise unterminated
   * line.
   *
   * @param str the object to store the given line (without newline)
   * @param maxLineLength the maximum number of bytes to store into str;
   *  the rest of the line is silently discarded.
   * @param maxBytesToConsume the maximum number of bytes to consume
   *  in this call.  This is only a hint, because if the line cross
   *  this threshold, we allow it to happen.  It can overshoot
   *  potentially by as much as one buffer length.
   *
   * @return the number of bytes read including the (longest) newline
   * found.
   *
   * @throws IOException if the underlying stream throws
   */
  public int readLine(Text str, int maxLineLength,
                      int maxBytesToConsume) throws IOException {
    /* We're reading data from in, but the head of the stream may be
     * already buffered in buffer, so we have several cases:
     * 1. No newline characters are in the buffer, so we need to copy
     *    everything and read another buffer from the stream.
     * 2. An unambiguously terminated line is in buffer, so we just
     *    copy to str.
     * 3. Ambiguously terminated line is in buffer, i.e. buffer ends
     *    in CR.  In this case we copy everything up to CR to str, but
     *    we also need to see what follows CR: if it's LF, then we
     *    need consume LF as well, so next call to readLine will read
     *    from after that.
     * We use a flag prevCharCR to signal if previous character was CR
     * and, if it happens to be at the end of the buffer, delay
     * consuming it until we have a chance to look at the char that
     * follows.
     */
    str.clear();
    int txtLength = 0; //tracks str.getLength(), as an optimization
    int newlineLength = 0; //length of terminating newline
    long bytesConsumed = 0;
    
    do {
      int startPosn = bufferPosn; //starting from where we left off the last time
      
      if (bufferPosn >= bufferLength) {
        startPosn = bufferPosn = 0;
        
        bufferLength = in.read(buffer);
        if (bufferLength <= 0)
          break; // EOF
      }
      
      for (; bufferPosn < bufferLength; ++bufferPosn) { //search for newline
        if (buffer[bufferPosn] == DELIMITER) {
          newlineLength = 1;
          ++bufferPosn; // at next invocation proceed from following byte
          break;
        }
      }
      
      int readLength = bufferPosn - startPosn;
      
      bytesConsumed += readLength;
      
      int appendLength = readLength - newlineLength;
      
      if (appendLength > maxLineLength - txtLength) {
        appendLength = maxLineLength - txtLength;
      }
      
      if (appendLength > 0) {
        str.append(buffer, startPosn, appendLength);
        txtLength += appendLength;
      }
    } while (newlineLength == 0 && bytesConsumed < maxBytesToConsume);

    if (bytesConsumed > (long)Integer.MAX_VALUE)
      throw new IOException("Too many bytes before newline: " + bytesConsumed);
    
    return (int)bytesConsumed;
  }

  /**
   * Read from the InputStream into the given Text.
   * @param str the object to store the given line
   * @param maxLineLength the maximum number of bytes to store into str.
   * @return the number of bytes read including the newline
   * @throws IOException if the underlying stream throws
   */
  public int readLine(Text str, int maxLineLength) throws IOException {
    return readLine(str, maxLineLength, Integer.MAX_VALUE);
}

  /**
   * Read from the InputStream into the given Text.
   * @param str the object to store the given line
   * @return the number of bytes read including the newline
   * @throws IOException if the underlying stream throws
   */
  public int readLine(Text str) throws IOException {
    return readLine(str, Integer.MAX_VALUE, Integer.MAX_VALUE);
  }
  
  public static void main(String[] args) throws Exception {
	  /*
    FileInputStream in = new FileInputStream(args[0]);
    DollarLineReader reader = new DollarLineReader(in);
    Text str = new Text();
    int i=1;
    int a = 0;
	while( (a = reader.readLine(str)) > 0) {
		System.out.print("Line " + a + " content=" + i + "=");
    	System.out.println(str.toString());
    	++i;
    }
    */
	byte [] bs = new byte[1024*1024];
	for(int j=0; j< bs.length; ++j) {
		bs[j] = 'A';
	}
	byte [] dol = new byte[1];
	dol[0] ='$';
	
	java.io.FileOutputStream o = new java.io.FileOutputStream("/home/cloudera/workspace/testfile/data/input/dollar/a.txt");
	for(int j =0; j < 130; ++j) {
		o.write(bs);
	}
	o.write(dol);	
	for(int j =0; j < 130; ++j) {
		o.write(bs);
	}
	o.close();
	System.out.println("done");
  }

}
