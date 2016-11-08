/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

/**
 *
 * @author Nikhil
 */
public class FolderNode extends ProjectItemNode {
    
    public FolderNode(String title, int type, ProjectItemNode parent) {
        super(title, type, parent);
    }
    
    public FolderNode(String title, GroupNode parent){
        super(title, ProjectItemNode.NODE_FOLDER, parent);
    }
    
}
