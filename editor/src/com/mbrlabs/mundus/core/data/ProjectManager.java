package com.mbrlabs.mundus.core.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.mbrlabs.mundus.Main;
import com.mbrlabs.mundus.core.Mundus;
import com.mbrlabs.mundus.core.data.home.MundusHome;
import com.mbrlabs.mundus.core.data.home.ProjectRef;
import com.mbrlabs.mundus.utils.Callback;

import java.io.File;

/**
 * @author Marcus Brummer
 * @version 25-11-2015
 */
public class ProjectManager {

    public static final String PROJECT_MODEL_DIR = "models/";

    public void createProject(String name, String folder) {
        ProjectRef ref = MundusHome.getInstance().createProjectRef(name, folder);

        String path = ref.getPath();
        new File(path).mkdirs();
        new File(path, PROJECT_MODEL_DIR).mkdirs();
    }


    /**
     * Loads the whole ProjectContext of a project.
     *
     * It does however not change the ui or any global data in Mundus.
     * This should be done in the callback of the asynchron method
     * {@link com.mbrlabs.mundus.core.data.ProjectManager#loadProject}.
     *
     *
     * @param projectRef    the project to load
     */
    private ProjectContext loadProject(ProjectRef projectRef) {
        ProjectContext context = new ProjectContext();

        return context;
    }

    /**
     * Loads project asynchronously.
     *
     * This method is tottaly self contained. It does not change global data in
     * {@link com.mbrlabs.mundus.core.Mundus} in any way.
     * The callback should update the global refrences & the UI.
     *
     * @param ref
     * @param callback
     */
    public void loadProject(ProjectRef ref, Callback<ProjectContext> callback) {
        new Thread() {
            @Override
            public void run() {
                ProjectContext context = loadProject(ref);
                Gdx.app.postRunnable(() -> callback.done(context));
            }
        }.start();
    }


    public void changeProject(ProjectContext context) {
        Mundus.projectContext = context;
        Mundus.ui.getSidebar().getEntityTab().reloadData();
        Mundus.ui.getSidebar().getTerrainTab().reloadData();
        Mundus.ui.getSidebar().getModelTab().reloadData();
        Gdx.graphics.setTitle(Main.TITLE + " - " + context.getRef().getName());
    }


}