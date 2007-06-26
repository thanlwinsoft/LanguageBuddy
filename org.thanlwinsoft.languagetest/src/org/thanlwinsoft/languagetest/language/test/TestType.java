/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/language/test/TestType.java $
 *  Revision        $LastChangedRevision: 852 $
 *  Last Modified:  $LastChangedDate: 2007-06-09 16:02:23 +0700 (Sat, 09 Jun 2007) $
 *  Last Change by: $LastChangedBy: keith $
 * -----------------------------------------------------------------------
 *  Copyright (C) 2003 Keith Stribley <devel@thanlwinsoft.org>
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
 * -----------------------------------------------------------------------
 */

package org.thanlwinsoft.languagetest.language.test;

import org.thanlwinsoft.languagetest.MessageUtil;

/**
 *
 * @author  keith
 */
public class TestType
{
    private int id = -1;
    private String name = "";
    private String code = "";
    /** Creates a new instance of TestType */
    private TestType(String name, int id, String code)
    {
        this.name = name;
        this.id = id;
        this.code = code;
    }
    public static final int LISTENING_FOREIGN_NATIVE_ID = 0;
    public static final int READING_FOREIGN_NATIVE_ID = 1;
    public static final int READING_NATIVE_FOREIGN_ID = 2;
    public static final int FLIP_CARD_ID = 3; // dummy type not counted in NUM_TEST_TYPES
    public static final int NUM_TEST_TYPES = 3;
    public static final String ARROW = " -> ";
    public int getId() { return id; }
    public String description() 
    { 
        switch (id)
        {
        case LISTENING_FOREIGN_NATIVE_ID:
            return MessageUtil.getString("Listening");
        case READING_FOREIGN_NATIVE_ID:
            return MessageUtil.getString("Reading");
        case READING_NATIVE_FOREIGN_ID:
            return MessageUtil.getString("SpeakingWriting");
        case FLIP_CARD_ID:
            return MessageUtil.getString("FlipCards");
        }
//        StringBuffer description = new StringBuffer(prefix);
//        if (LanguageConfig.getCurrent() != null)
//        {
//            // name is dynamic based on current language
//            if (id == READING_NATIVE_FOREIGN_ID)
//            {
//               description.append(LanguageConfig.getCurrent().getNativeLanguage());
//               description.append(ARROW);
//               description.append(LanguageConfig.getCurrent().getForeignLanguage());
//            }
//            else if (id == FLIP_CARD_ID)
//            {
//               description.append(LanguageConfig.getCurrent().getNativeLanguage());
//               description.append(" + ");
//               description.append(LanguageConfig.getCurrent().getForeignLanguage());
//            }
//            else
//            {
//                description.append(LanguageConfig.getCurrent().getForeignLanguage());
//                description.append(ARROW);
//                description.append(LanguageConfig.getCurrent().getNativeLanguage());
//            }
//        }
//        return description.toString();
        return name;
    }
    public String toString()
    {
        return new String(name);
    }
    public String getCode()
    {
        return new String(code);
    }
    public static final TestType LISTENING_FOREIGN_NATIVE = 
        new TestType("Listening",
                      LISTENING_FOREIGN_NATIVE_ID, 
                      "FL");
    public static final TestType READING_FOREIGN_NATIVE = 
        new TestType("Reading",
                     READING_FOREIGN_NATIVE_ID,
                     "FR");
    public static final TestType READING_NATIVE_FOREIGN = 
        new TestType("Speaking / Writing",
                     READING_NATIVE_FOREIGN_ID,
                     "NR");
    public static final TestType FLIP_CARD = 
        new TestType("Flip Cards",
                     FLIP_CARD_ID,
                     "FC");
    /** method to get type from ID. Returns null if ID invalid */
    public static TestType getById(int theId)
    {
        TestType type = null;
        switch (theId)
        {
            case LISTENING_FOREIGN_NATIVE_ID:
                type = LISTENING_FOREIGN_NATIVE;
                break;
            case READING_FOREIGN_NATIVE_ID:
                type = READING_FOREIGN_NATIVE;
                break;
            case READING_NATIVE_FOREIGN_ID:
                type = READING_NATIVE_FOREIGN;
                break;
            case FLIP_CARD_ID:
                type = FLIP_CARD;
                break;
            default:
                type = null;
        }
        return type;        
    }
}
