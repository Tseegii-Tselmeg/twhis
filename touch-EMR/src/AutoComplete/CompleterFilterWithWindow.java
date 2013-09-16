package AutoComplete;

import Diagnosis.Frm_DiagnosisInfo;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;


public class CompleterFilterWithWindow extends CompleterFilter
{
  
   
    public CompleterFilterWithWindow(Object[] completerObjs, JTextField textField) {
        super(completerObjs, textField);
         _init();
    }


  @Override
  public void insertString(FilterBypass filterBypass, int offset, String string, AttributeSet attributeSet) throws BadLocationException
  {
    setFilterWindowVisible(false);
    super.insertString(filterBypass, offset, string, attributeSet);
  }

  @Override
  public void remove(FilterBypass filterBypass, int offset, int length) throws BadLocationException
  {
    setFilterWindowVisible(false);
    super.remove(filterBypass, offset, length);
  }

  @Override
  public void replace(FilterBypass filterBypass, int offset, int length, String string, AttributeSet attributeSet) throws BadLocationException
  {
    if (_isAdjusting)
    {
      filterBypass.replace(offset, length, string, attributeSet);
      return;
    }

    super.replace(filterBypass, offset, length, string, attributeSet);
    
    if (getLeadingSelectedIndex() == -1)
    {
      if (isFilterWindowVisible())
        setFilterWindowVisible(false);
      return;
    }
    
    _lm.setFilter(_preText);
    
    if (!isFilterWindowVisible())
      setFilterWindowVisible(true);

    else
      _setWindowHeight();
      _list.setSelectedValue(_textField.getText(), true);
  }


