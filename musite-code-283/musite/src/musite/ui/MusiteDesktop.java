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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import musite.MusiteInit;

import musite.prediction.PredictionResult;

import musite.io.xml.PredictionResultXMLReader;

import musite.ui.task.ReadTask;
import musite.ui.task.TaskUtil;
import musite.ui.util.JTabbedPaneWithCloseIcons;

import musite.util.FileExtensionsFilter;
import musite.util.FilePathParser;

/**
 *
 * @author Jianjiong Gao
 */
public class MusiteDesktop extends javax.swing.JFrame {

    /** Creates new form MusiteMainUI */
    public MusiteDesktop() {
        predictionPanel = new MusitePredictionPanel();
        resultPanels = new ArrayList();
        initComponents();
        setupMainTabPane();
        installCloseHandler();
    }

    private void installCloseHandler() {
        mainTabbedPane.addTabActionListener(JTabbedPaneWithCloseIcons.DELETE_ACTION,
                new JTabbedPaneWithCloseIcons.TabActionListener() {
            public void actionOnTab(int tabNumber) {
                MusiteResultPanel panel = resultPanels.get(tabNumber-1); // the first one is the prediction panel
                if (!panel.isSaved()) {
                    int ret = JOptionPane.showConfirmDialog(MusiteDesktop.this, "Would you like to save the prediction result before closing?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (ret == JOptionPane.CANCEL_OPTION) {
                        return;
                    } else if (ret == JOptionPane.YES_OPTION) {
                        if (!panel.saveResult(false)) {
                            return;
                        }
                    }
                }
                
                panel.dispose();
                removeResultPanel(panel);
            }            
        });

        mainTabbedPane.addTabActionListener(JTabbedPaneWithCloseIcons.RENAME_ACTION,
                new JTabbedPaneWithCloseIcons.TabActionListener() {
            public void actionOnTab(int tabNumber) {
                MusiteResultPanel panel = resultPanels.get(tabNumber-1); // the first one is the prediction panel
                JTextField textField = textField = new JTextField(10);
                textField.setText(panel.getPanelName());
                int ret = JOptionPane.showOptionDialog(MusiteDesktop.this, textField,
                        "Rename to", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, null, null);
                if(ret == JOptionPane.OK_OPTION) {
                    String name = textField.getText();
                    if (name==null||name.length()==0) {
                        JOptionPane.showMessageDialog(MusiteDesktop.this, "Name can not be empty.");
                        return;
                    }
                    mainTabbedPane.setTitleAt(tabNumber, name);
                    panel.setPanelName(name);
               }
            }
        });

        // deactivate default handler
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // now install
        addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            boolean saved = true;
            for (MusiteResultPanel panel : resultPanels) {
                if (!panel.isSaved()) {
                    saved = false;
                    break;
                }
            }

            if (saved)
                System.exit(0);

            int result = JOptionPane.showConfirmDialog(MusiteDesktop.this,
                    "Would you like to save the predicted result(s) before exiting Musite?",
                    "Save before exit?", JOptionPane.YES_NO_CANCEL_OPTION);
            if (result == JOptionPane.NO_OPTION) {
                System.exit(0);
            } else if (result == JOptionPane.YES_OPTION) {
                for (MusiteResultPanel panel : resultPanels) {
                    if (!panel.isSaved()) {
                        if (!panel.saveResult(false)) {
//                                int ret = JOptionPane.showConfirmDialog(MusiteDesktop.this,
//                                        "Would you still like to exit Musite?",
//                                        "Exit?", JOptionPane.YES_NO_OPTION);
//                                if (ret == JOptionPane.YES_OPTION) {
//                                    System.exit(0);
//                                } else
                            return;
                        }
                    }
                }

                System.exit(0);
            } 
        }
    });
}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JMenuBar MusiteMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem manageModelMenuItem = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        javax.swing.JMenuItem loadResultMenuItem = new javax.swing.JMenuItem();
        saveResultMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu toolsMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem trainMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu featureExtractionMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem disorderMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu fileProcessingMenu = new javax.swing.JMenu();
        javax.swing.JMenu fileConversionMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem fasta2xmlMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem xml2fastaMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem siteAnnotation2xmlMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem uniprot2MusiteMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem phosphoelm2MusiteMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem phosphat2xmlMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem phosphoPeptides2MusiteMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu infoRetriveMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem accessionExportMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem extractSurrMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu fileFilteringMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem filterProteinByAccMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem filterProteinByOrganismMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem ptmFilterMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem filterProteinInformationMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem filterResidueAnnotationMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu statsticsMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aaStatisticsMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem enzymeStatisticsMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem siteOverlapMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem nrBuildMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem mergeMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem supportMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Musite");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        fileMenu.setText("File");
        fileMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fileMenuMouseEntered(evt);
            }
        });

        manageModelMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        manageModelMenuItem.setText("Manage Trained Models");
        manageModelMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageModelMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(manageModelMenuItem);
        fileMenu.add(jSeparator5);

        loadResultMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        loadResultMenuItem.setText("Open Predicted Result");
        loadResultMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadResultMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(loadResultMenuItem);

        saveResultMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveResultMenuItem.setText("Save Predicted Result");
        saveResultMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveResultMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveResultMenuItem);

        MusiteMenuBar.add(fileMenu);

        toolsMenu.setText("Tools");

        trainMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        trainMenuItem.setText("Prediction Model Training");
        trainMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trainMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(trainMenuItem);

        featureExtractionMenu.setText("Feature Extraction");

        disorderMenuItem.setText("Disorder Prediction");
        disorderMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disorderMenuItemActionPerformed(evt);
            }
        });
        featureExtractionMenu.add(disorderMenuItem);

        toolsMenu.add(featureExtractionMenu);

        fileProcessingMenu.setText("File Processing");

        fileConversionMenu.setText("File Conversion");

        fasta2xmlMenuItem.setText("Convert FASTA to Musite XML");
        fasta2xmlMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fasta2xmlMenuItemActionPerformed(evt);
            }
        });
        fileConversionMenu.add(fasta2xmlMenuItem);

        xml2fastaMenuItem.setText("Convert Musite XML to FASTA");
        xml2fastaMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xml2fastaMenuItemActionPerformed(evt);
            }
        });
        fileConversionMenu.add(xml2fastaMenuItem);

        siteAnnotation2xmlMenuItem.setText("Convert Sites Report to Musite XML");
        siteAnnotation2xmlMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siteAnnotation2xmlMenuItemActionPerformed(evt);
            }
        });
        fileConversionMenu.add(siteAnnotation2xmlMenuItem);

        uniprot2MusiteMenuItem.setText("Convert UniProt XML to Musite XML");
        uniprot2MusiteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uniprot2MusiteMenuItemActionPerformed(evt);
            }
        });
        fileConversionMenu.add(uniprot2MusiteMenuItem);

        phosphoelm2MusiteMenuItem.setText("Convert Phospho.ELM Report to Musite XML");
        phosphoelm2MusiteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phosphoelm2MusiteMenuItemActionPerformed(evt);
            }
        });
        fileConversionMenu.add(phosphoelm2MusiteMenuItem);

        phosphat2xmlMenuItem.setText("Convert PhosPhAt Report to Musite XML");
        phosphat2xmlMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phosphat2xmlMenuItemActionPerformed(evt);
            }
        });
        fileConversionMenu.add(phosphat2xmlMenuItem);

        phosphoPeptides2MusiteMenuItem.setText("Convert Phosphopeptides to Musite XML");
        phosphoPeptides2MusiteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phosphoPeptides2MusiteMenuItemActionPerformed(evt);
            }
        });
        fileConversionMenu.add(phosphoPeptides2MusiteMenuItem);

        fileProcessingMenu.add(fileConversionMenu);

        infoRetriveMenu.setText("Information Retrieving");

        accessionExportMenuItem.setText("Retrieve Accessions from Musite XML");
        accessionExportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accessionExportMenuItemActionPerformed(evt);
            }
        });
        infoRetriveMenu.add(accessionExportMenuItem);

        extractSurrMenuItem.setText("Extract Surrounding Sequences of Sites from Musite XML");
        extractSurrMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                extractSurrMenuItemActionPerformed(evt);
            }
        });
        infoRetriveMenu.add(extractSurrMenuItem);

        fileProcessingMenu.add(infoRetriveMenu);

        fileFilteringMenu.setText("File Filtering");

        filterProteinByAccMenuItem.setText("Filter proteins by accessions");
        filterProteinByAccMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterProteinByAccMenuItemActionPerformed(evt);
            }
        });
        fileFilteringMenu.add(filterProteinByAccMenuItem);

        filterProteinByOrganismMenuItem.setText("Filter proteins by organism");
        filterProteinByOrganismMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterProteinByOrganismMenuItemActionPerformed(evt);
            }
        });
        fileFilteringMenu.add(filterProteinByOrganismMenuItem);

        ptmFilterMenuItem.setText("Filter proteins by PTM");
        ptmFilterMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ptmFilterMenuItemActionPerformed(evt);
            }
        });
        fileFilteringMenu.add(ptmFilterMenuItem);

        filterProteinInformationMenuItem.setText("Filter protein information");
        filterProteinInformationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterProteinInformationMenuItemActionPerformed(evt);
            }
        });
        fileFilteringMenu.add(filterProteinInformationMenuItem);

        filterResidueAnnotationMenuItem.setText("Filter Residue Annotations by PTM and enzymes");
        filterResidueAnnotationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterResidueAnnotationMenuItemActionPerformed(evt);
            }
        });
        fileFilteringMenu.add(filterResidueAnnotationMenuItem);

        fileProcessingMenu.add(fileFilteringMenu);

        statsticsMenu.setText("Statistics");

        aaStatisticsMenuItem.setText("Sites Statistics");
        aaStatisticsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aaStatisticsMenuItemActionPerformed(evt);
            }
        });
        statsticsMenu.add(aaStatisticsMenuItem);

        enzymeStatisticsMenuItem.setText("Enzyme Statistics");
        enzymeStatisticsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enzymeStatisticsMenuItemActionPerformed(evt);
            }
        });
        statsticsMenu.add(enzymeStatisticsMenuItem);

        siteOverlapMenuItem.setText("Site Overlap Among Files");
        siteOverlapMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siteOverlapMenuItemActionPerformed(evt);
            }
        });
        statsticsMenu.add(siteOverlapMenuItem);

        fileProcessingMenu.add(statsticsMenu);

        nrBuildMenuItem.setText("Build Non-Redundant Dataset");
        nrBuildMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nrBuildMenuItemActionPerformed(evt);
            }
        });
        fileProcessingMenu.add(nrBuildMenuItem);

        mergeMenuItem.setText("Merge Musite XML Files");
        mergeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mergeMenuItemActionPerformed(evt);
            }
        });
        fileProcessingMenu.add(mergeMenuItem);

        toolsMenu.add(fileProcessingMenu);

        MusiteMenuBar.add(toolsMenu);

        helpMenu.setText("Help");

        supportMenuItem.setText("Support");
        supportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supportMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(supportMenuItem);

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        MusiteMenuBar.add(helpMenu);

        setJMenuBar(MusiteMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void trainMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trainMenuItemActionPerformed
        MusiteTrainDialog dialog = new MusiteTrainDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_trainMenuItemActionPerformed

    private void manageModelMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageModelMenuItemActionPerformed
        ModelManageFrame frame = new ModelManageFrame();
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }//GEN-LAST:event_manageModelMenuItemActionPerformed

    private void disorderMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disorderMenuItemActionPerformed
        DisorderPredictionDialog dialog = new DisorderPredictionDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_disorderMenuItemActionPerformed

    private void fasta2xmlMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fasta2xmlMenuItemActionPerformed
        Fasta2XmlDialog dialog = new Fasta2XmlDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_fasta2xmlMenuItemActionPerformed

    private void uniprot2MusiteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uniprot2MusiteMenuItemActionPerformed
        Uniprot2MusiteDialog dialog = new Uniprot2MusiteDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_uniprot2MusiteMenuItemActionPerformed

    private void xml2fastaMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xml2fastaMenuItemActionPerformed
        Xml2FastaDialog dialog = new Xml2FastaDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_xml2fastaMenuItemActionPerformed

    private void aaStatisticsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aaStatisticsMenuItemActionPerformed
        AminoAcidStatisticsDialog dialog = new AminoAcidStatisticsDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_aaStatisticsMenuItemActionPerformed

    private void mergeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mergeMenuItemActionPerformed
        MergeFileDialog dialog = new MergeFileDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_mergeMenuItemActionPerformed

    private void nrBuildMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nrBuildMenuItemActionPerformed
        ProteinsNRBuildDialog dialog = new ProteinsNRBuildDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_nrBuildMenuItemActionPerformed

    private void loadResultMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadResultMenuItemActionPerformed
        JFileChooser fc = new JFileChooser(MusiteInit.defaultPath);

        ArrayList<String> exts = new ArrayList<String>(2);
        exts.add(".xml");
        exts.add(".xml.gz");
        fc.addChoosableFileFilter(new FileExtensionsFilter(exts,"XML file (.xml, .xml.gz)"));

        exts = new ArrayList<String>(2);
        exts.add("pred.xml");
        exts.add("pred.xml.gz");
        fc.addChoosableFileFilter(new FileExtensionsFilter(exts,"Musite Prediction file (.pred.xml, .pred.xml.gz)"));

        fc.setDialogTitle("Select a file...");
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            MusiteInit.defaultPath = file.getParent();

            String filePath = MusiteInit.defaultPath + File.separator + file.getName();

            PredictionResultXMLReader reader = PredictionResultXMLReader.createReader();
            ReadTask<PredictionResult> task = new ReadTask(reader, filePath);
            TaskUtil.execute(task);

            if (!task.success()) {
                JOptionPane.showMessageDialog(this, "Failed to load the result.");
                return;
            }
            PredictionResult result = task.getResultObject();

            String name = FilePathParser.getName(filePath);
            if (name.endsWith(".xml"))
                name = name.substring(0,name.length()-4);
            if (name.endsWith(".pred"))
                name = name.substring(0, name.length()-5);
            try {
                MusiteResultPanel resultPanel
                        = new MusiteResultPanel(name, result, true);
                addResultPanel(resultPanel);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to load!");
                return;
            }
            JOptionPane.showMessageDialog(this, "Loaded!");
        }
    }//GEN-LAST:event_loadResultMenuItemActionPerformed

    private void accessionExportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accessionExportMenuItemActionPerformed
        AccessionExportDialog dialog = new AccessionExportDialog(this,true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_accessionExportMenuItemActionPerformed

    private void fileMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileMenuMouseEntered
        boolean resultTabSelected = mainTabbedPane.getSelectedIndex()>0;
        saveResultMenuItem.setEnabled(resultTabSelected);
    }//GEN-LAST:event_fileMenuMouseEntered

    private void saveResultMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveResultMenuItemActionPerformed
        int iTab = mainTabbedPane.getSelectedIndex();
        MusiteResultPanel panel = getResultPanels().get(iTab-1);
        panel.saveResult(true);
    }//GEN-LAST:event_saveResultMenuItemActionPerformed

    private void extractSurrMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_extractSurrMenuItemActionPerformed
        SurroundSequenceExtractDialog dialog = new SurroundSequenceExtractDialog(this,true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_extractSurrMenuItemActionPerformed

    private void phosphoelm2MusiteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phosphoelm2MusiteMenuItemActionPerformed
        Phosphoelm2MusiteDialog dialog = new Phosphoelm2MusiteDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_phosphoelm2MusiteMenuItemActionPerformed

    private void filterProteinByAccMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterProteinByAccMenuItemActionPerformed
        ProteinsFilterByAccessionDialog dialog = new ProteinsFilterByAccessionDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_filterProteinByAccMenuItemActionPerformed

    private void filterProteinByOrganismMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterProteinByOrganismMenuItemActionPerformed
        ProteinsFilterByOrganismDialog dialog = new ProteinsFilterByOrganismDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_filterProteinByOrganismMenuItemActionPerformed

    private void filterProteinInformationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterProteinInformationMenuItemActionPerformed
        ProteinsInfomationFilterDialog dialog = new ProteinsInfomationFilterDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_filterProteinInformationMenuItemActionPerformed

    private void filterResidueAnnotationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterResidueAnnotationMenuItemActionPerformed
        ResidueAnnotationFilterByPTMEnzymeDialog dialog = new ResidueAnnotationFilterByPTMEnzymeDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_filterResidueAnnotationMenuItemActionPerformed

    private void phosphat2xmlMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phosphat2xmlMenuItemActionPerformed
        Phosphat2MusiteDialog dialog = new Phosphat2MusiteDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_phosphat2xmlMenuItemActionPerformed

    private void phosphoPeptides2MusiteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phosphoPeptides2MusiteMenuItemActionPerformed
        PhosphoPeptide2MusiteDialog dialog = new PhosphoPeptide2MusiteDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_phosphoPeptides2MusiteMenuItemActionPerformed

    private void siteAnnotation2xmlMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siteAnnotation2xmlMenuItemActionPerformed
        ReadResidueAnnotationDialog dialog = new ReadResidueAnnotationDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_siteAnnotation2xmlMenuItemActionPerformed

    private void enzymeStatisticsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enzymeStatisticsMenuItemActionPerformed
        EnzymeStatisticsDialog dialog = new EnzymeStatisticsDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_enzymeStatisticsMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        AboutDialog dialog = new AboutDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void supportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supportMenuItemActionPerformed
        SupportDialog dialog = new SupportDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_supportMenuItemActionPerformed

    private void siteOverlapMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siteOverlapMenuItemActionPerformed
        SiteOverlapDialog dialog = new SiteOverlapDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_siteOverlapMenuItemActionPerformed

    private void ptmFilterMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ptmFilterMenuItemActionPerformed
        ProteinsFilterByPTMDialog dialog = new ProteinsFilterByPTMDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_ptmFilterMenuItemActionPerformed

    private void setupMainTabPane() {
        mainTabbedPane = new musite.ui.util.JTabbedPaneWithCloseIcons();
        
        mainTabbedPane.setMinimumSize(new java.awt.Dimension(400, 300));
        mainTabbedPane.setPreferredSize(new java.awt.Dimension(800, 600));

        mainTabbedPane.addTab("Prediction Panel", predictionPanel);

        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(mainTabbedPane, gridBagConstraints);
        pack();
    }

    public MusitePredictionPanel getPredictionPanel() {
        return predictionPanel;
    }

    public List<MusiteResultPanel> getResultPanels() {
        return resultPanels;
    }

    void addResultPanel(MusiteResultPanel panel) {
        resultPanels.add(panel);
        mainTabbedPane.addClosableTab(panel.getPanelName(), panel);
        mainTabbedPane.setSelectedComponent(panel);
    }

    void removeResultPanel(MusiteResultPanel panel) {
        int idx = resultPanels.indexOf(panel);
        if (idx!=-1) {
            mainTabbedPane.removeTabAt(idx+1);
            resultPanels.remove(panel);
        }

        //mainTabbedPane.setSelectedIndex(0);
    }

    private MusitePredictionPanel predictionPanel;
    private List<MusiteResultPanel> resultPanels;
    private musite.ui.util.JTabbedPaneWithCloseIcons mainTabbedPane;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JMenuItem saveResultMenuItem;
    // End of variables declaration//GEN-END:variables

}
