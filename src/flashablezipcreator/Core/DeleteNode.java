/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashablezipcreator.Core;

import flashablezipcreator.Protocols.Types;

/**
 *
 * @author Nikhil
 */
public class DeleteNode extends ProjectItemNode {

    public String zipPath = "";

    public DeleteNode(String title, GroupNode parent) {
        super(title, Types.NODE_DELETE, parent);
    }

    public String getDeleteLocation() {
        return super.getTitle();//.replaceAll("_", "/");
    }

}