  private void _init()
  {
    _fwl = new FilterWindowListener();
    _lm = new FilterListModel(_objectList);
    _tfkl = new TextFieldKeyListener();
    _textField.addKeyListener(_tfkl);

    EscapeAction escape = new EscapeAction();
    _textField.registerKeyboardAction(escape, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

  }
  
  public boolean isFilterWindowVisible()
  {
    return ((_win != null) && (_win.isVisible()));
  }
  
  public void setCaseSensitive(boolean caseSensitive)
  {
    super.setCaseSensitive(caseSensitive);
    _lm.setCaseSensitive(caseSensitive);
  }
  
  public void setFilterWindowVisible(boolean visible)
  {
    if (visible)
    {
      _initWindow();
      _list.setModel(_lm);
      _win.setVisible(true);
      _textField.requestFocus();
      _textField.addFocusListener(_fwl);
    }
    else
    {
      if (_win == null)
        return;
      
      _win.setVisible(false);
      _win.removeFocusListener(_fwl);
      Window ancestor = SwingUtilities.getWindowAncestor(_textField);
      ancestor.removeMouseListener(_fwl);
      _textField.removeFocusListener(_fwl);
      _textField.removeAncestorListener(_fwl);
      _list.removeMouseListener(_lml);
      _list.removeListSelectionListener(_lsl);
      _lsl = null;
      _lml = null;
      _win.dispose();
      _win = null;
      _list = null;
    }
  }
    
  private void _initWindow()
  {
    Window ancestor = SwingUtilities.getWindowAncestor(_textField);
    _win = new JWindow(ancestor);
    _win.addWindowFocusListener(_fwl);
    _textField.addAncestorListener(_fwl);
//    ancestor.addMouseListener(_fwl);
    _lsl = new ListSelListener();
    _lml = new ListMouseListener();

    _list = new JList(_lm);
    _list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    _list.setFocusable(false);       //讓滑鼠點選表單停留在畫面不消失
    _list.setPrototypeCellValue("Prototype");
    _list.addListSelectionListener(_lsl);
    _list.addMouseListener(_lml);
    
    _sp = new JScrollPane(_list,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    _sp.setFocusable(false);
    _sp.getVerticalScrollBar().setFocusable(false);
    _setWindowHeight();
//    _win.setLocation(_textField.getLocationOnScreen().x, _textField.getLocationOnScreen().y + _textField.getHeight());
    _win.getContentPane().add(_sp);
  }
  
  private void _setWindowHeight()
  {
    int height = _list.getFixedCellHeight() * Math.min(MAX_VISIBLE_ROWS, _lm.getSize());
    height += _list.getInsets().top + _list.getInsets().bottom;
    height += _sp.getInsets().top + _sp.getInsets().bottom;
    
    _win.setSize(_textField.getWidth(), height);
    _sp.setSize(_textField.getWidth(), height); // bottom border fails to draw without this
  }
  
    @Override
  public void setCompleterMatches(Object[] objectsToMatch)
  {
    if (isFilterWindowVisible())
      setFilterWindowVisible(false);
    
    super.setCompleterMatches(objectsToMatch);
    _lm.setCompleterMatches(objectsToMatch);
  }
  
  class EscapeAction extends AbstractAction
  {
      public void actionPerformed(ActionEvent e)
      {
        if (isFilterWindowVisible())
          setFilterWindowVisible(false);
      }
  }
  
  private class FilterWindowListener extends MouseAdapter
    implements AncestorListener, FocusListener, WindowFocusListener
  {
    public void ancestorMoved(AncestorEvent event)
    {
      setFilterWindowVisible(false);
    }
    public void ancestorAdded(AncestorEvent event)
    {
      setFilterWindowVisible(false);
    }
    public void ancestorRemoved(AncestorEvent event)
    {
      setFilterWindowVisible(false);
    }
    
    public void focusLost(FocusEvent e) 
    {
      if (e.getOppositeComponent() != _win){
        setFilterWindowVisible(false);
      }
    }
    
    public void focusGained(FocusEvent e){}
    
    public void windowLostFocus(WindowEvent e)
    {
      Window w = e.getOppositeWindow();

      if (w.getFocusOwner() != _textField)
        setFilterWindowVisible(false);
    }
    public void windowGainedFocus(WindowEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent e)
    {
      setFilterWindowVisible(false);
    }
  }

    private Frm_DiagnosisInfo getFrame(){
        return this._frame;
    }



  private class TextFieldKeyListener extends KeyAdapter    
  {
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        
      if (!((e.getKeyCode() == KeyEvent.VK_DOWN) ||
            (e.getKeyCode() == KeyEvent.VK_UP) ||
            ((e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) && (isFilterWindowVisible())) ||
            ((e.getKeyCode() == KeyEvent.VK_PAGE_UP) && (isFilterWindowVisible())) ||
            (e.getKeyCode() == KeyEvent.VK_ENTER)))
        return;

      if ((e.getKeyCode() == KeyEvent.VK_DOWN) && !isFilterWindowVisible()) //e.getKeyCode() == 8  8:Backsapce按鍵代碼
      {
        _preText = _textField.getText();
        _lm.setFilter(_preText);
        
        if (_lm.getSize() > 0)
          setFilterWindowVisible(true);
        else  
          return;
      }

      if (e.getKeyCode() == KeyEvent.VK_ENTER)
      {
        if (isFilterWindowVisible()){
            setFilterWindowVisible(false);
          if (m_listValue.length >= 2) {
                boolean decide = getFrame().isCodeAtHashMap(m_listValue[0].trim());
                if(decide == true) {
                   _textField.setText(m_listValue[0]+"   ");
                } else {
                   _textField.setText("");
                }
          }
        } else {     //enter選取後的動作
            
          if (m_listValue != null && m_listValue.length >= 2) {
                boolean decide = getFrame().isCodeAtHashMap(m_listValue[0].trim());
                if(decide == true) {
                   _textField.setText(m_listValue[0]+"   ");
                } else {
                   _textField.setText("");
                }
          }
        }  
        _textField.setCaretPosition(_textField.getText().length());
        return;
      }
      
      int index = -1;
      
      if (e.getKeyCode() == KeyEvent.VK_DOWN)
        index = Math.min(_list.getSelectedIndex() + 1, _list.getModel().getSize()-1);
      else if (e.getKeyCode() == KeyEvent.VK_UP)
        index = Math.max(_list.getSelectedIndex() - 1, 0);
      else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP)
        index = Math.max(_list.getSelectedIndex() - MAX_VISIBLE_ROWS, 0);
      else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
        index = Math.min(_list.getSelectedIndex() + MAX_VISIBLE_ROWS, _list.getModel().getSize()-1);
      if (index == -1)
        return;
        
      _list.setSelectedIndex(index);
      _list.scrollRectToVisible(_list.getCellBounds(index, index));
      
    }

    @Override
    public void keyReleased(KeyEvent e){
        m_listValue = _textField.getText().split("  ");
        //回傳table  autoCompleteList表單值切割的陣列
        if (m_listValue.length > 1) {
            getFrame().setTableValue(m_listValue);
        } else {
            m_listValue = new String[6];
            for(int i = 1; i < m_listValue.length; i++ ){
                m_listValue[i] = null;
            }
            getFrame().setTableValue(m_listValue);
        }
      }
  }



  private class ListSelListener implements ListSelectionListener
  {
    public void valueChanged(ListSelectionEvent e)
    {   
      _isAdjusting = true;
      _textField.setText(_list.getSelectedValue().toString());
      _isAdjusting = false;
      _textField.select(_preText.length(), _textField.getText().length());
    }
  }
  
  private class ListMouseListener extends MouseAdapter
  {
    @Override
    public void mouseClicked(MouseEvent e)
    {
      if (e.getClickCount() == 2)
        setFilterWindowVisible(false);
    }
  }


  protected FilterWindowListener _fwl;
  protected JWindow _win;
  protected TextFieldKeyListener _tfkl;
  protected ListSelListener _lsl;
  protected ListMouseListener _lml;
  protected JList _list;
  protected JScrollPane _sp;
  protected FilterListModel _lm;
  private String[] m_listValue;


  
  protected boolean _isAdjusting = false;
  public static int MAX_VISIBLE_ROWS = 8;
}
