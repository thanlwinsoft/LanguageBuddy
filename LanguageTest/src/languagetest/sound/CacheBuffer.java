/*
 * CacheBuffer.java
 *
 * Created on February 6, 2004, 4:09 PM
 */

package languagetest.sound;

/** Class to cache data from an AudioFile to save it being reparsed each time
 * @author Keith Stribley
 */
public class CacheBuffer
{
    int bufferLength = 0;
    int startIndex = 0;
    int cachedLength = 0;
    long startOffset = 0;
    byte [] buffer = null;
    /** Creates a new instance of CacheBuffer
     * @param size Cache size in bytes
     */
    public CacheBuffer(int size)
    {
        this.bufferLength = size;
        buffer = new byte[bufferLength];
    }
    /** Reinitialises cache resetting the start position of the cache
     * within the audio file.
     * @param startOffset start byte offset of cache within the audio file
     */    
    public void reinit(long startOffset)
    {
        this.startOffset = startOffset;
        this.cachedLength = 0;
        this.startIndex = 0;
        System.out.println("CacheBuffer init at " + startOffset);
    }
    
    /** Retrieves the position of the start of the cached data within
     * the audio file.
     * @return Byte offset of start of cache within audiofile
     */    
    public long getStartOffset() { return startOffset; }
    /** Retreives the length of data actually held in the cache
     * @return length of data in cache in bytes
     */    
    public long getCachedLength() { return cachedLength; }
    /**
     * Reads data cached from sound file if it is in cache
     * @param readBuffer buffer to write data into
     * @param readStartOffset start byte offset of data in sound file
     * @param length number of bytes to read
     * @return number of bytes read
     */
    public int read(byte [] readBuffer, long readStartOffset, int length)
    {
        int read = 0;
        if (readStartOffset >= startOffset &&
            readStartOffset < startOffset + cachedLength)
        {
            long actualLength = length;
            if (startOffset + cachedLength < readStartOffset + length)
            {
                actualLength = startOffset + cachedLength - readStartOffset;
            }
            for (; read < actualLength; read++)
            {
                readBuffer[read] = getData(readStartOffset + read);
            }
        }
        return read;
    }
    
    /** Writes data into the cache. It is assumed that the data to be
     * written in sequential to the last write. The first byte to be
     * written should be at the position {@link #getWriteOffset()}
     * @param writeBuffer Buffer containing data to write
     * @param length length of data to write
     * @return Number of bytes actually written
     */    
    public int write(byte [] writeBuffer, int length)
    {
        int i;
        int writeIndex = indexFromOffset(startOffset + cachedLength);
        for (i = 0; i < length; i++)
        {
            if (writeIndex >= bufferLength) writeIndex = 0;
            buffer[writeIndex] = writeBuffer[i];    
            writeIndex++;
            
            if (cachedLength == bufferLength)
            {
                startIndex++;
                startOffset++;
                if (startIndex >= bufferLength) startIndex = 0;
            }
            else
            {
                cachedLength++;
            }
        }
        return i;
    }
    
    /** Retrieves a byte of data from cache.
     * @param offset Offset in file
     * @return data at offset
     */    
    protected byte getData(long offset)
    {
        return buffer[indexFromOffset(offset)];
    }
    
    /** Converts an offset in the audiofile into the index of the byte in the cache.
     * @param offset Byte offset in audiofile
     * @return index in cache byte array
     */    
    protected int indexFromOffset(long offset)
    {
        int bufferIndex = (int)(offset - startOffset + startIndex);
        if (bufferIndex >= bufferLength) bufferIndex -= bufferLength;
        return bufferIndex;
    }
    
    /** Retrieves the byte offset in the audiofile of the end of the data cache. This
     * is the index that should be used for the next write operation.
     * @return Byte offset for next write
     */    
    public long getWriteOffset()
    {
        return startOffset + cachedLength;
    }
    
    /** Test whether the specified offset in the audio file is held in the cache.
     * @param offset Offset in audiofile
     * @return true if offset is cached.
     */    
    public boolean isCached(long offset)
    {
        boolean cached = false;
        if (offset < startOffset) cached = false;
        else  if (offset < startOffset + cachedLength) 
        {
            cached = true;
        }
        return cached;
    }
    /** Frees the cache buffer. The cache cannot be used after clear has been
     * called. */    
    public void clear()
    {
        buffer = null;
        bufferLength = 0;
        startIndex = 0;
        cachedLength = 0;
        startOffset = 0;
    }
}
