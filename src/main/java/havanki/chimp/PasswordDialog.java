/*
 * CHIMP 1.0 - Cyber Helper Internet Monkey Program
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
import java.util.Arrays;
import javax.swing.*;

public class PasswordDialog extends JDialog implements ActionListener
{
  private static final String OK = "OK";
  private static final String CANCEL = "Cancel";
  private static final String PLAIN = "No Password";

  JPasswordField pwdField;
  JPasswordField verField;
  private JButton okButton;
  private JButton cancelButton;
  private JButton plainButton;

  boolean verify;
  char password[];

  public char[] getPassword() { return password; }
  public static void cleanCharArray(char[] a) {
    Arrays.fill(a, (char) 0);
  }

  private class OkAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
      PasswordDialog.this.actionPerformed(
          new ActionEvent(e.getSource(), ActionEvent.ACTION_FIRST, OK));
    }
  }
  private OkAction okAction = new OkAction();

  @Override
  public void actionPerformed (ActionEvent e) {
    String command = e.getActionCommand();

    if (command.equals(CANCEL)) {
      password = null;
    }
    if (command.equals(PLAIN)) {
      password = new char[0];
    }
    if (command.equals(OK)) {
      password = pwdField.getPassword();
      if (password.length == 0) {
        JOptionPane.showMessageDialog(this, "Please enter a password.",
                                      "No Password", JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (verify) {
        char[] verifyPwd = verField.getPassword();
        if (!Arrays.equals(password, verifyPwd)) {
          JOptionPane.showMessageDialog(this,
              "Could not verify password. Try again.",
              "Password Mismatch", JOptionPane.ERROR_MESSAGE);
          cleanCharArray(verifyPwd);
          return;
        }
      } // end if (verify)
    } // end if (OK)

    setVisible (false);
    dispose();
  }

  public PasswordDialog(Frame owner, String label, boolean verify) {
    super(owner, "Password Entry", true);
    this.verify = verify;

    Container cp = getContentPane();
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(5,5,5,5),
        BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(),
            BorderFactory.createEmptyBorder(5,5,5,5))));

    // Center = entry panel
    JPanel overEntryPanel = new JPanel();
    overEntryPanel.setLayout(new BorderLayout());
    JPanel entryPanel = new JPanel();
    entryPanel.setLayout(new BorderLayout(5, 5));
    overEntryPanel.add(entryPanel, BorderLayout.NORTH);

    // Top = label
    JLabel l = new JLabel(label);
    entryPanel.add(l, BorderLayout.NORTH);

    // Left center = labels
    JPanel labelPanel = new JPanel();
    if (verify)
      labelPanel.setLayout(new GridLayout(2, 0));
    else
      labelPanel.setLayout(new GridLayout(1, 0));
    l = new JLabel("Password"); labelPanel.add(l);
    if (verify) {
      l = new JLabel("Verify"); labelPanel.add(l);
    }
    entryPanel.add(labelPanel, BorderLayout.WEST);

    // Right center = fields
    JPanel fieldPanel = new JPanel();
    if (verify)
      fieldPanel.setLayout(new GridLayout(2, 0));
    else
      fieldPanel.setLayout(new GridLayout(1, 0));
    pwdField = new JPasswordField();
    if (!verify) {
      pwdField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                                 "doOK");
      pwdField.getActionMap().put("doOK", okAction);
    }
    fieldPanel.add(pwdField);
    if (verify) {
      verField = new JPasswordField();
      verField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                                 "doOK");
      verField.getActionMap().put("doOK", okAction);
      fieldPanel.add(verField);
    }

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
    plainButton = new JButton(PLAIN);
    plainButton.addActionListener(this);
    plainButton.setActionCommand(PLAIN);
    buttonPanel.add(plainButton);

    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    cp.add(mainPanel);

    pack();
    setLocationRelativeTo(owner);
    setVisible(true);
  }
}
