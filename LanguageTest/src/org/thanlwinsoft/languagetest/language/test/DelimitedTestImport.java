/*
 * AsciiTestImport.java
 *
 * Created on March 18, 2004, 11:50 AM
 */

package org.thanlwinsoft.languagetest.language.test;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.StringTokenizer;
/**
 *
 * @author  keith
 * 
 */
public class DelimitedTestImport
{
    private final String FIELD_REGEXP = "\"([^\"]*)\"";
    private final String DELIMIT_REGEXP = "[,\t]";
    private final String [] LIMITERS = {",","\t"};
    private final String MAIN_REGEXP = 
        FIELD_REGEXP + DELIMIT_REGEXP + FIELD_REGEXP + 
        "(" + DELIMIT_REGEXP + FIELD_REGEXP + ")?";
    private final String DOUBLE_QUOTE = "\"\"";
    private final String SINGLE_QUOTE = "\"";
    private final char COMMENT_PREFIX = '#';
    // group 0 is the whole expression
    private final int NATIVE_GROUP = 1;
    private final int FOREIGN_GROUP = 2;
    private final int THIRD_FIELD_GROUP = 3;
    private final int SOUND_GROUP = 4;
    private File asciiFile = null;
    private TestModule module = null;
    /** Creates a new instance of DelimitedTestImport */
    public DelimitedTestImport(File asciiFile, TestModule mod)
    {
        this.asciiFile = asciiFile;
        this.module = mod;
        
    }
    
    public boolean parse()
    {
        Pattern pattern = Pattern.compile(DOUBLE_QUOTE);
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(asciiFile));
            String line = reader.readLine();
            int col = 0;
            int tokenStart = 0; // first index to check for double quotes
            final boolean STATE_COL_START = false;
            final boolean STATE_QUOTED_COL = true;
            boolean state = STATE_COL_START;
            String [] cols = new String[4];
            while (line != null)
            {
                col = 0;
                cols[0] = "";
                cols[1] = "";
                cols[2] = "";
                cols[3] = "";
                // ignore empty lines & those starting with a comment char
                if (line.length() == 0 ||
                    line.charAt(0) == COMMENT_PREFIX) 
                {
                    line = reader.readLine();
                    continue;
                }
                StringTokenizer st = null;
                int limiterIndex = 0;
                String delimiter = null;
                // choose first delimiter that has at least 2 tokens
                // in rare cases this might cause the wrong delimiter to
                // be chosen
                do 
                {   
                    delimiter = LIMITERS[limiterIndex];
                    st = new StringTokenizer(line,delimiter);
                    if (st.countTokens() > 1) break;
                } while (++limiterIndex < LIMITERS.length);
                boolean isQuoted = false;
                while (st.hasMoreTokens())
                {
                    String token = st.nextToken();
                    if (state == STATE_COL_START)
                    {
                        if (token.charAt(0) == '"') 
                        {
                            isQuoted = true;
                            tokenStart = 1;
                        }
                        else
                        {
                            isQuoted = false;
                        }
                    }
                    else
                    {
                        tokenStart = 0;
                    }
                    if (isQuoted)
                    {
                        // check end character is a token
                        // count number of quotes at end of string
                        int checkIndex = token.length() - 1;
                        int quoteCount = 0;
                        while ((checkIndex >= tokenStart) && 
                               (token.charAt(checkIndex) == '"'))
                        {
                            quoteCount++;
                            checkIndex--;
                        }
                        // even number of quotes this is not a true end
                        if (quoteCount % 2 == 0)  
                        {
                            // its double quoted so this is a false token!
                            cols[col] += 
                                token.substring(tokenStart) + delimiter;
                            state = STATE_QUOTED_COL;
                        }
                        else
                        {
                            // end of column
                            cols[col] += token.substring(tokenStart,
                                token.length() - 1);
                            // col finished, so now remove double quotes
                            cols[col] = pattern.matcher(cols[col])
                                .replaceAll(SINGLE_QUOTE);
                            state = STATE_COL_START;                            
                            col++;
                        }
                    }                        
                    else // not quoted, so its a complete column entry
                    {
                        cols[col] = token;
                        col++;
                        state = STATE_COL_START;
                    }
                    
                }
                TestItem item = null;
                if (col > 1)
                {
                    item = new TestItem(module);
                    item.setNative(cols[0]);
                    item.setForeign(cols[1]);
                    if (col > 2)
                    {
                        item.setSound(cols[2]);
                        if (col > 3)
                        {
                            item.setPicture(cols[3]);
                        }
                    }
                    module.insertTestItem(item);
                }
                
                line = reader.readLine();
                
            }
            reader.close();
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }
    
    /**
     *  This might be useful for other file formats, but it is difficult to
     * cope with quoted quotes
     */
    public boolean regExpParse()
    {
        Pattern pattern = Pattern.compile(MAIN_REGEXP);
        Matcher matcher = null;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(asciiFile));
            String line = reader.readLine();
            while (line != null)
            {
                matcher = pattern.matcher(line);
                if (matcher.matches() &&
                    matcher.group(NATIVE_GROUP) != null &&
                    matcher.group(FOREIGN_GROUP) != null)
                {
                    TestItem item = new TestItem(module);
                    item.setNative(matcher.group(NATIVE_GROUP));
                    item.setForeign(matcher.group(FOREIGN_GROUP));
                    if (matcher.group(THIRD_FIELD_GROUP) != null)
                    {
                        item.setSound(matcher.group(SOUND_GROUP));
                    }
                    module.insertTestItem(item);
                }
                line = reader.readLine();
            }
            reader.close();
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }
}
