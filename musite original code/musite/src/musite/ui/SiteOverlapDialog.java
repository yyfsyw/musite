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
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import musite.MusiteInit;
import musite.Proteins;
import musite.ProteinsUtil;
import musite.PTM;

import musite.io.Reader;
import musite.io.fasta.ModifiedProteinsFastaReaderBuilder;
import musite.io.xml.ProteinsXMLReader;

import musite.ui.task.AbstractTask;
import musite.ui.task.ReadTask;
import musite.ui.task.TaskUtil;

import musite.util.FileExtensionsFilter;

/**
 *
 * @author Jianjiong Gao
 */
public class SiteOverlapDialog extends javax.swing.JDialog {

    /** Creates new form Fasta2XmlDialog */
    public SiteOverlapDialog(java.awt.Frame parent, boolean modal) {
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

        javax.swing.JPanel ptmPanel = new javax.swing.JPanel();
        ptmComboBox = new javax.swing.JComboBox();
        javax.swing.JPanel fromPanel = new javax.swing.JPanel();
        fromScrollPane = new javax.swing.JScrollPane();
        fromList = new javax.swing.JList();
        javax.swing.JPanel addRmvFromPanel = new javax.swing.JPanel();
        javax.swing.JButton addFromButton = new javax.swing.JButton();
        javax.swing.JButton rmvFromButton = new javax.swing.JButton();
        javax.swing.JPanel OKPanel = new javax.swing.JPanel();
        OKBtn = new javax.swing.JButton();
        javax.swing.JButton cancelBtn = new javax.swing.JButton();
        javax.swing.JScrollPane resScrollPane = new javax.swing.JScrollPane();
        resTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Merging files");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        ptmPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("PTM type"));
        ptmPanel.setLayout(new java.awt.GridBagLayout());

        Vector vector = new Vector();
        vector.addAll(Arrays.asList(PTM.values()));
        ptmComboBox.setModel(new javax.swing.DefaultComboBoxModel(vector));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        ptmPanel.add(ptmComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(ptmPanel, gridBagConstraints);

        fromPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Musite XML files"));
        fromPanel.setLayout(new java.awt.GridBagLayout());

        fromScrollPane.setViewportView(fromList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        fromPanel.add(fromScrollPane, gridBagConstraints);

        addRmvFromPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        addFromButton.setText("Add a file");
        addFromButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFromButtonActionPerformed(evt);
            }
        });
        addRmvFromPanel.add(addFromButton);

