package com.esporter.client.vue.component;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxEditor;

import com.esporter.client.vue.MasterFrame;

public class ComboBoxRendererEditor extends BasicComboBoxEditor {
	private JLabel label = new JLabel();
    private JPanel panel = new JPanel();
    private Object selectedItem;

    public ComboBoxRendererEditor() {
        
        label.setOpaque(false);
        label.setFont(new Font("Cambria", Font.BOLD, 12));
        label.setForeground(MasterFrame.COLOR_TEXT);
         
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
        panel.add(label);
        panel.setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);
    }
    
    public Component getEditorComponent() {
        return this.panel;
    }
     
    public Object getItem() {
        return "[" + this.selectedItem.toString() + "]";
    }
     
    public void setItem(Object item) {
        this.selectedItem = item;
        label.setText(item.toString());
    }
}
