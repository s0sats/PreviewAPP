package com.namoadigital.prj001.ui.act050;

import com.namoadigital.prj001.model.SO_Favorite_Item;

import java.util.ArrayList;
import java.util.List;

public class Act050_Main_Presenter implements Act050_Main_Contract.I_Presenter {

    Act050_Main_Contract.I_Frag_Favorite mView;

    public Act050_Main_Presenter( Act050_Main_Contract.I_Frag_Favorite mView) {
        this.mView = mView;
    }

    @Override
    public void getFavoriteList() {
        List<SO_Favorite_Item> dummie = new ArrayList<>();
        dummie.add(new SO_Favorite_Item(1, 16 ,3,"CESAR #1", "#C6ADFF",  "#000000",10, null, "USER", null,null,null,null,null));
        dummie.add(new SO_Favorite_Item(1, 16 ,3,"CESAR #2", "#FFFFFF",  "#000000",10, null, "USER", null,null,null,null,null));
        dummie.add(new SO_Favorite_Item(1, 16 ,3,"CESAR #3", "#0044FF",  "#FFFFFF",10, null, "USER", null,null,null,null,null));
        mView.populatedFavoritesList(dummie);
    }
}
