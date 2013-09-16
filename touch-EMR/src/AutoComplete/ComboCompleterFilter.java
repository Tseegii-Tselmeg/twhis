package AutoComplete;

import Diagnosis.Frm_DiagnosisTherapy;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Class to add completion mechanism to combo boxes.
 * The class assumes that the look and field uses a JTextField as the combo
 * box editor. A check should be done to ensure this is true before
 * adding this class to a ComboBox.
 * 
 * To add to a combo, call the static method addCompletionMechanism(yourCombo),
 * or do the following:
 * 
 *   if (!(myCombo.getEditor().getEditorComponent() instanceof JTextField))
 *     return;
 *   
 *   JTextField tf = (JTextField)myCombo.getEditor().getEditorComponent();
 *   PlainDocument pd = new PlainDocument();
 *   _filter = new ComboCompleterFilter(myCombo);
 *   pd.setDocumentFilter(_filter);
 *   tf.setDocument(pd);
 *
 * @author ncochran
 *
 */


public class ComboCompleterFilter extends AbstractCompleterFilter
{

  public ComboCompleterFilter(JComboBox combo)
  {
    _combo = combo;
  }

    public ComboCompleterFilter(Frm_DiagnosisTherapy aThis) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

  @Override
  public int getCompleterListSize()
  {
    return _combo.getModel().getSize();
  }

  @Override
  public Object getCompleterObjectAt(int i)
  {
    return _combo.getItemAt(i);
  }

  @Override
  public JTextField getTextField()
  {
    return (JTextField)_combo.getEditor().getEditorComponent();
  }
  
  /**
   * Helper method to add auto-completion to a jcombobox or derivation.
   * 
   * The look and feel must use a JTextField as the combo box editor (or null
   * will be returned).
   * 
   * The JTextField will have it's document set to a new PlainDocument and the returned
   * filter will be set to autocomplete the contents.
   * Use the returned filter to set options 
   * such as case-sensitivity.
   * 
   * @param combo
   */
  static public ComboCompleterFilter addCompletionMechanism(JComboBox combo)
  {

    if (!(combo.getEditor().getEditorComponent() instanceof JTextField))
      return null;
    
    JTextField tf = (JTextField)combo.getEditor().getEditorComponent();
    PlainDocument pd = new PlainDocument();
    ComboCompleterFilter  filter = new ComboCompleterFilter(combo);
    pd.setDocumentFilter(filter);
    tf.setDocument(pd);
    
    return filter;
  }

  @Override
  public void replace(FilterBypass filterBypass, int offset, int length, String string, AttributeSet attributeSet) throws BadLocationException
  {
    super.replace(filterBypass, offset, length, string, attributeSet);

    System.out.println("現在輸入的那一個字 "+string);   //現在輸入的那一個字
//    setSearch(string);
    // Try to select the item in the combo list 
    if (_firstSelectedIndex != -1)   //判斷資料表內有值嗎
    {
      JTextField tf = (JTextField)_combo.getEditor().getEditorComponent();
      System.out.println("tf.getText()  "+tf.getSelectedText());
      int preTextLen = _preText.length();
      String text = tf.getText();
      _combo.setSelectedIndex(_firstSelectedIndex);
      filterBypass.replace(0, tf.getDocument().getLength(), text, attributeSet);
      
      getTextField().select(preTextLen, tf.getDocument().getLength());
    }
  }
//    public void setSearch(String condition) {
//        m_DiagnosisTherapy.setSearch(condition);
//    }

  private Frm_DiagnosisTherapy m_DiagnosisTherapy;
  private JComboBox _combo;
}
