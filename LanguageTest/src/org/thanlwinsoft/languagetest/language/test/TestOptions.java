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
    private TestItemFilter [] filters = new TestItemFilter[0];
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
    public void setFilters(TestItemFilter [] filters)
    {
        this.filters = null;
    }
    public TestItemFilter [] getFilters()
    {
        return filters;
    }
}
