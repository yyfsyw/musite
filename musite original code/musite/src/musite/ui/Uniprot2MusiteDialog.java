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

import java.io.File;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import musite.MusiteInit;
import musite.PTM;
import musite.Proteins;

import musite.io.xml.UniProtXMLReader;
import musite.io.xml.ProteinsXMLWriter;

import musite.ui.task.ReadTask;
import musite.ui.task.WriteTask;
import musite.ui.task.TaskUtil;
import musite.ui.util.CheckComboBox;

import musite.util.FileExtensionsFilter;
import musite.util.FilePathParser;

/**
 *
 * @author Jianjiong Gao
 */
public class Uniprot2MusiteDialog extends javax.swing.JDialog {

    /** Creates new form Fasta2XmlDialog */
    public Uniprot2MusiteDialog(java.awt.Frame parent, boolean modal) {
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

        javax.swing.JPanel uniprotPanel = new javax.swing.JPanel();
        javax.swing.JPanel unportFilePanel = new javax.swing.JPanel();
        uniprotFileTextField = new javax.swing.JTextField();
        javax.swing.JButton uniprotFileButton = new javax.swing.JButton();
        javax.swing.JPanel xmlPanel = new javax.swing.JPanel();
        javax.swing.JPanel xmlFilePanel = new javax.swing.JPanel();
        xmlFileTextField = new javax.swing.JTextField();
        javax.swing.JButton xmlFileButton = new javax.swing.JButton();
        javax.swing.JPanel ptmPanel = new javax.swing.JPanel();
        javax.swing.JPanel statusPanel = new javax.swing.JPanel();
        bysimilarityCheckBox = new javax.swing.JCheckBox();
        probableCheckBox = new javax.swing.JCheckBox();
        potentialCheckBox = new javax.swing.JCheckBox();
        javax.swing.JPanel optionPanel = new javax.swing.JPanel();
        keepAllAccCheckBox = new javax.swing.JCheckBox();
        javax.swing.JPanel OKPanel = new javax.swing.JPanel();
        OKBtn = new javax.swing.JButton();
        javax.swing.JButton cancelBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Convert from UniProt XML to Musite XML");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        uniprotPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("From UniProt XML file"));
        uniprotPanel.setLayout(new java.awt.GridBagLayout());

        unportFilePanel.setLayout(new java.awt.GridBagLayout());

        uniprotFileTextField.setToolTipText("Please select a FASTA training file");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        unportFilePanel.add(uniprotFileTextField, gridBagConstraints);

        uniprotFileButton.setText("Open");
        uniprotFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uniprotFileButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        unportFilePanel.add(uniprotFileButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        uniprotPanel.add(unportFilePanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(uniprotPanel, gridBagConstraints);

        xmlPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Save to Musite XML File"));
        xmlPanel.setMinimumSize(new java.awt.Dimension(400, 63));
        xmlPanel.setPreferredSize(new java.awt.Dimension(500, 63));
        xmlPanel.setLayout(new java.awt.GridBagLayout());

        xmlFilePanel.setLayout(new java.awt.GridBagLayout());

        xmlFileTextField.setToolTipText("Please select a FASTA training file");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        xmlFilePanel.add(xmlFileTextField, gridBagConstraints);

        xmlFileButton.setText("Open");
        xmlFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xmlFileButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        xmlFilePanel.add(xmlFileButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        xmlPanel.add(xmlFilePanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(xmlPanel, gridBagConstraints);

        ptmPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("PTM type"));
        ptmPanel.setLayout(new javax.swing.BoxLayout(ptmPanel, javax.swing.BoxLayout.LINE_AXIS));

        ptmCombo = new CheckComboBox(PTM.values(), true);
        ptmCombo.setShowAllAndNone(true);
        ptmPanel.add(ptmCombo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(ptmPanel, gridBagConstraints);

        statusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Site annotation status"));
        statusPanel.setLayout(new java.awt.GridBagLayout());

        bysimilarityCheckBox.setText("Include those sites annotated by similarity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        statusPanel.add(bysimilarityCheckBox, gridBagConstraints);

        probableCheckBox.setText("Include those sites annotated as probable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        statusPanel.add(probableCheckBox, gridBagConstraints);

        potentialCheckBox.setText("Include those sites anntated as potential");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        statusPanel.add(potentialCheckBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(statusPanel, gridBagConstraints);

        optionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Other options"));
        optionPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        keepAllAccCheckBox.setText("Keep all accession numbers");
        optionPanel.add(keepAllAccCheckBox);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(optionPanel, gridBagConstraints);

        OKBtn.setText("   OK   ");
        OKBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKBtnActionPerformed(evt);
            }
        });
        OKPanel.add(OKBtn);

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });
        OKPanel.add(cancelBtn);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(OKPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void uniprotFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uniprotFileButtonActionPerformed
        JFileChooser fc = new JFileChooser(MusiteInit.defaultPath);
        ArrayList<String> exts = new ArrayList<String>(1);
        String ext = "xml";
        exts.add(ext);
        fc.setFileFilter(new FileExtensionsFilter(exts,"XML file (.xml)"));
        //fc.setAcceptAllFileFilterUsed(true);
        fc.setDialogTitle("Select a XML file...");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            MusiteInit.defaultPath = file.getParent();

            String filePath = MusiteInit.defaultPath + File.separator + file.getName();
            uniprotFileTextField.setText(filePath);

            String fileName = FilePathParser.getName(filePath);
            xmlFileTextField.setText(MusiteInit.defaultPath+File.separator+fileName+".xml");
        }
}//GEN-LAST:event_uniprotFileButtonActionPerformed

    private void xmlFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xmlFileButtonActionPerformed
        JFileChooser fc;
        String defFile = xmlFileTextField.getText();
        if (defFile.length()>0) {
            fc = new JFileChooser(FilePathParser.getDir(defFile));
        } else {
            fc = new JFileChooser(MusiteInit.defaultPath);
        }

        String ext = "xml";
        fc.setSelectedFile(new File(defFile));
        ArrayList<String> exts = new ArrayList<String>(1);
        exts.add(ext);
        fc.setFileFilter(new FileExtensionsFilter(exts,"XML file (.xml)"));
        //fc.setAcceptAllFileFilterUsed(true);
        fc.setDialogTitle("Save to...");
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            MusiteInit.defaultPath = file.getParent();

            String filePath = MusiteInit.defaultPath + File.separator + file.getName();
            xmlFileTextField.setText(filePath);
        }
    }//GEN-LAST:event_xmlFileButtonActionPerformed

