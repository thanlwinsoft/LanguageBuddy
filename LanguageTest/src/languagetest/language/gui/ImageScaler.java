/*
 * ImageScaler.java
 *
 * Created on February 17, 2004, 8:46 PM
 */

package languagetest.language.gui;

import java.io.File;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.event.ComponentAdapter;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.AbstractButton;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.ImageIcon;
/**
 *
 * @author  keith
 */
public class ImageScaler extends ComponentAdapter implements ActionListener 
{
    private JLabel label = null;
    private AbstractButton button = null;
    private JScrollPane scrollPane = null;
    private BufferedImage picture = null;
    private File pictureFile = null;
    private Timer pictureTimer = null;
    /** Creates a new instance of ImageScaler */
    public ImageScaler(JComponent component, JScrollPane scrollPane)
    {
        this.scrollPane = scrollPane;
        scrollPane.addComponentListener(this);
        if (component instanceof JLabel) label = (JLabel)component;
        else if (component instanceof AbstractButton)
        {
            button = (AbstractButton)component;
        }
        else
        {
            throw new IllegalArgumentException(
                "Unexpected component. Expected a label or button");
        }
    }
    /** called when scrollPane resized
     */
    public void componentResized(java.awt.event.ComponentEvent evt)
    {
        System.out.println("Picture pane Resized");
        scaleImage();
    }
        
    /** Called from timer
     */
    public void actionPerformed(java.awt.event.ActionEvent e)
    {
        scaleImage();
    }
    
    public void setPicture(File pictureFile) throws IOException
    {
        if (pictureFile == null) 
        {
            this.pictureFile = null;
            this.picture = null;
            if (label != null) label.setIcon(null);
            else if (button != null) button.setIcon(null);
        }
        else
        {
            if (this.pictureFile == null ||
                !this.pictureFile.equals(pictureFile))
            {
                // only reload if file has actually changed
                this.pictureFile = pictureFile;
                this.picture = ImageIO.read(pictureFile);
                scaleImage();
            }
        }
    }
    
    public void scaleImage()
    {
        if (picture == null) return; // only relevant if picture present
        Dimension viewDim = scrollPane.getViewport().getExtentSize();
        //int count = 0;
        // if image hasn't yet loaded, then dimensions will be wrong
        if (viewDim == null || 
            picture.getHeight() == -1 || picture.getWidth() == -1)
        {
            if (pictureTimer == null)
            {
                pictureTimer = new Timer(200, this);
                pictureTimer.start();
            }
            else
            {
                pictureTimer.start();
            }
        }
        else
        {
            if (pictureTimer != null) pictureTimer.stop();
        }
        double viewRatio = viewDim.getHeight() / viewDim.getWidth();
        double picRatio = (double)picture.getHeight() / (double)picture.getWidth();
        int width = -1;
        int height = -1;
        // if picture is smaller that panel just display it, otherwise scale to
        // fit
        if (viewDim.getHeight() > picture.getHeight() &&
            viewDim.getWidth() > picture.getWidth())
        {
            // original size
            width = picture.getWidth();
            height = picture.getHeight();
        }
        else if (viewRatio > picRatio)
        {
            // view is higher than pic so width determines size
            width = (int)viewDim.getWidth();
            height = (int)(viewDim.getWidth() * picRatio);
        }
        else
        {
            height = (int)viewDim.getHeight();
            width = (int)(viewDim.getHeight() / picRatio);
        }
        if (width > 0 && height > 0)
        {
            System.out.println("Scaling picture " + width + "x" + height);
            Image image = picture.getScaledInstance(width, height, 
                java.awt.Image.SCALE_SMOOTH);
            // reuse icon if possible to save memory
            Icon oldIcon = null;
            if (label != null) oldIcon = label.getIcon();
            else if (button != null) oldIcon = button.getIcon();
            if (oldIcon != null && oldIcon instanceof ImageIcon)
            {
                ((ImageIcon)oldIcon).setImage(image);  
                if (label != null)  
                {
                    //label.repaint();
                    label.revalidate();
                }
                if (button != null) 
                {
                    //button.repaint();
                    button.revalidate();
                }
                scrollPane.repaint();
            }
            else
            {
                if (label != null) 
                {
                    label.setIcon(new ImageIcon(image));
                    label.revalidate();
                    //label.repaint();
                }
                else if (button != null)
                {
                    button.setIcon(new ImageIcon(image));
                    button.revalidate();
                    //button.repaint();
                }
                scrollPane.repaint();
            }
        }
        int xPos = 0;
        int yPos = 0;
        if (width < viewDim.getWidth()) 
        {
            xPos = (int)((viewDim.getWidth() - width) / 2.0);
        }
        if (height < viewDim.getHeight()) 
        {
            yPos = (int)((viewDim.getHeight() - height) / 2.0);
        }
        scrollPane.getViewport().setViewPosition(new Point(xPos, yPos));
    }
    
    
    
    
    
}
