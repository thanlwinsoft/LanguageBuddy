/*
 * TextActionHandler.java
 *
 * Created on April 8, 2004, 5:15 PM
 */

package languagetest.language.gui;

import java.awt.Event;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.util.Hashtable;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.text.Keymap;
import javax.swing.undo.UndoManager;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.event.UndoableEditListener;
import javax.swing.event.UndoableEditEvent;

import javax.swing.text.JTextComponent;

/**
 *
 * @author  keith
 * derived from Sun's Tutorial examples
 */
public class TextActionHandler implements UndoableEditListener
{
    private JTextComponent textComponent = null;
    private UndoManager undo = null;
    private Hashtable actions = null;
    private UndoAction undoAction = null;
    private RedoAction redoAction = null;
    public final static String UNDO = "Undo";
    public final static String REDO = "Redo";
    /** Creates a new instance of TextActionHandler */
    public TextActionHandler(JTextComponent textComponent)
    {
        this.textComponent = textComponent;
        undo = new UndoManager();
        undoAction = new UndoAction();
        redoAction = new RedoAction();
        createActionTable(textComponent);
        addKeymapBindings();
        textComponent.getDocument().addUndoableEditListener(this);
    }
    
    public void reset()
    {
        undo.discardAllEdits();
    }
    
    //This one listens for edits that can be undone.
    public void undoableEditHappened(UndoableEditEvent e) 
    {
        //Remember the edit and update the menus.
        undo.addEdit(e.getEdit());
        undoAction.updateUndoState();
        redoAction.updateRedoState();
    }
    
    
    private void createActionTable(JTextComponent textComponent) 
    {
        actions = new Hashtable();
        Action[] actionsArray = textComponent.getActions();
        for (int i = 0; i < actionsArray.length; i++) 
        {
            Action a = actionsArray[i];
            actions.put(a.getValue(Action.NAME), a);
        }
        actions.put(undoAction.getValue(Action.NAME), undoAction);
        actions.put(redoAction.getValue(Action.NAME), redoAction);
    }

    /* Add keymap bindings for undo / redo */
    protected void addKeymapBindings() 
    {
        //Add a new key map to the keymap hierarchy.
        Keymap keymap = JTextComponent.addKeymap("UndoRedoBindings",
                                           textComponent.getKeymap());

        //Ctrl-z for undo
        Action action = getActionByName(UNDO);
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
        keymap.addActionForKeyStroke(key, action);

        // Ctrl-y for redo
        action = getActionByName(REDO);
        key = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
        keymap.addActionForKeyStroke(key, action);

        textComponent.setKeymap(keymap);
    }
    
    public Action getActionByName(String name) 
    {
        return (Action)(actions.get(name));
    }

    class UndoAction extends AbstractAction 
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = -2232814767609191452L;

		public UndoAction() 
        {
            super(UNDO);
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) 
        {
            if (undo.canUndo())
            {
                try 
                {
                    undo.undo();
                } 
                catch (CannotUndoException ex) 
                {
                    System.out.println("Unable to undo: " + ex);
                    ex.printStackTrace();
                }
                updateUndoState();
                redoAction.updateRedoState();
            }
        }

        protected void updateUndoState() 
        {
            if (undo.canUndo()) 
            {
                setEnabled(true);
                putValue(Action.NAME, undo.getUndoPresentationName());
            } 
            else 
            {
                setEnabled(false);
                putValue(Action.NAME, UNDO);
            }
        }
    }

    class RedoAction extends AbstractAction 
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 4571376857997127027L;

		public RedoAction() 
        {
            super(REDO);
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) 
        {
            if (undo.canRedo())
            {
                try 
                {
                    undo.redo();
                } 
                catch (CannotRedoException ex) 
                {
                    System.out.println("Unable to redo: " + ex);
                    ex.printStackTrace();
                }
                updateRedoState();
                undoAction.updateUndoState();
            }
        }

        protected void updateRedoState() 
        {
            if (undo.canRedo()) 
            {
                setEnabled(true);
                putValue(Action.NAME, undo.getRedoPresentationName());
            } 
            else 
            {
                setEnabled(false);
                putValue(Action.NAME, REDO);
            }
        }
    }
}
