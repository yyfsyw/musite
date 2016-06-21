/**
 * Musite
 * Copyright (C) 2010 Digital Biology Laboratory, University Of Missouri
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package musite.ui;

/**
 *
 * @author Jianjiong Gao
 */
public class SupportDialog extends javax.swing.JDialog {

    /** Creates new form SupportDialog */
    public SupportDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        javax.swing.JPanel websitePanel = new javax.swing.JPanel();
        javax.swing.JTextField websiteTextField = new javax.swing.JTextField();
        javax.swing.JPanel contactPanel = new javax.swing.JPanel();
        javax.swing.JLabel emailLabel = new javax.swing.JLabel();
        javax.swing.JTextField emailTextField = new javax.swing.JTextField();
        groupLabel = new javax.swing.JLabel();
        javax.swing.JTextField groupTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Support");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        websitePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Project website"));
        websitePanel.setLayout(new javax.swing.BoxLayout(websitePanel, javax.swing.BoxLayout.LINE_AXIS));

        websiteTextField.setText("http://musite.sourceforge.net/");
        websitePanel.add(websiteTextField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(websitePanel, gridBagConstraints);

        contactPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Send your questions to"));
        contactPanel.setLayout(new java.awt.GridBagLayout());

        emailLabel.setText("E-mail:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        contactPanel.add(emailLabel, gridBagConstraints);

        emailTextField.setText("musite@googlegroups.com");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        contactPanel.add(emailTextField, gridBagConstraints);

        groupLabel.setText("Discussion group:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        contactPanel.add(groupLabel, gridBagConstraints);

        groupTextField.setText("http://groups.google.com/group/musite");
        groupTextField.setPreferredSize(new java.awt.Dimension(300, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        contactPanel.add(groupTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(contactPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel groupLabel;
    // End of variables declaration//GEN-END:variables

}