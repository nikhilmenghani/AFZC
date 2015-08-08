/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package flashablezipcreator.Core;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Nikhil
 */
public class ProjectTree extends JFrame implements TreeSelectionListener {

    private JTree tree;
    
    public ProjectTree() throws IOException{
        super("Demo Project");
        setLayout(new BorderLayout());
        
//        ProjectItemNode rootNode = ProjectTreeBuilder.build();
//        TreeOperations to = new TreeOperations();
//        to.buildDirectory(rootNode);
//        TreeModel model = new ProjectTreeModel(rootNode);
//        tree = new JTree(model);
        //tree.setCellRenderer(new NodeRenderer());
        tree.setCellRenderer(new DefaultTreeCellRenderer());
        tree.addTreeSelectionListener(this);
        add(new JScrollPane(tree), BorderLayout.CENTER);
        
        for(int i = 0 ; i < tree.getRowCount(); i++){
            tree.expandRow(i);
        }
        setSize (200,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    @Override
    public void valueChanged(TreeSelectionEvent tse) {
        ProjectItemNode node = (ProjectItemNode)tree.getLastSelectedPathComponent();
        if(node == null){
            return;
        }
        
        JOptionPane.showMessageDialog(this, "You have selected: " + node.parent);
    }
    
    public static void main(String args[]){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            
        }
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                try {
                    new ProjectTree().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(ProjectTree.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
