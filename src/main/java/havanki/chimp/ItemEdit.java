/*
 * CHIMP 1.1.1 - Cyber Helper Internet Monkey Program
 * Copyright (C) 2001-2015 Bill Havanki
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package havanki.chimp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class ItemEdit extends JDialog implements ActionListener
{
  private static final String OK = "OK";
  private static final String CANCEL = "Cancel";
  private static final String APPLY = "Apply";

  private SecureItemTable tbl;
  String selectedTitle;
  private boolean adding;
  private boolean hidePassword;
  private boolean itemModified;

  private JTextField titleField;
  private JTextField resourceField;
  private JTextField usernameField;
  private JTextField clearPasswordField;
  private JPasswordField passwordField;
  private JTextField commentsField;
  private JButton okButton;
  private JButton cancelButton;
  private JButton applyButton;

  public boolean isItemModified() { return itemModified; }

  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();

    if (command.equals(APPLY) || command.equals(OK)) {
      String newTitle = titleField.getText();
      if (newTitle.equals("")) {
        JOptionPane.showMessageDialog(this, "You must provide a title.",
                                      "No Title", JOptionPane.ERROR_MESSAGE);
        return;
      }

      // Check for duplicate title. ADD = always, MODIFY = only if changed.
      boolean titleChanged = !(newTitle.equals(selectedTitle));
      if (adding || (!adding && titleChanged)) {
        for (String currentTitle : tbl.getTitlesInOrder()) {
          if (currentTitle.equals(newTitle)) {
            int rc = JOptionPane.showConfirmDialog(this,
                "There is already an item with that title. Replace it?",
                "Replace?", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if (rc == JOptionPane.NO_OPTION) return;  // = abort
            break;
          }
        }
      }

      // Remove an old item if necessary.
      // ADD = no, it'll be overwritten in table.
      // MODIFY = if title changed, remove item with old title.
      if (!adding && titleChanged) {
        tbl.remove(selectedTitle);
      }

      // Create a modified item!
      SecureItemBuilder b = new SecureItemBuilder(newTitle)
          .resource(resourceField.getText())
          .username(usernameField.getText());
      if (hidePassword)
        b.password(passwordField.getPassword());
      else
        b.password(clearPasswordField.getText().toCharArray());
      b.comments(commentsField.getText());

      // Add modified item, replacing previous item (if title is the same).
      SecureItem newItem = b.build();
      tbl.put(newItem);
      itemModified = true;

      // Update selectedTitle. Now it's as if the dialog was just opened.
      selectedTitle = newItem.getTitle();
    }

    if (command.equals(CANCEL) || command.equals(OK)) {
      setVisible(false);
      dispose();
    }
  }

  public ItemEdit(Frame owner, SecureItem item, SecureItemTable tbl,
                  boolean adding, boolean hidePassword) {
    super(owner, "Edit " + item.getTitle(), true);
    this.tbl = tbl;
    this.adding = adding;
    this.hidePassword = hidePassword;
    if (!adding) selectedTitle = item.getTitle();
    itemModified = false;

    Container cp = getContentPane();
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createCompoundBorder
      (BorderFactory.createEmptyBorder(5,5,5,5),
       BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),
                                          BorderFactory.createEmptyBorder(5,5,5,5))));

    // Center = entry panel
    JPanel overEntryPanel = new JPanel();
    overEntryPanel.setLayout(new BorderLayout());
    JPanel entryPanel = new JPanel();
    entryPanel.setLayout(new BorderLayout(5, 5));
    overEntryPanel.add(entryPanel, BorderLayout.NORTH);

    // Left center = labels
    JPanel labelPanel = new JPanel();
    labelPanel.setLayout(new GridLayout(5, 0));
    JLabel l = new JLabel("Title"); labelPanel.add(l);
    l = new JLabel("Resource"); labelPanel.add(l);
    l = new JLabel("Username"); labelPanel.add(l);
    l = new JLabel("Password"); labelPanel.add(l);
    l = new JLabel("Comments"); labelPanel.add(l);
    entryPanel.add(labelPanel, BorderLayout.WEST);

    // Right center = fields
    JPanel fieldPanel = new JPanel();
    fieldPanel.setLayout(new GridLayout(5, 0));
    titleField = new JTextField(item.getTitle());
    fieldPanel.add(titleField);
    resourceField = new JTextField(item.getResource());
    fieldPanel.add(resourceField);
    usernameField = new JTextField(item.getUsername());
    fieldPanel.add(usernameField);
    char[] itemPasswordChars = item.getPassword();
    String itemPassword = "";
    if (itemPasswordChars != null) {
      itemPassword = new String(itemPasswordChars);
    }
    if (hidePassword) {
      passwordField = new JPasswordField(itemPassword);
      fieldPanel.add(passwordField);
    } else {
      clearPasswordField = new JTextField(itemPassword);
      fieldPanel.add(clearPasswordField);
    }

    //    commentsArea = new JTextArea(item.getComments(), 4, 25);
    //    JScrollPane commentsScroll = new JScrollPane(commentsArea);
    commentsField = new JTextField(item.getComments());
    fieldPanel.add(commentsField);

    entryPanel.add(fieldPanel);

    mainPanel.add(entryPanel, BorderLayout.CENTER);

    // Bottom = buttons
    JPanel buttonPanel = new JPanel();

    okButton = new JButton(OK);
    okButton.addActionListener(this);
    okButton.setActionCommand(OK);
    buttonPanel.add(okButton);
    cancelButton = new JButton(CANCEL);
    cancelButton.addActionListener(this);
    cancelButton.setActionCommand(CANCEL);
    buttonPanel.add(cancelButton);
    applyButton = new JButton(APPLY);
    applyButton.addActionListener(this);
    applyButton.setActionCommand(APPLY);
    buttonPanel.add(applyButton);

    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    cp.add(mainPanel);

    pack();
    setLocationRelativeTo(owner);
    setVisible(true);
  }
}
