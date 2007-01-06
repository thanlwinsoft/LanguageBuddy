/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.thanlwinsoft.eclipse.widgets.SoundPlayer;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.custom.StyledText;

/**
 * @author keith
 *
 */
public class TestControlPanel extends Composite
{
    private TestView testView = null;
    private SoundPlayer player = null;
    private Group testControlGroup = null;
    private Button answerButton = null;
    private Label markLabel = null;
    private Button correctButton = null;
    private Button wrongButton = null;
    private Group flipGroup = null;
    private Button pauseButton = null;
    private Slider flipInterval = null;
    private Group statusGroup = null;
    private StyledText statusText = null;
    public TestControlPanel(TestView view, Composite parent, int style)
    {
        super(parent, style);
        this.testView = view;
        RowLayout layout = new RowLayout();
        layout.type = SWT.VERTICAL;
        layout.pack = true;
        setLayout(layout);
        player = new SoundPlayer(this);
		initialize();
    }
    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        createTestControlGroup();
        createFlipGroup();
        createStatusGroup();
        setFlipControlVisible(false);
        setTestControlVisible(false);
    }
    public SoundPlayer player()
    {
        return player;
    }
    /**
     * This method initializes testControlGroup	
     *
     */
    private void createTestControlGroup()
    {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        testControlGroup = new Group(this, SWT.NONE);
        testControlGroup.setLayout(gridLayout);
        answerButton = new Button(testControlGroup, SWT.TOGGLE);
        markLabel = new Label(testControlGroup, SWT.NONE);
        markLabel.setText("Label");
        correctButton = new Button(testControlGroup, SWT.NONE);
        wrongButton = new Button(testControlGroup, SWT.NONE);
        answerButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                testView.showAnswer(answerButton.getSelection());
                correctButton.setEnabled(answerButton.getSelection());
                wrongButton.setEnabled(answerButton.getSelection());
            }
        });
        correctButton
                .addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
                {
                    public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
                    {
                        answerButton.setSelection(false);
                        correctButton.setEnabled(false);
                        wrongButton.setEnabled(false);
                        testView.markTest(true);
                    }
                });
        wrongButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                answerButton.setSelection(false);
                correctButton.setEnabled(false);
                wrongButton.setEnabled(false);
                testView.markTest(false);
            }
        });
        answerButton.setText(MessageUtil.getString("ShowAnswer"));
        answerButton.setToolTipText(MessageUtil.getString("ShowAnswerToolTip"));
        correctButton.setText(MessageUtil.getString("Correct"));
        correctButton.setToolTipText(MessageUtil.getString("CorrectToolTip"));
        wrongButton.setText(MessageUtil.getString("Wrong"));
        wrongButton.setToolTipText(MessageUtil.getString("WrongToolTip"));
        correctButton.setEnabled(false);
        wrongButton.setEnabled(false);
        markLabel.setText(MessageUtil.getString("MarkYourself"));
    }
    /**
     * This method initializes flipGroup	
     *
     */
    private void createFlipGroup()
    {
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 1;
        flipGroup = new Group(this, SWT.NONE);
        flipGroup.setLayout(gridLayout1);
        flipGroup.setBounds(new Rectangle(5, 5, 14, 14));
        pauseButton = new Button(flipGroup, SWT.NONE);
        pauseButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                System.out.println("widgetSelected()"); // TODO Auto-generated Event stub widgetSelected()
            }
        });
        pauseButton.setText(MessageUtil.getString("Pause"));
        pauseButton.setText(MessageUtil.getString("PauseTooltip"));
        
        flipInterval = new Slider(flipGroup, SWT.NONE);
        flipInterval.setToolTipText(MessageUtil.getString("FlipIntervalTooltip"));
    }
    /**
     * This method initializes statusGroup	
     *
     */
    private void createStatusGroup()
    {
        statusGroup = new Group(this, SWT.NONE);
        statusGroup.setLayout(new GridLayout());
        statusGroup.setBounds(new Rectangle(5, 5, 14, 14));
        statusText = new StyledText(statusGroup, SWT.READ_ONLY);
    }
    
    protected void setTestControlVisible(boolean visible)
    {
        testControlGroup.setVisible(visible);
        testControlGroup.setLayoutDeferred(!visible);
        if (visible)
            testControlGroup.pack();
        else
            testControlGroup.setBounds(0, 0, 0, 0);
        statusGroup.setVisible(visible);
        statusGroup.setLayoutDeferred(!visible);
    }
    protected void setFlipControlVisible(boolean visible)
    {
        flipGroup.setVisible(visible);
        flipGroup.setLayoutDeferred(!visible);
        if (visible)
            flipGroup.pack();
        else
        {
            flipGroup.setSize(flipGroup.getSize().x, 0);
        }
        
        statusGroup.setVisible(visible);
        //statusGroup.setLayoutDeferred(!visible);
    }
    
}
