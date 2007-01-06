/**
 * 
 */
package org.thanlwinsoft.languagetest.language.test;

/**
 * Class to hold the various options for a test
 * @author keith
 *
 */
public class TestOptions
{
    protected TestType type = null;
    protected boolean useHistory = true;
    protected boolean repeatUntilLearnt = true;
    protected boolean includeNew = true;
    public TestOptions(TestType type)
    {
        this.type = type;
    }
    public TestOptions(TestType type, boolean useHistory, boolean repeatUntilLearnt)
    {
        this.type = type;
        this.useHistory = useHistory;
        this.repeatUntilLearnt = repeatUntilLearnt;
    }
}
