/*
 * NullStripOutputStream.java
 *
 * Created on February 14, 2004, 4:29 PM
 */

package languagetest.sound;

/**
 *
 * @author  keith
 */
public class NullStripOutputStream extends java.io.BufferedOutputStream
{
    private boolean isStart = true;
    /** Creates a new instance of NullStripOutputStream */
    public NullStripOutputStream(java.io.OutputStream out)
    {
        super(out);
        
    }
    /** checks initial bytes for nulls and refuses to write them
     * this gets around a bug in the mp3 writing
     */
    public void write(byte[] b, int off, int len) throws java.io.IOException
    {
        if (isStart)
        {
            int i = 0;
            for (i = 0; i<len; i++)
            {
                if (b[off + i] != 0) break;
            }
            if (i < len)
            {
                super.write(b,off + i, len - i);
                isStart = false;
            }
        }
        else super.write(b,off,len);
    }
    
}
