/*
 * Copyright (c) 2007-2012 The Broad Institute, Inc.
 * SOFTWARE COPYRIGHT NOTICE
 * This software and its documentation are the copyright of the Broad Institute, Inc. All rights are reserved.
 *
 * This software is supplied without any warranty or guaranteed support whatsoever. The Broad Institute is not responsible for its use, misuse, or functionality.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 */

/*
 * GenomeSelectionDialog.java
 *
 * Created on November 8, 2007, 3:51 PM
 */

package org.broad.igv.ui;

import org.broad.igv.feature.genome.GenomeListItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog box for selecting genomes. User can choose from a list,
 * which is filtered according
 */
public class GenomeSelectionDialog extends javax.swing.JDialog {

    private boolean isCanceled = true;
    private GenomeListItem selectedItem = null;
    private List<GenomeListItem> allListItems;
    private DefaultListModel genomeListModel;

    public GenomeSelectionDialog(java.awt.Frame parent, Object[] allListItems, GenomeListItem defaultItem) {
        super(parent);
        initComponents();
        setLocationRelativeTo(parent);

        initData(allListItems, defaultItem);
    }

    private void initData(Object[] inputListItems, GenomeListItem defaultItem) {
        this.selectedItem = defaultItem;
        this.allListItems = new ArrayList<GenomeListItem>(inputListItems.length);
        for (Object listItem : inputListItems) {
            if (listItem instanceof GenomeListItem) {
                this.allListItems.add((GenomeListItem) listItem);
            }
        }
        rebuildGenomeList();

    }

    private void rebuildGenomeList() {
        String filterText = genomeEntry.getText().trim().toLowerCase();
        rebuildGenomeList(filterText);
    }

    /**
     * Filter the list of displayed genomes so we only show this
     * with the text the user entered.
     */
    private void rebuildGenomeList(String filterText) {
        if (genomeListModel == null) {
            genomeListModel = new DefaultListModel();
            genomeList.setModel(genomeListModel);
        }
        genomeListModel.clear();
        filterText = filterText.toLowerCase().trim();
        for (GenomeListItem listItem : allListItems) {
            if (listItem.getDisplayableName().toLowerCase().contains(filterText)) {
                genomeListModel.addElement(listItem);
            }
        }

    }


    public GenomeListItem getSelectedItem() {
        return selectedItem;//(isCanceled ? null : selectedItem);
    }

    private void genomeEntryKeyTyped(KeyEvent e) {

    }

    /**
     * If a genome is single clicked, we just store the selection.
     * When a genome is double clicked, we treat that as the user
     * wanting to load the genome.
     *
     * @param e
     */
    private void genomeListMouseClicked(MouseEvent e) {
        switch (e.getClickCount()) {
            case 1:
                selectedItem = (GenomeListItem) genomeList.getSelectedValue();
                break;
            case 2:
                okButtonActionPerformed(null);
                break;
        }
    }

    private void genomeEntryKeyReleased(KeyEvent e) {
        rebuildGenomeList(genomeEntry.getText());
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    private void initComponents() {
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        genomeEntry = new JTextField();
        scrollPane1 = new JScrollPane();
        genomeList = new JList();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setPreferredSize(new Dimension(250, 500));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

                //---- label1 ----
                label1.setText("Genomes");
                label1.setHorizontalAlignment(SwingConstants.LEFT);
                contentPanel.add(label1);

                //---- genomeEntry ----
                genomeEntry.setToolTipText("Filter genome list");
                genomeEntry.setMaximumSize(new Dimension(2147483647, 28));
                genomeEntry.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        genomeEntryKeyReleased(e);
                    }
                });
                contentPanel.add(genomeEntry);

                //======== scrollPane1 ========
                {

                    //---- genomeList ----
                    genomeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    genomeList.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            genomeListMouseClicked(e);
                        }
                    });
                    scrollPane1.setViewportView(genomeList);
                }
                contentPanel.add(scrollPane1);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 85, 80};
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed(e);
                    }
                });
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cancelButtonActionPerformed(e);
                    }
                });
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        isCanceled = true;
        selectedItem = null;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        isCanceled = false;
        selectedItem = (GenomeListItem) genomeList.getSelectedValue();
        setVisible(false);
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JTextField genomeEntry;
    private JScrollPane scrollPane1;
    private JList genomeList;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // End of variables declaration//GEN-END:variables

}
