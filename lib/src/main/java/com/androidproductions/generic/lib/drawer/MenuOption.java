package com.androidproductions.generic.lib.drawer;

import android.app.Fragment;

import java.util.concurrent.Callable;

public class MenuOption {
    private final int TitleRes;

    public int getResource() {
        return Resource;
    }

    public int getTitle() {
        return TitleRes;
    }

    private final int Resource;
    private final Callable<Fragment> FragmentFactory;

    public MenuOption(int title, int resource, Callable<Fragment> fragmentFactory) {
        TitleRes = title;
        Resource = resource;
        FragmentFactory = fragmentFactory;
    }

    public Fragment createFragment() {
        try {
            return FragmentFactory.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
