/*
 * -----------------------------------------------------------------------
 *  File:           $HeadURL$
 *  Revision        $LastChangedRevision$
 *  Last Modified:  $LastChangedDate$
 *  Last Change by: $LastChangedBy$
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
/**
 * 
 */
package org.thanlwinsoft.languagetest.eclipse.views;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.apache.xmlbeans.XmlException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.thanlwinsoft.languagetest.eclipse.LanguageTestPlugin;
import org.thanlwinsoft.languagetest.eclipse.charts.CumulativeTestPassChart;
import org.thanlwinsoft.languagetest.eclipse.workspace.WorkspaceLanguageManager;
import org.thanlwinsoft.languagetest.language.test.TestManager;
import org.thanlwinsoft.languagetest.language.test.TestType;
import org.thanlwinsoft.languagetest.language.test.XmlBeansTestHistory;
import org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryDocument;
import org.thanlwinsoft.schemas.languagetest.history.ModuleHistoryType;

/**
 * Display graphs of a users Test History 
 * @author keith
 *
 */
public class ChartHistoryView extends ViewPart
{
    public final static String ID = 
        "org.thanlwinsoft.languagetest.eclipse.views.ChartHistoryView";
    private Composite controls = null;
    private Canvas canvas = null;
    private Chart chart = null;
    private ChartDisplay chartDisplay = null;
    private Vector users = null;
    private Combo userCombo = null;
    private Combo langCombo = null;
    private Combo testTypeCombo = null;
    private Combo graphTypeCombo = null;
    private IProject userProject = null;
    private ChartHistoryProvider provider = null;
    
