package ui.fields;

import org.yunghegel.gdx.ui.widgets.SLabel;
import org.yunghegel.gdx.ui.widgets.STextField;
import ui.CustomWidget;

public class TextFieldWithLabel extends CustomWidget {

    private int width = -1;

    protected STextField textField;
    private SLabel label;

    public TextFieldWithLabel(String labelText, int width) {
        super();
        this.width = width;
        textField = new STextField(" ","console");
        label = new SLabel(labelText);
        setupUI();
    }

    public TextFieldWithLabel(String labelText) {
        this(labelText, -1);
    }

    private void setupUI() {
        if (width > 0) {
            add(label).center().width(width * 0.2f).pad(0,2f,0,2f);
            add(textField).right().width(width * 0.8f).row();
        } else {
            add(label).center().expandX().pad(0,2f,0,2f);
            add(textField).right().expandX().row();
        }
    }

    public String getText() {
        return textField.getText();
    }

    public void setEditable(boolean editable) {
        textField.setDisabled(!editable);
    }

    public void clear() {
        textField.setText("");
    }

    public void setText(String text) {
        textField.setText(text);
    }

    public void setLabelText(String text) {
        label.setText(toString());
    }



}