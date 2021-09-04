package com.easy.core.action.function;

import com.easy.core.entity.RestServiceChooseByNameContributor;
import com.easy.core.entity.RestServiceFilteringGotoByModel;
import com.easy.core.entity.TreeNavigationItem;
import com.intellij.ide.actions.GotoActionBase;
import com.intellij.ide.util.gotoByName.ChooseByNamePopup;
import com.intellij.ide.util.gotoByName.FilteringGotoByModel;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class RestServiceGotoAction extends GotoActionBase {

    public RestServiceGotoAction() {
        super();
    }

    @Override
    protected void gotoActionPerformed(@NotNull AnActionEvent e) {
        try {
            RestServiceChooseByNameContributor chooseByName = new RestServiceChooseByNameContributor(e.getProject());
            ChooseByNameContributor[] butor = new ChooseByNameContributor[]{chooseByName};
            FilteringGotoByModel model = new RestServiceFilteringGotoByModel(e.getProject(), butor);
            GotoActionCallback callback = new GotoActionCallback() {

                @Override
                public void elementChosen(ChooseByNamePopup chooseByNamePopup, Object navigationItem) {
                    if (navigationItem instanceof TreeNavigationItem) {
                        TreeNavigationItem restServiceNavigationItem = (TreeNavigationItem) navigationItem;
                        restServiceNavigationItem.navigate(true);
                    }
                }
            };
            showNavigationPopup(e, model, callback);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            Messages.showErrorDialog(ignored.getMessage(), "RestServiceGotoAction");
        }
    }

//    @Override
//    public void setDefaultIcon(boolean isDefaultIconSet) {
//        super.setDefaultIcon(false);
//    }
}