        rmvFromButton.setText("Remove selected file(s)");
        rmvFromButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rmvFromButtonActionPerformed(evt);
            }
        });
        addRmvFromPanel.add(rmvFromButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        fromPanel.add(addRmvFromPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(fromPanel, gridBagConstraints);

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

        resScrollPane.setViewportView(resTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 4.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(resScrollPane, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OKBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKBtnActionPerformed
        if (fromFiles.size()<2) {
            JOptionPane.showMessageDialog(this, "Error: please select at lease two Musite XML files.");
            return;
        }

        final PTM ptm = (PTM)ptmComboBox.getSelectedItem();

        //Reading phospho data
        final ArrayList<Proteins> proteinsList = new ArrayList();
        for (String file : fromFiles) {
            Reader reader;
            if (file.toLowerCase().endsWith(".fasta")) {
                reader = new ModifiedProteinsFastaReaderBuilder()
                    .ptm(ptm).build();
            } else {
                reader = ProteinsXMLReader.createReader();
            }

            ReadTask<Proteins> readTask = new ReadTask(reader, file);
            TaskUtil.execute(readTask);
            if (!readTask.success()) {
                JOptionPane.showMessageDialog(this, "Failed to read the file:\n"+file);
                return;
            }

            proteinsList.add(readTask.getResultObject());
        }

        AbstractTask task = new AbstractTask("Calculating") {
            public void run() {
                    try {
                            taskMonitor.setPercentCompleted(-1);

                            taskMonitor.setStatus("Calculating.");

                            obj = ProteinsUtil.siteOverlap(proteinsList, ptm, ptm.getAminoAcids());

                            taskMonitor.setPercentCompleted(100);
                            taskMonitor.setStatus("done.");

                            success = true;
                    } catch (Exception e) {
                            taskMonitor.setPercentCompleted(100);
                            taskMonitor.setStatus("Failed to read.\n"+e.getMessage());
                            e.printStackTrace();
                            return;
                    }
            }
        };
        TaskUtil.execute(task);
        if (!task.success()) {
            JOptionPane.showMessageDialog(this, "Failed");
            return;
        }

        Map<String, Map<Integer, Set<Integer>>> overlap = (Map)task.getResultObject();

        // set the table
        Vector<Vector> tableData = new Vector();

        Vector vec;
        
        int n = fromFiles.size();
        for (int i=0; i<n; i++) {
            String file = fromFiles.get(i);
            vec = new Vector(2);
            tableData.add(vec);
            vec.add(i);
            vec.add(file);
        }

        vec = new Vector(2);
        tableData.add(vec);
        vec.add("");
        vec.add("");

        Map<String,Integer> count = countOverlap(overlap);
        for (Map.Entry<String,Integer> entry:count.entrySet()) {
            vec = new Vector(2);
            tableData.add(vec);
            vec.add(entry.getKey());
            vec.add(entry.getValue());
        }

        vec = new Vector(2);
        vec.add("");
        vec.add("");
        resTable.setModel(new javax.swing.table.DefaultTableModel(tableData,vec));
}//GEN-LAST:event_OKBtnActionPerformed

    private Map<String,Integer> countOverlap(Map<String, Map<Integer, Set<Integer>>> overlap) {
        Map<String,Integer> res = new TreeMap();
        for(Map<Integer, Set<Integer>> map : overlap.values()) {
            for (Set<Integer> set:map.values()) {
                String str = set.toString();
                Integer num = res.get(str);
                if (num==null) {
                    res.put(str, 1);
                } else {
                    res.put(str, num+1);
                }
            }
        }
        return res;
    }

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        setVisible(false);
        dispose();
}//GEN-LAST:event_cancelBtnActionPerformed

    private void addFromButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFromButtonActionPerformed
        JFileChooser fc = new JFileChooser(MusiteInit.defaultPath);
        ArrayList<String> exts = new ArrayList<String>(1);
        exts.add("fasta");
        fc.setFileFilter(new FileExtensionsFilter(exts,"Fasta file (.fasta)"));

        exts = new ArrayList<String>(1);
        exts.add("xml");
        fc.setFileFilter(new FileExtensionsFilter(exts,"Musite XML file (.xml)"));
        
        //fc.setAcceptAllFileFilterUsed(true);
        
        fc.setDialogTitle("Add a Musite data file...");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            MusiteInit.defaultPath = file.getParent();

            String filePath = MusiteInit.defaultPath + File.separator + file.getName();
            if (fromFiles.contains(filePath)) {
                JOptionPane.showMessageDialog(this, "This file has already been added.");
            }

            fromFiles.add(filePath);
            fromList.setListData(fromFiles);
        }
    }//GEN-LAST:event_addFromButtonActionPerformed

    private void rmvFromButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rmvFromButtonActionPerformed
        int[] idx = fromList.getSelectedIndices();
        for (int i=idx.length-1; i>=0; i--) {
            fromFiles.remove(idx[i]);
            fromList.setListData(fromFiles);
        }
    }//GEN-LAST:event_rmvFromButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton OKBtn;
    private javax.swing.JList fromList;
    private javax.swing.JScrollPane fromScrollPane;
    private javax.swing.JComboBox ptmComboBox;
    private javax.swing.JTable resTable;
    // End of variables declaration//GEN-END:variables
    Vector<String> fromFiles = new Vector();
}