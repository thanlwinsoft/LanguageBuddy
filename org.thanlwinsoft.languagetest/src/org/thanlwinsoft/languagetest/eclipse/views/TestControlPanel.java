/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL: http://keith-laptop/svn/krs/LanguageTest/trunk/org.thanlwinsoft.languagetest/src/org/thanlwinsoft/languagetest/eclipse/views/TestControlPanel.java $
 *  Revision        $LastChangedRevision: 1388 $
 *  Last Modified:  $LastChangedDate: 2009-01-31 19:32:00 +0700 (Sat, 31 Jan 2009) $
 *  Last Change by: $LastChangedBy: keith $
 * -----------------------------------------------------------------------
 *  Copyright (C) 2007 Keith Stribley <devel@thanlwinsoft.org>
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
package org.thanlwinsoft.languagetest.eclipse.views;

import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.thanlwinsoft.eclipse.widgets.SoundPlayer;
import org.thanlwinsoft.languagetest.MessageUtil;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
    private Spinner repeatSpinner = null;
    private Group statusGroup = null;
    private StyledText statusText = null;
    public final static int MIN_PERIOD = 1000;
    public final static int MAX_PERIOD = 30000;// 30sec
    
    public TestControlPanel(TestView view, Composite parent, int style)
    {
        super(parent, style);
        this.testView = view;
        //RowLayout layout = new RowLayout();
        FormLayout layout = new FormLayout();
        //layout.type = SWT.VERTICAL;
        //layout.pack = true;
        
        initialize();
        player = new SoundPlayer(this);
        FormData fd = new FormData();
        fd.top = new FormAttachment(0, 0);
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(100, 0);
        player.setLayoutData(fd);
        setLayout(layout);
        fd = new FormData();
        fd.top = new FormAttachment(player);
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(100, 0);
        testControlGroup.setLayoutData(fd);
        fd = new FormData();
        fd.top = new FormAttachment(player);
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(100, 0);
        flipGroup.setLayoutData(fd);
        fd = new FormData();
        // assume test control group is taller than flip group
        fd.top = new FormAttachment(testControlGroup);
        fd.bottom = new FormAttachment(100, 0);
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(100, 0);
        statusGroup.setLayoutData(fd);
    }
    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        createTestControlGroup();
        createFlipGroup();
        createStatusGroup();
        setGroupVisible(testControlGroup, false);
        setGroupVisible(flipGroup, false);
        setStatusVisible(false);
        //pack();
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
        gridLayout.numColumns = 2;
        testControlGroup = new Group(this, SWT.NONE);
        testControlGroup.setLayout(gridLayout);
        testControlGroup.setText(MessageUtil.getString("TestGroupLabel"));
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
        ImageDescriptor img = LanguageTestPlugin.getImageDescriptor("icons/correct16.png");
        if (img != null)
            correctButton.setImage(img.createImage(getDisplay()));
        img = LanguageTestPlugin.getImageDescriptor("icons/wrong16.png");
        if (img != null)
            wrongButton.setImage(img.createImage(getDisplay()));
        
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        gridData.horizontalAlignment = SWT.FILL;
        answerButton.setLayoutData(gridData);
        gridData = new GridData();
        gridData.horizontalSpan = 2;
        markLabel.setLayoutData(gridData);
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
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        flipGroup = new Group(this, SWT.NONE);
        
        flipGroup.setLayout(layout);
        flipGroup.setText(MessageUtil.getString("FlipCardsGroup"));
        Label labelInterval = new Label(flipGroup, SWT.LEAD);
        labelInterval.setText(MessageUtil.getString("FlipInterval"));
        GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
        layoutData.horizontalSpan = 2;
        labelInterval.setLayoutData(layoutData);
        flipInterval = new Slider(flipGroup, SWT.NONE);
        flipInterval.setMinimum(MIN_PERIOD);
        flipInterval.setMaximum(MAX_PERIOD);
        flipInterval.setThumb(1000);
        flipInterval.setPageIncrement(5000);
        flipInterval.setIncrement(1000);
        layoutData = new GridData(GridData.FILL_HORIZONTAL);
        layoutData.horizontalSpan = 2;
        flipInterval.setLayoutData(layoutData);
        int flipMs = LanguageTestPlugin.getPrefStore().getInt(TestView.FLIP_PERIOD_PREF);
        
        flipInterval.setSelection(flipMs);
        flipInterval.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e)
            {
                
            }

            public void widgetSelected(SelectionEvent e)
            {
                flipInterval.setToolTipText(MessageUtil.getString("FlipIntervalToolTip", 
                                Integer.toString(flipInterval.getSelection()/1000)));
                LanguageTestPlugin.getPrefStore().setValue(TestView.FLIP_PERIOD_PREF, 
                                flipInterval.getSelection());
                try
                {
                    LanguageTestPlugin.getPrefStore().save();
                }
                catch (IOException ioe)
                {
                    LanguageTestPlugin.log(IStatus.WARNING, ioe.getMessage(), ioe);
                }
            }});
        flipInterval.setToolTipText(MessageUtil.getString("FlipIntervalToolTip", 
                        Integer.toString(flipMs/1000)));
        Label labelRepeat = new Label(flipGroup, SWT.LEAD);
        labelRepeat.setText(MessageUtil.getString("RepeatLabel"));
        repeatSpinner = new Spinner(flipGroup, SWT.NONE);
        int repeatCount = LanguageTestPlugin.getPrefStore().getInt(TestView.FLIP_REPEAT_PREF);
        repeatSpinner.setValues(repeatCount, 1, 20, 0, 1, 5);
        repeatSpinner.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e)
            {
                
            }

            public void widgetSelected(SelectionEvent e)
            {
                repeatSpinner.setToolTipText(MessageUtil.getString("RepeatCountToolTip", 
                                Integer.toString(repeatSpinner.getSelection())));
                LanguageTestPlugin.getPrefStore().setValue(TestView.FLIP_REPEAT_PREF, 
                                repeatSpinner.getSelection());
                try
                {
                    LanguageTestPlugin.getPrefStore().save();
                }
                catch (IOException ioe)
                {
                    LanguageTestPlugin.log(IStatus.WARNING, ioe.getMessage(), ioe);
                }
            }});
        repeatSpinner.setToolTipText(MessageUtil.getString("RepeatCountToolTip", 
                        Integer.toString(repeatCount)));
        
        pauseButton = new Button(flipGroup, SWT.TOGGLE);
        pauseButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
        {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
            {
                
            }
        });
        pauseButton.setText(MessageUtil.getString("Pause"));
        pauseButton.setToolTipText(MessageUtil.getString("PauseToolTip"));
        layoutData = new GridData(GridData.FILL_HORIZONTAL);
        layoutData.horizontalSpan = 2;
        pauseButton.setLayoutData(layoutData);
    }
    /**
     * This method initializes statusGroup	
     *
     */
    private void createStatusGroup()
    {
        statusGroup = new Group(this, SWT.NONE);
        statusGroup.setText(MessageUtil.getString("StatusGroup"));
        statusGroup.setLayout(new FillLayout());
        //statusGroup.setBounds(new Rectangle(5, 5, 40, 40));
        statusText = new StyledText(statusGroup, SWT.READ_ONLY | SWT.MULTI);
        statusText.setSize(100, 100);
    }
    
    protected void setTestControlVisible(boolean visible)
    {
        setGroupVisible(testControlGroup,visible);
    }
    protected void setFlipControlVisible(boolean visible)
    {
        setGroupVisible(flipGroup, visible);
    }
    protected void setStatusVisible(boolean visible)
    {
        setGroupVisible(statusGroup,visible);
        
    }
    private void setGroupVisible(Group group, boolean visible)
    {
        
        group.setVisible(visible);
    }
    /** Checks whether the pause button is pressed in
     * @return
     */
    public boolean isTestPaused()
    {
        return pauseButton.getSelection();
    }
    public void setStatus(String message)
    {
        statusText.setText(message);
        statusText.setToolTipText(message);
    }
}
