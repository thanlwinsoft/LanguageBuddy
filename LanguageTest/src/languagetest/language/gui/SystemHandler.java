/*
 * SystemHandler.java
 *
 * Created on March 8, 2004, 5:21 PM
 */

package languagetest.language.gui;


import java.io.IOException;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

import languagetest.language.test.UserConfig;
import languagetest.language.test.UserConfigListener;
/**
 *
 * @author  keith
 */
public class SystemHandler implements UserConfigListener
{
    public final static String PLAY_MIXER = "PlayMixer";
    public final static String REC_MIXER = "RecMixer";
    public final static String CHAR_MAP = "CharMap";
    private static SystemHandler instance = null;
    private Process charMapProcess = null;
    private Process volMixerProcess = null;
    private Process recMixerProcess = null;
    private Preferences packagePref = null;
    private UserConfigDialog configDialog = null;
    /** Creates a new instance of SystemHandler */
    public SystemHandler()
    {
        packagePref = Preferences.userNodeForPackage(this.getClass());
        UserConfig.addListener(this);    
    }
    static public SystemHandler getInstance()
    {
        if (instance == null) instance = new SystemHandler();
        return instance;
    }
    public String getCharMapCommand()
    {
        Preferences packagePref = 
                Preferences.userNodeForPackage(this.getClass());
        String charMap = "charmap";
        if (System.getProperty("os.name").indexOf("Windows")>-1)
        {
            charMap = "charmap.exe";
        }
        else if (System.getProperty("os.name").indexOf("Linux")>-1)
        {
            charMap = "kcharselect";
        }
        charMap = packagePref.get(CHAR_MAP, charMap);
        return charMap;
    }
    public String getVolMixerCommand()
    {
        Preferences packagePref = 
                Preferences.userNodeForPackage(this.getClass());
        String command = "mixer";
        if (System.getProperty("os.name").indexOf("Windows")>-1)
        {
            command = "sndvol32.exe /p";
        }
        else if (System.getProperty("os.name").indexOf("Linux")>-1)
        {
            command = "kmix";
        }
        command = packagePref.get(PLAY_MIXER, command);
        return command;
    }
    public String getRecMixerCommand()
    {
        Preferences packagePref = 
                Preferences.userNodeForPackage(this.getClass());
        String command = "mixer";
        if (System.getProperty("os.name").indexOf("Windows")>-1)
        {
            command = "sndvol32.exe /r";
        }
        else if (System.getProperty("os.name").indexOf("Linux")>-1)
        {
            command = "kmix";
        }
        command = packagePref.get(REC_MIXER, command);
        return command;
    }
    public void openCharMap()
    {
        String command = getCharMapCommand();
        charMapProcess = openCommand(command, charMapProcess);
        packagePref.put(CHAR_MAP, command);
    }
    
    public void openVolMixer() 
    {
        String command = getVolMixerCommand();
        volMixerProcess = openCommand(command, volMixerProcess);
        if (command.equals(getRecMixerCommand()))
        {
            recMixerProcess = volMixerProcess;
        }
        packagePref.put(PLAY_MIXER, command);
    }
    public void openRecMixer() 
    {
        String command = getRecMixerCommand();
        recMixerProcess = openCommand(command, recMixerProcess);
        if (command.equals(getVolMixerCommand()))
        {
            volMixerProcess = recMixerProcess;
        }
        packagePref.put(REC_MIXER, command);
    }
    
    protected Process openCommand(String command, Process oldProcess) 
    {
        Process process = oldProcess;
        boolean open = false;
        try
        {
            if (process == null)
            {
                process = Runtime.getRuntime().exec(command);
                open = true;
            }
            else
            {
                try
                {
                    // previous instance has terminated, so load another
                    process.exitValue();
                    process = Runtime.getRuntime().exec(command);
                    open = true;
                }
                catch (IllegalThreadStateException e)
                {
                    // not yet terminated so don't do anything
                    JOptionPane.showMessageDialog(null,
                        "\"" + command + 
                        "\" is already running. Look on your taskbar!");

                }
            }
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null,
                "Failed to run the external command:\n" + command + 
                "\nPlease check the Executable path in the Config Dialog");
        }
        return process;
    }
    public void closeExternalPrograms()
    {
        if (charMapProcess != null)
        {
            charMapProcess.destroy();
        }
        if (volMixerProcess != null)
        {
            volMixerProcess.destroy();
        }
        if (recMixerProcess != null && volMixerProcess != recMixerProcess)
        {
            recMixerProcess.destroy();
        }
    }
    
    public UserConfigDialog getConfigDialog(JFrame mainFrame)
    {
        if (configDialog == null)
        {
            configDialog = new UserConfigDialog(mainFrame, true);
        }
        return configDialog;
    }
    
    public void userConfigChanged(languagetest.language.test.UserConfig newConfig)
    {
        // force config data to be reloaded
        configDialog = null;
    }
    
}
