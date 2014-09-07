/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.filetype;

import java.io.IOException;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@Messages({
    "LBL_PSML_LOADER=Files of PSML"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_PSML_LOADER",
        mimeType = "text/x-psml",
        extension = {"psml", "PSML", "psm", "PSM"}
        )
@DataObject.Registration(
        mimeType = "text/x-psml",
        iconBase = "org/bag3d/netbeans/psml/filetype/shapes.gif",
        displayName = "#LBL_PSML_LOADER",
        position = 300
        )
@ActionReferences({
    @ActionReference(
            path = "Loaders/text/x-psml/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200
            ),
    @ActionReference(
            path = "Loaders/text/x-psml/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300
            ),
    @ActionReference(
            path = "Loaders/text/x-psml/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500
            ),
    @ActionReference(
            path = "Loaders/text/x-psml/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600
            ),
    @ActionReference(
            path = "Loaders/text/x-psml/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800
            ),
    @ActionReference(
            path = "Loaders/text/x-psml/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000
            ),
    @ActionReference(
            path = "Loaders/text/x-psml/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200
            ),
    @ActionReference(
            path = "Loaders/text/x-psml/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300
            ),
    @ActionReference(
            path = "Loaders/text/x-psml/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400
            )
})
public class PSMLDataObject extends MultiDataObject {

    public PSMLDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/x-psml", true);
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @MultiViewElement.Registration(
            displayName = "#LBL_PSML_EDITOR",
            iconBase = "org/bag3d/netbeans/psml/filetype/shapes.gif",
            mimeType = "text/x-psml",
            persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
            preferredID = "PSML",
            position = 1000
            )
    @Messages("LBL_PSML_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MultiViewEditorElement(lkp);
    }

}
