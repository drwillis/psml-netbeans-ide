/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bag3d.netbeans.psml.project;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.tools.ant.module.api.support.ActionUtils;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.CopyOperationImplementation;
import org.netbeans.spi.project.DeleteOperationImplementation;
import org.netbeans.spi.project.MoveOrRenameOperationImplementation;
import org.netbeans.spi.project.support.ant.AntBasedProjectRegistration;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author arwillis
 */
@AntBasedProjectRegistration(
        type = AntBasedProject.TYPE,
        iconResource = AntBasedProject.ICON_RESOURCE,
        sharedName = AntBasedProject.NAME_SHARED,
        sharedNamespace = AntBasedProject.NAME_SPACE_SHARED,
        privateName = AntBasedProject.NAME_PRIVATE,
        privateNamespace = AntBasedProject.NAME_SPACE_PRIVATE
)
public class AntBasedProject implements Project {

    public static final String TYPE = "org.bag3d.netbeans.psml.project";
    public static final String NAME_SPACE_SHARED = "http://visionlab.homelinux.org/ns/psml-project/1";
    public static final String NAME_SHARED = "data";
    public static final String NAME_PRIVATE = "project-private";
    public static final String NAME_SPACE_PRIVATE = "http://visionlab.homelinux.org/ns/psml-project-private/1";
    public static final String ICON_RESOURCE = "org/bag3d/netbeans/psml/project/resources/project_icon.png";

    public static final String PROJECT_PSML_SRCDIR = "psml";
    public static final String PROJECT_JAVA_SRCDIR = "java/src";
    final AntProjectHelper helper;

    public AntBasedProject(AntProjectHelper helper) {
        this.helper = helper;
    }

    @Override
    public Lookup getLookup() {
        return Lookups.fixed(new Object[]{
            this,
            helper,
            new Info(),
            new AntBasedProjectLogicalView(this),
            new AntBasedActionProvider(),
            new AntBasedProjectMoveOrRenameOperation(),
            new AntBasedProjectCopyOperation(),
            new AntBasedProjectDeleteOperation(this)
        });
    }

    @Override
    public FileObject getProjectDirectory() {
        return helper.getProjectDirectory();
    }

    FileObject getSubFolder(String foldername, boolean create) {
        FileObject result
                = getProjectDirectory().getFileObject(foldername);
        if (result == null && create) {
            try {
                result = getProjectDirectory().createFolder(foldername);
            } catch (IOException ioe) {
                Exceptions.printStackTrace(ioe);
            }
        }
        return result;
    }

    private final class Info implements ProjectInformation {

        @Override
        public String getName() {
            return helper.getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(
                    ICON_RESOURCE));
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
        }

        @Override
        public Project getProject() {
            return AntBasedProject.this;
        }
    }

    private final class AntBasedActionProvider implements ActionProvider {

        private final String[] supported = new String[]{
            ActionProvider.COMMAND_MOVE,
            ActionProvider.COMMAND_RENAME,
            ActionProvider.COMMAND_DELETE,
            ActionProvider.COMMAND_COPY,
            ActionProvider.COMMAND_BUILD,
            ActionProvider.COMMAND_REBUILD,
            ActionProvider.COMMAND_CLEAN,
            ActionProvider.COMMAND_COMPILE_SINGLE,
            ActionProvider.COMMAND_RUN,
            ActionProvider.COMMAND_RUN_SINGLE
        };

        @Override
        public String[] getSupportedActions() {
            return supported;
        }

        @Override
        public void invokeAction(String string, Lookup lookup) throws IllegalArgumentException {
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_RENAME)) {
                DefaultProjectOperations.performDefaultRenameOperation(AntBasedProject.this, "");
            }
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_MOVE)) {
                DefaultProjectOperations.performDefaultMoveOperation(AntBasedProject.this);
            }
            if (string.equals(ActionProvider.COMMAND_DELETE)) {
                DefaultProjectOperations.performDefaultDeleteOperation(AntBasedProject.this);
            }
            if (string.equals(ActionProvider.COMMAND_COPY)) {
                DefaultProjectOperations.performDefaultCopyOperation(AntBasedProject.this);
            }
            //Here we find the Ant script and call the target we need!
            if (string.equals(ActionProvider.COMMAND_BUILD)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"compile"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (string.equals(ActionProvider.COMMAND_REBUILD)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"clean","compile"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (string.equals(ActionProvider.COMMAND_CLEAN)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"clean"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }            
            if (string.equals(ActionProvider.COMMAND_COMPILE_SINGLE)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"compile-single"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (string.equals(ActionProvider.COMMAND_RUN)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"run"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (string.equals(ActionProvider.COMMAND_RUN_SINGLE)) {
                try {
                    FileObject buildImpl = helper.getProjectDirectory().getFileObject("build.xml");
                    ActionUtils.runTarget(buildImpl, new String[]{"run-single"}, null);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

        @Override
        public boolean isActionEnabled(String command, Lookup lookup) throws IllegalArgumentException {
            if ((command.equals(ActionProvider.COMMAND_RENAME))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_MOVE))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_DELETE))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_COPY))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_BUILD))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_REBUILD))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_CLEAN))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_COMPILE_SINGLE))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_RUN))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_RUN_SINGLE))) {
                return true;
            } else {
                throw new IllegalArgumentException(command);
            }
        }
    }

    private final class AntBasedProjectMoveOrRenameOperation implements MoveOrRenameOperationImplementation {

        @Override
        public List<FileObject> getMetadataFiles() {
            return new ArrayList<FileObject>();
        }

        @Override
        public List<FileObject> getDataFiles() {
            return new ArrayList<FileObject>();
        }

        @Override
        public void notifyRenaming() throws IOException {
        }

        @Override
        public void notifyRenamed(String nueName) throws IOException {
        }

        @Override
        public void notifyMoving() throws IOException {
        }

        @Override
        public void notifyMoved(Project original, File originalPath, String nueName) throws IOException {
        }
    }

    private final class AntBasedProjectCopyOperation implements CopyOperationImplementation {

        @Override
        public List<FileObject> getMetadataFiles() {
            return new ArrayList<FileObject>();
        }

        @Override
        public List<FileObject> getDataFiles() {
            return new ArrayList<FileObject>();
        }

        @Override
        public void notifyCopying() throws IOException {
        }

        @Override
        public void notifyCopied(Project prjct, File file, String string) throws IOException {
        }
    }

    private final class AntBasedProjectDeleteOperation implements DeleteOperationImplementation {

        private final AntBasedProject project;

        private AntBasedProjectDeleteOperation(AntBasedProject project) {
            this.project = project;
        }

        @Override
        public List<FileObject> getMetadataFiles() {
            return new ArrayList<FileObject>();
        }

//        @Override
//        public List<FileObject> getDataFiles() {
//            return new ArrayList<FileObject>();
//        }
        @Override
        public List<FileObject> getDataFiles() {
            List<FileObject> files = new ArrayList<FileObject>();
            FileObject[] projectChildren = project.getProjectDirectory().getChildren();
            for (FileObject fileObject : projectChildren) {
                addFile(project.getProjectDirectory(), fileObject.getNameExt(), files);
            }
            return files;
        }

        private void addFile(FileObject projectDirectory, String fileName, List<FileObject> result) {
            FileObject file = projectDirectory.getFileObject(fileName);
            if (file != null) {
                result.add(file);
            }
        }

        @Override
        public void notifyDeleting() throws IOException {
        }

        @Override
        public void notifyDeleted() throws IOException {
        }
    }
}