    private void OKBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKBtnActionPerformed
        String dirUniprot = uniprotFileTextField.getText();
        if (dirUniprot.length()==0) {
            JOptionPane.showMessageDialog(this, "Error: specify a Uniprot XML file.");
            return;
        }

        String dirXml = xmlFileTextField.getText();
        if (dirXml.length()==0) {
            JOptionPane.showMessageDialog(this, "Error: specify the xml file to save.");
            return;
        }

        if (musite.util.IOUtil.fileExist(dirXml)) {
            int ret = JOptionPane.showConfirmDialog(this, "Are you sure to replace the existing xml file?",
                    null, JOptionPane.YES_NO_OPTION);
            if (ret==JOptionPane.NO_OPTION) {
                return;
            }
        }

        Set<PTM> ptms = new HashSet(ptmCombo.getSelectedItems());

        //Reading phospho data
        UniProtXMLReader uniprotReader = new UniProtXMLReader();
        uniprotReader.setPTMFilter(ptms); //TODO: make this a user option
        uniprotReader.setIncludeBySimilarity(bysimilarityCheckBox.isSelected());
        uniprotReader.setIncludeProbable(probableCheckBox.isSelected());
        uniprotReader.setIncludePotential(potentialCheckBox.isSelected());
        uniprotReader.setKeepAllIds(keepAllAccCheckBox.isSelected());
        ReadTask<Proteins> readTask = new ReadTask(uniprotReader, dirUniprot);
        TaskUtil.execute(readTask);
        if (!readTask.success()) {
            JOptionPane.showMessageDialog(this, "Failed to read the uniprot file");
            return;
        }
        Proteins proteins = readTask.getResultObject();

        //Write to xml file
        ProteinsXMLWriter writer = ProteinsXMLWriter.createWriter();
        WriteTask xmlWriteTask = new WriteTask(proteins, writer, dirXml);
        TaskUtil.execute(xmlWriteTask);
        if (!xmlWriteTask.success()) {
            JOptionPane.showMessageDialog(this, "Failed to write the xml file");
            return;
        }

        JOptionPane.showMessageDialog(this, "Successfully converted from Uniprot to Musite XML.");
        
        this.setVisible(false);
        this.dispose();
}//GEN-LAST:event_OKBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        setVisible(false);
        dispose();
}//GEN-LAST:event_cancelBtnActionPerformed

    private CheckComboBox ptmCombo;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton OKBtn;
    private javax.swing.JCheckBox bysimilarityCheckBox;
    private javax.swing.JCheckBox keepAllAccCheckBox;
    private javax.swing.JCheckBox potentialCheckBox;
    private javax.swing.JCheckBox probableCheckBox;
    private javax.swing.JTextField uniprotFileTextField;
    private javax.swing.JTextField xmlFileTextField;
    // End of variables declaration//GEN-END:variables

}
