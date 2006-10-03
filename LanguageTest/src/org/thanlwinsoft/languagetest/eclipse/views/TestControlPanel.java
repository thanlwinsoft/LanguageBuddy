/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.thanlwinsoft.eclipse.widgets.SoundPlayer;

/**
 * @author keith
 *
 */
public class TestControlPanel extends Composite
{
    private SoundPlayer player = null;
    public TestControlPanel(Composite parent, int style)
    {
        super(parent, style);
        RowLayout layout = new RowLayout();
        layout.type = SWT.VERTICAL;
        setLayout(layout);
        player = new SoundPlayer(this);
    }
    public SoundPlayer player()
    {
        return player;
    }
}
