/*
 *  Copyright (C) 2004 Keith Stribley <tech@thanlwinsoft.org>
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 *  MA 02110-1301 USA
 */

package org.thanlwinsoft.languagetest.sound;

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
