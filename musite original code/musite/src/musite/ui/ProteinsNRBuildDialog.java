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

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import musite.MusiteInit;
import musite.Proteins;
import musite.PTM;

import musite.misc.nr.BlastClustProteinCluster;
import musite.misc.nr.CDHitProteinCluster;
import musite.misc.nr.ProteinCluster;
import musite.misc.nr.ProteinNR100Builder;
import musite.misc.nr.ProteinNRBuilderImpl;

import musite.io.xml.ProteinsXMLReader;
import musite.io.xml.ProteinsXMLWriter;

import musite.ui.task.ProteinsNRBuildTask;
import musite.ui.task.ReadTask;
import musite.ui.task.WriteTask;
import musite.ui.task.TaskUtil;

import musite.util.FileExtensionsFilter;
import musite.util.FilePathParser;

/**
 *
 * @author Jianjiong Gao
 */
public class ProteinsNRBuildDialog extends javax.swing.JDialog {

    /** Creates new form Fasta2XmlDialog */
    public ProteinsNRBuildDialog(java.awt.Frame parent, boolean modal) {
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

        javax.swing.ButtonGroup singleBothButtonGroup = new javax.swing.ButtonGroup();
        javax.swing.JPanel programPanel = new javax.swing.JPanel();
        programComboBox = new javax.swing.JComboBox();
        javax.swing.JPanel originalPanel = new javax.swing.JPanel();
        javax.swing.JPanel originalFilePanel = new javax.swing.JPanel();
        originalFileTextField = new javax.swing.JTextField();
        javax.swing.JButton originalFileButton = new javax.swing.JButton();
        javax.swing.JPanel targetPanel = new javax.swing.JPanel();
        javax.swing.JPanel targetFilePanel = new javax.swing.JPanel();
        targetFileTextField = new javax.swing.JTextField();
        javax.swing.JButton targetFileButton = new javax.swing.JButton();
        cdHitParameterPanel = new javax.swing.JPanel();
        javax.swing.JLabel identityLabel = new javax.swing.JLabel();
        identityTextField = new javax.swing.JTextField();
        javax.swing.JLabel identityPercLabel = new javax.swing.JLabel();
        blastClustParamPanel = new javax.swing.JPanel();
        javax.swing.JPanel simCovPanel = new javax.swing.JPanel();
        javax.swing.JLabel simLabel = new javax.swing.JLabel();
        simTextField = new javax.swing.JTextField();
        javax.swing.JLabel simDescLabel = new javax.swing.JLabel();
        javax.swing.JLabel covLabel = new javax.swing.JLabel();
        covTextField = new javax.swing.JTextField();
        javax.swing.JLabel covDescLabel = new javax.swing.JLabel();
        javax.swing.JPanel bothOrSinglePanel = new javax.swing.JPanel();
        bothRadioButton = new javax.swing.JRadioButton();
        singleRadioButton = new javax.swing.JRadioButton();
        javax.swing.JPanel OKPanel = new javax.swing.JPanel();
        OKBtn = new javax.swing.JButton();
        javax.swing.JButton cancelBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Non-redundant builder");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        programPanel.setVisible(false); //TODO: clear the code
        programPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Choose a NR dataset building program"));
        programPanel.setLayout(new javax.swing.BoxLayout(programPanel, javax.swing.BoxLayout.LINE_AXIS));

        programComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CD-Hit", "BLASTclust" }));
        programComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programComboBoxActionPerformed(evt);
            }
        });
        programPanel.add(programComboBox);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(programPanel, gridBagConstraints);

        originalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Redundant Musite XML file"));
        originalPanel.setLayout(new java.awt.GridBagLayout());

        originalFilePanel.setLayout(new java.awt.GridBagLayout());

        originalFileTextField.setToolTipText("Please select a FASTA training file");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        originalFilePanel.add(originalFileTextField, gridBagConstraints);

        originalFileButton.setText("Open");
        originalFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                originalFileButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        originalFilePanel.add(originalFileButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        originalPanel.add(originalFilePanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(originalPanel, gridBagConstraints);

        targetPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Save Non-Redundant Musite XML file to"));
        targetPanel.setMinimumSize(new java.awt.Dimension(400, 63));
        targetPanel.setPreferredSize(new java.awt.Dimension(500, 63));
        targetPanel.setLayout(new java.awt.GridBagLayout());

        targetFilePanel.setLayout(new java.awt.GridBagLayout());

        targetFileTextField.setToolTipText("Please select a FASTA training file");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        targetFilePanel.add(targetFileTextField, gridBagConstraints);

        targetFileButton.setText("Open");
        targetFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                targetFileButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        targetFilePanel.add(targetFileButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        targetPanel.add(targetFilePanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(targetPanel, gridBagConstraints);

        cdHitParameterPanel.setVisible(programComboBox.getSelectedIndex()==0);
        cdHitParameterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));
        cdHitParameterPanel.setLayout(new java.awt.GridBagLayout());

        identityLabel.setText("Identify threshold:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        cdHitParameterPanel.add(identityLabel, gridBagConstraints);

        identityTextField.setText("50");
        identityTextField.setPreferredSize(new java.awt.Dimension(60, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        cdHitParameterPanel.add(identityTextField, gridBagConstraints);

        identityPercLabel.setForeground(new java.awt.Color(153, 153, 153));
        identityPercLabel.setText("% (Integer between 40 and 100)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        cdHitParameterPanel.add(identityPercLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(cdHitParameterPanel, gridBagConstraints);

        blastClustParamPanel.setVisible(programComboBox.getSelectedIndex()==1);
        blastClustParamPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));
        blastClustParamPanel.setLayout(new java.awt.GridBagLayout());

        simCovPanel.setLayout(new java.awt.GridBagLayout());

        simLabel.setText("Similarity threshold:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        simCovPanel.add(simLabel, gridBagConstraints);

        simTextField.setText("50");
        simTextField.setPreferredSize(new java.awt.Dimension(60, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        simCovPanel.add(simTextField, gridBagConstraints);

        simDescLabel.setForeground(new java.awt.Color(153, 153, 153));
        simDescLabel.setText("% (Integer between 3 and 100)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        simCovPanel.add(simDescLabel, gridBagConstraints);

        covLabel.setText("Coverage threshold:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        simCovPanel.add(covLabel, gridBagConstraints);

        covTextField.setText("0.9");
        covTextField.setPreferredSize(new java.awt.Dimension(60, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        simCovPanel.add(covTextField, gridBagConstraints);

        covDescLabel.setForeground(new java.awt.Color(153, 153, 153));
        covDescLabel.setText("(Number between 0.0 and 1.0)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        simCovPanel.add(covDescLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        blastClustParamPanel.add(simCovPanel, gridBagConstraints);

        bothOrSinglePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Require coverage on both or only one sequence of a pair?"));
        bothOrSinglePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        singleBothButtonGroup.add(bothRadioButton);
        bothRadioButton.setText("Both");
        bothOrSinglePanel.add(bothRadioButton);

        singleBothButtonGroup.add(singleRadioButton);
        singleRadioButton.setSelected(true);
        singleRadioButton.setText("Single");
        bothOrSinglePanel.add(singleRadioButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        blastClustParamPanel.add(bothOrSinglePanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(blastClustParamPanel, gridBagConstraints);

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

    private void originalFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_originalFileButtonActionPerformed
        JFileChooser fc = new JFileChooser(MusiteInit.defaultPath);
        ArrayList<String> exts = new ArrayList<String>(1);
        String ext = "xml";
        exts.add(ext);
        fc.setFileFilter(new FileExtensionsFilter(exts,"XML file (.xml)"));
        
        //fc.setAcceptAllFileFilterUsed(true);
        fc.setDialogTitle("Select a Musite XML file...");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            MusiteInit.defaultPath = file.getParent();

            String filePath = MusiteInit.defaultPath + File.separator + file.getName();
            originalFileTextField.setText(filePath);

            String fileName = FilePathParser.getName(filePath);
            String saveTo = MusiteInit.defaultPath+File.separator+fileName+".nr.xml";
            targetFileTextField.setText(saveTo);
        }
}//GEN-LAST:event_originalFileButtonActionPerformed

    private void targetFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_targetFileButtonActionPerformed
        JFileChooser fc;
        String defFile = targetFileTextField.getText();
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
            targetFileTextField.setText(filePath);
        }
    }//GEN-LAST:event_targetFileButtonActionPerformed

    private void OKBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKBtnActionPerformed
        String dirOriginal = originalFileTextField.getText();
        if (dirOriginal.length()==0) {
            JOptionPane.showMessageDialog(this, "Error: specify a file containing the protein sequences.");
            return;
        }

        String dirXml = targetFileTextField.getText();
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

        int identity = 50;
        int sim = 50;
        double cov = 0.9;
        boolean both = bothRadioButton.isSelected();

        boolean cdhit = programComboBox.getSelectedIndex()==0;
        if (cdhit) {
            String strIdentity = identityTextField.getText();
            if (strIdentity.length()==0) {
                JOptionPane.showMessageDialog(this, "Error: no similarity threshold specified.");
                return;
            }

            try {
                identity = Integer.parseInt(strIdentity);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: identify threshold can only be an integer number between 40 and 100.");
                return;
            }

            if (identity<40 || identity>100) {
                JOptionPane.showMessageDialog(this, "Error: similarity threshold can only be an integer number between 40 and 100.");
                return;
            }
        } else { // blastclust
            String strSim = simTextField.getText();
            if (strSim.length()==0) {
                JOptionPane.showMessageDialog(this, "Error: no similarity threshold specified.");
                return;
            }

            try {
                sim = Integer.parseInt(strSim);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: similarity threshold can only be an integer number between 3 and 100.");
                return;
            }

            if (sim<3 || sim>100) {
                JOptionPane.showMessageDialog(this, "Error: similarity threshold can only be an integer number between 3 and 100.");
                return;
            }

            String strCov = covTextField.getText();
            if (strSim.length()==0) {
                JOptionPane.showMessageDialog(this, "Error: no coverage threshold specified.");
                return;
            }

            try {
                cov = Double.parseDouble(strCov);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: coverage threshold can only be an number between 0 and 0.1.");
                return;
            }

            if (cov<0 || cov>1) {
                JOptionPane.showMessageDialog(this, "Error: coverage threshold can only be an number between 0 and 0.1.");
                return;
            }
        }

        //Reading phospho data
        ProteinsXMLReader reader = ProteinsXMLReader.createReader();

        ReadTask<Proteins> readTask = new ReadTask(reader,dirOriginal);
        TaskUtil.execute(readTask);
        if (!readTask.success()) {
            JOptionPane.showMessageDialog(this, "Failed to read the original file");
            return;
        }
        Proteins proteins = readTask.getResultObject();

        // Build nr100 to save some time
        // and to merge information (e.g. phosphosites) for identical protein
        ProteinNR100Builder nr100Builder = new ProteinNR100Builder();
        ProteinsNRBuildTask nr100Task = new ProteinsNRBuildTask(nr100Builder, proteins);
        TaskUtil.execute(nr100Task);
        if (!nr100Task.success()) {
            JOptionPane.showMessageDialog(this, "Failed to build the nr db.");
            return;
        }
        proteins = nr100Task.getNRProteins();

        // Build nr
        ProteinCluster cluster = null;
        if (cdhit) {
            if (identity<100) {
                String dirCDHit = MusiteInit.globalProps.getProperty(MusiteInit.GLOBAL_PROP_CDHIT_FILE);
                cluster = new CDHitProteinCluster(dirCDHit, identity);
            }
        } else {
            if (sim<100 || cov<1 || !both) { // if not exactly same
                String blosumDir = MusiteInit.MATRIX_DIR;
                String dirBlastClust = MusiteInit.globalProps.getProperty(MusiteInit.GLOBAL_PROP_BLAST_CLUST_FILE);
                cluster = new BlastClustProteinCluster(
                        dirBlastClust, blosumDir, sim, cov, both);
            }
        }

        if (cluster!=null) {
            PTM ptm = PTM.PHOSPHORYLATION; // TODO: make this an option?
            ProteinNRBuilderImpl nrBuilder = new ProteinNRBuilderImpl(cluster, ptm);

            ProteinsNRBuildTask nrTask = new ProteinsNRBuildTask(nrBuilder, proteins);
            TaskUtil.execute(nrTask);
            if (!nrTask.success()) {
                JOptionPane.showMessageDialog(this, "Failed to build the nr db.");
                return;
            }
            proteins = nrTask.getNRProteins();
        }

        //Write to xml file
        ProteinsXMLWriter writer = ProteinsXMLWriter.createWriter();
        WriteTask xmlWriteTask = new WriteTask(proteins, writer, dirXml);
        TaskUtil.execute(xmlWriteTask);
        if (!xmlWriteTask.success()) {
            JOptionPane.showMessageDialog(this, "Failed to write the xml file");
            return;
        }

        JOptionPane.showMessageDialog(this, "Done.");
        
        this.setVisible(false);
        this.dispose();
}//GEN-LAST:event_OKBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        setVisible(false);
        dispose();
}//GEN-LAST:event_cancelBtnActionPerformed

    private void programComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programComboBoxActionPerformed
        boolean cdhit = programComboBox.getSelectedIndex()==0;
        cdHitParameterPanel.setVisible(cdhit);
        blastClustParamPanel.setVisible(!cdhit);
        pack();
    }//GEN-LAST:event_programComboBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton OKBtn;
    private javax.swing.JPanel blastClustParamPanel;
    private javax.swing.JRadioButton bothRadioButton;
    private javax.swing.JPanel cdHitParameterPanel;
    private javax.swing.JTextField covTextField;
    private javax.swing.JTextField identityTextField;
    private javax.swing.JTextField originalFileTextField;
    private javax.swing.JComboBox programComboBox;
    private javax.swing.JTextField simTextField;
    private javax.swing.JRadioButton singleRadioButton;
    private javax.swing.JTextField targetFileTextField;
    // End of variables declaration//GEN-END:variables

}