    public ChartHistoryView()
    {
        this.provider = new CumulativeTestPassChart();
        users = new Vector();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent)
    {
        FormLayout layout = new FormLayout();
        //layout.type = SWT.VERTICAL;
        //layout.fill = true;
        //Composite c = new Composite(parent, SWT.NONE);
        
        //c.setLayout(new FillLayout());
        //c.setLayoutData(fd);
        try
        {
            // this is a bit of a hack to make sure that the relevant classes
            // are found. If this isn't done, you get class not found exceptions
            Bundle bcds = Platform.getBundle("org.eclipse.birt.chart.device.swt");
            Class swtRenderer = 
                bcds.loadClass("org.eclipse.birt.chart.device.swt.SwtRendererImpl");
           
            
            canvas = new Canvas(parent, SWT.NONE);
            //GridData gd = new GridData();
            //gd.grabExcessHorizontalSpace = true;
            //gd.grabExcessVerticalSpace = true;
            chartDisplay = new ChartDisplay();
            canvas.addControlListener(chartDisplay);
            canvas.addPaintListener(chartDisplay);
            chartDisplay.setCanvas(canvas);
            canvas.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
            controls = new Composite(parent, SWT.LEFT);
            
            
            FormData cfd = new FormData();
            cfd.left = new FormAttachment(0, 0);
            cfd.top = new FormAttachment(0,0);
            cfd.right = new FormAttachment(100, 0);
            controls.setLayoutData(cfd);
            FormData fd = new FormData();
            fd.left = new FormAttachment(0, 0);
            fd.top = new FormAttachment(controls);
            fd.right = new FormAttachment(100, 0);
            fd.bottom = new FormAttachment(100, 0);
            canvas.setLayoutData(fd);
            
            parent.setLayout(layout);
            createControl();
        } 
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            LanguageTestPlugin.log(IStatus.ERROR, "Failed to find SwtRenderer for chart", e);
        }

    }

    private void createControl()
    {
//        GridLayout layout = new GridLayout();
//        layout.numColumns = 4;
        RowLayout layout = new RowLayout();
        layout.type = SWT.HORIZONTAL;
        layout.wrap = true;
        
        controls.setLayout(layout);
        userCombo = new Combo(controls, SWT.DROP_DOWN);
        IProject [] userProjects = WorkspaceLanguageManager.findUserProjects();
        for (int i = 0; i < userProjects.length; i++)
        {
            userCombo.add(userProjects[i].getName());
            users.add(userProjects[i]);
        }
        langCombo = new Combo(controls, SWT.DROP_DOWN);
//        GridData gd = new GridData();
//        gd.widthHint = 100;
//        langCombo.setLayoutData(gd);
        testTypeCombo = new Combo(controls, SWT.DROP_DOWN);
        graphTypeCombo = new Combo(controls, SWT.DROP_DOWN);
        
        for (int t = 0; t < TestType.NUM_TEST_TYPES; t++)
        {
            testTypeCombo.add(TestType.getById(t).description());
        }
        for (int t = 0; t < provider.getTypes().length; t++)
        {
            graphTypeCombo.add(provider.getTypes()[t]);
        }
        userCombo.addSelectionListener(new SelectionListener(){

            public void widgetDefaultSelected(SelectionEvent e){}
            public void widgetSelected(SelectionEvent e)
            {
                userChanged();
                
            }});
        langCombo.addSelectionListener(new SelectionListener(){

            public void widgetDefaultSelected(SelectionEvent e){}
            public void widgetSelected(SelectionEvent e)
            {
                createChart();
            }
        });
        testTypeCombo.addSelectionListener(new SelectionListener(){

            public void widgetDefaultSelected(SelectionEvent e){}
            public void widgetSelected(SelectionEvent e)
            {
                createChart();
            }
        });
        graphTypeCombo.addSelectionListener(new SelectionListener(){

            public void widgetDefaultSelected(SelectionEvent e){}
            public void widgetSelected(SelectionEvent e)
            {
                createChart();
            }
        });
        // default selection
        if (users.size() > 0)
        {
            userCombo.select(0);
            testTypeCombo.select(TestType.READING_NATIVE_FOREIGN_ID);
            graphTypeCombo.select(0);
            userChanged();
        }
    }
    
    private void userChanged()
    {
        if (userCombo.getSelectionIndex() > -1 &&
            userProject != users.elementAt(userCombo.getSelectionIndex()))
        {
            userProject = (IProject)users.elementAt(userCombo.getSelectionIndex());
            langCombo.removeAll();
            IFolder folder = userProject.getFolder(TestManager.HISTORY_DIR);
            if (folder != null)
            {
                IResource[] members;
                try
                {
                    members = folder.members();
                    for (int i = 0; i < members.length; i++)
                        langCombo.add(members[i].getName());
                    if (members.length > 0)
                        langCombo.select(0);
                    controls.pack();
                }
                catch (CoreException e1)
                {
                    LanguageTestPlugin.log(IStatus.WARNING, 
                            e1.getLocalizedMessage(), e1);
                }
            }
            createChart();
        }
    }
    
    /** create the chart using the provider if there is enough data for one */
    protected void createChart()
    {
        
        if (userProject == null || langCombo.getSelectionIndex() < 0 || 
            testTypeCombo.getSelectionIndex() < 0 || 
            graphTypeCombo.getSelectionIndex() < 0 ||
            canvas.isDisposed())
        {
            return;
        }
        provider.reset();
        provider.setType(graphTypeCombo.getSelectionIndex());
        final IFolder folder = userProject.getFolder(TestManager.HISTORY_DIR);
        if (folder == null) return;
        final TestType testType = TestType.getById(testTypeCombo.getSelectionIndex());
        final Display display = canvas.getDisplay();
        final int langSelection = langCombo.getSelectionIndex();
        Job job = new Job("CreateChart") {

            protected IStatus run(IProgressMonitor monitor)
            {
                IStatus status = new Status(IStatus.OK, LanguageTestPlugin.ID, 
                        0, "CreateChart", null);
                try
                {
                    IResource [] members = folder.members();
                    if (members.length > langSelection)
                    {
                        IResource r = members[langSelection];
                        if (r instanceof IFolder)
                        {
                            IFolder hFolder = (IFolder)r;
                            members = hFolder.members();
                            for (int i = 0; i < members.length; i++)
                            {
                                if (members[i] instanceof IFile)
                                {
                                    IFile file = (IFile)members[i];
                                    InputStream is = file.getContents();
                                    try
                                    {
                                        ModuleHistoryDocument doc = 
                                            ModuleHistoryDocument.Factory.parse(is);
                                        ModuleHistoryType mht = doc.getModuleHistory();
                                        if (mht != null)
                                        {
                                            provider.parse(mht, testType);
                                        }
                                    } 
                                    catch (XmlException e)
                                    {
                                        LanguageTestPlugin.log(IStatus.WARNING, 
                                                e.getLocalizedMessage(), e);
                                        status = new Status(IStatus.WARNING, 
                                                LanguageTestPlugin.ID, 
                                                0, "CreateChart", e);
                                    } 
                                    catch (IOException e)
                                    {
                                        LanguageTestPlugin.log(IStatus.WARNING, 
                                                e.getLocalizedMessage(), e);
                                        status = new Status(IStatus.WARNING, 
                                                LanguageTestPlugin.ID, 
                                                0, "CreateChart", e);
                                    }
                                }
                            }
                        }
                    }
                    
                } 
                catch (CoreException e)
                {
                    LanguageTestPlugin.log(IStatus.WARNING, e.getLocalizedMessage(), e);
                    status = new Status(IStatus.WARNING, 
                            LanguageTestPlugin.ID, 
                            0, "CreateChart", e);
                }
                chart = provider.createChart();
                display.asyncExec(new Runnable() {

                    public void run()
                    {
                        chartDisplay.renderModel(chart);
                    }});
                
                return status;
            }};
        job.setPriority(Job.BUILD);
        job.schedule();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    public void setFocus()
    {
        controls.setFocus();
    }
    
    public Chart getChart() { return chart; }
}
